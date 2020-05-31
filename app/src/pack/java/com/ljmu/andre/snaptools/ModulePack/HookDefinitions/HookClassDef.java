package com.ljmu.andre.snaptools.ModulePack.HookDefinitions;

import androidx.annotation.NonNull;
import com.ljmu.andre.ConstantDefiner.Constant;
import com.ljmu.andre.ConstantDefiner.ConstantDefiner;

public class HookClassDef extends ConstantDefiner<HookClassDef.HookClass> {
    public static final HookClass AB_TEST_MANAGER = new HookClass("AB_TEST_MANAGER", "aoqr");//
    public static final HookClass CAMERA_FRAGMENT = new HookClass("CAMERA_FRAGMENT", "com.snapchat.android.app.feature.camera.CameraFragmentV2");
    public static final HookClass CAPTION_MANAGER_CLASS = new HookClass("CAPTION_MANAGER_CLASS", "urb");//
    public static final HookClass CHAT_BODY_METADATA = new HookClass("CHAT_BODY_METADATA", "atjc");//
    public static final HookClass CHAT_DIRECT_VIEW_MARKER = new HookClass("CHAT_DIRECT_VIEW_MARKER", "ahtv");//
    public static final HookClass CHAT_GROUP_VIEW_MARKER = new HookClass("CHAT_GROUP_VIEW_MARKER", "ahuy");//
    public static final HookClass CHAT_HEADER_METADATA = new HookClass("CHAT_HEADER_METADATA", "atep");//
    public static final HookClass CHAT_IMAGE_METADATA = new HookClass("CHAT_IMAGE_METADATA", "aida");//
    public static final HookClass CHAT_MESSAGE_VIEW_HOLDER = new HookClass("CHAT_MESSAGE_VIEW_HOLDER", "aimn");//
    public static final HookClass CHAT_METADATA = new HookClass("CHAT_METADATA", "aswo");//
    public static final HookClass CHAT_METADATA_JSON_PARSER = new HookClass("CHAT_METADATA_JSON_PARSER", "aswp");//
    public static final HookClass CHAT_METADATA_JSON_PARSER_SECOND = new HookClass("CHAT_METADATA_JSON_PARSER_SECOND", "atjs");//
    public static final HookClass CHAT_NOTIFICATION_CREATOR = new HookClass("CHAT_NOTIFICATION_CREATOR", "ahth");// UNSURE ON THIS ONE
    public static final HookClass CHAT_V10_BUILDER = new HookClass("CHAT_V10_BUILDER", "ahxy");//
    public static final HookClass CHAT_VIDEO = new HookClass("CHAT_VIDEO", "aipq");//
    public static final HookClass CHAT_VIDEO_METADATA = new HookClass("CHAT_VIDEO_METADATA", "aivs");//
    public static final HookClass CHEETAH_PROFILE_SETTINGS_CREATOR = new HookClass("CHEETAH_PROFILE_SETTINGS_CREATOR", "afdb");//
    public static final HookClass COUNTDOWNTIMER_VIEW = new HookClass("COUNTDOWNTIMER_VIEW", "com.snap.opera.view.CountdownTimerView");
    public static final HookClass DOWNLOADER_RUNNABLE = new HookClass("DOWNLOADER_RUNNABLE", "aoew$2");//Not sure what the dollar sign is for but ill keep it
    public static final HookClass ENCRYPTED_STREAM_BUILDER = new HookClass("ENCRYPTED_STREAM_BUILDER", "anxy");//
    public static final HookClass ENCRYPTION_ALGORITHM = new HookClass("ENCRYPTION_ALGORITHM", "com.snapchat.android.framework.crypto.CbcEncryptionAlgorithm");
    public static final HookClass ENUM_BATCHED_SNAP_POSITION = new HookClass("ENUM_BATCHED_SNAP_POSITION", "aaiw");//
    public static final HookClass ENUM_LENS_ACTIVATOR_TYPE = new HookClass("ENUM_LENS_ACTIVATOR_TYPE", "com.looksery.sdk.domain.Category.ActivatorType");
    public static final HookClass ENUM_LENS_TYPE = new HookClass("ENUM_LENS_TYPE", "afwd$c");//
    public static final HookClass ENUM_SNAP_ADVANCE_MODES = new HookClass("ENUM_SNAP_ADVANCE_MODES", "tez");
    public static final HookClass EXPERIMENT_BASE = new HookClass("EXPERIMENT_BASE", "aoqo");
    public static final HookClass FONT_CLASS = new HookClass("FONT_CLASS", "android.graphics.Typeface");
    public static final HookClass FRIEND_PROFILE_POPUP_FRAGMENT = new HookClass("FRIEND_PROFILE_POPUP_FRAGMENT", "com.snapchat.android.app.feature.miniprofile.internal.friend.FriendMiniProfilePopupFragment");
    public static final HookClass GROUP_SNAP_METADATA = new HookClass("GROUP_SNAP_METADATA", "aidr");//Unsure on this, seems to be pretty different but contains similar stuff to aotd
    public static final HookClass GROUP_SNAP_WRAPPER = new HookClass("GROUP_SNAP_WRAPPER", "tpp");//
    public static final HookClassDef INST = new HookClassDef();
    public static final HookClass LENS = new HookClass("LENS", "afwd");//
    public static final HookClass LENS_APPLICATION_CONTEXT_ENUM = new HookClass("LENS_APPLICATION_CONTEXT_ENUM", "afwe");//
    public static final HookClass LENS_ASSET_BUILT = new HookClass("LENS_ASSET_BUILT", "afwf");//
    public static final HookClass LENS_ASSET_LOAD_MODE = new HookClass("LENS_ASSET_LOAD_MODE", "afwz");// Unsure on this, seems similar but also some different code
    public static final HookClass LENS_ASSET_TYPE = new HookClass("LENS_ASSET_TYPE", "afwf$a");//
    public static final HookClass LENS_AUTHENTICATION = new HookClass("LENS_AUTHENTICATION", "com.snapchat.android.app.feature.lenses.internal.security.LensesSecurityManager");
    public static final HookClass LENS_CAMERA_CONTEXT_ENUM = new HookClass("LENS_CAMERA_CONTEXT_ENUM", "afwh");//
    public static final HookClass LENS_CATEGORY = new HookClass("LENS_CATEGORY", "afwm");//
    public static final HookClass LENS_CATEGORY_RESOLVER = new HookClass("LENS_CATEGORY_RESOLVER", "afwk");//
    public static final HookClass LENS_CONTEXT_HOLDER = new HookClass("LENS_CONTEXT_HOLDER", "afwq");
    public static final HookClass LENS_LOADER = new HookClass("LENS_LOADER", "afss");//
    public static final HookClass LENS_SLUG = new HookClass("LENS_SLUG", "atqf");//
    public static final HookClass LENS_TRACK = new HookClass("LENS_TRACK", "atup");//
    public static final HookClass META_DATA_BUILDER = new HookClass("META_DATA_BUILDER", "ajfg");//
    public static final HookClass NETWORK_DISPATCHER = new HookClass("NETWORK_DISPATCHER", "ahtc");//
    public static final HookClass NETWORK_MANAGER = new HookClass("NETWORK_MANAGER", "aoci");//
    public static final HookClass NEW_CONCENTRIC_TIMERVIEW = new HookClass("NEW_CONCENTRIC_TIMERVIEW", "com.snap.opera.view.NewConcentricTimerView");
    public static final HookClass OPERA_PAGE_VIEW = new HookClass("OPERA_PAGE_VIEW", "com.snap.opera.view.OperaPageView");
    public static final HookClass RECEIVED_SNAP = new HookClass("RECEIVED_SNAP", "amuu");//
    public static final HookClass RECEIVED_SNAP_ENCRYPTION = new HookClass("RECEIVED_SNAP_ENCRYPTION", "aiue");//
    public static final HookClass RECEIVED_SNAP_PAYLOAD_BUILDER = new HookClass("RECEIVED_SNAP_PAYLOAD_BUILDER", "abiz");//
    public static final HookClass SCREENSHOT_DETECTOR = new HookClass("SCREENSHOT_DETECTOR", "aolm");//
    public static final HookClass SENT_BATCHED_VIDEO = new HookClass("SENT_BATCHED_VIDEO", "acpk");//
    public static final HookClass SENT_IMAGE = new HookClass("SENT_IMAGE", "amux");//
    public static final HookClass SENT_SNAP_BASE = new HookClass("SENT_SNAP_BASE", "amtz");//
    public static final HookClass SENT_VIDEO = new HookClass("SENT_VIDEO", "amvk");//
    public static final HookClass SHARE_IMAGE = new HookClass("SHARE_IMAGE", "acmu");//
    public static final HookClass SHARE_VIDEO = new HookClass("SHARE_VIDEO", "acmv");//
    public static final HookClass SNAPCHAT_CAPTION_VIEW_CLASS = new HookClass("SNAPCHAT_CAPTION_VIEW_CLASS", "com.snapchat.android.app.feature.creativetools.caption.SnapCaptionView");
    public static final HookClass SNAP_BASE = new HookClass("SNAP_BASE", "aisv"); // Checked
    public static final HookClass SNAP_COUNTDOWN_CONTROLLER = new HookClass("SNAP_COUNTDOWN_CONTROLLER", "amdd");
    public static final HookClass SNAP_STATUS = new HookClass("SNAP_STATUS", "aisv$a");// Unsure if correct
    public static final HookClass STORY_ADVANCER = new HookClass("STORY_ADVANCER", "sxh");//
    public static final HookClass STORY_DATA_DISCOVER = new HookClass("STORY_DATA_DISCOVER", "iqw");//
    public static final HookClass STORY_DATA_DYNAMIC = new HookClass("STORY_DATA_DYNAMIC", "iqx");
    public static final HookClass STORY_DATA_MAP = new HookClass("STORY_DATA_MAP", "iqy");
    public static final HookClass STORY_DATA_MOMENT = new HookClass("STORY_DATA_MOMENT", "irk");
    public static final HookClass STORY_DATA_PROMOTED = new HookClass("STORY_DATA_PROMOTED", "iqz");
    public static final HookClass STORY_FRIEND_RECENT = new HookClass("STORY_FRIEND_RECENT", "ell");
    public static final HookClass STORY_FRIEND_VIEWED = new HookClass("STORY_FRIEND_VIEWED", "acad");
    public static final HookClass STORY_LOADER = new HookClass("STORY_LOADER", "abqh");
    public static final HookClass STORY_MANAGER = new HookClass("STORY_MANAGER", "abxz");
    public static final HookClass STORY_METADATA = new HookClass("STORY_METADATA", "tpp");
    public static final HookClass STORY_METADATA_LOADER = new HookClass("STORY_METADATA_LOADER", "acbz");
    public static final HookClass STORY_SNAP = new HookClass("STORY_SNAP", "ammt");
    //public static final HookClass STORY_SNAP_AD_LOADER = new HookClass("STORY_SNAP_AD_LOADER", "abrz");
    public static final HookClass STORY_SNAP_PAYLOAD_BUILDER = new HookClass("STORY_SNAP_PAYLOAD_BUILDER", "abtf");
    public static final HookClass STORY_SPONSORED = new HookClass("STORY_ADVERT", "eng");
    public static final HookClass STORY_STATUS_UPDATER = new HookClass("STORY_STATUS_UPDATER", "abts");
    public static final HookClass TEXTURE_VIDEO_VIEW = new HookClass("TEXTURE_VIDEO_VIEW", "com.snap.opera.shared.view.TextureVideoView");
    public static final HookClass USER_PREFS = new HookClass("USER_PREFS", "com.snapchat.android.core.user.UserPrefsImpl");

    public static class HookClass extends Constant {
        private String strClass;

        public HookClass(String name, @NonNull String value) {
            super(name);
            this.strClass = value;
        }

        public String getStrClass() {
            return this.strClass;
        }
    }
}
