package com.nineworldsdeep.gauntlet.sqlite;

/**
 * Created by brent on 5/27/16.
 */
public class NwdContract {

    //region tables
    public static final String TABLE_DISPLAY_NAME = "DisplayName";
    public static final String TABLE_PATH = "Path";
    public static final String TABLE_HASH = "Hash";
    public static final String TABLE_DEVICE = "Device";
    public static final String TABLE_FILE = "File";
    public static final String TABLE_TAG = "Tag";
    public static final String TABLE_FILE_TAGS = "FileTags";
    public static final String TABLE_AUDIO_TRANSCRIPT = "AudioTranscript";
//    public static final String TABLE_LOCAL_CONFIG = "LocalConfig";

    public static final String TABLE_SYNERGY_LIST = "SynergyList";
    public static final String TABLE_SYNERGY_LIST_ITEM = "SynergyListItem";
    public static final String TABLE_SYNERGY_ITEM = "SynergyItem";
    public static final String TABLE_SYNERGY_TO_DO = "SynergyToDo";
    //endregion

    //region columns

    //region V4a

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
//    public static final String COLUMN_LOCAL_CONFIG_ID = "LocalConfigId";
//    public static final String COLUMN_LOCAL_CONFIG_KEY = "LocalConfigKey";
//    public static final String COLUMN_LOCAL_CONFIG_VALUE = "LocalConfigValue";

    //endregion

    public static final String COLUMN_MEDIA_DEVICE_PATH_VERIFIED_PRESENT =
            "MediaDevicePathVerifiedPresent";
    public static final String COLUMN_MEDIA_DEVICE_PATH_VERIFIED_MISSING =
            "MediaDevicePathVerifiedMissing";

    public static final String COLUMN_COUNT = "Count";

    public static final String COLUMN_SYNERGY_LIST_ID = "SynergyListId";
    public static final String COLUMN_SYNERGY_LIST_NAME = "SynergyListName";
    public static final String COLUMN_SYNERGY_LIST_ACTIVATED_AT = "SynergyListActivatedAt";
    public static final String COLUMN_SYNERGY_LIST_SHELVED_AT = "SynergyListShelvedAt";
    public static final String COLUMN_SYNERGY_LIST_CREATED_AT = "SynergyListCreatedAt";
    public static final String COLUMN_SYNERGY_LIST_UPDATED_AT = "SynergyListUpdatedAt";

    public static final String COLUMN_SYNERGY_LIST_ITEM_ID = "SynergyListItemId";
    public static final String COLUMN_SYNERGY_LIST_ITEM_CREATED_AT = "SynergyListItemCreatedAt";
    public static final String COLUMN_SYNERGY_LIST_ITEM_UPDATED_AT  = "SynergyListItemUpdatedAt";
    public static final String COLUMN_SYNERGY_LIST_ITEM_POSITION = "SynergyListItemPosition";

    public static final String COLUMN_SYNERGY_ITEM_ID = "SynergyItemId";
    public static final String COLUMN_SYNERGY_ITEM_VALUE = "SynergyItemValue";
    public static final String COLUMN_SYNERGY_ITEM_CREATED_AT = "SynergyItemCreatedAt";
    public static final String COLUMN_SYNERGY_ITEM_UPDATED_AT = "SynergyItemUpdatedAt";

    public static final String COLUMN_SYNERGY_TO_DO_ID = "SynergyToDoId";
    public static final String COLUMN_SYNERGY_TO_DO_ACTIVATED_AT = "SynergyToDoActivatedAt";
    public static final String COLUMN_SYNERGY_TO_DO_COMPLETED_AT = "SynergyToDoCompletedAt";
    public static final String COLUMN_SYNERGY_TO_DO_ARCHIVED_AT = "SynergyToDoArchivedAt";
    public static final String COLUMN_SYNERGY_TO_DO_CREATED_AT = "SynergyToDoCreatedAt";
    public static final String COLUMN_SYNERGY_TO_DO_UPDATED_AT = "SynergyToDoUpdatedAt";

    //endregion

    //region SynergyV5_DDL

    public static final String DROP_SYNERGY_LIST = "DROP TABLE SynergyList;";
    public static final String DROP_SYNERGY_TO_DO = "DROP TABLE SynergyToDo;";
    public static final String DROP_SYNERGY_LIST_ITEM =
            "DROP TABLE SynergyListItem;";

    public static final String CREATE_SYNERGY_LIST =

        "CREATE TABLE " + TABLE_SYNERGY_LIST + " ( "
            + COLUMN_SYNERGY_LIST_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  "
            + COLUMN_SYNERGY_LIST_NAME + " TEXT NOT NULL UNIQUE, "
            + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " TEXT, "
            + COLUMN_SYNERGY_LIST_SHELVED_AT + " TEXT, "
            + COLUMN_SYNERGY_LIST_CREATED_AT + " TEXT, "
            + COLUMN_SYNERGY_LIST_UPDATED_AT + " TEXT "
        + ") ";

    public static final String CREATE_SYNERGY_LIST_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_LIST + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST + "  "
                + "SET " + COLUMN_SYNERGY_LIST_CREATED_AT + " = CURRENT_TIMESTAMP,  "
                       + COLUMN_SYNERGY_LIST_UPDATED_AT + " = CURRENT_TIMESTAMP, "
                       + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " = CURRENT_TIMESTAMP "
                + "WHERE " + TABLE_SYNERGY_LIST + "." + COLUMN_SYNERGY_LIST_ID + " = NEW." + COLUMN_SYNERGY_LIST_ID + "; "
            + "END ";

    public static final String CREATE_SYNERGY_LIST_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST + "UpdatedAt  "
            + "AFTER UPDATE ON " + TABLE_SYNERGY_LIST + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST + " "
                + "SET " + COLUMN_SYNERGY_LIST_UPDATED_AT + " = CURRENT_TIMESTAMP "
                + "WHERE " + TABLE_SYNERGY_LIST + "." + COLUMN_SYNERGY_LIST_ID + " = NEW." + COLUMN_SYNERGY_LIST_ID + "; "
            + "END ";



    public static final String CREATE_SYNERGY_ITEM =

