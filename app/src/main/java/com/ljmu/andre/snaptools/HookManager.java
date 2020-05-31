package com.ljmu.andre.snaptools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.view.ContextThemeWrapper;

import com.ljmu.andre.ErrorLogger.ErrorLogger;
import com.ljmu.andre.GsonPreferences.Preferences;
import com.ljmu.andre.snaptools.Databases.CacheDatabase;
import com.ljmu.andre.snaptools.Dialogs.Content.FrameworkLoadError;
import com.ljmu.andre.snaptools.Dialogs.DialogFactory;
import com.ljmu.andre.snaptools.Dialogs.ThemedDialog;
import com.ljmu.andre.snaptools.Dialogs.ThemedDialog.ThemedClickListener;
import com.ljmu.andre.snaptools.Framework.FrameworkManager;
import com.ljmu.andre.snaptools.Framework.Utils.PackLoadState;
import com.ljmu.andre.snaptools.Networking.VolleyHandler;
import com.ljmu.andre.snaptools.Utils.ContextHelper;
import com.ljmu.andre.snaptools.Utils.ModuleChecker;
import com.ljmu.andre.snaptools.Utils.TimberUtils;
import com.ljmu.andre.snaptools.Utils.UnhookManager;
import com.ljmu.andre.snaptools.Utils.XposedUtils.ST_MethodHook;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import timber.log.Timber;

import static com.ljmu.andre.GsonPreferences.Preferences.getPref;
import static com.ljmu.andre.GsonPreferences.Preferences.putPref;
import static com.ljmu.andre.snaptools.Utils.FrameworkPreferencesDef.ACCEPTED_TOS;
import static com.ljmu.andre.snaptools.Utils.FrameworkPreferencesDef.CHECK_PACK_UPDATES_SC;
import static com.ljmu.andre.snaptools.Utils.FrameworkPreferencesDef.NOTIFY_ON_LOAD;
import static com.ljmu.andre.snaptools.Utils.FrameworkPreferencesDef.REPACKAGE_NAME;
import static com.ljmu.andre.snaptools.Utils.FrameworkPreferencesDef.SYSTEM_ENABLED;
import static com.ljmu.andre.snaptools.Utils.NotificationUtils.showLoadedNotification;
import static com.ljmu.andre.snaptools.Utils.ResourceMapper.getResId;
import static com.ljmu.andre.snaptools.Utils.UnhookManager.addUnhook;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

/**
 * This class was created by Andre R M (SID: 701439)
 * It and its contents are free to use by all
 */

public class HookManager implements IXposedHookLoadPackage {

    // ===========================================================================

    private static AtomicBoolean hasHooked = new AtomicBoolean(false);
    private Context snapContext;
    private Activity snapActivity;


