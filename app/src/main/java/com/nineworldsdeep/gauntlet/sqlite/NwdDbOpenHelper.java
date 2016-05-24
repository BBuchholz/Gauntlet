package com.nineworldsdeep.gauntlet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brent on 5/12/16.
 */
public class NwdDbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "nwd";
    
    //tables
    public static final String TABLE_DISPLAY_NAME = "DisplayName";
    public static final String TABLE_PATH = "Path";
    public static final String TABLE_HASH = "Hash";
    public static final String TABLE_DEVICE = "Device";
    public static final String TABLE_FILE = "File";
    public static final String TABLE_TAG = "Tag";
    public static final String TABLE_FILE_TAGS = "FileTags";
    
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

    // Since I plan to use my previous naming convention that is in place for the NWD ecosystem
    // we will be using "AS _id" in our statements when we get to that bridge
    //
    // BELOW QUOTED FROM: http://stackoverflow.com/questions/3192064/about-id-field-in-android-sqlite
    //
    // "_id is useful when you are using the enhanced Adapters which make use of a
    // Cursor (e.g. ResourceCursorAdapter). It's used by these adapters to provide
    // an ID which can be used to refer to the specific row in the table which relates
    // the the item in whatever the adapter is being used for (e.g. a row in a ListView).
    //
    // "It's not necessary if you're not going to be using classes which need an _id
    // column in a cursor, and you can also use "as _id" to make another column appear
    // as though it's called _id in your cursor." - stack overflow user: Al Sutton

    private static final String DATABASE_CREATE_DISPLAY_NAME =

            "CREATE TABLE " + TABLE_DISPLAY_NAME +" (" +

                    COLUMN_DISPLAY_NAME_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    COLUMN_DISPLAY_NAME_VALUE + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_CREATE_PATH =

            "CREATE TABLE " + TABLE_PATH +" (" +

                    COLUMN_PATH_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    COLUMN_PATH_VALUE + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_CREATE_HASH =

            "CREATE TABLE " + TABLE_HASH +" (" +

                    COLUMN_HASH_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    COLUMN_HASH_VALUE + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_CREATE_DEVICE =

            "CREATE TABLE " + TABLE_DEVICE +" (" +

                    COLUMN_DEVICE_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    COLUMN_DEVICE_DESCRIPTION + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_CREATE_FILE =

            "CREATE TABLE " + TABLE_FILE + " (" +

                    COLUMN_FILE_ID + " INTEGER NOT NULL " +
                        "PRIMARY KEY AUTOINCREMENT UNIQUE, " +

                    COLUMN_DEVICE_ID + " INTEGER NOT NULL REFERENCES " +
                        TABLE_DEVICE + " (" + COLUMN_DEVICE_ID + "), " +

                    COLUMN_PATH_ID + " INTEGER NOT NULL REFERENCES " +
                        TABLE_PATH + " (" + COLUMN_PATH_ID + "), " +

                    COLUMN_HASH_ID + " INTEGER REFERENCES " +
                        TABLE_HASH + " (" + COLUMN_HASH_ID + "), " +

                    COLUMN_DISPLAY_NAME_ID + " INTEGER REFERENCES " +
                        TABLE_DISPLAY_NAME + " (" + COLUMN_DISPLAY_NAME_ID + "), " +

                    COLUMN_FILE_HASHED_AT + " TEXT, " +

                    "UNIQUE(" + COLUMN_DEVICE_ID + ", " + COLUMN_FILE_ID + ")" +
            ")";

    private static final String DATABASE_CREATE_FILE_TAGS =

            "CREATE TABLE " + TABLE_FILE_TAGS + " (" +
                    COLUMN_FILE_ID + " INTEGER NOT NULL REFERENCES " +
                        TABLE_FILE + " (" + COLUMN_FILE_ID + "), " +
                    COLUMN_TAG_ID + " INTEGER NOT NULL REFERENCES " +
                    TABLE_TAG + " (" + COLUMN_TAG_ID + "), " +
                    "UNIQUE(" + COLUMN_FILE_ID + ", " + COLUMN_FILE_ID + ")" +
            ")";

    private static final String DATABASE_CREATE_TAG =

            "CREATE TABLE " + TABLE_TAG +" (" +

                    COLUMN_TAG_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    COLUMN_TAG_VALUE + " TEXT NOT NULL UNIQUE " +
            ")";


    // TODO: just use this as a template, read on for reasoning:
    // File must be the central entity for all linkage, paths, display names, hashes, tags, and
    // devices. Querying should support individual peripheral entites (eg. get all paths where hash
    // equals @hash).
    private static final String DATABASE_LINK_DISPLAY_NAME_TO_PATH =
            "" + //just for indentation purposes
                    "INSERT OR IGNORE INTO DisplayName (DisplayNameValue) VALUES ('Test2'); " +

                    "INSERT OR IGNORE INTO Path (PathValue) VALUES ('test/path/to/something'); " +