            "CREATE TABLE " + TABLE_SYNERGY_ITEM + " ("
            + COLUMN_SYNERGY_ITEM_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  "
            + COLUMN_SYNERGY_ITEM_VALUE + " TEXT NOT NULL UNIQUE, "
            + COLUMN_SYNERGY_ITEM_CREATED_AT + " TEXT, "
            + COLUMN_SYNERGY_ITEM_UPDATED_AT + " TEXT "
            + ") ";

    public static final String CREATE_SYNERGY_ITEM_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_ITEM + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_ITEM + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_ITEM + "  "
            + "	SET " + COLUMN_SYNERGY_ITEM_CREATED_AT + " = CURRENT_TIMESTAMP,  "
            + "		   " + COLUMN_SYNERGY_ITEM_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "	WHERE " + TABLE_SYNERGY_ITEM + "." + COLUMN_SYNERGY_ITEM_ID + " = NEW." + COLUMN_SYNERGY_ITEM_ID + "; "
            + "END ";

    public static final String CREATE_SYNERGY_ITEM_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_ITEM + "UpdatedAt "
            + "AFTER UPDATE ON " + TABLE_SYNERGY_ITEM + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_ITEM + "  "
            + "	SET " + COLUMN_SYNERGY_ITEM_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "	WHERE " + TABLE_SYNERGY_ITEM + "." + COLUMN_SYNERGY_ITEM_ID + " = NEW." + COLUMN_SYNERGY_ITEM_ID + "; "
            + "END ";



    public static final String CREATE_SYNERGY_LIST_ITEM =

            "CREATE TABLE " + TABLE_SYNERGY_LIST_ITEM + " ( "
            + COLUMN_SYNERGY_LIST_ITEM_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  "
            + COLUMN_SYNERGY_LIST_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SYNERGY_LIST + " (" + COLUMN_SYNERGY_LIST_ID + "),  "
            + COLUMN_SYNERGY_ITEM_ID + " INTEGER NOT NULL REFERENCES " + TABLE_SYNERGY_ITEM + " (" + COLUMN_SYNERGY_ITEM_ID + "), "
            + COLUMN_SYNERGY_LIST_ITEM_POSITION + " INTEGER, "
            + COLUMN_SYNERGY_LIST_ITEM_CREATED_AT + " TEXT, "
            + COLUMN_SYNERGY_LIST_ITEM_UPDATED_AT + " TEXT,  "
            + "	UNIQUE(" + COLUMN_SYNERGY_LIST_ID + ", " + COLUMN_SYNERGY_ITEM_ID + ") "
            + ") ";

    public static final String CREATE_SYNERGY_LIST_ITEM_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST_ITEM + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_LIST_ITEM + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST_ITEM + "  "
            + "	SET " + COLUMN_SYNERGY_LIST_ITEM_CREATED_AT + " = CURRENT_TIMESTAMP,  "
            + "		   " + COLUMN_SYNERGY_LIST_ITEM_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "	WHERE " + TABLE_SYNERGY_LIST_ITEM + "." + COLUMN_SYNERGY_LIST_ITEM_ID + " = NEW." + COLUMN_SYNERGY_LIST_ITEM_ID + "; "
            + "END ";

    public static final String CREATE_SYNERGY_LIST_ITEM_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_LIST_ITEM + "UpdatedAt "
            + "AFTER UPDATE ON " + TABLE_SYNERGY_LIST_ITEM + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_LIST_ITEM + "  "
            + "	SET " + COLUMN_SYNERGY_LIST_ITEM_UPDATED_AT + " = CURRENT_TIMESTAMP "
            + "	WHERE " + TABLE_SYNERGY_LIST_ITEM + "." + COLUMN_SYNERGY_LIST_ITEM_ID + " = NEW." + COLUMN_SYNERGY_LIST_ITEM_ID + "; "
            + "END ";



    public static final String CREATE_SYNERGY_TO_DO =

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

    public static final String CREATE_SYNERGY_TO_DO_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_TO_DO + "CreatedAt  "
            + "AFTER INSERT ON " + TABLE_SYNERGY_TO_DO + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_TO_DO + "  "
                + "SET " + COLUMN_SYNERGY_TO_DO_CREATED_AT + " = CURRENT_TIMESTAMP,  "
                       + "" + COLUMN_SYNERGY_TO_DO_UPDATED_AT + " = CURRENT_TIMESTAMP "
                + "WHERE " + TABLE_SYNERGY_TO_DO + "." + COLUMN_SYNERGY_TO_DO_ID + " = NEW." + COLUMN_SYNERGY_TO_DO_ID + "; "
            + "END ";

    public static final String CREATE_SYNERGY_TO_DO_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_SYNERGY_TO_DO + "UpdatedAt "
            + "AFTER UPDATE ON " + TABLE_SYNERGY_TO_DO + " "
            + "BEGIN "
            + "UPDATE " + TABLE_SYNERGY_TO_DO + "  "
                + "SET " + COLUMN_SYNERGY_TO_DO_UPDATED_AT + " = CURRENT_TIMESTAMP "
                + "WHERE " + TABLE_SYNERGY_TO_DO + "." + COLUMN_SYNERGY_TO_DO_ID + " = NEW." + COLUMN_SYNERGY_TO_DO_ID + "; "
            + "END ";

    //endregion

    //region SynergyV5_SELECT

    public static final String
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

    public static final String
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

    public static final String SYNERGY_V5_SELECT_ACTIVE_LISTS =

            "SELECT " + COLUMN_SYNERGY_LIST_NAME + " "
            + "FROM " + TABLE_SYNERGY_LIST + " "
            + "WHERE " + COLUMN_SYNERGY_LIST_SHELVED_AT + " IS NULL "
            + "   OR " + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " >= " +
                         COLUMN_SYNERGY_LIST_SHELVED_AT + " "
            + "ORDER BY " + COLUMN_SYNERGY_LIST_NAME + "; ";

    public static final String SYNERGY_V5_SELECT_SHELVED_LISTS =

            "SELECT " + COLUMN_SYNERGY_LIST_NAME + " "
            + "FROM " + TABLE_SYNERGY_LIST + " "
            + "WHERE " + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " < " + COLUMN_SYNERGY_LIST_SHELVED_AT + " "
            + "ORDER BY " + COLUMN_SYNERGY_LIST_NAME + "; ";

