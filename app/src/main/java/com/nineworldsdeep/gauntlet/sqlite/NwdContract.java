package com.nineworldsdeep.gauntlet.sqlite;

public class NwdContract {

    //region tables
    static final String TABLE_DISPLAY_NAME = "DisplayName";
    static final String TABLE_PATH = "Path";
    static final String TABLE_HASH = "Hash";
    static final String TABLE_DEVICE = "Device";
    static final String TABLE_FILE = "File";
    static final String TABLE_TAG = "Tag";
    static final String TABLE_FILE_TAGS = "FileTags";
    static final String TABLE_AUDIO_TRANSCRIPT = "AudioTranscript";
//    static final String TABLE_LOCAL_CONFIG = "LocalConfig";

    private static final String TABLE_SYNERGY_LIST = "SynergyList";
    private static final String TABLE_SYNERGY_LIST_ITEM = "SynergyListItem";
    private static final String TABLE_SYNERGY_ITEM = "SynergyItem";
    private static final String TABLE_SYNERGY_TO_DO = "SynergyToDo";
    //endregion

    //region columns

    //region V4a

    static final String COLUMN_DISPLAY_NAME_ID = "DisplayNameId";
    public static final String COLUMN_DISPLAY_NAME_VALUE = "DisplayNameValue";
    static final String COLUMN_PATH_ID = "PathId";
    public static final String COLUMN_PATH_VALUE = "PathValue";
    static final String COLUMN_HASH_ID = "HashId";
    public static final String COLUMN_HASH_VALUE = "HashValue";
    static final String COLUMN_DEVICE_ID = "DeviceId";
    public static final String COLUMN_DEVICE_DESCRIPTION = "DeviceDescription";
    public static final String COLUMN_FILE_ID = "FileId";
    public static final String COLUMN_FILE_HASHED_AT = "FileHashedAt";
    static final String COLUMN_TAG_ID = "TagId";
    public static final String COLUMN_TAG_VALUE = "TagValue";
    static final String COLUMN_FILE_TAGS_ID = "FileTagsId";
    public static final String COLUMN_FILE_DESCRIPTION = "FileDescription";
    static final String COLUMN_AUDIO_TRANSCRIPT_ID = "AudioTranscriptId";
    public static final String COLUMN_AUDIO_TRANSCRIPT_VALUE = "AudioTranscriptValue";
    public static final String COLUMN_FILE_NAME = "FileName";
//    static final String COLUMN_LOCAL_CONFIG_ID = "LocalConfigId";
//    static final String COLUMN_LOCAL_CONFIG_KEY = "LocalConfigKey";
//    static final String COLUMN_LOCAL_CONFIG_VALUE = "LocalConfigValue";

    //endregion

    static final String COLUMN_MEDIA_DEVICE_PATH_VERIFIED_PRESENT =
            "MediaDevicePathVerifiedPresent";
    static final String COLUMN_MEDIA_DEVICE_PATH_VERIFIED_MISSING =
            "MediaDevicePathVerifiedMissing";

    static final String COLUMN_COUNT = "Count";

    static final String COLUMN_SYNERGY_LIST_ID = "SynergyListId";
    static final String COLUMN_SYNERGY_LIST_NAME = "SynergyListName";
    static final String COLUMN_SYNERGY_LIST_ACTIVATED_AT = "SynergyListActivatedAt";
    static final String COLUMN_SYNERGY_LIST_SHELVED_AT = "SynergyListShelvedAt";
    private static final String COLUMN_SYNERGY_LIST_CREATED_AT = "SynergyListCreatedAt";
    private static final String COLUMN_SYNERGY_LIST_UPDATED_AT = "SynergyListUpdatedAt";

    static final String COLUMN_SYNERGY_LIST_ITEM_ID = "SynergyListItemId";
    private static final String COLUMN_SYNERGY_LIST_ITEM_CREATED_AT = "SynergyListItemCreatedAt";
    private static final String COLUMN_SYNERGY_LIST_ITEM_UPDATED_AT  = "SynergyListItemUpdatedAt";
    static final String COLUMN_SYNERGY_LIST_ITEM_POSITION = "SynergyListItemPosition";

    static final String COLUMN_SYNERGY_ITEM_ID = "SynergyItemId";
    static final String COLUMN_SYNERGY_ITEM_VALUE = "SynergyItemValue";
    private static final String COLUMN_SYNERGY_ITEM_CREATED_AT = "SynergyItemCreatedAt";
    private static final String COLUMN_SYNERGY_ITEM_UPDATED_AT = "SynergyItemUpdatedAt";

    static final String COLUMN_SYNERGY_TO_DO_ID = "SynergyToDoId";
    static final String COLUMN_SYNERGY_TO_DO_ACTIVATED_AT = "SynergyToDoActivatedAt";
    static final String COLUMN_SYNERGY_TO_DO_COMPLETED_AT = "SynergyToDoCompletedAt";
    static final String COLUMN_SYNERGY_TO_DO_ARCHIVED_AT = "SynergyToDoArchivedAt";
    private static final String COLUMN_SYNERGY_TO_DO_CREATED_AT = "SynergyToDoCreatedAt";
    private static final String COLUMN_SYNERGY_TO_DO_UPDATED_AT = "SynergyToDoUpdatedAt";

    //endregion

    //region SynergyV5_DDL

    static final String DROP_SYNERGY_LIST = "DROP TABLE SynergyList;";
    static final String DROP_SYNERGY_TO_DO = "DROP TABLE SynergyToDo;";
    static final String DROP_SYNERGY_LIST_ITEM =
            "DROP TABLE SynergyListItem;";

    static final String CREATE_SYNERGY_LIST =

        "CREATE TABLE " + TABLE_SYNERGY_LIST + " ( "
            + COLUMN_SYNERGY_LIST_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  "
            + COLUMN_SYNERGY_LIST_NAME + " TEXT NOT NULL UNIQUE, "
            + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " TEXT, "
            + COLUMN_SYNERGY_LIST_SHELVED_AT + " TEXT, "
            + COLUMN_SYNERGY_LIST_CREATED_AT + " TEXT, "
            + COLUMN_SYNERGY_LIST_UPDATED_AT + " TEXT "
        + ") ";

    static final String CREATE_SYNERGY_LIST_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_LIST + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST + "  "
                + "SET " + COLUMN_SYNERGY_LIST_CREATED_AT + " = CURRENT_TIMESTAMP,  "
                       + COLUMN_SYNERGY_LIST_UPDATED_AT + " = CURRENT_TIMESTAMP, "
                       + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " = CURRENT_TIMESTAMP "
                + "WHERE " + TABLE_SYNERGY_LIST + "." + COLUMN_SYNERGY_LIST_ID + " = NEW." + COLUMN_SYNERGY_LIST_ID + "; "
            + "END ";

    static final String CREATE_SYNERGY_LIST_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST + "UpdatedAt  "
            + "AFTER UPDATE ON " + TABLE_SYNERGY_LIST + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST + " "
                + "SET " + COLUMN_SYNERGY_LIST_UPDATED_AT + " = CURRENT_TIMESTAMP "
                + "WHERE " + TABLE_SYNERGY_LIST + "." + COLUMN_SYNERGY_LIST_ID + " = NEW." + COLUMN_SYNERGY_LIST_ID + "; "
            + "END ";



    static final String CREATE_SYNERGY_ITEM =

            "CREATE TABLE " + TABLE_SYNERGY_ITEM + " ("
            + COLUMN_SYNERGY_ITEM_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  "
            + COLUMN_SYNERGY_ITEM_VALUE + " TEXT NOT NULL UNIQUE, "
            + COLUMN_SYNERGY_ITEM_CREATED_AT + " TEXT, "
            + COLUMN_SYNERGY_ITEM_UPDATED_AT + " TEXT "
            + ") ";

    static final String CREATE_SYNERGY_ITEM_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_ITEM + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_ITEM + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_ITEM + "  "
            + "	SET " + COLUMN_SYNERGY_ITEM_CREATED_AT + " = CURRENT_TIMESTAMP,  "
            + "		   " + COLUMN_SYNERGY_ITEM_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "	WHERE " + TABLE_SYNERGY_ITEM + "." + COLUMN_SYNERGY_ITEM_ID + " = NEW." + COLUMN_SYNERGY_ITEM_ID + "; "
            + "END ";

    static final String CREATE_SYNERGY_ITEM_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_ITEM + "UpdatedAt "
            + "AFTER UPDATE ON " + TABLE_SYNERGY_ITEM + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_ITEM + "  "
            + "	SET " + COLUMN_SYNERGY_ITEM_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "	WHERE " + TABLE_SYNERGY_ITEM + "." + COLUMN_SYNERGY_ITEM_ID + " = NEW." + COLUMN_SYNERGY_ITEM_ID + "; "
            + "END ";



    static final String CREATE_SYNERGY_LIST_ITEM =

            "CREATE TABLE " + TABLE_SYNERGY_LIST_ITEM + " ( "
            + COLUMN_SYNERGY_LIST_ITEM_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  "
            + COLUMN_SYNERGY_LIST_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SYNERGY_LIST + " (" + COLUMN_SYNERGY_LIST_ID + "),  "
            + COLUMN_SYNERGY_ITEM_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SYNERGY_ITEM + " (" + COLUMN_SYNERGY_ITEM_ID + "), "
            + COLUMN_SYNERGY_LIST_ITEM_POSITION + " INTEGER, "
            + COLUMN_SYNERGY_LIST_ITEM_CREATED_AT + " TEXT, "
            + COLUMN_SYNERGY_LIST_ITEM_UPDATED_AT + " TEXT,  "
            + "	UNIQUE(" + COLUMN_SYNERGY_LIST_ID + ", " + COLUMN_SYNERGY_ITEM_ID + ") "
            + ") ";

    static final String CREATE_SYNERGY_LIST_ITEM_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST_ITEM + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_LIST_ITEM + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST_ITEM + "  "
            + "	SET " + COLUMN_SYNERGY_LIST_ITEM_CREATED_AT + " = CURRENT_TIMESTAMP,  "
            + "		   " + COLUMN_SYNERGY_LIST_ITEM_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "	WHERE " + TABLE_SYNERGY_LIST_ITEM + "." + COLUMN_SYNERGY_LIST_ITEM_ID + " = NEW." + COLUMN_SYNERGY_LIST_ITEM_ID + "; "
            + "END ";

    static final String CREATE_SYNERGY_LIST_ITEM_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST_ITEM + "UpdatedAt "
            + "AFTER UPDATE ON " + TABLE_SYNERGY_LIST_ITEM + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST_ITEM + "  "
            + "	SET " + COLUMN_SYNERGY_LIST_ITEM_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "	WHERE " + TABLE_SYNERGY_LIST_ITEM + "." + COLUMN_SYNERGY_LIST_ITEM_ID + " = NEW." + COLUMN_SYNERGY_LIST_ITEM_ID + "; "
            + "END ";



    static final String CREATE_SYNERGY_TO_DO =

            "CREATE TABLE " + TABLE_SYNERGY_TO_DO + " ( "
            + "" + COLUMN_SYNERGY_TO_DO_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  "
            + "" + COLUMN_SYNERGY_LIST_ITEM_ID + " INTEGER NOT NULL REFERENCES " +
                    TABLE_SYNERGY_LIST_ITEM + " (" + COLUMN_SYNERGY_LIST_ITEM_ID + "),  "
            + "" + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + " TEXT, "
            + "" + COLUMN_SYNERGY_TO_DO_COMPLETED_AT + " TEXT, "
            + "" + COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + " TEXT, "
            + "" + COLUMN_SYNERGY_TO_DO_CREATED_AT + " TEXT, "
            + "" + COLUMN_SYNERGY_TO_DO_UPDATED_AT + " TEXT,  "
            + "UNIQUE(" + COLUMN_SYNERGY_LIST_ITEM_ID + ") "
        + ") ";

    static final String CREATE_SYNERGY_TO_DO_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_TO_DO + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_TO_DO + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_TO_DO + "  "
                + "SET " + COLUMN_SYNERGY_TO_DO_CREATED_AT + " = CURRENT_TIMESTAMP,  "
                       + "" + COLUMN_SYNERGY_TO_DO_UPDATED_AT + " = CURRENT_TIMESTAMP "
                + "WHERE " + TABLE_SYNERGY_TO_DO + "." + COLUMN_SYNERGY_TO_DO_ID + " = NEW." + COLUMN_SYNERGY_TO_DO_ID + "; "
            + "END ";

    static final String CREATE_SYNERGY_TO_DO_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_TO_DO + "UpdatedAt "
            + "AFTER UPDATE ON " + TABLE_SYNERGY_TO_DO + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_TO_DO + "  "
                + "SET " + COLUMN_SYNERGY_TO_DO_UPDATED_AT + " = CURRENT_TIMESTAMP "
                + "WHERE " + TABLE_SYNERGY_TO_DO + "." + COLUMN_SYNERGY_TO_DO_ID + " = NEW." + COLUMN_SYNERGY_TO_DO_ID + "; "
            + "END ";

    //endregion

    //region SynergyV5_SELECT