//TODO: this needs to be an upsert into FILE ???? I don't know right now, need to play with this in sql
                    "INSERT OR IGNORE INTO junc_DisplayName_Path (DisplayNameId, PathId) " +
                    "VALUES ( " +
                        "(SELECT DisplayNameId FROM DisplayName WHERE DisplayNameValue = 'Test2'), " +
                        "(SELECT PathId FROM Path WHERE PathValue = 'test/path/to/something') );";

    /**
     * Opens the internal database for Gauntlet/NWD
     * @param context
     */
    public NwdDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // FROM: http://stackoverflow.com/questions/5332328/sqliteopenhelper-problem-with-fully-qualified-db-path-name/9168969#9168969

    /**
     * Opens an external database for Gauntlet/NWD with the specified name
     * in the NWD/sqlite directory. Intended for imports and exports.
     * @param context
     * @param databaseName
     */
    public NwdDbOpenHelper(final Context context, String databaseName)
    {
        super(new NwdDatabaseContextWrapper(context), databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //TODO
        // see: http://stackoverflow.com/a/16594703/670768
        // be sure to implement our create and update statements to check if exists (do not drop
        // tables, see the answer. It will save us from fucking up when importing external
        // tables)
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // SEE PRODUCTION REFERENCE BELOW
        // FOR NOW THIS IS DEV VERSION (do the drop table thing - they show it in the bad example here: https://thebhwgroup.com/blog/how-android-sqlite-onupgrade)

        // see: http://stackoverflow.com/a/16594703/670768
        // be sure to implement our create and update statements to check if exists (do not drop
        // tables, see the answer. It will save us from fucking up when importing external
        // tables)

        //TODO
    }

//    //PRODUCTION REFERENCE (original reference here: https://thebhwgroup.com/blog/how-android-sqlite-onupgrade)
//    public SQLiteHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    //database values
//    private static final String DATABASE_NAME      = "demoApp.db";
//    private static final int DATABASE_VERSION      = 3;
//    public static final String COLUMN_ID           = "_id";
//
//    //team table
//    public static final String TABLE_TEAM       = "team";
//    public static final String COLUMN_MASCOT    = "mascot";
//    public static final String COLUMN_CITY      = "city";
//    public static final String COLUMN_COACH     = "coach";
//    public static final String COLUMN_STADIUM   = "stadium";
//
//    private static final String DATABASE_CREATE_TEAM = "create table "
//            + TABLE_TEAM + "(" + COLUMN_ID + " integer primary key autoincrement, "
//            + COLUMN_NAME + " string, "
//            + COLUMN_MASCOT + " string, "
//            + COLUMN_COACH + " string, "
//            + COLUMN_STADIUM + " string, "
//            + COLUMN_CITY + " string);";
//
//    private static final String DATABASE_ALTER_TEAM_1 = "ALTER TABLE "
//            + TABLE_TEAM + " ADD COLUMN " + COLUMN_COACH + " string;";
//
//    private static final String DATABASE_ALTER_TEAM_2 = "ALTER TABLE "
//            + TABLE_TEAM + " ADD COLUMN " + COLUMN_STADIUM + " string;";
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(DATABASE_CREATE_TEAM);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < 2) {
//            db.execSQL(DATABASE_ALTER_TEAM_1);
//        }
//        if (oldVersion < 3) {
//            db.execSQL(DATABASE_ALTER_TEAM_2);
//        }
//    }
}
