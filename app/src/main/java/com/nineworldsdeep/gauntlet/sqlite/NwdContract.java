package com.nineworldsdeep.gauntlet.sqlite;

/**
 * Created by brent on 5/27/16.
 */
public class NwdContract {
    //tables
    public static final String TABLE_DISPLAY_NAME = "DisplayName";
    public static final String TABLE_PATH = "Path";
    public static final String TABLE_HASH = "Hash";
    public static final String TABLE_DEVICE = "Device";
    public static final String TABLE_FILE = "File";
    public static final String TABLE_TAG = "Tag";
    public static final String TABLE_FILE_TAGS = "FileTags";
    public static final String TABLE_AUDIO_TRANSCRIPT = "AudioTranscript";
    public static final String TABLE_LOCAL_CONFIG = "LocalConfig";
    //columns
    public static final String COLUMN_DISPLAY_NAME_ID = "DisplayNameId";
    public static final String COLUMN_DISPLAY_NAME_VALUE = "DisplayNameValue";
    public static final String COLUMN_PATH_ID = "PathId";
    public static final String COLUMN_PATH_VALUE = "PathValue";
    public static final String COLUMN_HASH_ID = "HashId";
    public static final String COLUMN_HASH_VALUE = "HashValue";
    public static final String COLUMN_DEVICE_ID = "DeviceId";
    public static final String COLUMN_DEVICE_DESCRIPTION = "DeviceDescription";
    public static final String COLUMN_FILE_ID = "FileId";
    public static final String COLUMN_FILE_HASHED_AT = "FileHashedAt";
    public static final String COLUMN_TAG_ID = "TagId";
    public static final String COLUMN_TAG_VALUE = "TagValue";
    public static final String COLUMN_FILE_TAGS_ID = "FileTagsId";
    public static final String COLUMN_FILE_DESCRIPTION = "FileDescription";
    public static final String COLUMN_AUDIO_TRANSCRIPT_ID = "AudioTranscriptId";
    public static final String COLUMN_AUDIO_TRANSCRIPT_VALUE = "AudioTranscriptValue";
    public static final String COLUMN_FILE_NAME = "FileName";
    public static final String COLUMN_LOCAL_CONFIG_ID = "LocalConfigId";
    public static final String COLUMN_LOCAL_CONFIG_KEY = "LocalConfigKey";
    public static final String COLUMN_LOCAL_CONFIG_VALUE = "LocalConfigValue";

    public static final String CREATE_SYNERGY_LIST = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_LIST_CREATED_TRIGGER = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_LIST_UPDATED_TRIGGER = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_ITEM = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_ITEM_CREATED_TRIGGER = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_ITEM_UPDATED_TRIGGER = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_LIST_ITEM = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_LIST_ITEM_CREATED_TRIGGER = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_LIST_ITEM_UPDATED_TRIGGER = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_TO_DO = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_TO_DO_CREATED_TRIGGER = "NOT-IMPLEMENTED";

    public static final String CREATE_SYNERGY_TO_DO_UPDATED_TRIGGER = "NOT-IMPLEMENTED";

}