    static final String
            SYNERGY_V5_SELECT_LIST_NAMES_WITH_ITEM_COUNTS =

                "SELECT sl." + COLUMN_SYNERGY_LIST_NAME + ", COUNT(sli." + COLUMN_SYNERGY_LIST_ITEM_ID + ") AS '" + COLUMN_COUNT + "' "
                + "FROM " + TABLE_SYNERGY_LIST + " sl "
                + "LEFT JOIN " + TABLE_SYNERGY_LIST_ITEM + " sli "
                + "ON sl." + COLUMN_SYNERGY_LIST_ID + " = sli." + COLUMN_SYNERGY_LIST_ID + " "
                + "LEFT JOIN " + TABLE_SYNERGY_TO_DO + " std "
                + "ON sli." + COLUMN_SYNERGY_LIST_ITEM_ID + " = std." + COLUMN_SYNERGY_LIST_ITEM_ID + " "
                + "WHERE (sl." + COLUMN_SYNERGY_LIST_SHELVED_AT + " IS NULL  "
                + "   OR sl." + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " >= sl." + COLUMN_SYNERGY_LIST_SHELVED_AT + ") "
                + "AND (std." + COLUMN_SYNERGY_TO_DO_ID + " IS NULL  "
                + "	OR (std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + " >= std." + COLUMN_SYNERGY_TO_DO_COMPLETED_AT + " "
                + "		AND std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + " >= std." + COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + ") "
                + "	) "
                + "GROUP BY sl." + COLUMN_SYNERGY_LIST_NAME + " "
                + "ORDER BY sl." + COLUMN_SYNERGY_LIST_NAME + "; ";

    static final String
        SYNERGY_V5_SELECT_LIST_NAMES_WITH_ITEM_COUNTS_NEW =

            "SELECT sl." + COLUMN_SYNERGY_LIST_NAME + ", SUM( " +
            "	CASE  " +
            "		WHEN std." + COLUMN_SYNERGY_TO_DO_ID + " IS NULL THEN 1 " +
            "		WHEN std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + " >= std." + COLUMN_SYNERGY_TO_DO_COMPLETED_AT + " " +
            "			AND std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + " >= std." + COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + " " +
            "		THEN 1 " +
            "		ELSE 0 " +
            "	END " +
            ") AS 'Count' " +
            "FROM " + TABLE_SYNERGY_LIST + " sl " +
            "LEFT JOIN " + TABLE_SYNERGY_LIST_ITEM + " sli " +
            "ON sl." + COLUMN_SYNERGY_LIST_ID + " = sli." + COLUMN_SYNERGY_LIST_ID + " " +
            "LEFT JOIN " + TABLE_SYNERGY_TO_DO + " std " +
            "ON sli." + COLUMN_SYNERGY_LIST_ITEM_ID + " = std." + COLUMN_SYNERGY_LIST_ITEM_ID + " " +
            "WHERE (sl." + COLUMN_SYNERGY_LIST_SHELVED_AT + " IS NULL  " +
            "   OR sl." + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " >= sl." + COLUMN_SYNERGY_LIST_SHELVED_AT + ") " +
            "GROUP BY sl." + COLUMN_SYNERGY_LIST_NAME + " " +
            "ORDER BY sl." + COLUMN_SYNERGY_LIST_NAME + "; ";

    static final String SYNERGY_V5_SELECT_ACTIVE_LISTS =

            "SELECT " + COLUMN_SYNERGY_LIST_NAME + " "
            + "FROM " + TABLE_SYNERGY_LIST + " "
            + "WHERE " + COLUMN_SYNERGY_LIST_SHELVED_AT + " IS NULL "
            + "   OR " + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " >= " +
                         COLUMN_SYNERGY_LIST_SHELVED_AT + " "
            + "ORDER BY " + COLUMN_SYNERGY_LIST_NAME + "; ";

    static final String SYNERGY_V5_SELECT_SHELVED_LISTS =

            "SELECT " + COLUMN_SYNERGY_LIST_NAME + " "
            + "FROM " + TABLE_SYNERGY_LIST + " "
            + "WHERE " + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " < " + COLUMN_SYNERGY_LIST_SHELVED_AT + " "
            + "ORDER BY " + COLUMN_SYNERGY_LIST_NAME + "; ";

    static final String SYNERGY_V5_SELECT_ID_FOR_LIST_NAME_X =

            "SELECT " + COLUMN_SYNERGY_LIST_ID + " "
            + "FROM " + TABLE_SYNERGY_LIST + " "
            + "WHERE " + COLUMN_SYNERGY_LIST_NAME + " = ? ;";

    static final String
            SYNERGY_V5_SELECT_ID_ACTIVATED_AT_SHELVED_AT_FOR_LIST_NAME =

        "SELECT " + COLUMN_SYNERGY_LIST_ID + ", "
                  + COLUMN_SYNERGY_LIST_ACTIVATED_AT + ", "
                  + COLUMN_SYNERGY_LIST_SHELVED_AT + " "
        + "FROM " + TABLE_SYNERGY_LIST + " "
        + "WHERE " + COLUMN_SYNERGY_LIST_NAME + " = ? ;";

    static final String
            SYNERGY_V5_SELECT_LIST_ITEM_ID_FOR_LIST_ID_ITEM_ID_X_Y =

            "SELECT " + COLUMN_SYNERGY_LIST_ITEM_ID + " "
            + "FROM " + TABLE_SYNERGY_LIST_ITEM + " "
            + "WHERE " + COLUMN_SYNERGY_LIST_ID + " = ?  "
            + "AND " + COLUMN_SYNERGY_ITEM_ID + " = ? ; ";

    static final String SYNERGY_V5_SELECT_ID_FOR_ITEM_VALUE_X =

            "SELECT " + COLUMN_SYNERGY_ITEM_ID + " "
            + "FROM " + TABLE_SYNERGY_ITEM + " "
            + "WHERE " + COLUMN_SYNERGY_ITEM_VALUE + " = ? ; ";

    static final String
            SYNERGY_V5_SELECT_ITEM_VALUES_BY_POSITION_FOR_LIST_ID_X =

            "SELECT si." + COLUMN_SYNERGY_ITEM_ID + ", "
            + "	    si." + COLUMN_SYNERGY_ITEM_VALUE + ", "
            + "	    sli." + COLUMN_SYNERGY_LIST_ITEM_POSITION + " "
            + "FROM " + TABLE_SYNERGY_LIST_ITEM + " sli "
            + "JOIN " + TABLE_SYNERGY_ITEM + " si "
            + "ON sli." + COLUMN_SYNERGY_ITEM_ID +
                    " = si." + COLUMN_SYNERGY_ITEM_ID + " "
            + "WHERE sli." + COLUMN_SYNERGY_LIST_ID + " = ? ; ";

    static final String
            SYNERGY_V5_SELECT_LIST_ITEMS_AND_TODOS_BY_POSITION_FOR_LIST_ID_X =

            "SELECT si." + COLUMN_SYNERGY_ITEM_ID + ", "
            + "	    si." + COLUMN_SYNERGY_ITEM_VALUE + ", "
            + "	    sli." + COLUMN_SYNERGY_LIST_ITEM_POSITION + ", "
            + "     sli." + COLUMN_SYNERGY_LIST_ITEM_ID + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_ID + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_COMPLETED_AT + ", "
            + "     std." +COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + " "
            + "FROM " + TABLE_SYNERGY_LIST_ITEM + " sli "
            + "JOIN " + TABLE_SYNERGY_ITEM + " si "
            + "ON sli." + COLUMN_SYNERGY_ITEM_ID + " = si." + COLUMN_SYNERGY_ITEM_ID + " "
            + "LEFT JOIN " + TABLE_SYNERGY_TO_DO + " std "
            + "ON sli." + COLUMN_SYNERGY_LIST_ITEM_ID + " = std." + COLUMN_SYNERGY_LIST_ITEM_ID + " "
            + "WHERE sli." + COLUMN_SYNERGY_LIST_ID + " = ? "
            +    "ORDER BY sli." + COLUMN_SYNERGY_LIST_ITEM_POSITION + "; ";

    //endregion

    //region SynergyV5_INSERT

    static final String SYNERGY_V5_ENSURE_LIST_NAME_X =

            "INSERT OR IGNORE INTO " + TABLE_SYNERGY_LIST + " "
            + "	(" + COLUMN_SYNERGY_LIST_NAME + ") "
            + "VALUES "
            + "	(?); ";

    static final String SYNERGY_V5_ENSURE_LIST_ITEM_POSITION_X_Y_Z =

            "INSERT OR IGNORE INTO " + TABLE_SYNERGY_LIST_ITEM + " "
            + "	(" + COLUMN_SYNERGY_LIST_ID + ",  "
            + "	 " + COLUMN_SYNERGY_ITEM_ID + ",  "
            + "	 " + COLUMN_SYNERGY_LIST_ITEM_POSITION +  ") "
            + "VALUES "
            + "	(?, ?, ?); ";

    static final String SYNERGY_V5_ENSURE_ITEM_VALUE_X =

            "INSERT OR IGNORE INTO " + TABLE_SYNERGY_ITEM + " "
            + "	(" + COLUMN_SYNERGY_ITEM_VALUE + ") "
            + "VALUES "
            + "	(?); ";

    static final String
            SYNERGY_V5_ENSURE_TO_DO_FOR_LIST_ITEM_ID_ID_AC_CO_AR =

            "INSERT OR IGNORE INTO " + TABLE_SYNERGY_TO_DO + "  "
            + "	(" + COLUMN_SYNERGY_LIST_ITEM_ID + ", "
            + "	 " + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + ", "
            + "	 " + COLUMN_SYNERGY_TO_DO_COMPLETED_AT + ", "
            + "	 " + COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + ")  "
            + "VALUES  "
            + "	(?, ?, ?, ?); ";

    //endregion

    //region SynergyV5_UPDATE

    static final String
            SYNERGY_V5_LIST_UPDATE_ACTIVATE_AT_SHELVED_AT_FOR_LIST_NAME_X_Y_Z =

            "UPDATE " + TABLE_SYNERGY_LIST + " "
            + "SET " + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " = MAX(IFNULL(" + COLUMN_SYNERGY_LIST_ACTIVATED_AT + ", ''), ?), "
            + "	   " + COLUMN_SYNERGY_LIST_SHELVED_AT + " = MAX(IFNULL(" + COLUMN_SYNERGY_LIST_SHELVED_AT + ", ''), ?) "
            + "WHERE " + COLUMN_SYNERGY_LIST_NAME + " = ?; ";

    static final String
            SYNERGY_V5_UPDATE_POSITION_FOR_LIST_ITEM_ID_X_Y =

            "UPDATE " + TABLE_SYNERGY_LIST_ITEM + " "
            + "SET " + COLUMN_SYNERGY_LIST_ITEM_POSITION + " = ? "
            + "WHERE " + COLUMN_SYNERGY_LIST_ITEM_ID + " = ? ; ";

    static final String
            SYNERGY_V5_UPDATE_TO_DO_WHERE_LIST_ITEM_ID_AC_CO_AR_ID =

            "UPDATE " + TABLE_SYNERGY_TO_DO + "  "
            + "SET " + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT
                    + " = MAX(IFNULL(" + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + ", ''), ?), "
            + "	   " + COLUMN_SYNERGY_TO_DO_COMPLETED_AT
                    + " = MAX(IFNULL(" + COLUMN_SYNERGY_TO_DO_COMPLETED_AT + ", ''), ?), "
            + "	   " + COLUMN_SYNERGY_TO_DO_ARCHIVED_AT
                    + " = MAX(IFNULL(" + COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + ", ''), ?) "
            + "WHERE " + COLUMN_SYNERGY_LIST_ITEM_ID + " = ?; ";

    //endregion

    private static final String TABLE_MEDIA_TRANSCRIPTION = "MediaTranscription";

    private static final String TABLE_MEDIA_DEVICE = "MediaDevice";
    private static final String TABLE_MEDIA_ROOT = "MediaRoot";
    private static final String TABLE_MEDIA_PATH = "MediaPath";
    private static final String TABLE_MEDIA = "Media";
    private static final String TABLE_MEDIA_DEVICE_PATH = "MediaDevicePath";
    private static final String TABLE_MEDIA_TAG = "MediaTag";
    private static final String TABLE_MEDIA_TAGGING = "MediaTagging";
    private static final String TABLE_MEDIA_TRANSCRIPT = "MediaTranscript";

    static final String COLUMN_MEDIA_ROOT_ID = "MediaRootId";
    static final String COLUMN_MEDIA_ROOT_PATH = "MediaRootPath";
    static final String COLUMN_MEDIA_DEVICE_ID = "MediaDeviceId";
    static final String COLUMN_MEDIA_DEVICE_DESCRIPTION = "MediaDeviceDescription";
    public static final String COLUMN_MEDIA_PATH_VALUE = "MediaPathValue";
    static final String COLUMN_MEDIA_FILE_NAME = "MediaFileName";
    static final String COLUMN_MEDIA_ID = "MediaId";
    static final String COLUMN_MEDIA_PATH_ID = "MediaPathId";
    static final String COLUMN_MEDIA_HASH = "MediaHash";
    static final String COLUMN_MEDIA_TAG_ID = "MediaTagId";
    public static final String COLUMN_MEDIA_TAG_VALUE = "MediaTagValue";
    static final String COLUMN_MEDIA_DESCRIPTION = "MediaDescription";

    private static final String COLUMN_MEDIA_TRANSCRIPT_ID = "MediaTranscriptId";
    private static final String COLUMN_MEDIA_TRANSCRIPT_VALUE = "MediaTranscriptValue";
    private static final String COLUMN_MEDIA_TRANSCRIPT_BEGIN_TIME = "MediaTranscriptBeginTime";
    private static final String COLUMN_MEDIA_TRANSCRIPT_END_TIME = "MediaTranscriptEndTime";
    private static final String COLUMN_MEDIA_TRANSCRIPT_CREATED_AT = "MediaTranscriptCreatedAt";
    private static final String COLUMN_MEDIA_TRANSCRIPT_UPDATED_AT = "MediaTranscriptUpdatedAt";
    private static final String COLUMN_MEDIA_TRANSCRIPTION_ID = "MediaTranscriptionId";
    private static final String COLUMN_MEDIA_TRANSCRIPTION_CREATED_AT = "MediaTranscriptionCreatedAt";
    private static final String COLUMN_MEDIA_TRANSCRIPTION_UPDATED_AT = "MediaTranscriptionUpdatedAt";
    private static final String COLUMN_MEDIA_CREATED_AT = "MediaCreatedAt";
    private static final String COLUMN_MEDIA_UPDATED_AT = "MediaUpdatedAt";
    private static final String COLUMN_MEDIA_ROOT_CREATED_AT = "MediaRootCreatedAt";
    private static final String COLUMN_MEDIA_ROOT_UPDATED_AT = "MediaRootUpdatedAt";
    private static final String COLUMN_MEDIA_PATH_CREATED_AT = "MediaPathCreatedAt";
    private static final String COLUMN_MEDIA_PATH_UPDATED_AT = "MediaPathUpdatedAt";
    static final String COLUMN_MEDIA_DEVICE_PATH_ID = "MediaDevicePathId";
    private static final String COLUMN_MEDIA_DEVICE_PATH_CREATED_AT = "MediaDevicePathCreatedAt";
    private static final String COLUMN_MEDIA_DEVICE_PATH_UPDATED_AT = "MediaDevicePathUpdatedAt";
    private static final String COLUMN_MEDIA_DEVICE_CREATED_AT = "MediaDeviceCreatedAt";
    private static final String COLUMN_MEDIA_DEVICE_UPDATED_AT = "MediaDeviceUpdatedAt";

    static final String COLUMN_MEDIA_TAGGING_ID = "MediaTaggingId";
    static final String COLUMN_MEDIA_TAGGING_TAGGED_AT = "MediaTaggingTaggedAt";
    static final String COLUMN_MEDIA_TAGGING_UNTAGGED_AT = "MediaTaggingUntaggedAt";
    private static final String COLUMN_MEDIA_TAGGING_CREATED_AT = "MediaTaggingCreatedAt";
    private static final String COLUMN_MEDIA_TAGGING_UPDATED_AT = "MediaTaggingUpdatedAt";
    private static final String COLUMN_MEDIA_TAG_CREATED_AT = "MediaTagCreatedAt";
    private static final String COLUMN_MEDIA_TAG_UPDATED_AT = "MediaTagUpdatedAt";

    static final String TABLE_LOCAL_CONFIG = "LocalConfig";

    static final String COLUMN_LOCAL_CONFIG_ID = "LocalConfigId";
    public static final String COLUMN_LOCAL_CONFIG_KEY = "LocalConfigKey";
    public static final String COLUMN_LOCAL_CONFIG_VALUE = "LocalConfigValue";
    private static final String COLUMN_LOCAL_CONFIG_CREATED_AT = "LocalConfigCreatedAt";
    private static final String COLUMN_LOCAL_CONFIG_UPDATED_AT = "LocalConfigUpdatedAt";

    static final String COLUMN_HIVE_ROOT_ID = "HiveRootId";
    static final String COLUMN_HIVE_ROOT_NAME = "HiveRootName";
    static final String COLUMN_HIVE_ROOT_ACTIVATED_AT = "HiveRootActivatedAt";
    static final String COLUMN_HIVE_ROOT_DEACTIVATED_AT = "HiveRootDeactivatedAt";
    private static final String COLUMN_HIVE_ROOT_CREATED_AT = "HiveRootCreatedAt";
    private static final String COLUMN_HIVE_ROOT_UPDATED_AT = "HiveRootUpdatedAt";
    private static final String TABLE_HIVE_ROOT = "HiveRoot";

    static final String CREATE_MEDIA_TAG =

            "CREATE TABLE " + TABLE_MEDIA_TAG + " ( " +
            "	" + COLUMN_MEDIA_TAG_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_TAG_VALUE + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_MEDIA_TAG_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TAG_UPDATED_AT + " TEXT " +
            ") ";

    static final String CREATE_MEDIA_TAG_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TAG + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_TAG + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TAG + "  " +
            "	SET " + COLUMN_MEDIA_TAG_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_TAG_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TAG + "." + COLUMN_MEDIA_TAG_ID + " = NEW." + COLUMN_MEDIA_TAG_ID + "; " +
            "END ";

    static final String CREATE_MEDIA_TAG_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TAG + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_TAG + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TAG + "  " +
            "	SET " + COLUMN_MEDIA_TAG_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TAG + "." + COLUMN_MEDIA_TAG_ID + " = NEW." + COLUMN_MEDIA_TAG_ID + "; " +
            "END " ;


    static final String CREATE_MEDIA_DEVICE =

            "CREATE TABLE " + TABLE_MEDIA_DEVICE + " ( " +
            "	" + COLUMN_MEDIA_DEVICE_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_DEVICE_DESCRIPTION + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_MEDIA_DEVICE_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_DEVICE_UPDATED_AT + " TEXT " +
            ") " ;

    static final String CREATE_MEDIA_DEVICE_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_DEVICE + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_DEVICE + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_DEVICE + "  " +
            "	SET " + COLUMN_MEDIA_DEVICE_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_DEVICE_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_DEVICE + "." + COLUMN_MEDIA_DEVICE_ID + " = NEW." + COLUMN_MEDIA_DEVICE_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_DEVICE_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_DEVICE + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_DEVICE + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_DEVICE + "  " +
            "	SET " + COLUMN_MEDIA_DEVICE_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_DEVICE + "." + COLUMN_MEDIA_DEVICE_ID + " = NEW." + COLUMN_MEDIA_DEVICE_ID + "; " +
            "END " ;


    static final String CREATE_MEDIA_TRANSCRIPT =

            "CREATE TABLE " + TABLE_MEDIA_TRANSCRIPT + " ( " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_VALUE + " TEXT, " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_BEGIN_TIME + " TEXT,  " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_END_TIME + " TEXT,  " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_UPDATED_AT + " TEXT,  " +
            "	UNIQUE(" + COLUMN_MEDIA_TRANSCRIPT_BEGIN_TIME + ", " + COLUMN_MEDIA_TRANSCRIPT_END_TIME + ") " +
            ") " ;

    static final String CREATE_MEDIA_TRANSCRIPT_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TRANSCRIPT + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_TRANSCRIPT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TRANSCRIPT + "  " +
            "	SET " + COLUMN_MEDIA_TRANSCRIPT_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_TRANSCRIPT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TRANSCRIPT + "." + COLUMN_MEDIA_TRANSCRIPT_ID + " = NEW." + COLUMN_MEDIA_TRANSCRIPT_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_TRANSCRIPT_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TRANSCRIPT + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_TRANSCRIPT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TRANSCRIPT + "  " +
            "	SET " + COLUMN_MEDIA_TRANSCRIPT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TRANSCRIPT + "." + COLUMN_MEDIA_TRANSCRIPT_ID + " = NEW." + COLUMN_MEDIA_TRANSCRIPT_ID + "; " +
            "END " ;


    static final String CREATE_MEDIA_PATH =

            "CREATE TABLE " + TABLE_MEDIA_PATH + " ( " +
            "	" + COLUMN_MEDIA_PATH_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_PATH_VALUE + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_MEDIA_PATH_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_PATH_UPDATED_AT + " TEXT " +
            ") ";

    static final String CREATE_MEDIA_PATH_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_PATH + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_PATH + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_PATH + "  " +
            "	SET " + COLUMN_MEDIA_PATH_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_PATH_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_PATH + "." + COLUMN_MEDIA_PATH_ID + " = NEW." + COLUMN_MEDIA_PATH_ID + "; " +
            "END ";

    static final String CREATE_MEDIA_PATH_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_PATH + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_PATH + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_PATH + "  " +
            "	SET " + COLUMN_MEDIA_PATH_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_PATH + "." + COLUMN_MEDIA_PATH_ID + " = NEW." + COLUMN_MEDIA_PATH_ID + "; " +
            "END ";


    static final String CREATE_MEDIA_ROOT =

            "CREATE TABLE " + TABLE_MEDIA_ROOT + " ( " +
            "	" + COLUMN_MEDIA_ROOT_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_DEVICE_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_DEVICE + " (" + COLUMN_MEDIA_DEVICE_ID + "),  " +
            "	" + COLUMN_MEDIA_ROOT_PATH + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_MEDIA_ROOT_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_ROOT_UPDATED_AT + " TEXT " +
            ") ";

    static final String CREATE_MEDIA_ROOT_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_ROOT + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_ROOT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_ROOT + "  " +
            "	SET " + COLUMN_MEDIA_ROOT_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_ROOT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_ROOT + "." + COLUMN_MEDIA_ROOT_ID + " = NEW." + COLUMN_MEDIA_ROOT_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_ROOT_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_ROOT + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_ROOT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_ROOT + "  " +
            "	SET " + COLUMN_MEDIA_ROOT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_ROOT + "." + COLUMN_MEDIA_ROOT_ID + " = NEW." + COLUMN_MEDIA_ROOT_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA =

            "CREATE TABLE " + TABLE_MEDIA + " ( " +
            "	" + COLUMN_MEDIA_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_FILE_NAME + " TEXT, " +
            "	" + COLUMN_MEDIA_DESCRIPTION + " TEXT,  " +
            "	" + COLUMN_MEDIA_HASH + " TEXT,  " +
            "	" + COLUMN_MEDIA_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_UPDATED_AT + " TEXT,   " +
            "	UNIQUE(" + COLUMN_MEDIA_HASH + ") " +
            ") ";

    static final String CREATE_MEDIA_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA + "  " +
            "	SET " + COLUMN_MEDIA_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA + "." + COLUMN_MEDIA_ID + " = NEW." + COLUMN_MEDIA_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA + "  " +
            "	SET " + COLUMN_MEDIA_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA + "." + COLUMN_MEDIA_ID + " = NEW." + COLUMN_MEDIA_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_DEVICE_PATH =

            "CREATE TABLE " + TABLE_MEDIA_DEVICE_PATH + " ( " +
            "	" + COLUMN_MEDIA_DEVICE_PATH_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA + " (" + COLUMN_MEDIA_ID + "),  " +
            "	" + COLUMN_MEDIA_DEVICE_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_DEVICE + " (" + COLUMN_MEDIA_DEVICE_ID + "), " +
            "	" + COLUMN_MEDIA_PATH_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_PATH + " (" + COLUMN_MEDIA_PATH_ID + "), " +
            "	" + COLUMN_MEDIA_DEVICE_PATH_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_DEVICE_PATH_UPDATED_AT + " TEXT,  " +
            "	UNIQUE(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_DEVICE_ID + ", " + COLUMN_MEDIA_PATH_ID + ") " +
            ") " ;

    static final String CREATE_MEDIA_DEVICE_PATH_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_DEVICE_PATH + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_DEVICE_PATH + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_DEVICE_PATH + "  " +
            "	SET " + COLUMN_MEDIA_DEVICE_PATH_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_DEVICE_PATH_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_DEVICE_PATH + "." + COLUMN_MEDIA_DEVICE_PATH_ID + " = NEW." + COLUMN_MEDIA_DEVICE_PATH_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_DEVICE_PATH_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_DEVICE_PATH + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_DEVICE_PATH + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_DEVICE_PATH + "  " +
            "	SET " + COLUMN_MEDIA_DEVICE_PATH_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_DEVICE_PATH + "." + COLUMN_MEDIA_DEVICE_PATH_ID + " = NEW." + COLUMN_MEDIA_DEVICE_PATH_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_TRANSCRIPTION =

            "CREATE TABLE " + TABLE_MEDIA_TRANSCRIPTION + " ( " +
            "	" + COLUMN_MEDIA_TRANSCRIPTION_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA + " (" + COLUMN_MEDIA_ID + "),  " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_TRANSCRIPT + " (" + COLUMN_MEDIA_TRANSCRIPT_ID + "), " +
            "	" + COLUMN_MEDIA_TRANSCRIPTION_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TRANSCRIPTION_UPDATED_AT + " TEXT,  " +
            "	UNIQUE(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_TRANSCRIPT_ID + ") " +
            ") " ;

    static final String CREATE_MEDIA_TRANSCRIPTION_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TRANSCRIPTION + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_TRANSCRIPTION + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TRANSCRIPTION + "  " +
            "	SET " + COLUMN_MEDIA_TRANSCRIPTION_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_TRANSCRIPTION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TRANSCRIPTION + "." + COLUMN_MEDIA_TRANSCRIPTION_ID + " = NEW." + COLUMN_MEDIA_TRANSCRIPTION_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_TRANSCRIPTION_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TRANSCRIPTION + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_TRANSCRIPTION + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TRANSCRIPTION + "  " +
            "	SET " + COLUMN_MEDIA_TRANSCRIPTION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TRANSCRIPTION + "." + COLUMN_MEDIA_TRANSCRIPTION_ID + " = NEW." + COLUMN_MEDIA_TRANSCRIPTION_ID + "; " +
            "END ";

    static final String CREATE_MEDIA_TAGGING =

            "CREATE TABLE " + TABLE_MEDIA_TAGGING + " ( " +
            "	" + COLUMN_MEDIA_TAGGING_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA + " (" + COLUMN_MEDIA_ID + "),  " +
            "	" + COLUMN_MEDIA_TAG_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_TAG + " (" + COLUMN_MEDIA_TAG_ID + "), " +
            "	" + COLUMN_MEDIA_TAGGING_TAGGED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TAGGING_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TAGGING_UPDATED_AT + " TEXT,  " +
            "	UNIQUE(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_TAG_ID + ") " +
            ") " ;

    static final String CREATE_MEDIA_TAGGING_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TAGGING + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_TAGGING + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TAGGING + "  " +
            "	SET " + COLUMN_MEDIA_TAGGING_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_TAGGING_UPDATED_AT + " = CURRENT_TIMESTAMP, " +
            "		   " + COLUMN_MEDIA_TAGGING_TAGGED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TAGGING + "." + COLUMN_MEDIA_TAGGING_ID + " = NEW." + COLUMN_MEDIA_TAGGING_ID + "; " +
            "END " ;

    static final String CREATE_MEDIA_TAGGING_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TAGGING + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_TAGGING + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TAGGING + "  " +
            "	SET " + COLUMN_MEDIA_TAGGING_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TAGGING + "." + COLUMN_MEDIA_TAGGING_ID + " = NEW." + COLUMN_MEDIA_TAGGING_ID + "; " +
            "END " ;

    static final String CREATE_LOCAL_CONFIG_V5 =

            "CREATE TABLE " + TABLE_LOCAL_CONFIG + " ( " +
            "	" + COLUMN_LOCAL_CONFIG_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_LOCAL_CONFIG_KEY + " TEXT NOT NULL UNIQUE,  " +
            "	" + COLUMN_LOCAL_CONFIG_VALUE + " TEXT, " +
            "	" + COLUMN_LOCAL_CONFIG_CREATED_AT + " TEXT, " +
            "	" + COLUMN_LOCAL_CONFIG_UPDATED_AT + " TEXT, " +
            "	UNIQUE(" + COLUMN_LOCAL_CONFIG_KEY + ", " + COLUMN_LOCAL_CONFIG_VALUE + ") " +
            ") " ;

    static final String CREATE_LOCAL_CONFIG_V5_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_LOCAL_CONFIG + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_LOCAL_CONFIG + " " +
            "BEGIN " +
            "UPDATE " + TABLE_LOCAL_CONFIG + "  " +
            "	SET " + COLUMN_LOCAL_CONFIG_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_LOCAL_CONFIG_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_LOCAL_CONFIG + "." + COLUMN_LOCAL_CONFIG_ID + " = NEW." + COLUMN_LOCAL_CONFIG_ID + "; " +
            "END " ;

    static final String CREATE_LOCAL_CONFIG_V5_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_LOCAL_CONFIG + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_LOCAL_CONFIG + " " +
            "BEGIN " +
            "UPDATE " + TABLE_LOCAL_CONFIG + "  " +
            "	SET " + COLUMN_LOCAL_CONFIG_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_LOCAL_CONFIG + "." + COLUMN_LOCAL_CONFIG_ID + " = NEW." + COLUMN_LOCAL_CONFIG_ID + "; " +
            "END " ;

    static final String LOCAL_CONFIG_GET_VALUE_FOR_KEY_X =

            "SELECT " + COLUMN_LOCAL_CONFIG_VALUE + " " +
            "FROM " + TABLE_LOCAL_CONFIG + " " +
            "WHERE " + COLUMN_LOCAL_CONFIG_KEY + " = ?; ";

    static final String MNEMOSYNE_V5_SELECT_MEDIA_DEVICE_BY_DESC_X =

            "SELECT " + COLUMN_MEDIA_DEVICE_ID + ",  " +
            "	    " + COLUMN_MEDIA_DEVICE_DESCRIPTION + " " +
            "FROM " + TABLE_MEDIA_DEVICE + " " +
            "WHERE " + COLUMN_MEDIA_DEVICE_DESCRIPTION + " = ?; " ;

    static final String INSERT_MEDIA_DEVICE_X =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_DEVICE + " "
            + "	(" + COLUMN_MEDIA_DEVICE_DESCRIPTION + ") "
            + "VALUES "
            + "	(?); " ;

    static final String MNEMOSYNE_V5_INSERT_LOCAL_CONFIG_KEY_VALUE_X_Y =

            "INSERT OR IGNORE INTO " + TABLE_LOCAL_CONFIG + " " +
            "	(" + COLUMN_LOCAL_CONFIG_KEY + ", " + COLUMN_LOCAL_CONFIG_VALUE + ") " +
            "VALUES " +
            "	(?, ?); ";

    static final String MNEMOSYNE_V5_UPDATE_LOCAL_CONFIG_VALUE_FOR_KEY_X_Y =

            "UPDATE " + TABLE_LOCAL_CONFIG + " " +
            "SET " + COLUMN_LOCAL_CONFIG_VALUE + " = ? " +
            "WHERE " + COLUMN_LOCAL_CONFIG_KEY + " = ?; " ;

    static final String MNEMOSYNE_V5_ADD_COLUMN_MEDIA_DEVICE_PATH_VERIFIED =

            "ALTER TABLE " + TABLE_MEDIA_DEVICE_PATH +
                    " ADD COLUMN " + COLUMN_MEDIA_DEVICE_PATH_VERIFIED_PRESENT + " TEXT; ";

    static final String MNEMOSYNE_V5_ADD_COLUMN_MEDIA_DEVICE_PATH_MISSING =

            "ALTER TABLE " + TABLE_MEDIA_DEVICE_PATH +
                    " ADD COLUMN " + COLUMN_MEDIA_DEVICE_PATH_VERIFIED_MISSING + " TEXT; ";

    static final String SELECT_FROM_MEDIA_DEVICE =

            "SELECT " + NwdContract.COLUMN_MEDIA_DEVICE_ID + ", "
                      + NwdContract.COLUMN_MEDIA_DEVICE_DESCRIPTION + " "
            + "FROM " + NwdContract.TABLE_MEDIA_DEVICE + "; ";

    static final String
            SELECT_ID_AND_PATH_FROM_MEDIA_ROOT_FOR_DEVICE_ID =

            "SELECT " + NwdContract.COLUMN_MEDIA_ROOT_ID + ", "
                      + NwdContract.COLUMN_MEDIA_ROOT_PATH + " "
            + "FROM " + NwdContract.TABLE_MEDIA_ROOT + " "
            + "WHERE " + NwdContract.COLUMN_MEDIA_DEVICE_ID + " = ? ; ";

    static final String INSERT_DEVICE_ID_PATH_INTO_MEDIA_ROOT =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_ROOT + " "
            + "	(" + COLUMN_MEDIA_DEVICE_ID + ", " + COLUMN_MEDIA_ROOT_PATH + ") "
            + "VALUES "
            + "	(?, ?); " ;

    static final String MNEMOSYNE_V4_GET_PATH_TAG_LINKS =

            "SELECT " +
                NwdContract.COLUMN_PATH_VALUE + ", " +
                NwdContract.COLUMN_TAG_VALUE + " " +
            "FROM " + NwdContract.TABLE_PATH + " " +
            "JOIN " + NwdContract.TABLE_FILE + " " +
            "ON " +
                NwdContract.TABLE_PATH + "." +
                    NwdContract.COLUMN_PATH_ID +
                " = " +
                NwdContract.TABLE_FILE + "." +
                    NwdContract.COLUMN_PATH_ID + " " +
            "JOIN " + NwdContract.TABLE_FILE_TAGS + " " +
            "ON " +
                NwdContract.TABLE_FILE + "." +
                    NwdContract.COLUMN_FILE_ID +
                " = " +
                NwdContract.TABLE_FILE_TAGS + "." +
                    NwdContract.COLUMN_FILE_ID + " " +
            "JOIN " + NwdContract.TABLE_TAG + " " +
            "ON " +
                NwdContract.TABLE_FILE_TAGS + "." +
                    NwdContract.COLUMN_TAG_ID +
                " = " +
                NwdContract.TABLE_TAG + "." +
                    NwdContract.COLUMN_TAG_ID + "";

    static final String INSERT_MEDIA_TAG_X =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_TAG + " " +
            "	(" + COLUMN_MEDIA_TAG_VALUE + ") " +
            "VALUES " +
            "	(?); ";

    static final String SELECT_MEDIA_TAG_ID_VALUE =

            "SELECT " + COLUMN_MEDIA_TAG_ID + ", " + COLUMN_MEDIA_TAG_VALUE + " " +
            "FROM " + TABLE_MEDIA_TAG + ";";

    static final String SELECT_MEDIA_FOR_HASH_X =

            "SELECT " + COLUMN_MEDIA_ID + ",  " +
            "	    " + COLUMN_MEDIA_FILE_NAME + ",  " +
            "	    " + COLUMN_MEDIA_DESCRIPTION + ",  " +
            "	    " + COLUMN_MEDIA_HASH + " " +
            "FROM " + TABLE_MEDIA + " " +
            "WHERE " + COLUMN_MEDIA_HASH + " = ? ";

    static final String INSERT_MEDIA_HASH_X =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA + " " +
            "	(" + COLUMN_MEDIA_HASH + ") " +
            "VALUES " +
            "	(?); ";

    static final String UPDATE_HASH_FOR_MEDIA_ID_X_Y =

            "UPDATE " + TABLE_MEDIA + " " +
            "SET " + COLUMN_MEDIA_HASH + " = ? " +
            "WHERE " + COLUMN_MEDIA_ID + " = ? ";

    static final String INSERT_MEDIA_PATH_X =

            "INSERT OR IGNORE INTO " + NwdContract.TABLE_MEDIA_PATH + " " +
            "	(" + NwdContract.COLUMN_MEDIA_PATH_VALUE + ") " +
            "VALUES " +
            "	(?); ";

    static final String SELECT_MEDIA_PATH_ID_FOR_PATH_X =

            "SELECT " + COLUMN_MEDIA_PATH_ID + " " +
            "FROM " + TABLE_MEDIA_PATH + " " +
            "WHERE " + COLUMN_MEDIA_PATH_VALUE + " = ?;";

    static final String INSERT_MEDIA_DEVICE_PATH_MID_DID_PID =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_DEVICE_PATH + " " +
            "	(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_DEVICE_ID + ", " + COLUMN_MEDIA_PATH_ID + ") " +
            "VALUES " +
            "	(?, ?, ?); ";

    static final String SELECT_MEDIA_WHERE_HASH_NOT_NULL_OR_WHITESPACE =

            "SELECT " + COLUMN_MEDIA_ID + ",  " +
            "	   " + COLUMN_MEDIA_FILE_NAME + ",  " +
            "	   " + COLUMN_MEDIA_DESCRIPTION + ",  " +
            "	   " + COLUMN_MEDIA_HASH + " " +
            "FROM " + TABLE_MEDIA + " " +
            "WHERE " + COLUMN_MEDIA_HASH + " IS NOT NULL AND trim(" + COLUMN_MEDIA_HASH + ", ' ') != ''; ";

    static final String INSERT_OR_IGNORE_MEDIA_TAGGING_X_Y =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_TAGGING + " " +
            "	(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_TAG_ID + ") " +
            "VALUES " +
            "	(?, ?); ";

    static final String UPDATE_MEDIA_TAGGING_TAGGED_UNTAGGED_WHERE_MEDIA_ID_AND_TAG_ID_W_X_Y_Z =

            "UPDATE " + TABLE_MEDIA_TAGGING + " " +
            "SET " + COLUMN_MEDIA_TAGGING_TAGGED_AT + " = MAX(IFNULL(" + COLUMN_MEDIA_TAGGING_TAGGED_AT + ", ''), ?), " +
            "	" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + " = MAX(IFNULL(" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + ", ''), ?) " +
            "WHERE " + COLUMN_MEDIA_ID + " = ? AND " + COLUMN_MEDIA_TAG_ID + " = ?; ";

