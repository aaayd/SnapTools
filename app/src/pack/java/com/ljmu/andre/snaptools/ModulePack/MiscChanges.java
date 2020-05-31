package com.ljmu.andre.snaptools.ModulePack;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.ljmu.andre.snaptools.Dialogs.Content.TextInputBasic;
import com.ljmu.andre.snaptools.Dialogs.DialogFactory;
import com.ljmu.andre.snaptools.Dialogs.ThemedDialog;
import com.ljmu.andre.snaptools.Dialogs.ThemedDialog.ThemedClickWrapper;
import com.ljmu.andre.snaptools.Fragments.FragmentHelper;
import com.ljmu.andre.snaptools.ModulePack.Fragments.KotlinViews.ColorPickerDialogExtension;
import com.ljmu.andre.snaptools.ModulePack.Fragments.KotlinViews.FontPickerDialogExtension;
import com.ljmu.andre.snaptools.ModulePack.Fragments.MiscChangesFragment;
import com.ljmu.andre.snaptools.Utils.ContextHelper;
import com.ljmu.andre.snaptools.Utils.ResourceMapper;
import com.ljmu.andre.snaptools.Utils.ResourceUtils;

import java.io.File;

import timber.log.Timber;

import static com.ljmu.andre.GsonPreferences.Preferences.getCreateDir;
import static com.ljmu.andre.GsonPreferences.Preferences.getPref;
import static com.ljmu.andre.GsonPreferences.Preferences.putPref;
import static com.ljmu.andre.snaptools.ModulePack.HookDefinitions.HookClassDef.SNAPCHAT_CAPTION_VIEW_CLASS;
import static com.ljmu.andre.snaptools.ModulePack.HookDefinitions.HookDef.CAPTION_CREATE_HOOK;
import static com.ljmu.andre.snaptools.ModulePack.HookDefinitions.HookDef.FONT_HOOK;
import static com.ljmu.andre.snaptools.ModulePack.HookDefinitions.HookVariableDef.SNAPCAPTIONVIEW_CONTEXT;
import static com.ljmu.andre.snaptools.ModulePack.Utils.ModulePreferenceDef.COPY_BUTTON;
import static com.ljmu.andre.snaptools.ModulePack.Utils.ModulePreferenceDef.CURRENT_FONT;
import static com.ljmu.andre.snaptools.ModulePack.Utils.ModulePreferenceDef.CUT_BUTTON;
import static com.ljmu.andre.snaptools.ModulePack.Utils.ModulePreferenceDef.FONTS_PATH;
import static com.ljmu.andre.snaptools.ModulePack.Utils.ModulePreferenceDef.FORCE_MULTILINE;
import static com.ljmu.andre.snaptools.ModulePack.Utils.ModulePreferenceDef.PASTE_BUTTON;

/*import com.flask.colorpicker.ColorPickerView.WHEEL_TYPE;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;*/

/**
 * This class was created by Andre R M (SID: 701439)
 * It and its contents are free to use by all
 */

public class MiscChanges extends ModuleHelper {
    public static boolean isInternalFontCall;

    public MiscChanges(String name, boolean canBeDisabled) {
        super(name, canBeDisabled);
    }

    public static Typeface createTypefaceSafe(File fontFile) {
        isInternalFontCall = true;
        return Typeface.createFromFile(fontFile);
    }

    @Override
    public FragmentHelper[] getUIFragments() {
        return new FragmentHelper[]{new MiscChangesFragment()};
    }

