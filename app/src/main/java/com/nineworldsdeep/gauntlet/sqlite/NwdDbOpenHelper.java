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
    
    //columns
    private static final String COLUMN_DISPLAY_ID = "DisplayNameId";

    // QUOTED FROM: http://stackoverflow.com/questions/3192064/about-id-field-in-android-sqlite
    // Cos I'm gonna use my previous naming convention used in the rest of the NWD ecosystem
    // we will be using "AS _id" in our statements when we get to that bridge
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

            "CREATE TABLE DisplayName (" +

                COLUMN_DISPLAY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                "DisplayNameValue TEXT NOT NULL UNIQUE " +

            ")";


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
        super(new DatabaseContext(context), databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //SEE PRODUCTION REFERENCE BELOW
        //FOR NOW THIS IS DEV VERSION
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