    public static final String SYNERGY_V5_SELECT_ID_FOR_LIST_NAME_X =

            "SELECT " + COLUMN_SYNERGY_LIST_ID + " "
            + "FROM " + TABLE_SYNERGY_LIST + " "
            + "WHERE " + COLUMN_SYNERGY_LIST_NAME + " = ? ;";

    public static final String
            SYNERGY_V5_SELECT_ID_ACTIVATED_AT_SHELVED_AT_FOR_LIST_NAME =

        "SELECT " + COLUMN_SYNERGY_LIST_ID + ", "
                  + COLUMN_SYNERGY_LIST_ACTIVATED_AT + ", "
                  + COLUMN_SYNERGY_LIST_SHELVED_AT + " "
        + "FROM " + TABLE_SYNERGY_LIST + " "
        + "WHERE " + COLUMN_SYNERGY_LIST_NAME + " = ? ;";

    public static final String
            SYNERGY_V5_SELECT_LIST_ITEM_ID_FOR_LIST_ID_ITEM_ID_X_Y =

            "SELECT " + COLUMN_SYNERGY_LIST_ITEM_ID + " "
            + "FROM " + TABLE_SYNERGY_LIST_ITEM + " "
            + "WHERE " + COLUMN_SYNERGY_LIST_ID + " = ?  "
            + "AND " + COLUMN_SYNERGY_ITEM_ID + " = ? ; ";

    public static final String SYNERGY_V5_SELECT_ID_FOR_ITEM_VALUE_X =

            "SELECT " + COLUMN_SYNERGY_ITEM_ID + " "
            + "FROM " + TABLE_SYNERGY_ITEM + " "
            + "WHERE " + COLUMN_SYNERGY_ITEM_VALUE + " = ? ; ";

    public static final String
            SYNERGY_V5_SELECT_ITEM_VALUES_BY_POSITION_FOR_LIST_ID_X =

            "SELECT si." + COLUMN_SYNERGY_ITEM_ID + ", "
            + "	    si." + COLUMN_SYNERGY_ITEM_VALUE + ", "
            + "	    sli." + COLUMN_SYNERGY_LIST_ITEM_POSITION + " "
            + "FROM " + TABLE_SYNERGY_LIST_ITEM + " sli "
            + "JOIN " + TABLE_SYNERGY_ITEM + " si "
            + "ON sli." + COLUMN_SYNERGY_ITEM_ID +
                    " = si." + COLUMN_SYNERGY_ITEM_ID + " "
            + "WHERE sli." + COLUMN_SYNERGY_LIST_ID + " = ? ; ";

    public static final String
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

    public static final String SYNERGY_V5_ENSURE_LIST_NAME_X =

            "INSERT OR IGNORE INTO " + TABLE_SYNERGY_LIST + " "
            + "	(" + COLUMN_SYNERGY_LIST_NAME + ") "
            + "VALUES "
            + "	(?); ";

    public static final String SYNERGY_V5_ENSURE_LIST_ITEM_POSITION_X_Y_Z =

            "INSERT OR IGNORE INTO " + TABLE_SYNERGY_LIST_ITEM + " "
            + "	(" + COLUMN_SYNERGY_LIST_ID + ",  "
            + "	 " + COLUMN_SYNERGY_ITEM_ID + ",  "
            + "	 " + COLUMN_SYNERGY_LIST_ITEM_POSITION +  ") "
            + "VALUES "
            + "	(?, ?, ?); ";

    public static final String SYNERGY_V5_ENSURE_ITEM_VALUE_X =

            "INSERT OR IGNORE INTO " + TABLE_SYNERGY_ITEM + " "
            + "	(" + COLUMN_SYNERGY_ITEM_VALUE + ") "
            + "VALUES "
            + "	(?); ";

    public static final String
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

    public static final String
            SYNERGY_V5_LIST_UPDATE_ACTIVATE_AT_SHELVED_AT_FOR_LIST_NAME_X_Y_Z =

            "UPDATE " + TABLE_SYNERGY_LIST + " "
            + "SET " + COLUMN_SYNERGY_LIST_ACTIVATED_AT + " = MAX(IFNULL(" + COLUMN_SYNERGY_LIST_ACTIVATED_AT + ", ''), ?), "
            + "	   " + COLUMN_SYNERGY_LIST_SHELVED_AT + " = MAX(IFNULL(" + COLUMN_SYNERGY_LIST_SHELVED_AT + ", ''), ?) "
            + "WHERE " + COLUMN_SYNERGY_LIST_NAME + " = ?; ";

    public static final String
            SYNERGY_V5_UPDATE_POSITION_FOR_LIST_ITEM_ID_X_Y =

            "UPDATE " + TABLE_SYNERGY_LIST_ITEM + " "
            + "SET " + COLUMN_SYNERGY_LIST_ITEM_POSITION + " = ? "
            + "WHERE " + COLUMN_SYNERGY_LIST_ITEM_ID + " = ? ; ";

    public static final String
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

    public static final String TABLE_MEDIA_TRANSCRIPTION = "MediaTranscription";

    public static final String TABLE_MEDIA_DEVICE = "MediaDevice";
    public static final String TABLE_MEDIA_ROOT = "MediaRoot";
    public static final String TABLE_MEDIA_PATH = "MediaPath";
    public static final String TABLE_MEDIA = "Media";
    public static final String TABLE_MEDIA_DEVICE_PATH = "MediaDevicePath";
    public static final String TABLE_MEDIA_TAG = "MediaTag";
    public static final String TABLE_MEDIA_TAGGING = "MediaTagging";
    public static final String TABLE_MEDIA_TRANSCRIPT = "MediaTranscript";

    public static final String COLUMN_MEDIA_ROOT_ID = "MediaRootId";
    public static final String COLUMN_MEDIA_ROOT_PATH = "MediaRootPath";
    public static final String COLUMN_MEDIA_DEVICE_ID = "MediaDeviceId";
    public static final String COLUMN_MEDIA_DEVICE_DESCRIPTION = "MediaDeviceDescription";
    public static final String COLUMN_MEDIA_PATH_VALUE = "MediaPathValue";
    public static final String COLUMN_MEDIA_FILE_NAME = "MediaFileName";
    public static final String COLUMN_MEDIA_ID = "MediaId";
    public static final String COLUMN_MEDIA_PATH_ID = "MediaPathId";
    public static final String COLUMN_MEDIA_HASH = "MediaHash";
    public static final String COLUMN_MEDIA_TAG_ID = "MediaTagId";
    public static final String COLUMN_MEDIA_TAG_VALUE = "MediaTagValue";
    public static final String COLUMN_MEDIA_DESCRIPTION = "MediaDescription";