    // ===========================================================================

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {


        /**
         * ===========================================================================
         * Self Hook -> forces isModuleActive() to return true
         * ===========================================================================
         */
        if (lpparam.appInfo != null && lpparam.appInfo.className != null && lpparam.appInfo.className.equals(STApplication.class.getName())) {
            try {
                findAndHookMethod(
                        ModuleChecker.class.getName(),
                        lpparam.classLoader,
                        "isModuleActive",
                        XC_MethodReplacement.returnConstant(true)
                );
                findAndHookMethod(
                        ModuleChecker.class.getName(),
                        lpparam.classLoader,
                        "getXposedVersion",
                        XC_MethodReplacement.returnConstant(XposedBridge.getXposedVersion())
                );
            } catch (Throwable t) {
                XposedBridge.log(t);
            }

            return;
        }

        /**
         * ===========================================================================
         * Check if the current loading process is Snapchat or if we have already
         * hooked the process
         * ===========================================================================
         */
        if (!lpparam.packageName.contains("com.snapchat.android")
                || hasHooked.getAndSet(true)) {
            // Stops multiple hooks from being applied
            if (hasHooked.get())
                Timber.d("Tried to reapply hooks!");

            return;
        }

        TimberUtils.plantAppropriateTree();

        Timber.d("PID: %s", Process.myPid());

        ErrorLogger.init();

        Timber.d("Loading preferences");
        Preferences.init(
                Preferences.getExternalPath() + "/" + STApplication.MODULE_TAG + "/"
        );

        if (!(boolean) getPref(SYSTEM_ENABLED)) {
            Timber.w("System Disabled... Aborting initialisation");
            return;
        }

        String pkgName = getPref(REPACKAGE_NAME);
        STApplication.PACKAGE = (pkgName == null ? HookManager.class.getPackage().getName() : pkgName);

        Timber.d("Snapchat Is Loading!");

        Timber.d("Hooking Application Attach");

        /*
        To avoid Hangs on Snapchat Startup, we have to hook as early as possible. These Hangs have
        mainly been caused by SnapTools' HookManager Class: The Framework has only been loaded when
        the Snapchat Activity has been created and started. We still need to hook Snapchat's
        Activity Class to make ContextHelper.getActivity() possible, a method that is widely used
        in SnapTools to get the Snapchat Activity (Displaying Save Button...). The Activity Hook is
        also used to display Errors, Warns and TOS Acceptance
         */

        Context[] moduleContext = new Context[1];
        //noinspection unchecked
        List<PackLoadState>[] packLoadStates = new List[1];
        Throwable[] throwables = new Throwable[1];
        addUnhook("System",
                findAndHookMethod(
                        "android.app.Application",
                        lpparam.classLoader,
                        "attach",
                        Context.class,
                        new ST_MethodHook() {
                            @Override
                            protected void after(MethodHookParam param) throws Throwable {
                                try {

                                    Timber.d("PID: %s", Process.myPid());
                                    Timber.d("Application Attach Called");

                                    snapContext = (Context) param.args[0];

                                    Application app = (Application) param.thisObject;
                                    moduleContext[0] = helpCreatePackageContext(app);
                                    if (moduleContext[0] == null)
                                        return;
                                    ContextHelper.set(moduleContext[0]);

                                    CacheDatabase.init(snapContext);
                                    VolleyHandler.init(snapContext);


                                    // ===========================================================================
                                    // TrialUtils.endTrialIfExpired(snapActivity);

                                    Timber.d("Loading FrameworkManager");

                                    // Load the certificate required to validate the ModulePack ==================
                                    // Security.init(ContextHelper.getModuleResources(snapActivity));

                                    long packLoadStart = System.currentTimeMillis();
                                    packLoadStates[0] = FrameworkManager.loadAllModulePacks(snapContext);
                                    long packLoadEnd = System.currentTimeMillis();

                                    Timber.d("Starting Inject");
                                    FrameworkManager.injectAllHooks(packLoadStates[0], lpparam.classLoader, snapContext);
                                    long packInjectEnd = System.currentTimeMillis();

                                    Timber.d("Load Times [Load: %s][Inject: %s]", (packLoadEnd - packLoadStart), (packInjectEnd - packLoadEnd));

                                    Timber.d("Framework loaded successfully");

                                } catch (Throwable t) {
                                    Timber.wtf(t, "Error inside LandingPage Hook");
                                    throwables[0] = t;
                                }
                            }
                        }
                ));

        addUnhook("System",
                findAndHookMethod(
                        "com.snapchat.android.LandingPageActivityV1",
                        lpparam.classLoader,
                        "onCreate",
                        Bundle.class,
                        new ST_MethodHook() {
                            @Override
                            protected void before(MethodHookParam param) {
                                snapActivity = (Activity) param.thisObject;
                                ContextHelper.setActivity(snapActivity);
                                UnhookManager.unhookAll("System");
                                FrameworkManager.prepareActivityAll(
                                        packLoadStates[0],
                                        lpparam.classLoader,
                                        snapActivity
                                );
                                /**
                                 * ===========================================================================
                                 * Initialisation Stage
                                 * ===========================================================================
                                 */

                                if (moduleContext == null) {
                                    new AlertDialog.Builder(new ContextThemeWrapper(snapActivity, android.R.style.Theme_Material_Dialog_Alert))
                                            .setTitle("Unable to start " + STApplication.MODULE_TAG)
                                            .setMessage(STApplication.MODULE_TAG + " could not detect its own Package Name and is unable to initialize the hooks correctly." +
                                                    "\n\nTo fix this, just open " + STApplication.MODULE_TAG + " once and re-open Snapchat. The correct PackageName will be set and everything should work correctly again." +
                                                    "\n\nThis can happen if you restore a backup of a repackaged Version of ST without restoring the preferences located on the SdCard or Internal Storage.")
                                            .setPositiveButton("Close Snapchat", (dialog, which) -> snapActivity.finish())
                                            .show();
                                    return;
                                }

                                if (!initTOS())
                                    return;

                                boolean hasFailed = false;
                                for (PackLoadState loadState : packLoadStates[0]) {
                                    if (loadState.hasFailed())
                                        hasFailed = true;
                                }

                                if (hasFailed) {
                                    new ThemedDialog(snapActivity)
                                            .setTitle("Framework Load Error")
                                            .setHeaderDrawable(
                                                    getResId(
                                                            moduleContext[0],
                                                            "error_header",
                                                            "drawable"
                                                    )
                                            )
                                            .setExtension(new FrameworkLoadError(packLoadStates[0]))
                                            .show();
                                    return;
                                }

                                if (throwables[0] != null) {
                                    if (throwables[0] instanceof NoSuchMethodError
                                            || throwables[0] instanceof NoClassDefFoundError)
                                        DialogFactory.createErrorDialog(
                                                snapActivity,
                                                "Error Loading SnapTools!",
                                                "Fatal error loading system!"
                                                        + "\nPlease ensure you have performed a full reboot before seeking help"
                                                        + "\n\nThere appears to be an invalid Pack/Apk combination installed. Please make sure your Pack and APK are both updated"
                                        ).show();
                                    else
                                        DialogFactory.createErrorDialog(
                                                snapActivity,
                                                "Error Loading SnapTools!",
                                                "Fatal error loading system!"
                                                        + "\nPlease ensure you have performed a full reboot before seeking help"
                                                        + "\n\n" + throwables[0].getMessage()
                                        ).show();
                                }
//                                BackgroundAuthVerifier.spoolVerifierThread();


                                if ((boolean) getPref(NOTIFY_ON_LOAD) && packLoadStates[0].size() > 0)
                                    showLoadedNotification(snapActivity);

                                if (getPref(CHECK_PACK_UPDATES_SC))
                                    FrameworkManager.checkPacksForUpdate(snapActivity);
                            }
                        }
                ));
    }

