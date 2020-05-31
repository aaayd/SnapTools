package com.ljmu.andre.snaptools.ModulePack.HookDefinitions;

import com.ljmu.andre.ConstantDefiner.Constant;
import com.ljmu.andre.ConstantDefiner.ConstantDefiner;

public class HookVariableDef extends ConstantDefiner<HookVariableDef.HookVariable> {
    public static final HookVariable BATCHED_MEDIA_ITEM_BOOLEAN = new HookVariable("BATCHED_MEDIA_ITEM_BOOLEAN", "f"); // Done
    public static final HookVariable BATCHED_MEDIA_LIST = new HookVariable("BATCHED_MEDIA_LIST", "aJ"); // Done
    public static final HookVariable CHAT_METADATA_MEDIA = new HookVariable("CHAT_METADATA_MEDIA", "c"); // Done
    public static final HookVariable CHAT_SAVING_LINKER = new HookVariable("CHAT_SAVING_LINKER", "H"); // Done (unsure)
    public static final HookVariable CHAT_SAVING_LINKER_CHAT_REF = new HookVariable("CHAT_SAVING_LINKER_CHAT_REF", "d"); // Done
    public static final HookVariable CHAT_TOP_PANEL_VIEW = new HookVariable("CHAT_TOP_PANEL_VIEW", "n");// Done
    public static final HookVariable FILTER_METADATA_CACHE = new HookVariable("FILTER_METADATA_CACHE", "a");
    public static final HookVariable FILTER_SERIALIZABLE_METADATA = new HookVariable("FILTER_SERIALIZABLE_METADATA", "a"); // Done
    public static final HookVariable GEOFILTER_VIEW_CREATION_ARG3 = new HookVariable("GEOFILTER_VIEW_CREATION_ARG3", "a");
    public static final HookVariable GROUP_ALGORITHM_WRAPPER_FIELD = new HookVariable("GROUP_ALGORITHM_WRAPPER_FIELD", "b"); // Done
    public static final HookVariable LENS_ACTIVATOR = new HookVariable("LENS_ACTIVATOR", "b"); // Done
    public static final HookVariable LENS_CATEGORY = new HookVariable("LENS_CATEGORY", "a"); // Done
    public static final HookVariable LENS_CATEGORY_MAP = new HookVariable("LENS_CATEGORY_MAP", "b"); // Done
    public static final HookVariable MCANONICALDISPLAYNAME = new HookVariable("MCANONICALDISPLAYNAME", "aM"); // Done
    public static final HookVariable NOTIFICATION_TYPE = new HookVariable("NOTIFICATION_TYPE", "b"); // Done
    public static final HookVariable NO_AUTO_ADVANCE = new HookVariable("NO_AUTO_ADVANCE", "NO_AUTO_ADVANCE");
    public static final HookVariable RECEIVED_SNAP_PAYLOAD_HOLDER = new HookVariable("RECEIVED_SNAP_PAYLOAD_HOLDER", "b"); // Done
    public static final HookVariable RECEIVED_SNAP_PAYLOAD_MAP = new HookVariable("RECEIVED_SNAP_PAYLOAD_MAP", "a"); // Done
    public static final HookVariable SENT_BATCHED_VIDEO_MEDIAHOLDER = new HookVariable("SENT_BATCHED_VIDEO_MEDIAHOLDER", "c"); // Done
    public static final HookVariable SENT_MEDIA_BATCH_DATA = new HookVariable("SENT_MEDIA_BATCH_DATA", "cx"); // Done
    public static final HookVariable SENT_MEDIA_BITMAP = new HookVariable("SENT_MEDIA_BITMAP", "P"); // Done
    public static final HookVariable SENT_MEDIA_TIMESTAMP = new HookVariable("SENT_MEDIA_TIMESTAMP", "az_"); // Done
    public static final HookVariable SENT_MEDIA_VIDEO_URI = new HookVariable("SENT_MEDIA_VIDEO_URI", "ba");
    public static final HookVariable SNAPCAPTIONVIEW_CONTEXT = new HookVariable("SNAPCAPTIONVIEW_CONTEXT", "b");
    public static final HookVariable SNAP_IS_ZIPPED = new HookVariable("SNAP_IS_ZIPPED", "aK"); // Done
    public static final HookVariable STORY_ADVANCER_DISPLAY_STATE = new HookVariable("STORY_ADVANCER_DISPLAY_STATE", "i"); // Done
    public static final HookVariable STORY_ADVANCER_METADATA = new HookVariable("STORY_ADVANCER_METADATA", "f"); // Done
    public static final HookVariable STORY_COLLECTION_MAP = new HookVariable("STORY_COLLECTION_MAP", "e"); // Done
    public static final HookVariable STORY_UPDATE_METADATA = new HookVariable("STORY_UPDATE_METADATA", "b"); // Done
    public static final HookVariable STORY_UPDATE_METADATA_ID = new HookVariable("STORY_UPDATE_METADATA_ID", "a"); // Done
    public static final HookVariable STORY_UPDATE_METADATA_LIST = new HookVariable("STORY_UPDATE_METADATA_LIST", "b");
    public static final HookVariable STREAM_TYPE_CHECK_BOOLEAN = new HookVariable("STREAM_TYPE_CHECK_BOOLEAN", "d");

    public static class HookVariable extends Constant {
        private final String varName;

        HookVariable(String name, String varName2) {
            super(name);
            this.varName = varName2;
        }

        public String getVarName() {
            return this.varName;
        }
    }
}