    public static final String COLUMN_MEDIA_TRANSCRIPT_ID = "MediaTranscriptId";
    public static final String COLUMN_MEDIA_TRANSCRIPT_VALUE = "MediaTranscriptValue";
    public static final String COLUMN_MEDIA_TRANSCRIPT_BEGIN_TIME = "MediaTranscriptBeginTime";
    public static final String COLUMN_MEDIA_TRANSCRIPT_END_TIME = "MediaTranscriptEndTime";
    public static final String COLUMN_MEDIA_TRANSCRIPT_CREATED_AT = "MediaTranscriptCreatedAt";
    public static final String COLUMN_MEDIA_TRANSCRIPT_UPDATED_AT = "MediaTranscriptUpdatedAt";
    public static final String COLUMN_MEDIA_TRANSCRIPTION_ID = "MediaTranscriptionId";
    public static final String COLUMN_MEDIA_TRANSCRIPTION_CREATED_AT = "MediaTranscriptionCreatedAt";
    public static final String COLUMN_MEDIA_TRANSCRIPTION_UPDATED_AT = "MediaTranscriptionUpdatedAt";
    public static final String COLUMN_MEDIA_CREATED_AT = "MediaCreatedAt";
    public static final String COLUMN_MEDIA_UPDATED_AT = "MediaUpdatedAt";
    public static final String COLUMN_MEDIA_ROOT_CREATED_AT = "MediaRootCreatedAt";
    public static final String COLUMN_MEDIA_ROOT_UPDATED_AT = "MediaRootUpdatedAt";
    public static final String COLUMN_MEDIA_PATH_CREATED_AT = "MediaPathCreatedAt";
    public static final String COLUMN_MEDIA_PATH_UPDATED_AT = "MediaPathUpdatedAt";
    public static final String COLUMN_MEDIA_DEVICE_PATH_ID = "MediaDevicePathId";
    public static final String COLUMN_MEDIA_DEVICE_PATH_CREATED_AT = "MediaDevicePathCreatedAt";
    public static final String COLUMN_MEDIA_DEVICE_PATH_UPDATED_AT = "MediaDevicePathUpdatedAt";
    public static final String COLUMN_MEDIA_DEVICE_CREATED_AT = "MediaDeviceCreatedAt";
    public static final String COLUMN_MEDIA_DEVICE_UPDATED_AT = "MediaDeviceUpdatedAt";

    public static final String COLUMN_MEDIA_TAGGING_ID = "MediaTaggingId";
    public static final String COLUMN_MEDIA_TAGGING_TAGGED_AT = "MediaTaggingTaggedAt";
    public static final String COLUMN_MEDIA_TAGGING_UNTAGGED_AT = "MediaTaggingUntaggedAt";
    public static final String COLUMN_MEDIA_TAGGING_CREATED_AT = "MediaTaggingCreatedAt";
    public static final String COLUMN_MEDIA_TAGGING_UPDATED_AT = "MediaTaggingUpdatedAt";
    public static final String COLUMN_MEDIA_TAG_CREATED_AT = "MediaTagCreatedAt";
    public static final String COLUMN_MEDIA_TAG_UPDATED_AT = "MediaTagUpdatedAt";

    public static final String TABLE_LOCAL_CONFIG = "LocalConfig";

    public static final String COLUMN_LOCAL_CONFIG_ID = "LocalConfigId";
    public static final String COLUMN_LOCAL_CONFIG_KEY = "LocalConfigKey";
    public static final String COLUMN_LOCAL_CONFIG_VALUE = "LocalConfigValue";
    public static final String COLUMN_LOCAL_CONFIG_CREATED_AT = "LocalConfigCreatedAt";
    public static final String COLUMN_LOCAL_CONFIG_UPDATED_AT = "LocalConfigUpdatedAt";

    public static final String COLUMN_HIVE_ROOT_ID = "HiveRootId";
    public static final String COLUMN_HIVE_ROOT_NAME = "HiveRootName";
    public static final String COLUMN_HIVE_ROOT_ACTIVATED_AT = "HiveRootActivatedAt";
    public static final String COLUMN_HIVE_ROOT_DEACTIVATED_AT = "HiveRootDeactivatedAt";
    public static final String COLUMN_HIVE_ROOT_CREATED_AT = "HiveRootCreatedAt";
    public static final String COLUMN_HIVE_ROOT_UPDATED_AT = "HiveRootUpdatedAt";
    public static final String TABLE_HIVE_ROOT = "HiveRoot";

    public static final String CREATE_MEDIA_TAG =

            "CREATE TABLE " + TABLE_MEDIA_TAG + " ( " +
            "	" + COLUMN_MEDIA_TAG_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_TAG_VALUE + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_MEDIA_TAG_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TAG_UPDATED_AT + " TEXT " +
            ") ";

    public static final String CREATE_MEDIA_TAG_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TAG + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_TAG + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TAG + "  " +
            "	SET " + COLUMN_MEDIA_TAG_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_TAG_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TAG + "." + COLUMN_MEDIA_TAG_ID + " = NEW." + COLUMN_MEDIA_TAG_ID + "; " +
            "END ";

    public static final String CREATE_MEDIA_TAG_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TAG + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_TAG + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TAG + "  " +
            "	SET " + COLUMN_MEDIA_TAG_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TAG + "." + COLUMN_MEDIA_TAG_ID + " = NEW." + COLUMN_MEDIA_TAG_ID + "; " +
            "END " ;


    public static final String CREATE_MEDIA_DEVICE =

            "CREATE TABLE " + TABLE_MEDIA_DEVICE + " ( " +
            "	" + COLUMN_MEDIA_DEVICE_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_DEVICE_DESCRIPTION + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_MEDIA_DEVICE_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_DEVICE_UPDATED_AT + " TEXT " +
            ") " ;

