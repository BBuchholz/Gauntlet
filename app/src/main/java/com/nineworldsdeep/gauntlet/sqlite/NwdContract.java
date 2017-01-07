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
    public static final String TABLE_LOCAL_CONFIG = "LocalConfig";

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
    public static final String COLUMN_LOCAL_CONFIG_ID = "LocalConfigId";
    public static final String COLUMN_LOCAL_CONFIG_KEY = "LocalConfigKey";
    public static final String COLUMN_LOCAL_CONFIG_VALUE = "LocalConfigValue";

    //endregion

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
}