//            "UPDATE " + TABLE_MEDIA_TAGGING + " " +
//            "SET " + COLUMN_MEDIA_TAGGING_TAGGED_AT + " = MAX(" + COLUMN_MEDIA_TAGGING_TAGGED_AT + ", ?), " +
//            "	" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + " = MAX(" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + ", ?) " +
//            "WHERE " + COLUMN_MEDIA_ID + " = ? AND " + COLUMN_MEDIA_TAG_ID + " = ?; ";

    static final String
            MNEMOSYNE_V5_GET_ACTIVE_TAGS_FOR_PATHS_FOR_DEVICE_NAME_X =

            "SELECT mt." + COLUMN_MEDIA_TAG_VALUE + ",  " +
            "	   mp." + COLUMN_MEDIA_PATH_VALUE + " " +
            "FROM " + TABLE_MEDIA_PATH + " mp " +
            "JOIN " + TABLE_MEDIA_DEVICE_PATH + " mdp " +
            "ON mp." + COLUMN_MEDIA_PATH_ID + " = mdp." + COLUMN_MEDIA_PATH_ID + " " +
            "JOIN " + TABLE_MEDIA_DEVICE + " md " +
            "ON mdp." + COLUMN_MEDIA_DEVICE_ID + " = md." + COLUMN_MEDIA_DEVICE_ID + " " +
            "JOIN " + TABLE_MEDIA + " m " +
            "ON mdp." + COLUMN_MEDIA_ID + " = m." + COLUMN_MEDIA_ID + " " +
            "JOIN " + TABLE_MEDIA_TAGGING + " mtg " +
            "ON m." + COLUMN_MEDIA_ID + " = mtg." + COLUMN_MEDIA_ID + " " +
            "JOIN " + TABLE_MEDIA_TAG + " mt " +
            "ON mtg." + COLUMN_MEDIA_TAG_ID + " = mt." + COLUMN_MEDIA_TAG_ID + "   " +
            "WHERE md." + COLUMN_MEDIA_DEVICE_DESCRIPTION + " = ? " +
            "AND mtg." + COLUMN_MEDIA_TAGGING_TAGGED_AT +
            " >= mtg." + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + "; ";

    static final String UPDATE_MEDIA_FILE_NAME_FOR_HASH_X_Y =

            "UPDATE " + TABLE_MEDIA + " " +
            "SET " + COLUMN_MEDIA_FILE_NAME + " = ? " +
            "WHERE " + COLUMN_MEDIA_HASH + " = ?; ";

    static final String UPDATE_MEDIA_DESCRIPTION_FOR_HASH_X_Y =

            "UPDATE " + TABLE_MEDIA + " " +
            "SET " + COLUMN_MEDIA_DESCRIPTION + " = ? " +
            "WHERE " + COLUMN_MEDIA_HASH + " = ?; ";

    static final String SELECT_MEDIA_TAGGINGS_FOR_HASH_X =

            "SELECT mt." + COLUMN_MEDIA_TAG_ID + ",  " +
            "	   mtg." + COLUMN_MEDIA_TAGGING_ID + ",  " +
            "	   m." + COLUMN_MEDIA_ID + ", " +
            "	   mt." + COLUMN_MEDIA_TAG_VALUE + ",  " +
            "	   m." + COLUMN_MEDIA_HASH + ",  " +
            "	   mtg." + COLUMN_MEDIA_TAGGING_TAGGED_AT + ",  " +
            "	   mtg." + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + " " +
            "FROM " + TABLE_MEDIA + " m " +
            "JOIN " + TABLE_MEDIA_TAGGING + " mtg " +
            "ON m." + COLUMN_MEDIA_ID + " = mtg." + COLUMN_MEDIA_ID + " " +
            "JOIN " + TABLE_MEDIA_TAG + " mt " +
            "ON mtg." + COLUMN_MEDIA_TAG_ID + " = mt." + COLUMN_MEDIA_TAG_ID + " " +
            "WHERE m." + COLUMN_MEDIA_HASH + " = ? " +
            "COLLATE NOCASE";

    static final String SELECT_MEDIA_DEVICE_PATHS_FOR_MEDIA_ID_X =

            "SELECT mdp." + COLUMN_MEDIA_DEVICE_PATH_ID + ", " +
            "	   m." + COLUMN_MEDIA_ID + ", " +
            "	   md." + COLUMN_MEDIA_DEVICE_ID + ", " +
            "	   mp." + COLUMN_MEDIA_PATH_ID + ", " +
            "	   mp." + COLUMN_MEDIA_PATH_VALUE + ", " +
            "	   md." + COLUMN_MEDIA_DEVICE_DESCRIPTION + ", " +
            "	   mdp." + COLUMN_MEDIA_DEVICE_PATH_VERIFIED_PRESENT + ", " +
            "	   mdp." + COLUMN_MEDIA_DEVICE_PATH_VERIFIED_MISSING + " " +
            "FROM " + TABLE_MEDIA + " m " +
            "JOIN " + TABLE_MEDIA_DEVICE_PATH + " mdp " +
            "ON m." + COLUMN_MEDIA_ID + " = mdp." + COLUMN_MEDIA_ID + " " +
            "JOIN " + TABLE_MEDIA_PATH + " mp " +
            "ON mp." + COLUMN_MEDIA_PATH_ID + " = mdp." + COLUMN_MEDIA_PATH_ID + " " +
            "JOIN " + TABLE_MEDIA_DEVICE + " md " +
            "ON md." + COLUMN_MEDIA_DEVICE_ID + " = mdp." + COLUMN_MEDIA_DEVICE_ID + " " +
            "WHERE m." + COLUMN_MEDIA_ID + " = ?; ";

    static final String INSERT_OR_IGNORE_MEDIA_DEVICE_PATH_X_Y_Z =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_DEVICE_PATH + " " +
            "	(" + COLUMN_MEDIA_ID + ", " +
            COLUMN_MEDIA_DEVICE_ID + ", " +
            COLUMN_MEDIA_PATH_ID + ") " +
            "VALUES " +
            "	(?, ?, ?); ";

    static final String SELECT_MEDIA_TAG_ID_FOR_VALUE_X =

            "SELECT " + COLUMN_MEDIA_TAG_ID + "  " +
            "FROM " + TABLE_MEDIA_TAG + " " +
            "WHERE " + COLUMN_MEDIA_TAG_VALUE + " = ? "; //+
            //"COLLATE NOCASE; "; //this may be our issue with tagging

    static final String SELECT_MEDIA_ID_FOR_HASH_X =

            "SELECT " + COLUMN_MEDIA_ID + " " +
            "FROM " + TABLE_MEDIA + " " +
            "WHERE " + COLUMN_MEDIA_HASH + " = ? " +
            "COLLATE NOCASE;";
    
    static final String CREATE_HIVE_ROOT =

            "CREATE TABLE " + TABLE_HIVE_ROOT + " ( " +
            "	" + COLUMN_HIVE_ROOT_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE, " +
            "	" + COLUMN_HIVE_ROOT_NAME + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_HIVE_ROOT_ACTIVATED_AT + " TEXT, " +
            "	" + COLUMN_HIVE_ROOT_DEACTIVATED_AT + " TEXT,  " +
            "	" + COLUMN_HIVE_ROOT_CREATED_AT + " TEXT, " +
            "	" + COLUMN_HIVE_ROOT_UPDATED_AT + " TEXT " +
            ") " ;

    static final String CREATE_HIVE_ROOT_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + COLUMN_HIVE_ROOT_CREATED_AT + "  " +
            "AFTER INSERT ON " + TABLE_HIVE_ROOT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_HIVE_ROOT + "  " +
            "	SET " + COLUMN_HIVE_ROOT_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_HIVE_ROOT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_HIVE_ROOT + "." + COLUMN_HIVE_ROOT_ID + " = NEW." + COLUMN_HIVE_ROOT_ID + "; " +
            "END " ;

    static final String CREATE_HIVE_ROOT_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + COLUMN_HIVE_ROOT_UPDATED_AT + " " +
            "AFTER UPDATE ON " + TABLE_HIVE_ROOT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_HIVE_ROOT + "  " +
            "	SET " + COLUMN_HIVE_ROOT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_HIVE_ROOT + "." + COLUMN_HIVE_ROOT_ID + " = NEW." + COLUMN_HIVE_ROOT_ID + "; " +
            "END " ;


    static final String SELECT_HIVE_ROOTS =

            "SELECT " + COLUMN_HIVE_ROOT_ID + ", " +
            "       " + COLUMN_HIVE_ROOT_NAME + ", " +
            "	    " + COLUMN_HIVE_ROOT_ACTIVATED_AT + ", " +
            "	    " + COLUMN_HIVE_ROOT_DEACTIVATED_AT + " " +
            "FROM " + TABLE_HIVE_ROOT + " ";

    static final String SELECT_ACTIVE_HIVE_ROOTS =

            "SELECT " + COLUMN_HIVE_ROOT_ID + ", " +
            "       " + COLUMN_HIVE_ROOT_NAME + ", " +
            "	    " + COLUMN_HIVE_ROOT_ACTIVATED_AT + ", " +
            "	    " + COLUMN_HIVE_ROOT_DEACTIVATED_AT + " " +
            "FROM " + TABLE_HIVE_ROOT + " " +
            "WHERE IFNULL(" + COLUMN_HIVE_ROOT_ACTIVATED_AT + ", '') >= IFNULL(" + COLUMN_HIVE_ROOT_DEACTIVATED_AT + ", '') ";

    static final String SELECT_DEACTIVATED_HIVE_ROOTS =

            "SELECT " + COLUMN_HIVE_ROOT_ID + ", " +
            "       " + COLUMN_HIVE_ROOT_NAME + ", " +
            "	    " + COLUMN_HIVE_ROOT_ACTIVATED_AT + ", " +
            "	    " + COLUMN_HIVE_ROOT_DEACTIVATED_AT + " " +
            "FROM " + TABLE_HIVE_ROOT + " " +
            "WHERE IFNULL(" + COLUMN_HIVE_ROOT_ACTIVATED_AT + ", '') < IFNULL(" + COLUMN_HIVE_ROOT_DEACTIVATED_AT + ", '') ";

    static final String INSERT_HIVE_ROOT_NAME_X =

            "INSERT OR IGNORE INTO " + TABLE_HIVE_ROOT + " " +
            "	(" + COLUMN_HIVE_ROOT_NAME + ") " +
            "VALUES " +
            "	(?) ";

    static final String DROP_SYNERGY_LIST_INSERT_TRIGGER =
            "DROP TRIGGER IF EXISTS Set" + TABLE_SYNERGY_LIST + "CreatedAt ";

    static final String CREATE_NEW_SYNERGY_LIST_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_LIST + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST + "  "
            + "SET " + COLUMN_SYNERGY_LIST_CREATED_AT + " = CURRENT_TIMESTAMP,  "
            + COLUMN_SYNERGY_LIST_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "WHERE " + TABLE_SYNERGY_LIST + "." + COLUMN_SYNERGY_LIST_ID + " = NEW." + COLUMN_SYNERGY_LIST_ID + "; "
            + "END ";

    static final String HIVE_ROOT_UPDATE_ACTIVATE_AT_DEACTIVATED_AT_FOR_NAME_X_Y_Z =

            "UPDATE " + TABLE_HIVE_ROOT + "  " +
            "SET " + COLUMN_HIVE_ROOT_ACTIVATED_AT + " = MAX(IFNULL(" + COLUMN_HIVE_ROOT_ACTIVATED_AT + ", ''), ?), " +
            "	" + COLUMN_HIVE_ROOT_DEACTIVATED_AT + " = MAX(IFNULL(" + COLUMN_HIVE_ROOT_DEACTIVATED_AT + ", ''), ?) " +
            "WHERE " + COLUMN_HIVE_ROOT_NAME + " = ?;  ";

    static final String HIVE_ROOT_SELECT_ID_ACTIVATED_AT_DEACTIVATED_AT_FOR_NAME =

            "SELECT " + COLUMN_HIVE_ROOT_ID + ", "
            + COLUMN_HIVE_ROOT_ACTIVATED_AT + ", "
            + COLUMN_HIVE_ROOT_DEACTIVATED_AT + " "
            + "FROM " + TABLE_HIVE_ROOT + " "
            + "WHERE " + COLUMN_HIVE_ROOT_NAME + " = ? ;";

    static final String SYNERGY_V5_SELECT_ARCHIVED_ITEMS_AND_TODOS_BY_POSITION_FOR_LIST_ID_X =

            "SELECT si." + COLUMN_SYNERGY_ITEM_ID + ", "
            + "	    si." + COLUMN_SYNERGY_ITEM_VALUE + ", "
            + "	    sli." + COLUMN_SYNERGY_LIST_ITEM_POSITION + ", "
            + "     sli." + COLUMN_SYNERGY_LIST_ITEM_ID + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_ID + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_COMPLETED_AT + ", "
            + "     std." +COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + " "
            + "FROM " + TABLE_SYNERGY_LIST_ITEM + " sli "
            + "JOIN " + TABLE_SYNERGY_ITEM + " si "
            + "ON sli." + COLUMN_SYNERGY_ITEM_ID + " = si." + COLUMN_SYNERGY_ITEM_ID + " "
            + "LEFT JOIN " + TABLE_SYNERGY_TO_DO + " std "
            + "ON sli." + COLUMN_SYNERGY_LIST_ITEM_ID + " = std." + COLUMN_SYNERGY_LIST_ITEM_ID + " "
            + "WHERE sli." + COLUMN_SYNERGY_LIST_ID + " = ? "
            + "AND std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + " < std." + COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + " "
            +   "ORDER BY sli." + COLUMN_SYNERGY_LIST_ITEM_POSITION + "; ";

    static final String SYNERGY_V5_SELECT_ACTIVE_ITEMS_AND_TODOS_BY_POSITION_FOR_LIST_ID_X =

            "SELECT si." + COLUMN_SYNERGY_ITEM_ID + ", "
            + "	    si." + COLUMN_SYNERGY_ITEM_VALUE + ", "
            + "	    sli." + COLUMN_SYNERGY_LIST_ITEM_POSITION + ", "
            + "     sli." + COLUMN_SYNERGY_LIST_ITEM_ID + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_ID + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + ", "
            + "     std." + COLUMN_SYNERGY_TO_DO_COMPLETED_AT + ", "
            + "     std." +COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + " "
            + "FROM " + TABLE_SYNERGY_LIST_ITEM + " sli "
            + "JOIN " + TABLE_SYNERGY_ITEM + " si "
            + "ON sli." + COLUMN_SYNERGY_ITEM_ID + " = si." + COLUMN_SYNERGY_ITEM_ID + " "
            + "LEFT JOIN " + TABLE_SYNERGY_TO_DO + " std "
            + "ON sli." + COLUMN_SYNERGY_LIST_ITEM_ID + " = std." + COLUMN_SYNERGY_LIST_ITEM_ID + " "
            + "WHERE sli." + COLUMN_SYNERGY_LIST_ID + " = ? "
            + "AND std." + COLUMN_SYNERGY_TO_DO_ACTIVATED_AT + " >= std." + COLUMN_SYNERGY_TO_DO_ARCHIVED_AT + " "
            +   "ORDER BY sli." + COLUMN_SYNERGY_LIST_ITEM_POSITION + "; ";

    ///////////////////////////////ARCHIVIST DDL////////////////////////////////////////////////////////////////////////////////////

    private static final String TABLE_SOURCE_LOCATION = "SourceLocation";



    static final String COLUMN_SOURCE_LOCATION_ID = "SourceLocationId";
    static final String COLUMN_SOURCE_LOCATION_VALUE = "SourceLocationValue";
    static final String COLUMN_SOURCE_LOCATION_CREATED_AT = "SourceLocationCreatedAt";
    static final String COLUMN_SOURCE_LOCATION_UPDATED_AT = "SourceLocationUpdatedAt";

    static final String CREATE_SOURCE_LOCATION =
            "CREATE TABLE " + TABLE_SOURCE_LOCATION + " ( " +
                    "	" + COLUMN_SOURCE_LOCATION_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_LOCATION_VALUE + " TEXT NOT NULL UNIQUE, " +
                    "	" + COLUMN_SOURCE_LOCATION_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_LOCATION_UPDATED_AT + " TEXT " +
                    ") ";

    static final String CREATE_SOURCE_LOCATION_CREATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_LOCATION_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE_LOCATION + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_LOCATION + "  " +
                    "	SET " + COLUMN_SOURCE_LOCATION_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_LOCATION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_LOCATION + "." + COLUMN_SOURCE_LOCATION_ID + " = NEW." + COLUMN_SOURCE_LOCATION_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_LOCATION_UPDATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_LOCATION_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE_LOCATION + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_LOCATION + "  " +
                    "	SET " + COLUMN_SOURCE_LOCATION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_LOCATION + "." + COLUMN_SOURCE_LOCATION_ID + " = NEW." + COLUMN_SOURCE_LOCATION_ID + "; " +
                    "END ";



    private static final String TABLE_SOURCE_ANNOTATION = "SourceAnnotation";

    private static final String COLUMN_SOURCE_ANNOTATION_ID = "SourceAnnotationId";
    private static final String COLUMN_SOURCE_ANNOTATION_VALUE = "SourceAnnotationValue";
    private static final String COLUMN_SOURCE_ANNOTATION_CREATED_AT = "SourceAnnotationCreatedAt";
    private static final String COLUMN_SOURCE_ANNOTATION_UPDATED_AT = "SourceAnnotationUpdatedAt";


    static final String CREATE_SOURCE_ANNOTATION =
            "CREATE TABLE " + TABLE_SOURCE_ANNOTATION + " ( " +
                    "	" + COLUMN_SOURCE_ANNOTATION_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_ANNOTATION_VALUE + " TEXT NOT NULL UNIQUE, " +
                    "	" + COLUMN_SOURCE_ANNOTATION_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_ANNOTATION_UPDATED_AT + " TEXT " +
                    ") ";

    static final String CREATE_SOURCE_ANNOTATION_CREATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_ANNOTATION_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE_ANNOTATION + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_ANNOTATION + "  " +
                    "	SET " + COLUMN_SOURCE_ANNOTATION_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_ANNOTATION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_ANNOTATION + "." + COLUMN_SOURCE_ANNOTATION_ID + " = NEW." + COLUMN_SOURCE_ANNOTATION_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_ANNOTATION_UPDATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_ANNOTATION_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE_ANNOTATION + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_ANNOTATION + "  " +
                    "	SET " + COLUMN_SOURCE_ANNOTATION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_ANNOTATION + "." + COLUMN_SOURCE_ANNOTATION_ID + " = NEW." + COLUMN_SOURCE_ANNOTATION_ID + "; " +
                    "END ";



    private static final String TABLE_SOURCE_TYPE = "SourceType";
    static final String COLUMN_SOURCE_TYPE_ID = "SourceTypeId";
    static final String COLUMN_SOURCE_TYPE_VALUE = "SourceTypeValue";
    private static final String COLUMN_SOURCE_TYPE_CREATED_AT = "SourceTypeCreatedAt";
    private static final String COLUMN_SOURCE_TYPE_UPDATED_AT = "SourceTypeUpdatedAt";



    static final String CREATE_SOURCE_TYPE =
            "CREATE TABLE " + TABLE_SOURCE_TYPE + " ( " +
                    "	" + COLUMN_SOURCE_TYPE_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_TYPE_VALUE + " TEXT NOT NULL UNIQUE, " +
                    "	" + COLUMN_SOURCE_TYPE_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_TYPE_UPDATED_AT + " TEXT " +
                    ") ";

    static final String CREATE_SOURCE_TYPE_CREATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_TYPE_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE_TYPE + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_TYPE + "  " +
                    "	SET " + COLUMN_SOURCE_TYPE_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_TYPE_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_TYPE + "." + COLUMN_SOURCE_TYPE_ID + " = NEW." + COLUMN_SOURCE_TYPE_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_TYPE_UPDATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_TYPE_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE_TYPE + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_TYPE + "  " +
                    "	SET " + COLUMN_SOURCE_TYPE_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_TYPE + "." + COLUMN_SOURCE_TYPE_ID + " = NEW." + COLUMN_SOURCE_TYPE_ID + "; " +
                    "END ";



    private static final String TABLE_SOURCE = "Source";
    static final String COLUMN_SOURCE_ID = "SourceId";
    static final String COLUMN_SOURCE_AUTHOR = "SourceAuthor";
    static final String COLUMN_SOURCE_DIRECTOR = "SourceDirector";
    static final String COLUMN_SOURCE_TITLE = "SourceTitle";
    static final String COLUMN_SOURCE_YEAR = "SourceYear";
    static final String COLUMN_SOURCE_URL = "SourceUrl";
    static final String COLUMN_SOURCE_RETRIEVAL_DATE = "SourceRetrievalDate";
    private static final String COLUMN_SOURCE_TAG = "SourceTag";
    private static final String COLUMN_SOURCE_CREATED_AT = "SourceCreatedAt";
    private static final String COLUMN_SOURCE_UPDATED_AT = "SourceUpdatedAt";


    static final String CREATE_SOURCE =
            "CREATE TABLE " + TABLE_SOURCE + " ( " +
                    "	" + COLUMN_SOURCE_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_TYPE_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SOURCE_TYPE + " (" + COLUMN_SOURCE_TYPE_ID + "), " +
                    "	" + COLUMN_SOURCE_AUTHOR + " TEXT, " +
                    "	" + COLUMN_SOURCE_DIRECTOR + " TEXT,  " +
                    "	" + COLUMN_SOURCE_TITLE + " TEXT,  " +
                    "	" + COLUMN_SOURCE_YEAR + " TEXT,  " +
                    "	" + COLUMN_SOURCE_URL + " TEXT,  " +
                    "	" + COLUMN_SOURCE_RETRIEVAL_DATE + " TEXT,  " +
                    "	" + COLUMN_SOURCE_TAG + " TEXT UNIQUE, " +
                    "	" + COLUMN_SOURCE_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_UPDATED_AT + " TEXT " +
                    ") ";

    static final String CREATE_SOURCE_CREATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE + "  " +
                    "	SET " + COLUMN_SOURCE_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE Source." + COLUMN_SOURCE_ID + " = NEW." + COLUMN_SOURCE_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_UPDATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE + "  " +
                    "	SET " + COLUMN_SOURCE_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE Source." + COLUMN_SOURCE_ID + " = NEW." + COLUMN_SOURCE_ID + "; " +
                    "END ";



    static final String TABLE_SOURCE_LOCATION_SUBSET = "SourceLocationSubset";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_ID = "SourceLocationSubsetId";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_VALUE = "SourceLocationSubsetValue";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_CREATED_AT = "SourceLocationSubsetCreatedAt";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_UPDATED_AT = "SourceLocationSubsetUpdatedAt";


    static final String CREATE_SOURCE_LOCATION_SUBSET =
            "CREATE TABLE " + TABLE_SOURCE_LOCATION_SUBSET + " ( " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_LOCATION_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SOURCE_LOCATION + " (" + COLUMN_SOURCE_LOCATION_ID + "),  " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_VALUE + " TEXT NOT NULL, " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_UPDATED_AT + " TEXT, " +
                    "	UNIQUE(" + COLUMN_SOURCE_LOCATION_ID + ", " + COLUMN_SOURCE_LOCATION_SUBSET_VALUE + ") " +
                    ") ";

    static final String CREATE_SOURCE_LOCATION_SUBSET_CREATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_LOCATION_SUBSET_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE_LOCATION_SUBSET + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_LOCATION_SUBSET + "  " +
                    "	SET " + COLUMN_SOURCE_LOCATION_SUBSET_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_LOCATION_SUBSET_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_LOCATION_SUBSET + "." + COLUMN_SOURCE_LOCATION_SUBSET_ID + " = NEW." + COLUMN_SOURCE_LOCATION_SUBSET_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_LOCATION_SUBSET_UPDATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_LOCATION_SUBSET_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE_LOCATION_SUBSET + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_LOCATION_SUBSET + "  " +
                    "	SET " + COLUMN_SOURCE_LOCATION_SUBSET_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_LOCATION_SUBSET + "." + COLUMN_SOURCE_LOCATION_SUBSET_ID + " = NEW." + COLUMN_SOURCE_LOCATION_SUBSET_ID + "; " +
                    "END ";


    static final String TABLE_SOURCE_LOCATION_SUBSET_ENTRY = "SourceLocationSubsetEntry";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID = "SourceLocationSubsetEntryId";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VALUE = "SourceLocationSubsetEntryValue";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_AT = "SourceLocationSubsetEntryVerifiedPresentAt";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_MISSING_AT = "SourceLocationSubsetEntryMissingAt";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_CREATED_AT = "SourceLocationSubsetEntryCreatedAt";
    static final String COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_UPDATED_AT = "SourceLocationSubsetEntryUpdatedAt";


    static final String CREATE_SOURCE_LOCATION_SUBSET_ENTRY =
            "CREATE TABLE " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + " ( " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SOURCE_LOCATION_SUBSET + " (" + COLUMN_SOURCE_LOCATION_SUBSET_ID + "), " +
                    "	" + COLUMN_SOURCE_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SOURCE + " (" + COLUMN_SOURCE_ID + "),  " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VALUE + " TEXT NOT NULL UNIQUE, " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_MISSING_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_UPDATED_AT + " TEXT, " +
                    "	UNIQUE(" + COLUMN_SOURCE_LOCATION_SUBSET_ID + ", " + COLUMN_SOURCE_ID + ", " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VALUE + ") " +
                    ") ";

    static final String CREATE_SOURCE_LOCATION_SUBSET_ENTRY_CREATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + "  " +
                    "	SET " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + "." + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID + " = NEW." + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_LOCATION_SUBSET_ENTRY_UPDATED_TRIGGER =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + "  " +
                    "	SET " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + "." + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID + " = NEW." + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID + "; " +
                    "END ";


    private static final String TABLE_SOURCE_EXCERPT = "SourceExcerpt";
    static final String COLUMN_SOURCE_EXCERPT_ID = "SourceExcerptId";
    static final String COLUMN_SOURCE_EXCERPT_VALUE = "SourceExcerptValue";
    static final String COLUMN_SOURCE_EXCERPT_PAGES = "SourceExcerptPages";
    static final String COLUMN_SOURCE_EXCERPT_BEGIN_TIME = "SourceExcerptBeginTime";
    static final String COLUMN_SOURCE_EXCERPT_END_TIME = "SourceExcerptEndTime";
    private static final String COLUMN_SOURCE_EXCERPT_CREATED_AT = "SourceExcerptCreatedAt";
    private static final String COLUMN_SOURCE_EXCERPT_UPDATED_AT = "SourceExcerptUpdatedAt";

    static final String CREATE_SOURCE_EXCERPT =
            "CREATE TABLE " + TABLE_SOURCE_EXCERPT + " ( " +
                    "	" + COLUMN_SOURCE_EXCERPT_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SOURCE + " (" + COLUMN_SOURCE_ID + "), " +
                    "	" + COLUMN_SOURCE_EXCERPT_VALUE + " TEXT, " +
                    "	" + COLUMN_SOURCE_EXCERPT_PAGES + " TEXT,  " +
                    "	" + COLUMN_SOURCE_EXCERPT_BEGIN_TIME + " TEXT,  " +
                    "	" + COLUMN_SOURCE_EXCERPT_END_TIME + " TEXT,  " +
                    "	" + COLUMN_SOURCE_EXCERPT_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_EXCERPT_UPDATED_AT + " TEXT " +
                    ") ";

    static final String CREATE_SOURCE_EXCERPT_CREATED_TRIGGER  =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_EXCERPT_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE_EXCERPT + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_EXCERPT + "  " +
                    "	SET " + COLUMN_SOURCE_EXCERPT_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_EXCERPT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_EXCERPT + "." + COLUMN_SOURCE_EXCERPT_ID + " = NEW." + COLUMN_SOURCE_EXCERPT_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_EXCERPT_UPDATED_TRIGGER  =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_EXCERPT_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE_EXCERPT + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_EXCERPT + "  " +
                    "	SET " + COLUMN_SOURCE_EXCERPT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_EXCERPT + "." + COLUMN_SOURCE_EXCERPT_ID + " = NEW." + COLUMN_SOURCE_EXCERPT_ID + "; " +
                    "END ";


    private static final String TABLE_SOURCE_EXCERPT_ANNOTATION = "SourceExcerptAnnotation";
    private static final String COLUMN_SOURCE_EXCERPT_ANNOTATION_ID = "SourceExcerptAnnotationId";
    private static final String COLUMN_SOURCE_EXCERPT_ANNOTATION_LINKED_AT = "SourceExcerptAnnotationLinkedAt";
    private static final String COLUMN_SOURCE_EXCERPT_ANNOTATION_UNLINKED_AT = "SourceExcerptAnnotationUnlinkedAt";
    private static final String COLUMN_SOURCE_EXCERPT_ANNOTATION_CREATED_AT = "SourceExcerptAnnotationCreatedAt";
    private static final String COLUMN_SOURCE_EXCERPT_ANNOTATION_UPDATED_AT = "SourceExcerptAnnotationUpdatedAt";

    static final String CREATE_SOURCE_EXCERPT_ANNOTATION  =
            "CREATE TABLE " + TABLE_SOURCE_EXCERPT_ANNOTATION + " ( " +
                    "	" + COLUMN_SOURCE_EXCERPT_ANNOTATION_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_EXCERPT_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SOURCE_EXCERPT + " (" + COLUMN_SOURCE_EXCERPT_ID + "),  " +
                    "	" + COLUMN_SOURCE_ANNOTATION_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SOURCE_ANNOTATION + " (" + COLUMN_SOURCE_ANNOTATION_ID + "), " +
                    "	" + COLUMN_SOURCE_EXCERPT_ANNOTATION_LINKED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_EXCERPT_ANNOTATION_UNLINKED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_EXCERPT_ANNOTATION_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_EXCERPT_ANNOTATION_UPDATED_AT + " TEXT,  " +
                    "	UNIQUE(" + COLUMN_SOURCE_EXCERPT_ID + ", " + COLUMN_SOURCE_ANNOTATION_ID + ") " +
                    ") ";

    static final String CREATE_SOURCE_EXCERPT_ANNOTATION_CREATED_TRIGGER  =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_EXCERPT_ANNOTATION_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE_EXCERPT_ANNOTATION + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_EXCERPT_ANNOTATION + "  " +
                    "	SET " + COLUMN_SOURCE_EXCERPT_ANNOTATION_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_EXCERPT_ANNOTATION_UPDATED_AT + " = CURRENT_TIMESTAMP, " +
                    "		   " + COLUMN_SOURCE_EXCERPT_ANNOTATION_LINKED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_EXCERPT_ANNOTATION + "." + COLUMN_SOURCE_EXCERPT_ANNOTATION_ID + " = NEW." + COLUMN_SOURCE_EXCERPT_ANNOTATION_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_EXCERPT_ANNOTATION_UPDATED_TRIGGER  =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_EXCERPT_ANNOTATION_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE_EXCERPT_ANNOTATION + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_EXCERPT_ANNOTATION + "  " +
                    "	SET " + COLUMN_SOURCE_EXCERPT_ANNOTATION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_EXCERPT_ANNOTATION + "." + COLUMN_SOURCE_EXCERPT_ANNOTATION_ID + " = NEW." + COLUMN_SOURCE_EXCERPT_ANNOTATION_ID + "; " +
                    "END ";

    private static final String TABLE_SOURCE_EXCERPT_TAGGING = "SourceExcerptTagging";
    static final String COLUMN_SOURCE_EXCERPT_TAGGING_ID = "SourceExcerptTaggingId";
    static final String COLUMN_SOURCE_EXCERPT_TAGGING_TAGGED_AT = "SourceExcerptTaggingTaggedAt";
    static final String COLUMN_SOURCE_EXCERPT_TAGGING_UNTAGGED_AT = "SourceExcerptTaggingUntaggedAt";
    private static final String COLUMN_SOURCE_EXCERPT_TAGGING_CREATED_AT = "SourceExcerptTaggingCreatedAt";
    private static final String COLUMN_SOURCE_EXCERPT_TAGGING_UPDATED_AT = "SourceExcerptTaggingUpdatedAt";

    static final String CREATE_SOURCE_EXCERPT_TAGGING  =
            "CREATE TABLE " + TABLE_SOURCE_EXCERPT_TAGGING + " ( " +
                    "	" + COLUMN_SOURCE_EXCERPT_TAGGING_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
                    "	" + COLUMN_SOURCE_EXCERPT_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SOURCE_EXCERPT + " (" + COLUMN_SOURCE_EXCERPT_ID + "),  " +
                    "	" + COLUMN_MEDIA_TAG_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_TAG + " (" + COLUMN_MEDIA_TAG_ID + "), " +
                    "	" + COLUMN_SOURCE_EXCERPT_TAGGING_TAGGED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_EXCERPT_TAGGING_UNTAGGED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_EXCERPT_TAGGING_CREATED_AT + " TEXT, " +
                    "	" + COLUMN_SOURCE_EXCERPT_TAGGING_UPDATED_AT + " TEXT,  " +
                    "	UNIQUE(" + COLUMN_SOURCE_EXCERPT_ID + ", " + COLUMN_MEDIA_TAG_ID + ") " +
                    ") ";

    static final String CREATE_SOURCE_EXCERPT_TAGGING_CREATED_TRIGGER  =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_EXCERPT_TAGGING_CREATED_AT + "  " +
                    "AFTER INSERT ON " + TABLE_SOURCE_EXCERPT_TAGGING + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_EXCERPT_TAGGING + "  " +
                    "	SET " + COLUMN_SOURCE_EXCERPT_TAGGING_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
                    "		   " + COLUMN_SOURCE_EXCERPT_TAGGING_UPDATED_AT + " = CURRENT_TIMESTAMP, " +
                    "		   " + COLUMN_SOURCE_EXCERPT_TAGGING_TAGGED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_EXCERPT_TAGGING + "." + COLUMN_SOURCE_EXCERPT_TAGGING_ID + " = NEW." + COLUMN_SOURCE_EXCERPT_TAGGING_ID + "; " +
                    "END ";

    static final String CREATE_SOURCE_EXCERPT_TAGGING_UPDATED_TRIGGER  =
            "CREATE TRIGGER Set" + COLUMN_SOURCE_EXCERPT_TAGGING_UPDATED_AT + " " +
                    "AFTER UPDATE ON " + TABLE_SOURCE_EXCERPT_TAGGING + " " +
                    "BEGIN " +
                    "UPDATE " + TABLE_SOURCE_EXCERPT_TAGGING + "  " +
                    "	SET " + COLUMN_SOURCE_EXCERPT_TAGGING_UPDATED_AT + " = CURRENT_TIMESTAMP " +
                    "	WHERE " + TABLE_SOURCE_EXCERPT_TAGGING + "." + COLUMN_SOURCE_EXCERPT_TAGGING_ID + " = NEW." + COLUMN_SOURCE_EXCERPT_TAGGING_ID + "; " +
                    "END ";



    static final String INSERT_OR_IGNORE_SOURCE_TYPE_VALUE =
            "INSERT OR IGNORE INTO " + TABLE_SOURCE_TYPE + " " +
                    "	(" + COLUMN_SOURCE_TYPE_VALUE + ") " +
                    "VALUES " +
                    "	(?); ";


    static final String SELECT_TYPE_ID_TYPE_VALUE_FROM_SOURCE_TYPE =

            "SELECT " + NwdContract.COLUMN_SOURCE_TYPE_ID + ", " +
                    "	    " + NwdContract.COLUMN_SOURCE_TYPE_VALUE + " " +
                    "FROM " + NwdContract.TABLE_SOURCE_TYPE + "; ";

    static final String INSERT_SOURCE_T_U_V_W_X_Y_Z =

            "INSERT OR IGNORE INTO " + TABLE_SOURCE + " " +
            "	(" + COLUMN_SOURCE_TYPE_ID + ", " +
            "	 " + COLUMN_SOURCE_TITLE + ", " +
            "	 " + COLUMN_SOURCE_AUTHOR + ", " +
            "	 " + COLUMN_SOURCE_DIRECTOR + ", " +
            "	 " + COLUMN_SOURCE_YEAR + ", " +
            "	 " + COLUMN_SOURCE_URL + ", " +
            "	 " + COLUMN_SOURCE_RETRIEVAL_DATE + ") " +
            "VALUES " +
            "	(?,?,?,?,?,?,?); ";

    static final String INSERT_OR_IGNORE_SOURCE_EXCERPT_SRCID_EXVAL_BTIME_ETIME_PGS_V_W_X_Y_Z =