    public static final String CREATE_MEDIA_DEVICE_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_DEVICE + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_DEVICE + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_DEVICE + "  " +
            "	SET " + COLUMN_MEDIA_DEVICE_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_DEVICE_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_DEVICE + "." + COLUMN_MEDIA_DEVICE_ID + " = NEW." + COLUMN_MEDIA_DEVICE_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_DEVICE_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_DEVICE + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_DEVICE + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_DEVICE + "  " +
            "	SET " + COLUMN_MEDIA_DEVICE_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_DEVICE + "." + COLUMN_MEDIA_DEVICE_ID + " = NEW." + COLUMN_MEDIA_DEVICE_ID + "; " +
            "END " ;


    public static final String CREATE_MEDIA_TRANSCRIPT =

            "CREATE TABLE " + TABLE_MEDIA_TRANSCRIPT + " ( " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_VALUE + " TEXT, " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_BEGIN_TIME + " TEXT,  " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_END_TIME + " TEXT,  " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_UPDATED_AT + " TEXT,  " +
            "	UNIQUE(" + COLUMN_MEDIA_TRANSCRIPT_BEGIN_TIME + ", " + COLUMN_MEDIA_TRANSCRIPT_END_TIME + ") " +
            ") " ;

    public static final String CREATE_MEDIA_TRANSCRIPT_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TRANSCRIPT + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_TRANSCRIPT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TRANSCRIPT + "  " +
            "	SET " + COLUMN_MEDIA_TRANSCRIPT_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_TRANSCRIPT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TRANSCRIPT + "." + COLUMN_MEDIA_TRANSCRIPT_ID + " = NEW." + COLUMN_MEDIA_TRANSCRIPT_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_TRANSCRIPT_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TRANSCRIPT + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_TRANSCRIPT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TRANSCRIPT + "  " +
            "	SET " + COLUMN_MEDIA_TRANSCRIPT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TRANSCRIPT + "." + COLUMN_MEDIA_TRANSCRIPT_ID + " = NEW." + COLUMN_MEDIA_TRANSCRIPT_ID + "; " +
            "END " ;


    public static final String CREATE_MEDIA_PATH =

            "CREATE TABLE " + TABLE_MEDIA_PATH + " ( " +
            "	" + COLUMN_MEDIA_PATH_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_PATH_VALUE + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_MEDIA_PATH_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_PATH_UPDATED_AT + " TEXT " +
            ") ";

    public static final String CREATE_MEDIA_PATH_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_PATH + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_PATH + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_PATH + "  " +
            "	SET " + COLUMN_MEDIA_PATH_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_PATH_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_PATH + "." + COLUMN_MEDIA_PATH_ID + " = NEW." + COLUMN_MEDIA_PATH_ID + "; " +
            "END ";

    public static final String CREATE_MEDIA_PATH_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_PATH + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_PATH + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_PATH + "  " +
            "	SET " + COLUMN_MEDIA_PATH_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_PATH + "." + COLUMN_MEDIA_PATH_ID + " = NEW." + COLUMN_MEDIA_PATH_ID + "; " +
            "END ";


    public static final String CREATE_MEDIA_ROOT =

            "CREATE TABLE " + TABLE_MEDIA_ROOT + " ( " +
            "	" + COLUMN_MEDIA_ROOT_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_DEVICE_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_DEVICE + " (" + COLUMN_MEDIA_DEVICE_ID + "),  " +
            "	" + COLUMN_MEDIA_ROOT_PATH + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_MEDIA_ROOT_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_ROOT_UPDATED_AT + " TEXT " +
            ") ";

    public static final String CREATE_MEDIA_ROOT_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_ROOT + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_ROOT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_ROOT + "  " +
            "	SET " + COLUMN_MEDIA_ROOT_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_ROOT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_ROOT + "." + COLUMN_MEDIA_ROOT_ID + " = NEW." + COLUMN_MEDIA_ROOT_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_ROOT_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_ROOT + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_ROOT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_ROOT + "  " +
            "	SET " + COLUMN_MEDIA_ROOT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_ROOT + "." + COLUMN_MEDIA_ROOT_ID + " = NEW." + COLUMN_MEDIA_ROOT_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA =

            "CREATE TABLE " + TABLE_MEDIA + " ( " +
            "	" + COLUMN_MEDIA_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_FILE_NAME + " TEXT, " +
            "	" + COLUMN_MEDIA_DESCRIPTION + " TEXT,  " +
            "	" + COLUMN_MEDIA_HASH + " TEXT,  " +
            "	" + COLUMN_MEDIA_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_UPDATED_AT + " TEXT,   " +
            "	UNIQUE(" + COLUMN_MEDIA_HASH + ") " +
            ") ";

    public static final String CREATE_MEDIA_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA + "  " +
            "	SET " + COLUMN_MEDIA_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA + "." + COLUMN_MEDIA_ID + " = NEW." + COLUMN_MEDIA_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA + "  " +
            "	SET " + COLUMN_MEDIA_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA + "." + COLUMN_MEDIA_ID + " = NEW." + COLUMN_MEDIA_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_DEVICE_PATH =