    private boolean initTOS() {
        if (!(boolean) getPref(ACCEPTED_TOS)) {
            Timber.w("Terms of service not accepted... Requesting acceptance");

            DialogFactory.createConfirmation(
                    snapActivity,
                    "Terms and Conditions",
                    "By pressing YES, you agree to our Terms of Service and our Privacy Policy, both of which can be viewed under the Legal page in the SnapTools app.",
                    new ThemedClickListener() {
                        @Override
                        public void clicked(ThemedDialog themedDialog) {
                            putPref(ACCEPTED_TOS, true);
                            themedDialog.dismiss();

                            DialogFactory.createBasicMessage(
                                    snapActivity,
                                    "Restart Required",
                                    "You have accepted the ToS, a restart of Snapchat will be required!"
                            ).show();
                        }
                    },
                    new ThemedClickListener() {
                        @Override
                        public void clicked(ThemedDialog themedDialog) {
                            themedDialog.dismiss();

                            DialogFactory.createErrorDialog(
                                    snapActivity,
                                    "Terms of Service Rejected",
                                    "You have NOT accepted the ToS therefore functionality is disabled"
                                            + "\n\n"
                                            + "It is advised that you uninstall this application to not see this message every launch"
                            ).show();

                            UnhookManager.abortSystem();
                        }
                    }
            ).setDismissable(false).show();

            return false;
        }

        return true;
    }

    /**
     * A Helper needed for App Repackaging that uses two possible options to create the ModuleContext.
     * It first tries to create a PackageContext with the PackageName from the Preferences.
     * The default PackageName is used as a fallback option in case the Preferences were wrong.
     *
     * @param app The context used to create new Package Context
     * @return The created Package Context, null if both options failed.
     */
    private Context helpCreatePackageContext(Application app) {
        try {
            return app.createPackageContext(STApplication.PACKAGE, Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            try {
                return app.createPackageContext(HookManager.class.getPackage().getName(), Context.CONTEXT_IGNORE_SECURITY);
            } catch (PackageManager.NameNotFoundException e1) {
                Timber.e(e, "ST PackageName (and ST Default PackageName) invalid, due to App Repackaging. User is advised to open SnapTools once.");
            }
        }
        return null;
    }
}