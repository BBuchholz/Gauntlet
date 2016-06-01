package com.nineworldsdeep.gauntlet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nineworldsdeep.gauntlet.Configuration;

/**
 * Created by brent on 5/12/16.
 */
public class NwdDbOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "nwd";

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

    /**
     * Opens the internal database for Gauntlet/NWD
     * @param context
     */
    public NwdDbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        if(deleteDatabaseForDevelopment()){

            context.deleteDatabase(DATABASE_NAME);
        }
    }

    /**
     * this is a utility method for both of the constructors.
     * it is designed to be used to trigger deletion
     * of the existing database during development
     * so changes to the structure can easily actuate
     * @return
     */
    private boolean deleteDatabaseForDevelopment(){

        return Configuration.isInDeleteDatabaseForDevelopmentMode();
    }

    // FROM: http://stackoverflow.com/questions/5332328/sqliteopenhelper-problem-with-fully-qualified-db-path-name/9168969#9168969

    /**
     * Opens an external database for Gauntlet/NWD with the specified name
     * in the NWD/sqlite directory. Intended for imports and exports.
     * @param context
     * @param databaseName
     */
    public NwdDbOpenHelper(Context context, String databaseName)
    {
        super(new NwdDbContextWrapper(context), databaseName, null, DATABASE_VERSION);

        context = new NwdDbContextWrapper(context);

        if(deleteDatabaseForDevelopment()){

            context.deleteDatabase(databaseName);
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase db){

        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // all of these are "CREATE IF NOT EXISTS" statements
        // to prevent accidental overwrites
        db.execSQL(DATABASE_CREATE_DISPLAY_NAME);
        db.execSQL(DATABASE_CREATE_PATH);
        db.execSQL(DATABASE_CREATE_HASH);
        db.execSQL(DATABASE_CREATE_DEVICE);
        db.execSQL(DATABASE_CREATE_TAG);
        db.execSQL(DATABASE_CREATE_FILE);
        db.execSQL(DATABASE_CREATE_FILE_TAGS);
        db.execSQL(DATABASE_CREATE_AUDIO_TRANSCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {

            db.execSQL("ALTER TABLE " + NwdContract.TABLE_FILE +
                    " ADD COLUMN "
                    + NwdContract.COLUMN_FILE_DESCRIPTION + " TEXT");

            db.execSQL(DATABASE_CREATE_AUDIO_TRANSCRIPT);
        }

//        if (oldVersion < 3) {
//            db.execSQL(DATABASE_ALTER_TABLE_2);
//        }

        //TODO
    }
}