            "CREATE TABLE " + TABLE_MEDIA_DEVICE_PATH + " ( " +
            "	" + COLUMN_MEDIA_DEVICE_PATH_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA + " (" + COLUMN_MEDIA_ID + "),  " +
            "	" + COLUMN_MEDIA_DEVICE_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_DEVICE + " (" + COLUMN_MEDIA_DEVICE_ID + "), " +
            "	" + COLUMN_MEDIA_PATH_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_PATH + " (" + COLUMN_MEDIA_PATH_ID + "), " +
            "	" + COLUMN_MEDIA_DEVICE_PATH_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_DEVICE_PATH_UPDATED_AT + " TEXT,  " +
            "	UNIQUE(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_DEVICE_ID + ", " + COLUMN_MEDIA_PATH_ID + ") " +
            ") " ;

    public static final String CREATE_MEDIA_DEVICE_PATH_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_DEVICE_PATH + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_DEVICE_PATH + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_DEVICE_PATH + "  " +
            "	SET " + COLUMN_MEDIA_DEVICE_PATH_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_DEVICE_PATH_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_DEVICE_PATH + "." + COLUMN_MEDIA_DEVICE_PATH_ID + " = NEW." + COLUMN_MEDIA_DEVICE_PATH_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_DEVICE_PATH_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_DEVICE_PATH + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_DEVICE_PATH + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_DEVICE_PATH + "  " +
            "	SET " + COLUMN_MEDIA_DEVICE_PATH_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_DEVICE_PATH + "." + COLUMN_MEDIA_DEVICE_PATH_ID + " = NEW." + COLUMN_MEDIA_DEVICE_PATH_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_TRANSCRIPTION =

            "CREATE TABLE " + TABLE_MEDIA_TRANSCRIPTION + " ( " +
            "	" + COLUMN_MEDIA_TRANSCRIPTION_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_MEDIA_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA + " (" + COLUMN_MEDIA_ID + "),  " +
            "	" + COLUMN_MEDIA_TRANSCRIPT_ID + " INTEGER NOT NULL REFERENCES " + TABLE_MEDIA_TRANSCRIPT + " (" + COLUMN_MEDIA_TRANSCRIPT_ID + "), " +
            "	" + COLUMN_MEDIA_TRANSCRIPTION_CREATED_AT + " TEXT, " +
            "	" + COLUMN_MEDIA_TRANSCRIPTION_UPDATED_AT + " TEXT,  " +
            "	UNIQUE(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_TRANSCRIPT_ID + ") " +
            ") " ;

    public static final String CREATE_MEDIA_TRANSCRIPTION_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TRANSCRIPTION + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_TRANSCRIPTION + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TRANSCRIPTION + "  " +
            "	SET " + COLUMN_MEDIA_TRANSCRIPTION_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_TRANSCRIPTION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TRANSCRIPTION + "." + COLUMN_MEDIA_TRANSCRIPTION_ID + " = NEW." + COLUMN_MEDIA_TRANSCRIPTION_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_TRANSCRIPTION_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TRANSCRIPTION + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_TRANSCRIPTION + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TRANSCRIPTION + "  " +
            "	SET " + COLUMN_MEDIA_TRANSCRIPTION_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TRANSCRIPTION + "." + COLUMN_MEDIA_TRANSCRIPTION_ID + " = NEW." + COLUMN_MEDIA_TRANSCRIPTION_ID + "; " +
            "END ";

    public static final String CREATE_MEDIA_TAGGING =

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

    public static final String CREATE_MEDIA_TAGGING_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TAGGING + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_MEDIA_TAGGING + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TAGGING + "  " +
            "	SET " + COLUMN_MEDIA_TAGGING_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_MEDIA_TAGGING_UPDATED_AT + " = CURRENT_TIMESTAMP, " +
            "		   " + COLUMN_MEDIA_TAGGING_TAGGED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TAGGING + "." + COLUMN_MEDIA_TAGGING_ID + " = NEW." + COLUMN_MEDIA_TAGGING_ID + "; " +
            "END " ;

    public static final String CREATE_MEDIA_TAGGING_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_MEDIA_TAGGING + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_MEDIA_TAGGING + " " +
            "BEGIN " +
            "UPDATE " + TABLE_MEDIA_TAGGING + "  " +
            "	SET " + COLUMN_MEDIA_TAGGING_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_MEDIA_TAGGING + "." + COLUMN_MEDIA_TAGGING_ID + " = NEW." + COLUMN_MEDIA_TAGGING_ID + "; " +
            "END " ;

    public static final String CREATE_LOCAL_CONFIG_V5 =

            "CREATE TABLE " + TABLE_LOCAL_CONFIG + " ( " +
            "	" + COLUMN_LOCAL_CONFIG_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE,  " +
            "	" + COLUMN_LOCAL_CONFIG_KEY + " TEXT NOT NULL UNIQUE,  " +
            "	" + COLUMN_LOCAL_CONFIG_VALUE + " TEXT, " +
            "	" + COLUMN_LOCAL_CONFIG_CREATED_AT + " TEXT, " +
            "	" + COLUMN_LOCAL_CONFIG_UPDATED_AT + " TEXT, " +
            "	UNIQUE(" + COLUMN_LOCAL_CONFIG_KEY + ", " + COLUMN_LOCAL_CONFIG_VALUE + ") " +
            ") " ;

    public static final String CREATE_LOCAL_CONFIG_V5_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_LOCAL_CONFIG + "CreatedAt  " +
            "AFTER INSERT ON " + TABLE_LOCAL_CONFIG + " " +
            "BEGIN " +
            "UPDATE " + TABLE_LOCAL_CONFIG + "  " +
            "	SET " + COLUMN_LOCAL_CONFIG_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_LOCAL_CONFIG_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_LOCAL_CONFIG + "." + COLUMN_LOCAL_CONFIG_ID + " = NEW." + COLUMN_LOCAL_CONFIG_ID + "; " +
            "END " ;

    public static final String CREATE_LOCAL_CONFIG_V5_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + TABLE_LOCAL_CONFIG + "UpdatedAt " +
            "AFTER UPDATE ON " + TABLE_LOCAL_CONFIG + " " +
            "BEGIN " +
            "UPDATE " + TABLE_LOCAL_CONFIG + "  " +
            "	SET " + COLUMN_LOCAL_CONFIG_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_LOCAL_CONFIG + "." + COLUMN_LOCAL_CONFIG_ID + " = NEW." + COLUMN_LOCAL_CONFIG_ID + "; " +
            "END " ;

    public static final String LOCAL_CONFIG_GET_VALUE_FOR_KEY_X =

            "SELECT " + COLUMN_LOCAL_CONFIG_VALUE + " " +
            "FROM " + TABLE_LOCAL_CONFIG + " " +
            "WHERE " + COLUMN_LOCAL_CONFIG_KEY + " = ?; ";

    public static final String MNEMOSYNE_V5_SELECT_MEDIA_DEVICE_BY_DESC_X =

            "SELECT " + COLUMN_MEDIA_DEVICE_ID + ",  " +
            "	    " + COLUMN_MEDIA_DEVICE_DESCRIPTION + " " +
            "FROM " + TABLE_MEDIA_DEVICE + " " +
            "WHERE " + COLUMN_MEDIA_DEVICE_DESCRIPTION + " = ?; " ;

    public static final String INSERT_MEDIA_DEVICE_X =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_DEVICE + " "
            + "	(" + COLUMN_MEDIA_DEVICE_DESCRIPTION + ") "
            + "VALUES "
            + "	(?); " ;

    public static final String MNEMOSYNE_V5_INSERT_LOCAL_CONFIG_KEY_VALUE_X_Y =

            "INSERT OR IGNORE INTO " + TABLE_LOCAL_CONFIG + " " +
            "	(" + COLUMN_LOCAL_CONFIG_KEY + ", " + COLUMN_LOCAL_CONFIG_VALUE + ") " +
            "VALUES " +
            "	(?, ?); ";

    public static final String MNEMOSYNE_V5_UPDATE_LOCAL_CONFIG_VALUE_FOR_KEY_X_Y =

            "UPDATE " + TABLE_LOCAL_CONFIG + " " +
            "SET " + COLUMN_LOCAL_CONFIG_VALUE + " = ? " +
            "WHERE " + COLUMN_LOCAL_CONFIG_KEY + " = ?; " ;

    public static final String MNEMOSYNE_V5_ADD_COLUMN_MEDIA_DEVICE_PATH_VERIFIED =

            "ALTER TABLE " + TABLE_MEDIA_DEVICE_PATH +
                    " ADD COLUMN " + COLUMN_MEDIA_DEVICE_PATH_VERIFIED_PRESENT + " TEXT; ";

    public static final String MNEMOSYNE_V5_ADD_COLUMN_MEDIA_DEVICE_PATH_MISSING =

            "ALTER TABLE " + TABLE_MEDIA_DEVICE_PATH +
                    " ADD COLUMN " + COLUMN_MEDIA_DEVICE_PATH_VERIFIED_MISSING + " TEXT; ";

    public static final String SELECT_FROM_MEDIA_DEVICE =

            "SELECT " + NwdContract.COLUMN_MEDIA_DEVICE_ID + ", "
                      + NwdContract.COLUMN_MEDIA_DEVICE_DESCRIPTION + " "
            + "FROM " + NwdContract.TABLE_MEDIA_DEVICE + "; ";

    public static final String
            SELECT_ID_AND_PATH_FROM_MEDIA_ROOT_FOR_DEVICE_ID =

            "SELECT " + NwdContract.COLUMN_MEDIA_ROOT_ID + ", "
                      + NwdContract.COLUMN_MEDIA_ROOT_PATH + " "
            + "FROM " + NwdContract.TABLE_MEDIA_ROOT + " "
            + "WHERE " + NwdContract.COLUMN_MEDIA_DEVICE_ID + " = ? ; ";

    public static final String INSERT_DEVICE_ID_PATH_INTO_MEDIA_ROOT =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_ROOT + " "
            + "	(" + COLUMN_MEDIA_DEVICE_ID + ", " + COLUMN_MEDIA_ROOT_PATH + ") "
            + "VALUES "
            + "	(?, ?); " ;

    public static final String MNEMOSYNE_V4_GET_PATH_TAG_LINKS =

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

    public static final String INSERT_MEDIA_TAG_X =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_TAG + " " +
            "	(" + COLUMN_MEDIA_TAG_VALUE + ") " +
            "VALUES " +
            "	(?); ";

    public static final String SELECT_MEDIA_TAG_ID_VALUE =

            "SELECT " + COLUMN_MEDIA_TAG_ID + ", " + COLUMN_MEDIA_TAG_VALUE + " " +
            "FROM " + TABLE_MEDIA_TAG + ";";

    public static final String SELECT_MEDIA_FOR_HASH_X =

            "SELECT " + COLUMN_MEDIA_ID + ",  " +
            "	    " + COLUMN_MEDIA_FILE_NAME + ",  " +
            "	    " + COLUMN_MEDIA_DESCRIPTION + ",  " +
            "	    " + COLUMN_MEDIA_HASH + " " +
            "FROM " + TABLE_MEDIA + " " +
            "WHERE " + COLUMN_MEDIA_HASH + " = ? ";

    public static final String INSERT_MEDIA_HASH_X =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA + " " +
            "	(" + COLUMN_MEDIA_HASH + ") " +
            "VALUES " +
            "	(?); ";

    public static final String UPDATE_HASH_FOR_MEDIA_ID_X_Y =

            "UPDATE " + TABLE_MEDIA + " " +
            "SET " + COLUMN_MEDIA_HASH + " = ? " +
            "WHERE " + COLUMN_MEDIA_ID + " = ? ";

    public static final String INSERT_MEDIA_PATH_X =

            "INSERT OR IGNORE INTO " + NwdContract.TABLE_MEDIA_PATH + " " +
            "	(" + NwdContract.COLUMN_MEDIA_PATH_VALUE + ") " +
            "VALUES " +
            "	(?); ";

    public static final String SELECT_MEDIA_PATH_ID_FOR_PATH_X =

            "SELECT " + COLUMN_MEDIA_PATH_ID + " " +
            "FROM " + TABLE_MEDIA_PATH + " " +
            "WHERE " + COLUMN_MEDIA_PATH_VALUE + " = ?;";

    public static final String INSERT_MEDIA_DEVICE_PATH_MID_DID_PID =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_DEVICE_PATH + " " +
            "	(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_DEVICE_ID + ", " + COLUMN_MEDIA_PATH_ID + ") " +
            "VALUES " +
            "	(?, ?, ?); ";

    public static final String SELECT_MEDIA_WHERE_HASH_NOT_NULL_OR_WHITESPACE =

            "SELECT " + COLUMN_MEDIA_ID + ",  " +
            "	   " + COLUMN_MEDIA_FILE_NAME + ",  " +
            "	   " + COLUMN_MEDIA_DESCRIPTION + ",  " +
            "	   " + COLUMN_MEDIA_HASH + " " +
            "FROM " + TABLE_MEDIA + " " +
            "WHERE " + COLUMN_MEDIA_HASH + " IS NOT NULL AND trim(" + COLUMN_MEDIA_HASH + ", ' ') != ''; ";

    public static final String INSERT_OR_IGNORE_MEDIA_TAGGING_X_Y =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_TAGGING + " " +
            "	(" + COLUMN_MEDIA_ID + ", " + COLUMN_MEDIA_TAG_ID + ") " +
            "VALUES " +
            "	(?, ?); ";

    public static final String UPDATE_MEDIA_TAGGING_TAGGED_UNTAGGED_WHERE_MEDIA_ID_AND_TAG_ID_W_X_Y_Z =

            "UPDATE " + TABLE_MEDIA_TAGGING + " " +
            "SET " + COLUMN_MEDIA_TAGGING_TAGGED_AT + " = MAX(IFNULL(" + COLUMN_MEDIA_TAGGING_TAGGED_AT + ", ''), ?), " +
            "	" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + " = MAX(IFNULL(" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + ", ''), ?) " +
            "WHERE " + COLUMN_MEDIA_ID + " = ? AND " + COLUMN_MEDIA_TAG_ID + " = ?; ";