    @Override
    public void loadHooks(ClassLoader snapClassLoader, Context snapContext) {
        String currentFontFile = getPref(CURRENT_FONT);

        if (!currentFontFile.equals("Default")) {
            hookMethod(
                    FONT_HOOK,
                    new HookWrapper((HookBefore) param -> {
                        if (isInternalFontCall) {
                            isInternalFontCall = false;
                            return;
                        }

                        String fontFilename = (String) param.args[0];

                        File fontDir = getCreateDir(FONTS_PATH);
                        File replacementFontFile = new File(fontDir, currentFontFile);

                        if (replacementFontFile.exists()) {
                            fontFilename = replacementFontFile.getAbsolutePath();
                            Timber.d("[FONT] Font handled by hook");
                        }

                        param.args[0] = fontFilename;
                    })
            );
        }

        hookMethod(
                CAPTION_CREATE_HOOK,
                new HookWrapper((HookBefore) param -> {
                    Timber.d("Attempting to load custom menu");

                    /**
                     * ===========================================================================
                     * Recreate Snapchat's context menu
                     * ===========================================================================
                     */
                    ActionMode actionMode = (ActionMode) param.args[0];
                    Menu menu = (Menu) param.args[1];
                    menu.clear();
                    actionMode.getMenuInflater().inflate(ResourceMapper.getResId(ContextHelper.getActivity(), "caption_context_menu", "menu"), menu);

                    EditText captionEditText = getObjectField(SNAPCAPTIONVIEW_CONTEXT, param.thisObject);

                    int paste = ResourceUtils.getId(ContextHelper.getActivity(), "menu_item_paste");
                    int cut = ResourceUtils.getId(ContextHelper.getActivity(), "menu_item_cut");
                    int copy = ResourceUtils.getId(ContextHelper.getActivity(), "menu_item_copy");

                    if (getPref(COPY_BUTTON)) {
                        menu.findItem(copy).setVisible(true);
                    }
                    if (getPref(CUT_BUTTON)) {
                        menu.findItem(cut).setVisible(true);
                    }
                    //TODO: check if something is on the clipboard
                    ClipboardManager clipboardManager = (ClipboardManager) ContextHelper.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    if (getPref(PASTE_BUTTON)) {
                        if (clipboardManager != null) {
                            if (!clipboardManager.hasPrimaryClip()) {
                                menu.findItem(paste).setVisible(false);

                            } else {
                                menu.findItem(paste).setVisible(true);
                            }
                        } else {
                            menu.findItem(paste).setVisible(true);
                        }
                    }
                    int bold = ResourceUtils.getId(ContextHelper.getActivity(), "menu_item_bold");
                    int italic = ResourceUtils.getId(ContextHelper.getActivity(), "menu_item_italic");
                    int underline = ResourceUtils.getId(ContextHelper.getActivity(), "menu_item_underline");
                    menu.findItem(bold).setVisible(true);
                    menu.findItem(italic).setVisible(true);
                    menu.findItem(underline).setVisible(true);

                    // ===========================================================================

                    /**
                     * ===========================================================================
                     * Add a BG Color Picker
                     * ===========================================================================
                     */
                    MenuItem bgColor = menu.add("BG Color");
                    bgColor.setOnMenuItemClickListener(item -> {
                        Timber.d("Changing BG color");
                        new ThemedDialog(ContextHelper.getActivity())
                                .setTitle("Color Picker")
                                .setExtension(
                                        new ColorPickerDialogExtension(
                                                ContextHelper.getActivity(),
                                                "primary",
                                                captionEditText::setBackgroundColor
                                        )
                                ).show();

                        return true;
                    });
                    bgColor.setVisible(true);

                    // ===========================================================================

                    /**
                     * ===========================================================================
                     * Add a Font Picker
                     * ===========================================================================
                     */
                    MenuItem font = menu.add("Font");
                    font.setOnMenuItemClickListener(item -> {
                        new ThemedDialog(ContextHelper.getActivity())
                                .setTitle("Choose Font")
                                .setExtension(
                                        new FontPickerDialogExtension(
                                                ContextHelper.getActivity(),
                                                MiscChangesFragment.getInstalledFonts(),
                                                s -> {
                                                    Timber.d("Selected font: " + s);
                                                    putPref(CURRENT_FONT, s);
                                                    captionEditText.setTypeface(MiscChangesFragment.getTypefaceSafe(s));
                                                }
                                        )
                                ).show();

                        return true;
                    });
                    font.setVisible(true);
                    // ===========================================================================

                    /**
                     * ===========================================================================
                     * Add a Size Picker
                     * ===========================================================================
                     */
                    MenuItem size = menu.add("Size");
                    size.setOnMenuItemClickListener(item -> {
                        DialogFactory.createBasicTextInputDialog(
                                ContextHelper.getActivity(),
                                "Font Size",
                                "What size should the text be set to?",
                                null,
                                null,
                                InputType.TYPE_CLASS_NUMBER,
                                new ThemedClickWrapper(themedDialog -> {
                                    String input = themedDialog.<TextInputBasic>getExtension()
                                            .getInputMessage();

                                    try {
                                        Float f = Float.parseFloat(input);
                                        captionEditText.setTextSize(f);
                                        themedDialog.dismiss();
                                        return;
                                    } catch (Exception ignored) {

                                    }

                                    themedDialog.dismiss();
                                })).show();

                        return true;
                    });
                    size.setVisible(true);
                    // ===========================================================================

                    param.setResult(true);
                })
        );


        hookAllConstructors(
                SNAPCHAT_CAPTION_VIEW_CLASS,
                new HookWrapper((HookAfter) param -> {
                    EditText text = (EditText) param.thisObject;
                    if (getPref(FORCE_MULTILINE)) {
                        text.setSingleLine(false);
                        text.setMaxLines(500);
                        text.setImeOptions(EditorInfo.IME_ACTION_NONE);
                        text.setFilters(new InputFilter[0]);

                        text.setOnLongClickListener(v -> {
                            text.showContextMenu();
                            return false;
                        });
                    }
                })
        );
    }
}