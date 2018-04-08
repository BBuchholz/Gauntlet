package com.nineworldsdeep.gauntlet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.Utils;

import java.util.HashMap;

public class NwdDbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 17;
    private static final String DATABASE_NAME = "nwd";

    private static HashMap<String, NwdDbOpenHelper> instances =
        new HashMap<>();

    private static final String DATABASE_CREATE_DISPLAY_NAME =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_DISPLAY_NAME +" (" +

                    NwdContract.COLUMN_DISPLAY_NAME_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    NwdContract.COLUMN_DISPLAY_NAME_VALUE + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_CREATE_PATH =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_PATH +" (" +

                    NwdContract.COLUMN_PATH_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    NwdContract.COLUMN_PATH_VALUE + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_CREATE_HASH =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_HASH +" (" +

                    NwdContract.COLUMN_HASH_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    NwdContract.COLUMN_HASH_VALUE + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_CREATE_LOCAL_CONFIG =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_LOCAL_CONFIG +" (" +

                    NwdContract.COLUMN_LOCAL_CONFIG_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    NwdContract.COLUMN_LOCAL_CONFIG_KEY + " TEXT NOT NULL UNIQUE, " +
                    NwdContract.COLUMN_LOCAL_CONFIG_VALUE + " TEXT " +
            ")";

    private static final String DATABASE_CREATE_DEVICE =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_DEVICE +" (" +

                    NwdContract.COLUMN_DEVICE_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    NwdContract.COLUMN_DEVICE_DESCRIPTION + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_CREATE_FILE =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_FILE + " (" +

                    NwdContract.COLUMN_FILE_ID + " INTEGER NOT NULL " +
                        "PRIMARY KEY AUTOINCREMENT UNIQUE, " +

                    NwdContract.COLUMN_DEVICE_ID + " INTEGER NOT NULL REFERENCES " +
                        NwdContract.TABLE_DEVICE + " (" + NwdContract.COLUMN_DEVICE_ID + "), " +

                    NwdContract.COLUMN_PATH_ID + " INTEGER NOT NULL REFERENCES " +
                        NwdContract.TABLE_PATH + " (" + NwdContract.COLUMN_PATH_ID + "), " +

                    NwdContract.COLUMN_HASH_ID + " INTEGER REFERENCES " +
                        NwdContract.TABLE_HASH + " (" + NwdContract.COLUMN_HASH_ID + "), " +

                    NwdContract.COLUMN_DISPLAY_NAME_ID + " INTEGER REFERENCES " +
                        NwdContract.TABLE_DISPLAY_NAME + " (" + NwdContract.COLUMN_DISPLAY_NAME_ID + "), " +

                    NwdContract.COLUMN_FILE_HASHED_AT + " TEXT, " +

                    NwdContract.COLUMN_FILE_DESCRIPTION + " TEXT, " +

                    NwdContract.COLUMN_FILE_NAME + " TEXT, " +

                    "UNIQUE(" + NwdContract.COLUMN_DEVICE_ID + ", " + NwdContract.COLUMN_PATH_ID + ")" +
            ")";

    private static final String DATABASE_CREATE_AUDIO_TRANSCRIPT =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_AUDIO_TRANSCRIPT + " (" +

                    NwdContract.COLUMN_AUDIO_TRANSCRIPT_ID + " INTEGER NOT NULL " +
                        "PRIMARY KEY AUTOINCREMENT UNIQUE, " +

                    NwdContract.COLUMN_FILE_ID + " INTEGER NOT NULL REFERENCES " +
                        NwdContract.TABLE_FILE + " (" + NwdContract.COLUMN_FILE_ID + "), " +

                    NwdContract.COLUMN_AUDIO_TRANSCRIPT_VALUE + " TEXT, " +

                    "UNIQUE(" + NwdContract.COLUMN_FILE_ID + ")" +
            ")";

    private static final String DATABASE_CREATE_FILE_TAGS =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_FILE_TAGS + " (" +

                    NwdContract.COLUMN_FILE_TAGS_ID + " INTEGER NOT NULL " +
                        "PRIMARY KEY AUTOINCREMENT UNIQUE, " +

                    NwdContract.COLUMN_FILE_ID + " INTEGER NOT NULL REFERENCES " +
                        NwdContract.TABLE_FILE + " (" + NwdContract.COLUMN_FILE_ID + "), " +

                    NwdContract.COLUMN_TAG_ID + " INTEGER NOT NULL REFERENCES " +
                    NwdContract.TABLE_TAG + " (" + NwdContract.COLUMN_TAG_ID + "), " +

                    "UNIQUE(" + NwdContract.COLUMN_FILE_ID + ", " + NwdContract.COLUMN_TAG_ID + ")" +
            ")";

    private static final String DATABASE_CREATE_TAG =

            "CREATE TABLE IF NOT EXISTS " + NwdContract.TABLE_TAG +" (" +

                    NwdContract.COLUMN_TAG_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    NwdContract.COLUMN_TAG_VALUE + " TEXT NOT NULL UNIQUE " +
            ")";

    private static final String DATABASE_DROP_PATH =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_PATH;
    private static final String DATABASE_DROP_HASH =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_HASH;
    private static final String DATABASE_DROP_DEVICE =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_DEVICE;
    private static final String DATABASE_DROP_TAG =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_TAG;
    private static final String DATABASE_DROP_FILE =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_FILE;
    private static final String DATABASE_DROP_FILE_TAGS =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_FILE_TAGS;
    private static final String DATABASE_DROP_AUDIO_TRANSCRIPT =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_AUDIO_TRANSCRIPT;
    private static final String DATABASE_DROP_LOCAL_CONFIG =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_LOCAL_CONFIG;
    private static final String DATABASE_DROP_DISPLAY_NAME =
        "DROP TABLE IF EXISTS " + NwdContract.TABLE_DISPLAY_NAME;

    public static NwdDbOpenHelper getInstance(Context c){

        return getInstance(c, null);
    }

    public static NwdDbOpenHelper getInstance(Context c, String dbName){

        if(Configuration.isInTestMode()){

            dbName = "test";

            if(!instances.containsKey(dbName)){

                instances.put(dbName, new NwdDbOpenHelper(c, dbName));
            }

        }else if(Utils.stringIsNullOrWhitespace(dbName) ||
                dbName.trim().equalsIgnoreCase("nwd")){

            dbName = "nwd";

            if(!instances.containsKey(dbName)){

                instances.put(dbName, new NwdDbOpenHelper(c));
            }

        } else {

            if(!instances.containsKey(dbName)){

                instances.put(dbName, new NwdDbOpenHelper(c, dbName));
            }
        }

        return instances.get(dbName);
    }

    /**
     * Opens the internal database for Gauntlet/NWD
     */
    private NwdDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        if(deleteDatabaseForDevelopment()){

            context.deleteDatabase(DATABASE_NAME);
        }
    }

    // FROM:
    // http://stackoverflow.com/questions/5332328/sqliteopenhelper-problem-with-fully-qualified-db-path-name/9168969#9168969

    /**
     * Opens an external database for Gauntlet/NWD with the specified name
     * in the NWD/sqlite directory. Intended for imports and exports.
     */
    private NwdDbOpenHelper(Context context, String databaseName)
    {
        super(new NwdDbContextWrapper(context), databaseName, null, DATABASE_VERSION);

        context = new NwdDbContextWrapper(context);

        if(deleteDatabaseForDevelopment()){

            context.deleteDatabase(databaseName);
        }
    }

    /**
     * this is a utility method for both of the constructors.
     * it is designed to be used to trigger deletion
     * of the existing database during development
     * so changes to the structure can easily actuate
     */
    private boolean deleteDatabaseForDevelopment(){

        return Configuration.isInDeleteDatabaseForDevelopmentMode();
    }

    @Override
    public void onConfigure(SQLiteDatabase db){

        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //executing in order of onUpgrade()
        createV4Tables(db);
        createSynergyV5Subset(db);
        modSynergyTablesCorrectionOne(db);
        dropAndRecreateSynergyToDoDbV11(db);
        dropAndRecreateSynergyToDoDbV12(db);
        createMnemosyneV5Subset(db);
        addColumnsDevicePathVerifiedAndMissing(db);
        addHiveRootTable(db);
        dropAndRecreateSynergyListInsertTrigger(db);
        createArchivistV5Subset(db);

        // TODO: markClean this up, should have a method that
        // doesn't do all the drop and recreate, just
        // using the upgrade methods for now
    }

    private void createV4Tables(SQLiteDatabase db) {

        // all of these are "CREATE IF NOT EXISTS" statements
        // to prevent accidental overwrites
        db.execSQL(DATABASE_CREATE_DISPLAY_NAME);
        db.execSQL(DATABASE_CREATE_PATH);
        db.execSQL(DATABASE_CREATE_HASH);
        db.execSQL(DATABASE_CREATE_DEVICE);
        db.execSQL(DATABASE_CREATE_TAG);
        db.execSQL(DATABASE_CREATE_LOCAL_CONFIG);
        db.execSQL(DATABASE_CREATE_FILE);
        db.execSQL(DATABASE_CREATE_FILE_TAGS);
        db.execSQL(DATABASE_CREATE_AUDIO_TRANSCRIPT);
    }

    private void dropAllPreV4Tables(SQLiteDatabase db){

        db.execSQL(DATABASE_DROP_FILE_TAGS);
        db.execSQL(DATABASE_DROP_AUDIO_TRANSCRIPT);
        db.execSQL(DATABASE_DROP_FILE);
        db.execSQL(DATABASE_DROP_DISPLAY_NAME);
        db.execSQL(DATABASE_DROP_PATH);
        db.execSQL(DATABASE_DROP_HASH);
        db.execSQL(DATABASE_DROP_DEVICE);
        db.execSQL(DATABASE_DROP_TAG);
        db.execSQL(DATABASE_DROP_LOCAL_CONFIG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//      //online example
//      if (oldVersion < 3) {
//          db.execSQL(DATABASE_ALTER_TABLE_2);
//      }

        if (oldVersion < 4) {

            //we made some changes that were not column adds, lets drop all
            dropAllPreV4Tables(db);

            createV4Tables(db);
        }

        if (oldVersion < 5) {

            createSynergyV5Subset(db);
        }

        if (oldVersion < 10){

            modSynergyTablesCorrectionOne(db);
        }

        if (oldVersion < 11){

            dropAndRecreateSynergyToDoDbV11(db);
        }

        if (oldVersion < 12){

            dropAndRecreateSynergyToDoDbV12(db);
        }

        if (oldVersion < 13){

            createMnemosyneV5Subset(db);
        }

        if (oldVersion < 14){

            addColumnsDevicePathVerifiedAndMissing(db);
        }

        if (oldVersion < 15){

            addHiveRootTable(db);
        }

        if (oldVersion < 16){

            dropAndRecreateSynergyListInsertTrigger(db);
        }

        if (oldVersion < 17){

            createArchivistV5Subset(db);
        }

    }

    private void dropAndRecreateSynergyListInsertTrigger(SQLiteDatabase db) {

        db.execSQL(NwdContract.DROP_SYNERGY_LIST_INSERT_TRIGGER);
        db.execSQL(NwdContract.CREATE_NEW_SYNERGY_LIST_CREATED_TRIGGER);
    }

    private void dropAndRecreateSynergyToDoDbV11(SQLiteDatabase db){

        db.execSQL(NwdContract.DROP_SYNERGY_TO_DO);

        //script has been modified
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO_UPDATED_TRIGGER);
    }

    private void dropAndRecreateSynergyToDoDbV12(SQLiteDatabase db){

        db.execSQL(NwdContract.DROP_SYNERGY_TO_DO);

        //script has been modified
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO_UPDATED_TRIGGER);
    }

    private void modSynergyTablesCorrectionOne(SQLiteDatabase db){

        //drop tables that needed modification
        db.execSQL(NwdContract.DROP_SYNERGY_TO_DO);
        db.execSQL(NwdContract.DROP_SYNERGY_LIST_ITEM);
        db.execSQL(NwdContract.DROP_SYNERGY_LIST);

        //recreate tables
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST);
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_ITEM);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO);

        //createdAt triggers
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_ITEM_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO_CREATED_TRIGGER);

        //updatedAt triggers
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_UPDATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_ITEM_UPDATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO_UPDATED_TRIGGER);
    }

    private void createSynergyV5Subset(SQLiteDatabase db) {

        db.execSQL(NwdContract.CREATE_SYNERGY_LIST);
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_SYNERGY_ITEM);
        db.execSQL(NwdContract.CREATE_SYNERGY_ITEM_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_ITEM_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_ITEM);
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_ITEM_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_LIST_ITEM_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SYNERGY_TO_DO_UPDATED_TRIGGER);

    }

    private void createMnemosyneV5Subset(SQLiteDatabase db){

        db.execSQL(NwdContract.CREATE_MEDIA_TAG);
        db.execSQL(NwdContract.CREATE_MEDIA_TAG_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_TAG_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_MEDIA_DEVICE);
        db.execSQL(NwdContract.CREATE_MEDIA_DEVICE_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_DEVICE_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_MEDIA_TRANSCRIPT);
        db.execSQL(NwdContract.CREATE_MEDIA_TRANSCRIPT_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_TRANSCRIPT_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_MEDIA_PATH);
        db.execSQL(NwdContract.CREATE_MEDIA_PATH_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_PATH_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_MEDIA_ROOT);
        db.execSQL(NwdContract.CREATE_MEDIA_ROOT_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_ROOT_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_MEDIA);
        db.execSQL(NwdContract.CREATE_MEDIA_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_MEDIA_DEVICE_PATH);
        db.execSQL(NwdContract.CREATE_MEDIA_DEVICE_PATH_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_DEVICE_PATH_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_MEDIA_TRANSCRIPTION);
        db.execSQL(NwdContract.CREATE_MEDIA_TRANSCRIPTION_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_TRANSCRIPTION_UPDATED_TRIGGER);

        db.execSQL(NwdContract.CREATE_MEDIA_TAGGING);
        db.execSQL(NwdContract.CREATE_MEDIA_TAGGING_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_MEDIA_TAGGING_UPDATED_TRIGGER);

        //replace local config (not used in previous code)
        db.execSQL(DATABASE_DROP_LOCAL_CONFIG);
        db.execSQL(NwdContract.CREATE_LOCAL_CONFIG_V5);
        db.execSQL(NwdContract.CREATE_LOCAL_CONFIG_V5_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_LOCAL_CONFIG_V5_UPDATED_TRIGGER);
    }

    private void createArchivistV5Subset(SQLiteDatabase db){

        //SourceLocation
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION);
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION_UPDATED_TRIGGER);

        //SourceAnnotation
        db.execSQL(NwdContract.CREATE_SOURCE_ANNOTATION);
        db.execSQL(NwdContract.CREATE_SOURCE_ANNOTATION_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_ANNOTATION_UPDATED_TRIGGER);

        //SourceType
        db.execSQL(NwdContract.CREATE_SOURCE_TYPE);
        db.execSQL(NwdContract.CREATE_SOURCE_TYPE_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_TYPE_UPDATED_TRIGGER);

        //Source
        db.execSQL(NwdContract.CREATE_SOURCE);
        db.execSQL(NwdContract.CREATE_SOURCE_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_UPDATED_TRIGGER);

        //SourceLocationSubset
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION_SUBSET);
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION_SUBSET_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION_SUBSET_UPDATED_TRIGGER);

        //SourceLocationSubsetEntry
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION_SUBSET_ENTRY);
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION_SUBSET_ENTRY_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_LOCATION_SUBSET_ENTRY_UPDATED_TRIGGER);

        //SourceExcerpt
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT);
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT_UPDATED_TRIGGER);

        //SourceExcerptAnnotation
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT_ANNOTATION);
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT_ANNOTATION_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT_ANNOTATION_UPDATED_TRIGGER);

        //TaggingBase
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT_TAGGING);
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT_TAGGING_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_SOURCE_EXCERPT_TAGGING_UPDATED_TRIGGER);
    }

    private void addColumnsDevicePathVerifiedAndMissing(SQLiteDatabase db){

        db.execSQL(NwdContract.MNEMOSYNE_V5_ADD_COLUMN_MEDIA_DEVICE_PATH_VERIFIED);
        db.execSQL(NwdContract.MNEMOSYNE_V5_ADD_COLUMN_MEDIA_DEVICE_PATH_MISSING);
    }

    private void addHiveRootTable(SQLiteDatabase db){

        db.execSQL(NwdContract.CREATE_HIVE_ROOT);
        db.execSQL(NwdContract.CREATE_HIVE_ROOT_CREATED_TRIGGER);
        db.execSQL(NwdContract.CREATE_HIVE_ROOT_UPDATED_TRIGGER);
    }
}
