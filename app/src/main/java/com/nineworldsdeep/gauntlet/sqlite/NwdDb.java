package com.nineworldsdeep.gauntlet.sqlite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by brent on 5/12/16.
 */
public class NwdDb {

    // Creating this separate class from the NwdDbOpenHelper per:
    // http://www.vogella.com/tutorials/AndroidSQLite/article.html
    // which does so, and I like the design better that way

    private SQLiteDatabase db;
    private NwdDbOpenHelper dbHelper;

    private static final String DATABASE_INSERT_DISPLAY_NAME =
            "INSERT OR IGNORE INTO " + NwdContract.COLUMN_DISPLAY_NAME_VALUE +
                    " (" + NwdContract.COLUMN_DISPLAY_NAME_VALUE + ") VALUES (?); ";

    private static final String DATABASE_INSERT_PATH =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_PATH +
                    " (" + NwdContract.COLUMN_PATH_VALUE + ") VALUES (?); ";

    private static final String DATABASE_INSERT_DEVICE =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_DEVICE +
                    " (" + NwdContract.COLUMN_DEVICE_DESCRIPTION + ") VALUES (?); ";

    private static final String DATABASE_UPDATE_DISPLAY_NAME_FOR_FILE =

            "UPDATE OR IGNORE " + NwdContract.TABLE_FILE + " " +
                    "SET " + NwdContract.COLUMN_DISPLAY_NAME_ID + " = " +
                        "(SELECT " + NwdContract.COLUMN_DISPLAY_NAME_ID + " " +
                         "FROM " + NwdContract.TABLE_DISPLAY_NAME + " " +
                         "WHERE " + NwdContract.COLUMN_DISPLAY_NAME_VALUE + " = ?) " +
                    "WHERE " + NwdContract.COLUMN_PATH_ID + " = " +
                        "(SELECT " + NwdContract.COLUMN_PATH_ID + " " +
                         "FROM " + NwdContract.TABLE_PATH + " " +
                         "WHERE " + NwdContract.COLUMN_PATH_VALUE + " = ?) " +
                    "AND " + NwdContract.COLUMN_DEVICE_ID + " = " +
                        "(SELECT " + NwdContract.COLUMN_DEVICE_ID + " " +
                         "FROM " + NwdContract.TABLE_DEVICE + " " +
                         "WHERE " + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ?); ";

    private static final String DATABASE_INSERT_DISPLAY_NAME_FOR_FILE =

            "INSERT OR IGNORE INTO File (DeviceId, PathId, DisplayNameId) " +
            "VALUES ( " +
                "(SELECT " + NwdContract.COLUMN_DEVICE_ID + " " +
                         "FROM " + NwdContract.TABLE_DEVICE + " " +
                         "WHERE " + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ?), " +
                "(SELECT " + NwdContract.COLUMN_PATH_ID + " " +
                         "FROM " + NwdContract.TABLE_PATH + " " +
                         "WHERE " + NwdContract.COLUMN_PATH_VALUE + " = ?), " +
                "(SELECT " + NwdContract.COLUMN_DISPLAY_NAME_ID + " " +
                         "FROM " + NwdContract.TABLE_DISPLAY_NAME + " " +
                         "WHERE " + NwdContract.COLUMN_DISPLAY_NAME_VALUE + " = ?)); ";

    /**
     * Opens/Creates the internal database for Gauntlet/NWD
     * @param context
     */
    public NwdDb(Context context){

        dbHelper = new NwdDbOpenHelper(context);
    }

    /**
     * Opens/Creates an external database for Gauntlet/NWD with the specified name
     * in the NWD/sqlite directory. Intended for imports and exports.
     * @param context
     * @param databaseName
     */
    public NwdDb(Context context, String databaseName){

        dbHelper = new NwdDbOpenHelper(context, databaseName);
    }

    public void open() throws SQLException {

        db = dbHelper.getWritableDatabase();
    }

    public void close(){

        dbHelper.close();
    }

    public void linkDisplayNameToFile(String deviceDescription,
                                      String filePath,
                                      String displayName){
        //insert or ignore device
        db.rawQuery(DATABASE_INSERT_DEVICE, new String[]{deviceDescription});
        //insert or ignore path
        db.rawQuery(DATABASE_INSERT_PATH, new String[]{filePath});
        //insert or ignore display name
        db.rawQuery(DATABASE_INSERT_DISPLAY_NAME, new String[]{displayName});
        //update or ignore file (if exists)
        db.rawQuery(DATABASE_UPDATE_DISPLAY_NAME_FOR_FILE,
                new String[]{displayName, filePath, deviceDescription});
        //insert or ignore file (if !exists)
        db.rawQuery(DATABASE_INSERT_DISPLAY_NAME_FOR_FILE,
                new String[]{deviceDescription, filePath, displayName});
    }
}