//            "UPDATE " + TABLE_MEDIA_TAGGING + " " +
//            "SET " + COLUMN_MEDIA_TAGGING_TAGGED_AT + " = MAX(" + COLUMN_MEDIA_TAGGING_TAGGED_AT + ", ?), " +
//            "	" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + " = MAX(" + COLUMN_MEDIA_TAGGING_UNTAGGED_AT + ", ?) " +
//            "WHERE " + COLUMN_MEDIA_ID + " = ? AND " + COLUMN_MEDIA_TAG_ID + " = ?; ";

    public static final String
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

    public static final String UPDATE_MEDIA_FILE_NAME_FOR_HASH_X_Y =

            "UPDATE " + TABLE_MEDIA + " " +
            "SET " + COLUMN_MEDIA_FILE_NAME + " = ? " +
            "WHERE " + COLUMN_MEDIA_HASH + " = ?; ";

    public static final String UPDATE_MEDIA_DESCRIPTION_FOR_HASH_X_Y =

            "UPDATE " + TABLE_MEDIA + " " +
            "SET " + COLUMN_MEDIA_DESCRIPTION + " = ? " +
            "WHERE " + COLUMN_MEDIA_HASH + " = ?; ";

    public static final String SELECT_MEDIA_TAGGINGS_FOR_HASH_X =

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

    public static final String SELECT_MEDIA_DEVICE_PATHS_FOR_MEDIA_ID_X =

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

    public static final String INSERT_OR_IGNORE_MEDIA_DEVICE_PATH_X_Y_Z =

            "INSERT OR IGNORE INTO " + TABLE_MEDIA_DEVICE_PATH + " " +
            "	(" + COLUMN_MEDIA_ID + ", " +
            COLUMN_MEDIA_DEVICE_ID + ", " +
            COLUMN_MEDIA_PATH_ID + ") " +
            "VALUES " +
            "	(?, ?, ?); ";

    public static final String SELECT_MEDIA_TAG_ID_FOR_VALUE_X =

            "SELECT " + COLUMN_MEDIA_TAG_ID + "  " +
            "FROM " + TABLE_MEDIA_TAG + " " +
            "WHERE " + COLUMN_MEDIA_TAG_VALUE + " = ? "; //+
            //"COLLATE NOCASE; "; //this may be our issue with tagging

    public static final String SELECT_MEDIA_ID_FOR_HASH_X =

            "SELECT " + COLUMN_MEDIA_ID + " " +
            "FROM " + TABLE_MEDIA + " " +
            "WHERE " + COLUMN_MEDIA_HASH + " = ? " +
            "COLLATE NOCASE;";
    
    public static final String CREATE_HIVE_ROOT =

            "CREATE TABLE " + TABLE_HIVE_ROOT + " ( " +
            "	" + COLUMN_HIVE_ROOT_ID + " INTEGER NOT NULL PRIMARY KEY UNIQUE, " +
            "	" + COLUMN_HIVE_ROOT_NAME + " TEXT NOT NULL UNIQUE, " +
            "	" + COLUMN_HIVE_ROOT_ACTIVATED_AT + " TEXT, " +
            "	" + COLUMN_HIVE_ROOT_DEACTIVATED_AT + " TEXT,  " +
            "	" + COLUMN_HIVE_ROOT_CREATED_AT + " TEXT, " +
            "	" + COLUMN_HIVE_ROOT_UPDATED_AT + " TEXT " +
            ") " ;

    public static final String CREATE_HIVE_ROOT_CREATED_TRIGGER =

            "CREATE TRIGGER Set" + COLUMN_HIVE_ROOT_CREATED_AT + "  " +
            "AFTER INSERT ON " + TABLE_HIVE_ROOT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_HIVE_ROOT + "  " +
            "	SET " + COLUMN_HIVE_ROOT_CREATED_AT + " = CURRENT_TIMESTAMP,  " +
            "		   " + COLUMN_HIVE_ROOT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_HIVE_ROOT + "." + COLUMN_HIVE_ROOT_ID + " = NEW." + COLUMN_HIVE_ROOT_ID + "; " +
            "END " ;

    public static final String CREATE_HIVE_ROOT_UPDATED_TRIGGER =

            "CREATE TRIGGER Set" + COLUMN_HIVE_ROOT_UPDATED_AT + " " +
            "AFTER UPDATE ON " + TABLE_HIVE_ROOT + " " +
            "BEGIN " +
            "UPDATE " + TABLE_HIVE_ROOT + "  " +
            "	SET " + COLUMN_HIVE_ROOT_UPDATED_AT + " = CURRENT_TIMESTAMP " +
            "	WHERE " + TABLE_HIVE_ROOT + "." + COLUMN_HIVE_ROOT_ID + " = NEW." + COLUMN_HIVE_ROOT_ID + "; " +
            "END " ;

    public static final String SELECT_ACTIVE_HIVE_ROOTS =

            "SELECT " + COLUMN_HIVE_ROOT_ID + ", " +
            "       " + COLUMN_HIVE_ROOT_NAME + ", " +
            "	    " + COLUMN_HIVE_ROOT_ACTIVATED_AT + ", " +
            "	    " + COLUMN_HIVE_ROOT_DEACTIVATED_AT + " " +
            "FROM " + TABLE_HIVE_ROOT + " ";

    public static final String INSERT_HIVE_ROOT_NAME_X =

            "INSERT OR IGNORE INTO " + TABLE_HIVE_ROOT + " " +
            "	(" + COLUMN_HIVE_ROOT_NAME + ") " +
            "VALUES " +
            "	(?) ";
}