//            "INSERT OR IGNORE INTO " + TABLE_SOURCE_EXCERPT + " " + //testing
            "INSERT INTO " + TABLE_SOURCE_EXCERPT + " " +
            "	(" +
                    COLUMN_SOURCE_ID + ", " +
                    COLUMN_SOURCE_EXCERPT_VALUE + ", " +
                    COLUMN_SOURCE_EXCERPT_BEGIN_TIME + ", " +
                    COLUMN_SOURCE_EXCERPT_END_TIME + ", " +
                    COLUMN_SOURCE_EXCERPT_PAGES + ") " +
            "VALUES  " +
            "	(?, ?, ?, ?, ?); ";

    static final String SELECT_SOURCES =

            "SELECT " + COLUMN_SOURCE_ID + ", " +
                    "	   " + COLUMN_SOURCE_TYPE_ID + ", " +
                    "	   " + COLUMN_SOURCE_TITLE + ", " +
                    "	   " + COLUMN_SOURCE_AUTHOR + ", " +
                    "	   " + COLUMN_SOURCE_DIRECTOR + ", " +
                    "	   " + COLUMN_SOURCE_YEAR + ", " +
                    "	   " + COLUMN_SOURCE_URL + ", " +
                    "	   " + COLUMN_SOURCE_RETRIEVAL_DATE + "  " +
                    "FROM " + TABLE_SOURCE + " ;";

    static final String SELECT_SOURCES_BY_TYPE_ID_X =

            "SELECT " + COLUMN_SOURCE_ID + ", " +
            "	   " + COLUMN_SOURCE_TYPE_ID + ", " +
            "	   " + COLUMN_SOURCE_TITLE + ", " +
            "	   " + COLUMN_SOURCE_AUTHOR + ", " +
            "	   " + COLUMN_SOURCE_DIRECTOR + ", " +
            "	   " + COLUMN_SOURCE_YEAR + ", " +
            "	   " + COLUMN_SOURCE_URL + ", " +
            "	   " + COLUMN_SOURCE_RETRIEVAL_DATE + "  " +
            "FROM " + TABLE_SOURCE + " " +
            "WHERE " + COLUMN_SOURCE_TYPE_ID + " = ?; ";

    static final String SELECT_SOURCE_EXCERPT_BY_ID =

            "SELECT " + COLUMN_SOURCE_EXCERPT_ID + ", " +
            "	    " + COLUMN_SOURCE_ID + ", " +
            "	    " + COLUMN_SOURCE_EXCERPT_VALUE + ", " +
            "	    " + COLUMN_SOURCE_EXCERPT_PAGES + ", " +
            "	    " + COLUMN_SOURCE_EXCERPT_BEGIN_TIME + ", " +
            "	    " + COLUMN_SOURCE_EXCERPT_END_TIME + " " +
            "FROM " + TABLE_SOURCE_EXCERPT + "  " +
            "WHERE " + COLUMN_SOURCE_EXCERPT_ID + " = ? ; ";

    static final String SELECT_SOURCE_EXCERPTS_FOR_SOURCE_ID_X =

            "SELECT " + COLUMN_SOURCE_EXCERPT_ID + ", " +
            "	    " + COLUMN_SOURCE_ID + ", " +
            "	    " + COLUMN_SOURCE_EXCERPT_VALUE + ", " +
            "	    " + COLUMN_SOURCE_EXCERPT_PAGES + ", " +
            "	    " + COLUMN_SOURCE_EXCERPT_BEGIN_TIME + ", " +
            "	    " + COLUMN_SOURCE_EXCERPT_END_TIME + " " +
            "FROM " + TABLE_SOURCE_EXCERPT + "  " +
            "WHERE " + COLUMN_SOURCE_ID + " = ? ; ";

    public static final String INSERT_OR_IGNORE_EXCERPT_TAGGING_X_Y =

            "INSERT OR IGNORE INTO " + TABLE_SOURCE_EXCERPT_TAGGING + " " +
            "	(" + COLUMN_SOURCE_EXCERPT_ID + ", " + COLUMN_MEDIA_TAG_ID + ") " +
            "VALUES " +
            "	(?, ?); ";

    public static final String SELECT_ARCHIVIST_SOURCE_EXCERPT_TAGGINGS_FOR_EXID =

            "SELECT mt." + COLUMN_MEDIA_TAG_ID + ", " +
            "	   sext." + COLUMN_SOURCE_EXCERPT_TAGGING_ID + ", " +
            "	   sext." + COLUMN_SOURCE_EXCERPT_ID + ", " +
            "	   mt." + COLUMN_MEDIA_TAG_VALUE + ", " +
            "	   sext." + COLUMN_SOURCE_EXCERPT_TAGGING_TAGGED_AT + ", " +
            "	   sext." + COLUMN_SOURCE_EXCERPT_TAGGING_UNTAGGED_AT + " " +
            "FROM " + TABLE_SOURCE_EXCERPT_TAGGING + " sext " +
            "JOIN " + TABLE_MEDIA_TAG + " mt  " +
            "ON sext." + COLUMN_MEDIA_TAG_ID + " = mt." + COLUMN_MEDIA_TAG_ID + " " +
            "WHERE sext." + COLUMN_SOURCE_EXCERPT_ID + " = ?; ";

    public static final String UPDATE_EXCERPT_TAGGING_TAGGED_UNTAGGED_WHERE_EXID_AND_TGID_W_X_Y_Z =

            "UPDATE " + TABLE_SOURCE_EXCERPT_TAGGING + "  " +
            "SET " + COLUMN_SOURCE_EXCERPT_TAGGING_TAGGED_AT + " = MAX(IFNULL(" + COLUMN_SOURCE_EXCERPT_TAGGING_TAGGED_AT + ", ''), ?), " +
            "	" + COLUMN_SOURCE_EXCERPT_TAGGING_UNTAGGED_AT + " = MAX(IFNULL(" + COLUMN_SOURCE_EXCERPT_TAGGING_UNTAGGED_AT + ", ''), ?) " +
            "WHERE " + COLUMN_SOURCE_EXCERPT_ID + " = ?  " +
            "AND " + COLUMN_MEDIA_TAG_ID + " = ? ; ";

    public static final String INSERT_OR_IGNORE_SOURCE_LOCATION_VALUE =

            "INSERT OR IGNORE INTO " + TABLE_SOURCE_LOCATION + " " +
            "	(" + COLUMN_SOURCE_LOCATION_VALUE + ") " +
            "VALUES " +
            "	(?); ";

    public static final String SELECT_LOCATION_ID_LOCATION_VALUE_FROM_SOURCE_LOCATION =

            "SELECT " + COLUMN_SOURCE_LOCATION_ID + ", " +
            "	    " + COLUMN_SOURCE_LOCATION_VALUE + " " +
            "FROM " + TABLE_SOURCE_LOCATION + "; ";

    public static final String INSERT_OR_IGNORE_SOURCE_LOCATION_SUBSET_FOR_LOCATION_ID_AND_SUBSET_VALUE_X_Y =

            "INSERT OR IGNORE INTO " + TABLE_SOURCE_LOCATION_SUBSET + "  " +
            "	(" + COLUMN_SOURCE_LOCATION_ID + ",  " +
            "	 " + COLUMN_SOURCE_LOCATION_SUBSET_VALUE + ") " +
            "VALUES  " +
            "	(?,?); ";

    public static final String SELECT_SOURCE_LOCATION_SUBSETS_BY_LOCATION_ID_X =

            "SELECT " + COLUMN_SOURCE_LOCATION_SUBSET_ID + ", " +
            "	   " + COLUMN_SOURCE_LOCATION_ID + ", " +
            "	   " + COLUMN_SOURCE_LOCATION_SUBSET_VALUE + " " +
            "FROM " + TABLE_SOURCE_LOCATION_SUBSET + " " +
            "WHERE " + COLUMN_SOURCE_LOCATION_ID + " = ? ; ";

    public static final String INSERT_OR_IGNORE_INTO_SOURCE_LOCATION_SUBSET_ENTRY_VALUES_SUBSET_ID_SOURCE_ID_ENTRY_VALUE_X_Y_Z =

            "INSERT OR IGNORE INTO " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + " " +
            "	(" + COLUMN_SOURCE_LOCATION_SUBSET_ID + ", " +
            "	 " + COLUMN_SOURCE_ID + ", " +
            "	 " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VALUE + ") " +
            " VALUES (?,?,?); ";

    public static final String SELECT_SOURCE_LOCATION_SUBSET_ENTRIES_FOR_SOURCE_ID_X =

            "SELECT slse." + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID + ", " +
            "	   slse." + COLUMN_SOURCE_LOCATION_SUBSET_ID + ", " +
            "	   sl." + COLUMN_SOURCE_LOCATION_VALUE + ", " +
            "	   sls." + COLUMN_SOURCE_LOCATION_SUBSET_VALUE + ", " +
            "	   slse." + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VALUE + ", " +
            "	   slse." + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_AT + ", " +
            "	   slse." + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_MISSING_AT + "	    " +
            "FROM " + TABLE_SOURCE_LOCATION + " sl " +
            "JOIN " + TABLE_SOURCE_LOCATION_SUBSET + " sls " +
            "ON sl." + COLUMN_SOURCE_LOCATION_ID + " = sls." + COLUMN_SOURCE_LOCATION_ID + " " +
            "JOIN " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + " slse " +
            "ON sls." + COLUMN_SOURCE_LOCATION_SUBSET_ID + " = slse." + COLUMN_SOURCE_LOCATION_SUBSET_ID + " " +
            "WHERE slse." + COLUMN_SOURCE_ID + " = ? ; ";

    public static final String SELECT_SOURCE_LOCATION_SUBSET_ENTRY_ID_FOR_SUBSET_ID_AND_SOURCE_ID_AND_ENTRY_VALUE_X_Y_Z =

            "SELECT " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID + " " +
            "FROM " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + " " +
            "WHERE " + COLUMN_SOURCE_LOCATION_SUBSET_ID + " = ? " +
            "AND " + COLUMN_SOURCE_ID + " = ? " +
            "AND " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VALUE + " = ?; ";

    public static final String UPDATE_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_VERIFIED_MISSING_FOR_ID_X_Y_Z =

            "UPDATE " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + "  " +
            "SET " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_AT + " = MAX(IFNULL(" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_AT + ", ''), ?), " +
            "	" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_MISSING_AT + " = MAX(IFNULL(" + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_MISSING_AT + ", ''), ?) " +
            "WHERE " + COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID + " = ? ; " ;

    public static final String DELETE_ARCHIVIST_SOURCE_EXCERPT_ANNOTATIONS_FOR_EXID =

            "DELETE FROM " + TABLE_SOURCE_EXCERPT_ANNOTATION + "  " +
            "WHERE " + COLUMN_SOURCE_EXCERPT_ID + " = ? ; ";

    public static final String DELETE_ARCHIVIST_SOURCE_EXCERPT_TAGGINGS_FOR_EXID =

            "DELETE FROM " + TABLE_SOURCE_EXCERPT_TAGGING + "  " +
                    "WHERE " + COLUMN_SOURCE_EXCERPT_ID + " = ? ; ";

    public static final String DELETE_ARCHIVIST_SOURCE_EXCERPTS_FOR_SOURCE_ID =

            "DELETE FROM " + TABLE_SOURCE_EXCERPT + "  " +
                    "WHERE " + COLUMN_SOURCE_ID + " = ? ; ";

    public static final String DELETE_ARCHIVIST_SOURCE_LOCATION_SUBSET_ENTRIES_FOR_SOURCE_ID =

            "DELETE FROM " + TABLE_SOURCE_LOCATION_SUBSET_ENTRY + "  " +
                    "WHERE " + COLUMN_SOURCE_ID + " = ? ; ";

    public static final String DELETE_ARCHIVIST_SOURCE_FOR_SOURCE_ID =

            "DELETE FROM " + TABLE_SOURCE + "  " +
                    "WHERE " + COLUMN_SOURCE_ID + " = ? ; ";

    public static final String SELECT_SOURCE_FOR_TID_TTL_AUT_DIR_YR_URL_RDT_TG =

            "SELECT " + COLUMN_SOURCE_ID + ", " +
            "       " + COLUMN_SOURCE_TYPE_ID + ", " +
            "       " + COLUMN_SOURCE_TITLE + ", " +
            "       " + COLUMN_SOURCE_AUTHOR + ", " +
            "       " + COLUMN_SOURCE_DIRECTOR + ", " +
            "       " + COLUMN_SOURCE_YEAR + ", " +
            "       " + COLUMN_SOURCE_URL + ", " +
            "       " + COLUMN_SOURCE_RETRIEVAL_DATE + ", " +
            "       " + COLUMN_SOURCE_TAG + " " +
            "FROM " + TABLE_SOURCE + "  " +
            "WHERE " + COLUMN_SOURCE_TYPE_ID + " = ?  " +
            "AND TRIM(IFNULL(" + COLUMN_SOURCE_TITLE + ", '')) = TRIM(IFNULL(?, '')) " +
            "AND TRIM(IFNULL(" + COLUMN_SOURCE_AUTHOR + ", '')) = TRIM(IFNULL(?, '')) " +
            "AND TRIM(IFNULL(" + COLUMN_SOURCE_DIRECTOR + ", '')) = TRIM(IFNULL(?, '')) " +
            "AND TRIM(IFNULL(" + COLUMN_SOURCE_YEAR + ", '')) = TRIM(IFNULL(?, '')) " +
            "AND TRIM(IFNULL(" + COLUMN_SOURCE_URL + ", '')) = TRIM(IFNULL(?, '')) " +
            "AND TRIM(IFNULL(" + COLUMN_SOURCE_RETRIEVAL_DATE + ", '')) = TRIM(IFNULL(?, '')) " +
            "AND TRIM(IFNULL(" + COLUMN_SOURCE_TAG + ", '')) = TRIM(IFNULL(?, '')); ";

    public static final String SELECT_SOURCE_LOCATION_ID_FOR_VALUE_X =

            "SELECT " + COLUMN_SOURCE_LOCATION_ID + " " +
            "FROM " + TABLE_SOURCE_LOCATION + " " +
            "WHERE " + COLUMN_SOURCE_LOCATION_VALUE + " = ? ; ";

    public static final String SELECT_SOURCE_LOCATION_SUBSET_ID_FOR_LOCATION_ID_AND_SUBSET_VALUE_X_Y =

            "SELECT " + COLUMN_SOURCE_LOCATION_SUBSET_ID + " " +
            "FROM " + TABLE_SOURCE_LOCATION_SUBSET + " " +
            "WHERE " + COLUMN_SOURCE_LOCATION_ID + " = ?  " +
            "AND " + COLUMN_SOURCE_LOCATION_SUBSET_VALUE + " = ? ; ";

    public static final String SELECT_SOURCE_EXCERPT_ID_FOR_SRCID_EXVAL_BTIME_ETIME_PGS_V_W_X_Y_Z =

            "SELECT " + COLUMN_SOURCE_EXCERPT_ID + " " +
            "FROM " + TABLE_SOURCE_EXCERPT + " " +
            "WHERE " + COLUMN_SOURCE_ID + " = ? " +
            "AND " + COLUMN_SOURCE_EXCERPT_VALUE + " = ?  " +
            "AND " + COLUMN_SOURCE_EXCERPT_BEGIN_TIME + " = ? " +
            "AND " + COLUMN_SOURCE_EXCERPT_END_TIME + " = ? " +
            "AND " + COLUMN_SOURCE_EXCERPT_PAGES + " = ? ; ";
}
