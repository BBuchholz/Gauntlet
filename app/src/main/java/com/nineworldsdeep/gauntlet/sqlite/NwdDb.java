package com.nineworldsdeep.gauntlet.sqlite;

/**
 * Created by brent on 5/12/16.
 */
public class NwdDb {

    // Creating this separate class from the NwdDbOpenHelper per:
    // http://www.vogella.com/tutorials/AndroidSQLite/article.html
    // which does so, and I like the design better that way

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

//    private static final String DATABASE_INSERT_DISPLAY_NAME_FOR_FILE =
//                        "INSERT OR IGNORE INTO File (DeviceId, PathId, DisplayNameId) " +
//            "VALUES ( " +
//                "(SELECT DisplayNameId FROM DisplayName WHERE DisplayNameValue = 'Test2'), " +
//                "(SELECT PathId FROM Path WHERE PathValue = 'test/path/to/something') );";
}
