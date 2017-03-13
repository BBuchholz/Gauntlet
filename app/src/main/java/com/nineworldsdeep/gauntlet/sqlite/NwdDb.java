package com.nineworldsdeep.gauntlet.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nineworldsdeep.gauntlet.core.AsyncOperation;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.IStatusActivity;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.mnemosyne.FileHashFragment;
import com.nineworldsdeep.gauntlet.mnemosyne.FileListItem;
import com.nineworldsdeep.gauntlet.mnemosyne.v4.PathTagLink;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaDevice;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaRoot;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaTagging;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MnemosyneV5ScanActivity;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Tag;
import com.nineworldsdeep.gauntlet.model.FileNode;
import com.nineworldsdeep.gauntlet.model.FileTagModelItem;
import com.nineworldsdeep.gauntlet.model.HashNode;
import com.nineworldsdeep.gauntlet.model.LocalConfigNode;
import com.nineworldsdeep.gauntlet.model.TagNode;
import com.nineworldsdeep.gauntlet.synergy.v2.ListEntry;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyUtils;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5List;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ListItem;
import com.nineworldsdeep.gauntlet.synergy.v5.SynergyV5ToDo;
import com.nineworldsdeep.gauntlet.tapestry.v1.TapestryUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by brent on 5/12/16.
 */
public class NwdDb {

    // Creating this separate class from the NwdDbOpenHelper per:
    // http://www.vogella.com/tutorials/AndroidSQLite/article.html
    // which does so, and I like the design better that way

    private final static Object lock = new Object();
    private static final String LOCAL_CONFIG_KEY_DEVICE_DESCRIPTION =
            "local-media-device-description";

    private SQLiteDatabase db;
    private NwdDbOpenHelper dbHelper;
    private File dbFilePath;
    private boolean isInternalDb;

    private static HashMap<String, NwdDb> instances =
            new HashMap<>();

    private static final String DATABASE_ENSURE_DISPLAY_NAME =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_DISPLAY_NAME +
                    " (" + NwdContract.COLUMN_DISPLAY_NAME_VALUE + ") VALUES (?); ";

    private static final String DATABASE_ENSURE_PATH =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_PATH +
                    " (" + NwdContract.COLUMN_PATH_VALUE + ") VALUES (?); ";

    private static final String DATABASE_ENSURE_HASH =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_HASH +
                    " (" + NwdContract.COLUMN_HASH_VALUE + ") VALUES (?); ";

    private static final String DATABASE_ENSURE_DEVICE =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_DEVICE +
                    " (" + NwdContract.COLUMN_DEVICE_DESCRIPTION + ") VALUES (?); ";

    private static final String DATABASE_ENSURE_TAG =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_TAG +
                    " (" + NwdContract.COLUMN_TAG_VALUE + ") VALUES (?); ";

    private static final String DATABASE_ENSURE_AUDIO_TRANSCRIPT =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_AUDIO_TRANSCRIPT +
                    " (" + NwdContract.COLUMN_AUDIO_TRANSCRIPT_VALUE + ") " +
            "VALUES (?); ";

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

    private static final String DATABASE_UPDATE_HASH_FOR_FILE =
            //"UPDATE " + NwdContract.TABLE_FILE + " " +
            "UPDATE OR IGNORE " + NwdContract.TABLE_FILE + " " +
                    "SET " + NwdContract.COLUMN_FILE_HASHED_AT + " = ?, " +
                        NwdContract.COLUMN_HASH_ID + " = " +
                        "(SELECT " + NwdContract.COLUMN_HASH_ID + " " +
                         "FROM " + NwdContract.TABLE_HASH + " " +
                         "WHERE " + NwdContract.COLUMN_HASH_VALUE + " = ?) " +
                    "WHERE " + NwdContract.COLUMN_PATH_ID + " = " +
                        "(SELECT " + NwdContract.COLUMN_PATH_ID + " " +
                         "FROM " + NwdContract.TABLE_PATH + " " +
                         "WHERE " + NwdContract.COLUMN_PATH_VALUE + " = ?) " +
                    "AND " + NwdContract.COLUMN_DEVICE_ID + " = " +
                        "(SELECT " + NwdContract.COLUMN_DEVICE_ID + " " +
                         "FROM " + NwdContract.TABLE_DEVICE + " " +
                         "WHERE " + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ?); ";

    private static final String DATABASE_ENSURE_DISPLAY_NAME_FOR_FILE =

            "INSERT OR IGNORE INTO File (" + NwdContract.COLUMN_DEVICE_ID + ", " +
                    NwdContract.COLUMN_PATH_ID + ", " +
                    NwdContract.COLUMN_DISPLAY_NAME_ID + ") " +
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


    private static final String DATABASE_ENSURE_AUDIO_TRANSCRIPT_FOR_FILE =

            "INSERT OR IGNORE INTO " + NwdContract.TABLE_AUDIO_TRANSCRIPT + " " +
                    "(" + NwdContract.COLUMN_FILE_ID + ", " +
                          NwdContract.COLUMN_AUDIO_TRANSCRIPT_VALUE + ") " +
            "VALUES ( " +
                "(SELECT " + NwdContract.COLUMN_FILE_ID + " " +
                         "FROM " + NwdContract.TABLE_FILE + " " +
                         "WHERE " + NwdContract.COLUMN_DEVICE_ID + " = " +
                            "(SELECT " + NwdContract.COLUMN_DEVICE_ID + " " +
                             "FROM " + NwdContract.TABLE_DEVICE + " " +
                             "WHERE " + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ?) " +
                         "AND " + NwdContract.COLUMN_PATH_ID + " = " +
                            "(SELECT " + NwdContract.COLUMN_PATH_ID + " " +
                             "FROM " + NwdContract.TABLE_PATH + " " +
                             "WHERE " + NwdContract.COLUMN_PATH_VALUE + " = ?)), " +
                "?); ";

    private static final String DATABASE_UPDATE_LOCAL_CONFIG =

            "UPDATE OR IGNORE " + NwdContract.TABLE_LOCAL_CONFIG + " " +
                    "SET " + NwdContract.COLUMN_LOCAL_CONFIG_VALUE + " = ? " +
                    "WHERE " + NwdContract.COLUMN_LOCAL_CONFIG_KEY + " = ?; ";

    private static final String DATABASE_ENSURE_LOCAL_CONFIG =

            "INSERT OR IGNORE INTO " + NwdContract.TABLE_LOCAL_CONFIG + " " +
                    "(" + NwdContract.COLUMN_LOCAL_CONFIG_KEY + ", " +
                          NwdContract.COLUMN_LOCAL_CONFIG_VALUE + ") " +
            "VALUES (?, ?); ";

    private static final String DATABASE_UPDATE_AUDIO_TRANSCRIPT_FOR_FILE =

            "UPDATE OR IGNORE " + NwdContract.TABLE_AUDIO_TRANSCRIPT + " " +
                    "SET " + NwdContract.COLUMN_AUDIO_TRANSCRIPT_VALUE + " = ? " +
                    "WHERE " + NwdContract.COLUMN_FILE_ID + " = " +
                        "(SELECT " + NwdContract.COLUMN_FILE_ID + " " +
                         "FROM " + NwdContract.TABLE_FILE + " " +
                         "WHERE PathId = " +
                            "(SELECT " + NwdContract.COLUMN_PATH_ID + " " +
                             "FROM " + NwdContract.TABLE_PATH + " " +
                             "WHERE " + NwdContract.COLUMN_PATH_VALUE + " = ?) " +
                         "AND " + NwdContract.COLUMN_DEVICE_ID + " = " +
                            "(SELECT " + NwdContract.COLUMN_DEVICE_ID + " " +
                            "FROM " + NwdContract.TABLE_DEVICE + " " +
                            "WHERE " + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ?)); ";

    private static final String DATABASE_ENSURE_TAG_FOR_FILE =

            //"INSERT INTO " + NwdContract.TABLE_FILE_TAGS + " " +
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_FILE_TAGS + " " +
                    "(" + NwdContract.COLUMN_FILE_ID + ", " +
                    NwdContract.COLUMN_TAG_ID + ") " +
            "VALUES ( " +
                "(SELECT " + NwdContract.COLUMN_FILE_ID + " " +
                         "FROM " + NwdContract.TABLE_FILE + " " +
                         "WHERE " + NwdContract.COLUMN_DEVICE_ID + " = " +
                            "(SELECT " + NwdContract.COLUMN_DEVICE_ID + " " +
                             "FROM " + NwdContract.TABLE_DEVICE + " " +
                             "WHERE " + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ?) " +
                         "AND " + NwdContract.COLUMN_PATH_ID + " = " +
                            "(SELECT " + NwdContract.COLUMN_PATH_ID + " " +
                             "FROM " + NwdContract.TABLE_PATH + " " +
                             "WHERE " + NwdContract.COLUMN_PATH_VALUE + " = ?)), " +
                "(SELECT " + NwdContract.COLUMN_TAG_ID + " " +
                         "FROM " + NwdContract.TABLE_TAG + " " +
                         "WHERE " + NwdContract.COLUMN_TAG_VALUE + " = ?)); ";

    private static final String DATABASE_ENSURE_HASH_FOR_FILE =

            //"INSERT INTO File (" + NwdContract.COLUMN_DEVICE_ID + ", " +
            "INSERT OR IGNORE INTO File (" + NwdContract.COLUMN_DEVICE_ID + ", " +
                    NwdContract.COLUMN_PATH_ID + ", " +
                    NwdContract.COLUMN_HASH_ID + ", " +
                    NwdContract.COLUMN_FILE_HASHED_AT + ") " +
            "VALUES ( " +
                "(SELECT " + NwdContract.COLUMN_DEVICE_ID + " " +
                         "FROM " + NwdContract.TABLE_DEVICE + " " +
                         "WHERE " + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ?), " +
                "(SELECT " + NwdContract.COLUMN_PATH_ID + " " +
                         "FROM " + NwdContract.TABLE_PATH + " " +
                         "WHERE " + NwdContract.COLUMN_PATH_VALUE + " = ?), " +
                "(SELECT " + NwdContract.COLUMN_HASH_ID + " " +
                         "FROM " + NwdContract.TABLE_HASH + " " +
                         "WHERE " + NwdContract.COLUMN_HASH_VALUE + " = ?), " +
                "?); ";


    private static final String DATABASE_ENSURE_FILE =
            "INSERT OR IGNORE INTO " + NwdContract.TABLE_FILE +
                    " (" + NwdContract.COLUMN_DEVICE_ID + ", " +
                    NwdContract.COLUMN_PATH_ID + ") " +
            "VALUES ( " +
                "(SELECT " + NwdContract.COLUMN_DEVICE_ID + " " +
                         "FROM " + NwdContract.TABLE_DEVICE + " " +
                         "WHERE " + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ?), " +
                "(SELECT " + NwdContract.COLUMN_PATH_ID + " " +
                         "FROM " + NwdContract.TABLE_PATH + " " +
                         "WHERE " + NwdContract.COLUMN_PATH_VALUE + " = ?)); ";

    private static final String DATABASE_GET_DISPLAY_NAMES_FOR_PATHS =
            "SELECT " +
                    "" + NwdContract.COLUMN_DEVICE_DESCRIPTION + ", " +
                    "" + NwdContract.COLUMN_PATH_VALUE + ", " +
                    "" + NwdContract.COLUMN_DISPLAY_NAME_VALUE + " " +
            "FROM " + NwdContract.TABLE_PATH + " p " +
            "JOIN " + NwdContract.TABLE_FILE + " f " +
            "ON p." + NwdContract.COLUMN_PATH_ID + " = f." + NwdContract.COLUMN_PATH_ID + " " +
            "JOIN " + NwdContract.TABLE_DISPLAY_NAME + " dn " +
            "ON f." + NwdContract.COLUMN_DISPLAY_NAME_ID + " = dn." + NwdContract.COLUMN_DISPLAY_NAME_ID + " " +
            "JOIN " + NwdContract.TABLE_DEVICE + " d " +
            "ON d." + NwdContract.COLUMN_DEVICE_ID + " = f." + NwdContract.COLUMN_DEVICE_ID + " " +
            "WHERE d." + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ? ; ";


    private static final String DATABASE_GET_HASHES_FOR_PATHS =
            "SELECT " +
                    "" + NwdContract.COLUMN_PATH_VALUE + ", " +
                    "" + NwdContract.COLUMN_HASH_VALUE + " " +
            "FROM " + NwdContract.TABLE_PATH + " p " +
            "JOIN " + NwdContract.TABLE_FILE + " f " +
            "ON p." + NwdContract.COLUMN_PATH_ID + " = f." + NwdContract.COLUMN_PATH_ID + " " +
            "JOIN " + NwdContract.TABLE_HASH + " h " +
            "ON f." + NwdContract.COLUMN_HASH_ID + " = h." + NwdContract.COLUMN_HASH_ID + " " +
            "JOIN " + NwdContract.TABLE_DEVICE + " d " +
            "ON d." + NwdContract.COLUMN_DEVICE_ID + " = f." + NwdContract.COLUMN_DEVICE_ID + " " +
            "WHERE d." + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ? ; ";


    private static final String DATABASE_GET_TAGS_FOR_PATHS =
            "SELECT " +
                    "" + NwdContract.COLUMN_PATH_VALUE + ", " +
                    "" + NwdContract.COLUMN_TAG_VALUE + " " +
            "FROM " + NwdContract.TABLE_PATH + " p " +
            "JOIN " + NwdContract.TABLE_FILE + " f " +
            "ON p." + NwdContract.COLUMN_PATH_ID + " = f." + NwdContract.COLUMN_PATH_ID + " " +
            "JOIN " + NwdContract.TABLE_FILE_TAGS + " ft " +
            "ON f." + NwdContract.COLUMN_FILE_ID + " = ft." + NwdContract.COLUMN_FILE_ID + " " +
            "JOIN " + NwdContract.TABLE_TAG + " t " +
            "ON ft." + NwdContract.COLUMN_TAG_ID + " = t." + NwdContract.COLUMN_TAG_ID + " " +
            "JOIN " + NwdContract.TABLE_DEVICE + " d " +
            "ON d." + NwdContract.COLUMN_DEVICE_ID + " = f." + NwdContract.COLUMN_DEVICE_ID + " " +
            "WHERE d." + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ? ; ";

    private static final String DATABASE_GET_DISPLAY_NAME_FOR_PATH =
            "SELECT " +
                    "" + NwdContract.COLUMN_DEVICE_DESCRIPTION + ", " +
                    "" + NwdContract.COLUMN_PATH_VALUE + ", " +
                    "" + NwdContract.COLUMN_DISPLAY_NAME_VALUE + " " +
            "FROM " + NwdContract.TABLE_PATH + " p " +
            "JOIN " + NwdContract.TABLE_FILE + " f " +
            "ON p." + NwdContract.COLUMN_PATH_ID + " = f." + NwdContract.COLUMN_PATH_ID + " " +
            "JOIN " + NwdContract.TABLE_DISPLAY_NAME + " dn " +
            "ON f." + NwdContract.COLUMN_DISPLAY_NAME_ID + " = dn." + NwdContract.COLUMN_DISPLAY_NAME_ID + " " +
            "JOIN " + NwdContract.TABLE_DEVICE + " d " +
            "ON d." + NwdContract.COLUMN_DEVICE_ID + " = f." + NwdContract.COLUMN_DEVICE_ID + " " +
            "WHERE d." + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ? " +
            "AND p." + NwdContract.COLUMN_PATH_VALUE + " = ? ; ";

    private static final String DATABASE_DELETE_TAGS_FOR_PATH =
            "DELETE FROM " + NwdContract.TABLE_FILE_TAGS + " " +
                    "WHERE " + NwdContract.COLUMN_FILE_ID + " = (" +
                        "SELECT f." + NwdContract.COLUMN_FILE_ID + " " +
                        "FROM " + NwdContract.TABLE_PATH + " p " +
                        "JOIN " + NwdContract.TABLE_FILE + " f " +
                        "ON p." + NwdContract.COLUMN_PATH_ID + " = f." + NwdContract.COLUMN_PATH_ID + " " +
                        "JOIN " + NwdContract.TABLE_DEVICE + " d " +
                        "ON f." + NwdContract.COLUMN_DEVICE_ID + " = d." + NwdContract.COLUMN_DEVICE_ID + " " +
                        "WHERE d." + NwdContract.COLUMN_DEVICE_DESCRIPTION + " = ? " +
                        "AND p." + NwdContract.COLUMN_PATH_VALUE + " = ? )";

    public static NwdDb getInstance(Context c){

        return getInstance(c, null);
    }

    public static NwdDb getInstance(Context c, String dbName) {

        synchronized (lock) {

            if (Configuration.isInTestMode()) {

                dbName = "test";

                if (!instances.containsKey(dbName)) {

                    instances.put(dbName, new NwdDb(c, dbName));
                }

            } else if (Utils.stringIsNullOrWhitespace(dbName) ||
                    dbName.trim().equalsIgnoreCase("nwd")) {

                dbName = "nwd";

                if (!instances.containsKey(dbName)) {

                    instances.put(dbName, new NwdDb(c));
                }

            } else {

                if (!instances.containsKey(dbName)) {

                    instances.put(dbName, new NwdDb(c, dbName));
                }
            }

            return instances.get(dbName);
        }
    }

    /**
     * Opens/Creates the internal database for Gauntlet/NWD
     * @param context
     */
    private NwdDb(Context context){

        dbHelper = NwdDbOpenHelper.getInstance(context);
        dbFilePath = context.getDatabasePath(dbHelper.getDatabaseName());
        isInternalDb = true;
    }

    /**
     * Opens/Creates an external database for Gauntlet/NWD with the specified name
     * in the NWD/sqlite directory. Intended for imports and exports.
     * @param context
     * @param databaseName
     */
    private NwdDb(Context context, String databaseName){

        //this is necessary for dbFilePath assignment below
        context = new NwdDbContextWrapper(context);

        dbHelper = NwdDbOpenHelper.getInstance(context, databaseName);
        dbFilePath = context.getDatabasePath(dbHelper.getDatabaseName());
        isInternalDb = false;
    }

    public void open() throws SQLException {

        db = dbHelper.getWritableDatabase();
    }

    public void close(){

        dbHelper.close();
    }

    public String getDatabaseName(){

        return dbHelper.getDatabaseName();
    }

    public boolean isInternalDb(){

        return isInternalDb;
    }

    /**
     * writes a timestamped copy of the database
     * to NWD/sqlite
     */
    public void export(Context context){

        try{

            String timeStampedDbName =
                    SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss() +
                            "-" + dbHelper.getDatabaseName();

            File exportFile = Configuration.getSqliteDb(timeStampedDbName);

            FileChannel src = new FileInputStream(dbFilePath).getChannel();
            FileChannel dest = new FileOutputStream(exportFile).getChannel();

            dest.transferFrom(src, 0, src.size());

            src.close();
            dest.close();

            Utils.toast(context, "database exported: " +
                    exportFile.getAbsolutePath());

        }catch(Exception ex){

            Utils.toast(context, "error exporting database: " + ex.getMessage());
        }
    }

    /**
     * writes a timestamped copy of the database
     * to NWD/sqlite.
     *
     * returns the output path
     */
    public String export() throws Exception{


        String timeStampedDbName =
                SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss() +
                        "-" + dbHelper.getDatabaseName();

        File exportFile = Configuration.getSqliteDb(timeStampedDbName);

        FileChannel src = new FileInputStream(dbFilePath).getChannel();
        FileChannel dest = new FileOutputStream(exportFile).getChannel();

        dest.transferFrom(src, 0, src.size());

        src.close();
        dest.close();

        return "database exported: " + exportFile.getAbsolutePath();
    }


    public void linkFileToDisplayName(String filePath, String displayName){

        linkFileToDisplayName(
                TapestryUtils.getCurrentDeviceName(),
                filePath,
                displayName);
    }

    /**
     * assumes an open transaction (open and close outside of this method)
     * @param deviceDescription
     * @param filePath
     * @param displayName
     */
    private void ensureDisplayName(String deviceDescription,
                                   String filePath,
                                   String displayName){

        //insert or ignore device
        db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceDescription});
        //insert or ignore path
        db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
        //insert or ignore display name
        db.execSQL(DATABASE_ENSURE_DISPLAY_NAME, new String[]{displayName});
        //update or ignore file (if exists)
        db.execSQL(DATABASE_UPDATE_DISPLAY_NAME_FOR_FILE,
                new String[]{displayName, filePath, deviceDescription});
        //insert or ignore file (if !exists)
        db.execSQL(DATABASE_ENSURE_DISPLAY_NAME_FOR_FILE,
                new String[]{deviceDescription, filePath, displayName});
    }

    public void linkFileToDisplayName(String deviceDescription,
                                      String filePath,
                                      String displayName){
        //open transaction
        db.beginTransaction();

        try{

//            //insert or ignore device
//            db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceDescription});
//            //insert or ignore path
//            db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
//            //insert or ignore display name
//            db.execSQL(DATABASE_ENSURE_DISPLAY_NAME, new String[]{displayName});
//            //update or ignore file (if exists)
//            db.execSQL(DATABASE_UPDATE_DISPLAY_NAME_FOR_FILE,
//                    new String[]{displayName, filePath, deviceDescription});
//            //insert or ignore file (if !exists)
//            db.execSQL(DATABASE_ENSURE_DISPLAY_NAME_FOR_FILE,
//                    new String[]{deviceDescription, filePath, displayName});

            ensureDisplayName(deviceDescription, filePath, displayName);

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error linking display name [" +
                    displayName + "] to file [" +
                    filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    /**
     * assumes an open transaction (open and close outside of this method)
     * @param deviceName
     * @param filePath
     * @param sha1Hash
     * @param hashedAt
     */
    private void ensureHash(String deviceName,
                            String filePath,
                            String sha1Hash,
                            String hashedAt){

        //insert or ignore device
        db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceName});
        //insert or ignore path
        db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
        //insert or ignore hash
        db.execSQL(DATABASE_ENSURE_HASH, new String[]{sha1Hash});
        //update or ignore file (if exists)
        db.execSQL(DATABASE_UPDATE_HASH_FOR_FILE,
                new String[]{hashedAt, sha1Hash, filePath, deviceName});
        //insert or ignore file (if !exists)
        db.execSQL(DATABASE_ENSURE_HASH_FOR_FILE,
                new String[]{deviceName, filePath, sha1Hash,hashedAt});
    }

    public void linkHashToFile(String deviceName,
                               String filePath,
                               String sha1Hash,
                               String hashedAt) {
        //open transaction
        db.beginTransaction();

        try{

//            //insert or ignore device
//            db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceName});
//            //insert or ignore path
//            db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
//            //insert or ignore hash
//            db.execSQL(DATABASE_ENSURE_HASH, new String[]{sha1Hash});
//            //update or ignore file (if exists)
//            db.execSQL(DATABASE_UPDATE_HASH_FOR_FILE,
//                    new String[]{hashedAt, sha1Hash, filePath, deviceName});
//            //insert or ignore file (if !exists)
//            db.execSQL(DATABASE_ENSURE_HASH_FOR_FILE,
//                    new String[]{deviceName, filePath, sha1Hash,hashedAt});

            ensureHash(deviceName, filePath, sha1Hash, hashedAt);

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error linking SHA1 hash [" +
                    sha1Hash + "] to file [" +
                    filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    /**
     * assumes an open transaction (open and close outside of this method)
     * @param deviceName
     * @param filePath
     * @param tag
     */
    private void ensureTag(String deviceName, String filePath, TagNode tag){

        String tagValue = tag.value();

        //insert or ignore device
        db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceName});
        //insert or ignore path
        db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
        //insert or ignore hash
        db.execSQL(DATABASE_ENSURE_TAG, new String[]{tagValue});
        //insert or ignore file tag entry
        db.execSQL(DATABASE_ENSURE_TAG_FOR_FILE,
                new String[]{deviceName, filePath, tagValue});
    }
    public void linkTagToFile(String deviceName, String filePath, TagNode tag) {

        //open transaction
        db.beginTransaction();

        try{

//            //insert or ignore device
//            db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceName});
//            //insert or ignore path
//            db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
//            //insert or ignore hash
//            db.execSQL(DATABASE_ENSURE_TAG, new String[]{tag});
//            //insert or ignore file tag entry
//            db.execSQL(DATABASE_ENSURE_TAG_FOR_FILE,
//                    new String[]{deviceName, filePath, tag});

            ensureTag(deviceName, filePath, tag);

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error linking tag [" +
                    tag + "] to file [" +
                    filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    public void linkTagsToFile(MultiMapString pathToTags){

        String deviceDescription = TapestryUtils.getCurrentDeviceName();

        //open transaction
        db.beginTransaction();

        for(String path : pathToTags.keySet()){

            for(String tag : pathToTags.get(path)) {

                tag = tag.trim();

                try{

                    //insert or ignore device
                    db.execSQL(DATABASE_ENSURE_DEVICE,
                            new String[]{deviceDescription});

                    //insert or ignore path
                    db.execSQL(DATABASE_ENSURE_PATH,
                            new String[]{path});

                    //insert or ignore tag
                    db.execSQL(DATABASE_ENSURE_TAG, new String[]{tag});

                    //insert or ignore file (if !exists)
                    db.execSQL(DATABASE_ENSURE_FILE,
                        new String[]{deviceDescription, path});

                    //insert or ignore file tag entry
                    db.execSQL(DATABASE_ENSURE_TAG_FOR_FILE,
                            new String[]{deviceDescription, path, tag});

                }catch(Exception ex) {

                    Utils.log("error linking tag [" +
                            tag + "] to file path [" +
                            path + "]: " + ex.getMessage());

                }
            }
        }

        db.setTransactionSuccessful();

        db.endTransaction();
    }

    /**
     * determines whether the current db address is consistent with
     * the current status of "test mode" in our configuration settings
     *
     * @return this method will return true if Configuration.isInTestMode
     * is TRUE and the db path DOES NOT contain "NWD-SNDBX".
     * it will also return true if Configuration.isInTestMode
     * returns FALSE and the path DOES contain "NWD-SNDBX".
     * the opposite conditios for each case return false;
     * intended for supporting switching in and out of test mode
     * in an activity (the db can be reopened/reassigned at
     * the alternate address)
     */
    public boolean needsTestModeRefresh() {

        if(Configuration.isInTestMode() &&
                !dbFilePath.getAbsolutePath().contains("NWD-SNDBX")){

            return true;
        }

        if(!Configuration.isInTestMode() &&
                dbFilePath.getAbsolutePath().contains("NWD-SNDBX")){

            return true;
        }

        return false;
    }

    public void ensureDevicePath(String deviceName, String filePath) {

        //open transaction
        db.beginTransaction();

        try{

            //insert or ignore device
            db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceName});
            //insert or ignore path
            db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
            //insert or ignore file (if !exists)
            db.execSQL(DATABASE_ENSURE_FILE,
                    new String[]{deviceName, filePath});

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error ensuring file for device [" +
                    deviceName + "] and path [" +
                    filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    /**
     * assumes an open transaction (open and close outside of this method)
     */
    private void ensureAudioTranscript(String deviceName,
                                       String filePath,
                                       String transcription){

        //insert or ignore device
        db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceName});
        //insert or ignore path
        db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
        //insert or ignore audio transcript
        db.execSQL(DATABASE_ENSURE_AUDIO_TRANSCRIPT,
                new String[]{transcription});
        //insert or ignore file (if !exists)
        db.execSQL(DATABASE_ENSURE_FILE,
                new String[]{deviceName, filePath});
        //update or ignore audio transcript (if exists)
        db.execSQL(DATABASE_UPDATE_AUDIO_TRANSCRIPT_FOR_FILE,
                new String[]{transcription, filePath, deviceName});
        //insert or ignore file (if !exists)
        db.execSQL(DATABASE_ENSURE_AUDIO_TRANSCRIPT_FOR_FILE,
                new String[]{deviceName, filePath, transcription});
    }

    public void updateAudioTranscriptForFile(String deviceName,
                                             String filePath,
                                             String transcription) {


        //open transaction
        db.beginTransaction();

        try{

//            //insert or ignore device
//            db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceName});
//            //insert or ignore path
//            db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
//            //insert or ignore audio transcript
//            db.execSQL(DATABASE_ENSURE_AUDIO_TRANSCRIPT,
//                    new String[]{transcription});
//            //insert or ignore file (if !exists)
//            db.execSQL(DATABASE_ENSURE_FILE,
//                    new String[]{deviceName, filePath});
//            //update or ignore audio transcript (if exists)
//            db.execSQL(DATABASE_UPDATE_AUDIO_TRANSCRIPT_FOR_FILE,
//                    new String[]{transcription, filePath, deviceName});
//            //insert or ignore file (if !exists)
//            db.execSQL(DATABASE_ENSURE_AUDIO_TRANSCRIPT_FOR_FILE,
//                    new String[]{deviceName, filePath, transcription});

            ensureAudioTranscript(deviceName, filePath, transcription);

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error updating audio transcript [" + transcription + "] " +
                    "for device [" + deviceName + "] " +
                    "and path [" + filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    /**
     * assumes an open transaction (open and close outside of this method)
     * @param key
     * @param val
     */
    private void ensureConfigValue(String key, String val){

        //update or ignore config value (if exists)
        db.execSQL(DATABASE_UPDATE_LOCAL_CONFIG,
                new String[]{val, key});
        //insert or ignore config value (if !exists)
        db.execSQL(DATABASE_ENSURE_LOCAL_CONFIG,
                new String[]{key, val});
    }

    public void setConfigValue(String key, String val) {

        //open transaction
        db.beginTransaction();

        try{

//            //update or ignore config value (if exists)
//            db.execSQL(DATABASE_UPDATE_LOCAL_CONFIG,
//                    new String[]{val, key});
//            //insert or ignore config value (if !exists)
//            db.execSQL(DATABASE_ENSURE_LOCAL_CONFIG,
//                    new String[]{key, val});

            ensureConfigValue(key, val);

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error updating local config key [" + key + "] " +
                    "with value [" + val + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    public boolean is(NwdDb db) {

        return (this.isInternalDb() == db.isInternalDb())
                &&
                (this.getDatabaseName().toLowerCase() ==
                        db.getDatabaseName().toLowerCase());
    }

    public List<Map<String, String>> getPathDisplayNamesForCurrentDevice() {

        List<Map<String, String>> pathDisplayNames =
                new ArrayList<>();

        //open transaction
        db.beginTransaction();

        try{

            String[] args =
                    new String[]{TapestryUtils.getCurrentDeviceName()};

            Cursor c =
                    db.rawQuery(DATABASE_GET_DISPLAY_NAMES_FOR_PATHS,args);

            String[] columnNames =
                    new String[]{ NwdContract.COLUMN_DISPLAY_NAME_VALUE,
                                  NwdContract.COLUMN_PATH_VALUE };

            if (c.getCount() > 0)
            {
                c.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(c, columnNames);

                    pathDisplayNames.add(record);

                } while (c.moveToNext());

                c.close();
            }

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error retrieving path display names " +
                    "for current device: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return pathDisplayNames;
    }

    private Map<String, String> cursorToRecord(Cursor c, String[] columnNames){

        Map<String, String> record =
                            new HashMap<>();

        for(String colName : columnNames){

            String colValue = null;

            if(!c.isNull(c.getColumnIndex(colName))){

                colValue = c.getString(c.getColumnIndex(colName));
            }

            record.put(colName, colValue);
        }

        return record;
    }

    public String getDisplayNameForPath(String filePath) {

        String displayName = "";

//        if(!db.isOpen()){
//
//            open();
//        }

        //open transaction
        db.beginTransaction();

        try{

            String[] args =
                    new String[]{TapestryUtils.getCurrentDeviceName(),
                                 filePath};

            Cursor c =
                    db.rawQuery(DATABASE_GET_DISPLAY_NAME_FOR_PATH,args);

            if (c.getCount() > 0)
            {
                c.moveToFirst();

                displayName =
                        c.getString(
                                c.getColumnIndex(
                                        NwdContract.COLUMN_DISPLAY_NAME_VALUE));

                c.close();
            }

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error retrieving path display names " +
                    "for current device: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return displayName;
    }

    public void populateIdAndTimeStampsForSynergyV5ListName(Context c,
                                                            SynergyV5List lst){

        db.beginTransaction();

        try{

            String listName = lst.getListName();

            String[] args =
                    new String[]{
                           listName
                    };

            Cursor cursor =
                    db.rawQuery(
        NwdContract.SYNERGY_V5_SELECT_ID_ACTIVATED_AT_SHELVED_AT_FOR_LIST_NAME,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SYNERGY_LIST_ID,
                            NwdContract.COLUMN_SYNERGY_LIST_ACTIVATED_AT,
                            NwdContract.COLUMN_SYNERGY_LIST_SHELVED_AT
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    //this method will grab the last entry, if multiple returned
                    //though list name is unique, so it should never matter
                    String idString =
                            record.get(NwdContract.COLUMN_SYNERGY_LIST_ID);

                    String activatedString =
                            record.get(NwdContract.COLUMN_SYNERGY_LIST_ACTIVATED_AT);
                    String shelvedString =
                            record.get(NwdContract.COLUMN_SYNERGY_LIST_SHELVED_AT);

                    Date activated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    activatedString);

                    Date shelved =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    shelvedString);

                    lst.setListId(Integer.parseInt(idString));
                    lst.setTimeStamps(activated, shelved);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception retrieving list id: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

    }

    public int getIdForSynergyV5ListName(Context c, String listName){

        int id = -1;

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{
                            listName
                    };

            Cursor cursor =
                    db.rawQuery(
                        NwdContract.SYNERGY_V5_SELECT_ID_FOR_LIST_NAME_X,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SYNERGY_LIST_ID
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    //this method will grab the last entry, if multiple returned
                    //though list name is unique, so it should never matter
                    String idString =
                            record.get(NwdContract.COLUMN_SYNERGY_LIST_ID);

                    id = Integer.parseInt(idString);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception retrieving list id: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return id;
    }

    public void linkFilesToDisplayNames(List<FileListItem> fileListItems) {

//        if(!db.isOpen()){
//
//            open();
//        }

        String deviceDescription = TapestryUtils.getCurrentDeviceName();

        //open transaction
        db.beginTransaction();

        for(FileListItem fli : fileListItems){

            String filePath = fli.getFile().getAbsolutePath();
            String displayName = fli.getDisplayName();


            try{

                //insert or ignore device
                db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceDescription});
                //insert or ignore path
                db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
                //insert or ignore display name
                db.execSQL(DATABASE_ENSURE_DISPLAY_NAME, new String[]{displayName});
                //update or ignore file (if exists)
                db.execSQL(DATABASE_UPDATE_DISPLAY_NAME_FOR_FILE,
                        new String[]{displayName, filePath, deviceDescription});
                //insert or ignore file (if !exists)
                db.execSQL(DATABASE_ENSURE_DISPLAY_NAME_FOR_FILE,
                        new String[]{deviceDescription, filePath, displayName});

            }catch(Exception ex) {

                Utils.log("error linking display name [" +
                        displayName + "] to file [" +
                        filePath + "]: " + ex.getMessage());

            }
        }

        db.setTransactionSuccessful();

        db.endTransaction();

    }

    public List<Map<String, String>> getPathHashRecordsForCurrentDevice() {

        List<Map<String,String>> pathHashes =
                new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{TapestryUtils.getCurrentDeviceName()};

            Cursor c =
                    db.rawQuery(DATABASE_GET_HASHES_FOR_PATHS, args);

            String[] columnNames =
                    new String[]{ NwdContract.COLUMN_HASH_VALUE,
                                  NwdContract.COLUMN_PATH_VALUE };

            if (c.getCount() > 0)
            {
                c.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(c, columnNames);

                    pathHashes.add(record);

                } while (c.moveToNext());

                c.close();
            }

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error retrieving path display names " +
                    "for current device: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return pathHashes;
    }

    public void linkFilesToHashes(HashMap<String, String> pathToHashMap) {


        //hack
        String hashedAt = SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss();

        db.beginTransaction();

        for(String path : pathToHashMap.keySet()){

            String deviceDescription = TapestryUtils.getCurrentDeviceName();
            String filePath = path;
            String hash= pathToHashMap.get(path);

            try{

                //insert or ignore device
                db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceDescription});
                //insert or ignore path
                db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
                //insert or ignore display name
                db.execSQL(DATABASE_ENSURE_HASH, new String[]{hash});
                //update or ignore file (if exists)
                db.execSQL(DATABASE_UPDATE_HASH_FOR_FILE,
                        new String[]{hashedAt, hash, filePath, deviceDescription});
                //insert or ignore file (if !exists)
                db.execSQL(DATABASE_ENSURE_HASH_FOR_FILE,
                        new String[]{deviceDescription, filePath, hash, hashedAt});

            }catch(Exception ex) {

                Utils.log("error linking hash [" +
                        hash + "] to file [" +
                        filePath + "]: " + ex.getMessage());

            }
        }

        db.setTransactionSuccessful();

        db.endTransaction();
    }

    public void linkFilesToHashes(List<FileHashFragment> fileHashFragments) {


        //hack
        String hashedAt = SynergyUtils.getCurrentTimeStamp_yyyyMMddHHmmss();

        db.beginTransaction();

        for(FileHashFragment fhf : fileHashFragments){

            String deviceDescription = TapestryUtils.getCurrentDeviceName();
            String filePath = fhf.getPath();
            String hash= fhf.getHash();

            try{

                //insert or ignore device
                db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceDescription});
                //insert or ignore path
                db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
                //insert or ignore display name
                db.execSQL(DATABASE_ENSURE_HASH, new String[]{hash});
                //update or ignore file (if exists)
                db.execSQL(DATABASE_UPDATE_HASH_FOR_FILE,
                        new String[]{hashedAt, hash, filePath, deviceDescription});
                //insert or ignore file (if !exists)
                db.execSQL(DATABASE_ENSURE_HASH_FOR_FILE,
                        new String[]{deviceDescription, filePath, hash, hashedAt});

            }catch(Exception ex) {

                Utils.log("error linking hash [" +
                        hash + "] to file [" +
                        filePath + "]: " + ex.getMessage());

            }
        }

        db.setTransactionSuccessful();

        db.endTransaction();
    }

    public List<Map<String, String>> getV5PathTagRecordsForCurrentDevice(){

                List<Map<String,String>> pathTags =
                new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{TapestryUtils.getCurrentDeviceName()};

            Cursor c =
                    db.rawQuery(
                NwdContract.MNEMOSYNE_V5_GET_TAGS_FOR_PATHS_FOR_DEVICE_NAME_X,
                            args);

            String[] columnNames =
                    new String[]{ NwdContract.COLUMN_MEDIA_TAG_VALUE,
                                  NwdContract.COLUMN_MEDIA_PATH_VALUE };

            if (c.getCount() > 0)
            {
                c.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(c, columnNames);

                    pathTags.add(record);

                } while (c.moveToNext());

                c.close();
            }

            db.setTransactionSuccessful();

        }finally {

            db.endTransaction();
        }

        return pathTags;
    }

    public List<Map<String, String>> getPathTagRecordsForCurrentDevice() {

        List<Map<String,String>> pathTags =
                new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{TapestryUtils.getCurrentDeviceName()};

            Cursor c =
                    db.rawQuery(DATABASE_GET_TAGS_FOR_PATHS, args);

            String[] columnNames =
                    new String[]{ NwdContract.COLUMN_TAG_VALUE,
                                  NwdContract.COLUMN_PATH_VALUE };

            if (c.getCount() > 0)
            {
                c.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(c, columnNames);

                    pathTags.add(record);

                } while (c.moveToNext());

                c.close();
            }

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error retrieving path tags " +
                    "for current device: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return pathTags;
    }

    public void removeTagsForFile(String path) {

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{
                            TapestryUtils.getCurrentDeviceName(), path};

            db.execSQL(DATABASE_DELETE_TAGS_FOR_PATH, args);

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error deleting tags " +
                    "for path [" + path + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    public void linkTagStringToFile(String path, String tags) {

        MultiMapString pathToTags = new MultiMapString();

        pathToTags.putCommaStringValues(path, tags);

        linkTagsToFile(pathToTags);
    }

    public List<LocalConfigNode> getLocalConfig(Context context) {

        final String DATABASE_GET_LOCAL_CONFIG =
                "" +
                        "SELECT " +
                        NwdContract.COLUMN_LOCAL_CONFIG_KEY + ", " +
                        NwdContract.COLUMN_LOCAL_CONFIG_VALUE + " " +

                        "FROM " +
                        NwdContract.TABLE_LOCAL_CONFIG;

        List<LocalConfigNode> cfg =
                new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor c =
                    db.rawQuery(DATABASE_GET_LOCAL_CONFIG, args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_LOCAL_CONFIG_KEY,
                            NwdContract.COLUMN_LOCAL_CONFIG_VALUE
                    };

            if (c.getCount() > 0)
            {
                c.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(c, columnNames);

                    cfg.add(new LocalConfigNode(record));

                } while (c.moveToNext());

                c.close();
            }

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log(context, "error retrieving path tags " +
                    "for current device: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return cfg;

        //throw new NotImplementedException("NwdDb.getLocalConfig() not implemented");
    }

    public List<FileNode> getFiles(Context context) {

        final String DATABASE_GET_FILES =
                "" +
                    "SELECT " +
                            "f." + NwdContract.COLUMN_FILE_ID + ", " +
                            "d." + NwdContract.COLUMN_DEVICE_DESCRIPTION + ", " +
                            "p." + NwdContract.COLUMN_PATH_VALUE + ", " +
                            "dn." + NwdContract.COLUMN_DISPLAY_NAME_VALUE + ", " +
                            "h." + NwdContract.COLUMN_HASH_VALUE + ", " +
                            "f." + NwdContract.COLUMN_FILE_HASHED_AT + ", " +
                            "f." + NwdContract.COLUMN_FILE_DESCRIPTION + ", " +
                            "f." + NwdContract.COLUMN_FILE_NAME + ", " +
                            "at." + NwdContract.COLUMN_AUDIO_TRANSCRIPT_VALUE + " " +
                        "FROM " + NwdContract.TABLE_FILE + " f " +
                        "JOIN " + NwdContract.TABLE_DEVICE + " d " +
                        "ON f." + NwdContract.COLUMN_DEVICE_ID + " = " +
                            "d." + NwdContract.COLUMN_DEVICE_ID + " " +
                        "JOIN " + NwdContract.TABLE_PATH + " p " +
                        "ON f." + NwdContract.COLUMN_PATH_ID + " = " +
                            "p." + NwdContract.COLUMN_PATH_ID + " " +
                        "LEFT JOIN " + NwdContract.TABLE_DISPLAY_NAME + " dn " +
                        "ON f." + NwdContract.COLUMN_DISPLAY_NAME_ID + " = " +
                            "dn." + NwdContract.COLUMN_DISPLAY_NAME_ID + " " +
                        "LEFT JOIN " + NwdContract.TABLE_HASH + " h " +
                        "ON f." + NwdContract.COLUMN_HASH_ID + " = " +
                            "h." + NwdContract.COLUMN_HASH_ID + " " +
                        "LEFT JOIN " + NwdContract.TABLE_AUDIO_TRANSCRIPT + " at " +
                        "ON f." + NwdContract.COLUMN_FILE_ID + " = " +
                            "at." + NwdContract.COLUMN_FILE_ID + " ";

        List<FileNode> files =
                new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor c =
                    db.rawQuery(DATABASE_GET_FILES, args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_FILE_ID,
                            NwdContract.COLUMN_DEVICE_DESCRIPTION,
                            NwdContract.COLUMN_PATH_VALUE,
                            NwdContract.COLUMN_DISPLAY_NAME_VALUE,
                            NwdContract.COLUMN_HASH_VALUE,
                            NwdContract.COLUMN_FILE_HASHED_AT,
                            NwdContract.COLUMN_FILE_DESCRIPTION,
                            NwdContract.COLUMN_FILE_NAME,
                            NwdContract.COLUMN_AUDIO_TRANSCRIPT_VALUE
                    };

            if (c.getCount() > 0)
            {
                c.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(c, columnNames);

                    files.add(new FileNode(record));

                } while (c.moveToNext());

                c.close();
            }

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log(context, "error retrieving path tags " +
                    "for current device: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }

        MultiMapString fileTags = getFileTags(context);

        for(FileNode f : files){

            if(fileTags.containsKey(f.getId())){

                for(String tag : fileTags.get(f.getId())){

                    //f.getTags().add(new TagModelItem(f, tag));
                    f.add(new TagNode(f, tag));
                }
            }
        }

        return files;
    }

    public MultiMapString getFileTags(Context context){

        final String DATABASE_GET_FILE_TAGS =
                "SELECT " +
                    "ft." + NwdContract.COLUMN_FILE_ID + ", " +
                    "t." + NwdContract.COLUMN_TAG_VALUE + " " +
                "FROM " + NwdContract.TABLE_FILE_TAGS + " ft " +
                "JOIN " + NwdContract.TABLE_TAG + " t " +
                "ON ft." + NwdContract.COLUMN_TAG_ID + " = " +
                    "t." + NwdContract.COLUMN_TAG_ID + " ";

        MultiMapString fileTags = new MultiMapString();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor c =
                    db.rawQuery(DATABASE_GET_FILE_TAGS, args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_FILE_ID,
                            NwdContract.COLUMN_TAG_VALUE
                    };

            if (c.getCount() > 0)
            {
                c.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(c, columnNames);

                    FileTagModelItem fileTag = new FileTagModelItem(record);

                    fileTags.put(fileTag.getFileId().toString(), fileTag.getTagValue());

                } while (c.moveToNext());

                c.close();
            }

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log(context, "error retrieving path tags " +
                    "for current device: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return fileTags;
    }

    public void importConfig(Context context, List<LocalConfigNode> cfg) {

        db.beginTransaction();

        for(LocalConfigNode lcmi : cfg){

            try{

                String key = lcmi.getKey();
                String val = lcmi.getValue();

                if(!Utils.stringIsNullOrWhitespace(key) &&
                        val != null){

                    ensureConfigValue(key, val);
                }

            }catch(Exception ex){

                Utils.log(context, "error importing config data for key [" +
                        lcmi.getKey() + "] and value [" + lcmi.getValue());
            }
        }

        db.setTransactionSuccessful();

        db.endTransaction();
    }

    public void importFiles(Context context, List<FileNode> files) {

        db.beginTransaction();

        for(FileNode fmi : files){

            String deviceDescription = fmi.getDevice();
            String filePath = fmi.getPath();
            String displayName = fmi.getDisplayName();

            try{

                if(!Utils.stringIsNullOrWhitespace(displayName)){

                    ensureDisplayName(deviceDescription, filePath, displayName);
                }

                if(fmi.hashCount() > 0){

                    Iterator<HashNode> itr = fmi.getHashes();

                    while(itr.hasNext()){

                        HashNode hmi = itr.next();

                        String hash = hmi.getHash();
                        String hashedAt = hmi.getHashedAt();

                        if(Utils.stringIsNullOrWhitespace(hashedAt)){

                            hashedAt =
                                    SynergyUtils
                                            .getCurrentTimeStamp_yyyyMMddHHmmss();
                        }

                        if(!Utils.stringIsNullOrWhitespace(hash)){

                            ensureHash(deviceDescription,
                                    filePath, hash, hashedAt);
                        }
                    }
                }

                if(fmi.tagCount() > 0){

                    Iterator<TagNode> itr = fmi.getTags();

                    while(itr.hasNext()){

                        TagNode tag = itr.next();

                        ensureTag(deviceDescription, filePath, tag);
                    }
                }

                String audioTranscript = fmi.getAudioTranscript();

                if(!Utils.stringIsNullOrWhitespace(audioTranscript)){

                    ensureAudioTranscript(deviceDescription,
                            filePath,audioTranscript);
                }

            }catch(Exception ex){

                Utils.log(context, "error importing file data for path [" +
                        fmi.getPath() + "] on device [" + fmi.getDevice());
            }
        }

        db.setTransactionSuccessful();

        db.endTransaction();
    }

    public void sync(Context context, SynergyV5List synLst) {

        String listName = synLst.getListName();
        String activated =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                        synLst.getActivatedAt());
        String shelved =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(
                        synLst.getShelvedAt());

        //populate list id if not set, creating list if !exists
        if(synLst.getListId() < 1){

            db.execSQL(NwdContract.SYNERGY_V5_ENSURE_LIST_NAME_X,
                    new String[]{listName});
        }

        //ensure current timestamps
        db.execSQL(
 NwdContract.SYNERGY_V5_LIST_UPDATE_ACTIVATE_AT_SHELVED_AT_FOR_LIST_NAME_X_Y_Z,
                    new String[]{activated, shelved, listName});

        populateIdAndTimeStampsForSynergyV5ListName(context, synLst);

        //MOVED FROM INSIDE load(context, synergyList)
        //TESTING, MOVE BACK IF PROBLEMS
        //get all items for list
        synergyV5PopulateListItems(context, synLst);

        // for each SynergyV5ListItem,
        // do the same (populate item id, ensure, etc.)
        for(int i = 0; i < synLst.getAllItems().size(); i++){

            SynergyV5ListItem sli = synLst.get(i);
            sync(context, synLst, sli, i);
        }
    }

    private void sync(Context context,
                      SynergyV5List lst,
                      SynergyV5ListItem sli,
                      int position){

        if(lst.getListId() < 1){

            Utils.toast(context, "SynergyV5List Id not set, item skipped");
            return;
        }

        if(sli.getItemId() < 1){

            db.execSQL(NwdContract.SYNERGY_V5_ENSURE_ITEM_VALUE_X,
                    new String[]{sli.getItemValue()});

            sli.setItemId(
                    getIdForSynergyV5ItemValue(context, sli.getItemValue()));
        }

        if(sli.getListItemId() < 1) {

            db.execSQL(NwdContract.SYNERGY_V5_ENSURE_LIST_ITEM_POSITION_X_Y_Z,
                    new String[]{
                            Integer.toString(lst.getListId()),
                            Integer.toString(sli.getItemId()),
                            Integer.toString(position)
                    });

            sli.setListItemId(
                    getListItemId(context, lst.getListId(), sli.getItemId()));
        }

        db.execSQL(NwdContract.SYNERGY_V5_UPDATE_POSITION_FOR_LIST_ITEM_ID_X_Y,
                new String[]{
                        Integer.toString(position),
                        Integer.toString(sli.getListItemId())
                });

        //need to check for SynergyToDo and sync
        SynergyV5ToDo toDo = sli.getToDo();

        if(toDo != null){

            String activated =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(toDo.getActivatedAt());
            String completed =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(toDo.getCompletedAt());
            String archived =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(toDo.getArchivedAt());

            db.execSQL(

                NwdContract.SYNERGY_V5_ENSURE_TO_DO_FOR_LIST_ITEM_ID_ID_AC_CO_AR,
                new String[]{
                        Integer.toString(sli.getListItemId()),
                        activated,
                        completed,
                        archived
                }
            );

            db.execSQL(

        NwdContract.SYNERGY_V5_UPDATE_TO_DO_WHERE_LIST_ITEM_ID_AC_CO_AR_ID,
                new String[]{
                        activated,
                        completed,
                        archived,
                        Integer.toString(sli.getListItemId())
                }
            );


        }
    }

    public int getListItemId(Context context, int listId, int itemId){

        int id = -1;

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{
                            Integer.toString(listId),
                            Integer.toString(itemId)
                    };

            Cursor cursor =
                    db.rawQuery(
            NwdContract.SYNERGY_V5_SELECT_LIST_ITEM_ID_FOR_LIST_ID_ITEM_ID_X_Y,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SYNERGY_LIST_ITEM_ID
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    //this method will grab the last entry, if multiple returned
                    //though list name is unique, so it should never matter
                    String idString =
                            record.get(NwdContract.COLUMN_SYNERGY_LIST_ITEM_ID);

                    id = Integer.parseInt(idString);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(context, "Exception retrieving list item id: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return id;
    }

    public int getIdForSynergyV5ItemValue(Context context, String itemValue){

        int id = -1;

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{
                            itemValue
                    };

            Cursor cursor =
                    db.rawQuery(
                        NwdContract.SYNERGY_V5_SELECT_ID_FOR_ITEM_VALUE_X,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SYNERGY_ITEM_ID
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    //this method will grab the last entry, if multiple returned
                    //though list name is unique, so it should never matter
                    String idString =
                            record.get(NwdContract.COLUMN_SYNERGY_ITEM_ID);

                    id = Integer.parseInt(idString);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(context, "Exception retrieving item id: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return id;
    }

    public ArrayList<String> synergyV5GetArchiveListNames(Context c) {

        ArrayList<String> listNames = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SYNERGY_V5_SELECT_SHELVED_LISTS,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SYNERGY_LIST_NAME
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                    listNames.add(
                            record.get(
                                    NwdContract.COLUMN_SYNERGY_LIST_NAME));

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception getting active list names: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return listNames;
    }

    public ArrayList<ListEntry> synergyV5GetActiveListEntries(Context c){

        ArrayList<ListEntry> lst = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                NwdContract.SYNERGY_V5_SELECT_LIST_NAMES_WITH_ITEM_COUNTS_NEW,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SYNERGY_LIST_NAME,
                            NwdContract.COLUMN_COUNT
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                    String listName =
                            record.get(NwdContract.COLUMN_SYNERGY_LIST_NAME);

                    int count =
                            Integer.parseInt(
                                    record.get(NwdContract.COLUMN_COUNT));

                    ListEntry entry = new ListEntry();
                    entry.setListName(listName);
                    entry.setItemCount(count);

                    lst.add(entry);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception getting active list entries: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return lst;
    }

    public ArrayList<String> synergyV5GetActiveListNames(Context c) {

        ArrayList<String> listNames = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SYNERGY_V5_SELECT_ACTIVE_LISTS,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SYNERGY_LIST_NAME
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                    listNames.add(
                            record.get(
                                    NwdContract.COLUMN_SYNERGY_LIST_NAME));

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception getting active list names: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return listNames;
    }

    public ArrayList<MediaDevice> v5GetAllMediaDevices(Context c){

        ArrayList<MediaDevice> lst = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                NwdContract.SELECT_FROM_MEDIA_DEVICE,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_MEDIA_DEVICE_ID,
                            NwdContract.COLUMN_MEDIA_DEVICE_DESCRIPTION
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                    int deviceId =
                            Integer.parseInt(
                                record.get(
                                        NwdContract.COLUMN_MEDIA_DEVICE_ID));

                    String description =
                        record.get(
                            NwdContract.COLUMN_MEDIA_DEVICE_DESCRIPTION);

                    MediaDevice md = new MediaDevice();
                    md.setMediaDeviceId(deviceId);
                    md.setMediaDeviceDescription(description);

                    lst.add(md);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "error getting media devices: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return lst;
    }


    public void synergyV5PopulateListItems(Context c,
                                           SynergyV5List lst) {

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{
                            Integer.toString(lst.getListId())
                    };

            Cursor cursor =
                    db.rawQuery(
        NwdContract
            .SYNERGY_V5_SELECT_LIST_ITEMS_AND_TODOS_BY_POSITION_FOR_LIST_ID_X,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SYNERGY_ITEM_ID,
                            NwdContract.COLUMN_SYNERGY_ITEM_VALUE,
                            NwdContract.COLUMN_SYNERGY_LIST_ITEM_POSITION,
                            NwdContract.COLUMN_SYNERGY_LIST_ITEM_ID,
                            NwdContract.COLUMN_SYNERGY_TO_DO_ID,
                            NwdContract.COLUMN_SYNERGY_TO_DO_ACTIVATED_AT,
                            NwdContract.COLUMN_SYNERGY_TO_DO_COMPLETED_AT,
                            NwdContract.COLUMN_SYNERGY_TO_DO_ARCHIVED_AT
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    String itemIdString,
                            itemValue,
                            positionString,
                            listItemIdString,
                            toDoIdString,
                            toDoActivatedAtString,
                            toDoCompletedAtString,
                            toDoArchivedAtString;

                    itemIdString =
                            record.get(NwdContract.COLUMN_SYNERGY_ITEM_ID);

                    itemValue =
                            record.get(NwdContract.COLUMN_SYNERGY_ITEM_VALUE);

                    positionString =
                            record.get(
                                NwdContract.COLUMN_SYNERGY_LIST_ITEM_POSITION);

                    listItemIdString =
                            record.get(
                                NwdContract.COLUMN_SYNERGY_LIST_ITEM_ID
                            );

                    toDoIdString =
                            record.get(NwdContract.COLUMN_SYNERGY_TO_DO_ID);

                    toDoActivatedAtString =
                            record.get(
                                NwdContract.COLUMN_SYNERGY_TO_DO_ACTIVATED_AT);

                    toDoCompletedAtString =
                            record.get(
                                NwdContract.COLUMN_SYNERGY_TO_DO_COMPLETED_AT);

                    toDoArchivedAtString =
                            record.get(
                                NwdContract.COLUMN_SYNERGY_TO_DO_ARCHIVED_AT);

                    SynergyV5ListItem sli = new SynergyV5ListItem(itemValue);
                    sli.setItemId(Integer.parseInt(itemIdString));
                    sli.setListItemId(Integer.parseInt(listItemIdString));

                    if(toDoIdString != null){

                        //has to do item
                        SynergyV5ToDo toDo = new SynergyV5ToDo();
                        toDo.setToDoId(Integer.parseInt(toDoIdString));

                        Date activated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    toDoActivatedAtString);

                        Date completed =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    toDoCompletedAtString);

                        Date archived =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    toDoArchivedAtString);

                        toDo.setTimeStamps(activated, completed, archived);

                        sli.setToDo(toDo);
                    }

                    lst.add(Integer.parseInt(positionString), sli);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    /**
     * will use LocalConfig table to look for
     * value associated with key "local-media-device-description"
     *
     * if that key is not found, will return null
     * @return
     */
    public String getLocalMediaDeviceDescription(Context c) {

        String localMediaDeviceDescription = null;

        db.beginTransaction();

        try{

            String configKey = LOCAL_CONFIG_KEY_DEVICE_DESCRIPTION;

            String[] args =
                    new String[]{
                           configKey
                    };

            Cursor cursor =
                    db.rawQuery(
                        NwdContract.LOCAL_CONFIG_GET_VALUE_FOR_KEY_X,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_LOCAL_CONFIG_VALUE
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                localMediaDeviceDescription =
                        record.get(NwdContract.COLUMN_LOCAL_CONFIG_VALUE);

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception retrieving list id: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return localMediaDeviceDescription;
    }

    /**
     * will use MediaDevice table to look for
     * description associated with key "local-media-device-description"
     * in LocalConfig table
     *
     * if that key or device is not found in either table, this will return null
     * @return
     */
    public MediaDevice getLocalMediaDevice(Context c) {

        MediaDevice md = null;
        String deviceDescription = getLocalMediaDeviceDescription(c);

        if(deviceDescription != null){

            db.beginTransaction();

            try{

                String[] args =
                        new String[]{
                               deviceDescription
                        };

                Cursor cursor =
                        db.rawQuery(
                            NwdContract.MNEMOSYNE_V5_SELECT_MEDIA_DEVICE_BY_DESC_X,
                            args);

                String[] columnNames =
                        new String[]{
                                NwdContract.COLUMN_MEDIA_DEVICE_ID,
                                NwdContract.COLUMN_MEDIA_DEVICE_DESCRIPTION
                        };

                if(cursor.getCount() > 0){

                    cursor.moveToFirst();

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    int mediaDeviceId =
                            Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_DEVICE_ID));

                    String mediaDeviceDescription =
                            record.get(NwdContract.COLUMN_MEDIA_DEVICE_DESCRIPTION);

                    md = new MediaDevice();
                    md.setMediaDeviceId(mediaDeviceId);
                    md.setMediaDeviceDescription(mediaDeviceDescription);

                    cursor.close();
                }

                db.setTransactionSuccessful();

            }catch (Exception ex){

                Utils.toast(c, "Exception retrieving list id: " +
                        ex.getMessage());

            }finally {

                db.endTransaction();
            }

        }

        return md;
    }

    public void ensureLocalMediaDevice(Context c, String localMediaDeviceDescription) {

        db.execSQL(NwdContract.MNEMOSYNE_V5_INSERT_LOCAL_CONFIG_KEY_VALUE_X_Y,
                    new String[]{
                            LOCAL_CONFIG_KEY_DEVICE_DESCRIPTION,
                            localMediaDeviceDescription});

        db.execSQL(NwdContract.MNEMOSYNE_V5_UPDATE_LOCAL_CONFIG_VALUE_FOR_KEY_X_Y,
                    new String[]{
                            localMediaDeviceDescription,
                            LOCAL_CONFIG_KEY_DEVICE_DESCRIPTION});

        db.execSQL(NwdContract.INSERT_INTO_MEDIA_DEVICE,
                    new String[]{
                            localMediaDeviceDescription});
    }

    public ArrayList<MediaRoot> v5GetMediaRootsForDeviceId(
            int mediaDeviceId, Context c) {

        ArrayList<MediaRoot> lst = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{
                            Integer.toString(mediaDeviceId)
                    };

            Cursor cursor =
                    db.rawQuery(
                NwdContract.SELECT_ID_AND_PATH_FROM_MEDIA_ROOT_FOR_DEVICE_ID,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_MEDIA_ROOT_ID,
                            NwdContract.COLUMN_MEDIA_ROOT_PATH
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                    int mediaRootId =
                            Integer.parseInt(
                                record.get(
                                        NwdContract.COLUMN_MEDIA_ROOT_ID));

                    String mediaRootPath =
                        record.get(
                            NwdContract.COLUMN_MEDIA_ROOT_PATH);

                    MediaRoot mr = new MediaRoot();
                    mr.setMediaDeviceId(mediaDeviceId);
                    mr.setMediaRootId(mediaRootId);
                    mr.setMediaRootPath(new File(mediaRootPath));

                    lst.add(mr);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "error getting media roots: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }

        return lst;
    }

    public void insertMediaRoot(Context c, int deviceId, File rootFolder) {

        if(rootFolder.isDirectory()){

            try{

                db.execSQL(
                    NwdContract.INSERT_DEVICE_ID_PATH_INTO_MEDIA_ROOT,
                    new String[]{
                            Integer.toString(deviceId),
                            rootFolder.getAbsolutePath()
                    }
                );

            }catch (Exception ex){

                Utils.toast(c, "error adding media root: " +
                        ex.getMessage());

            }
        }
    }

    public ArrayList<PathTagLink> getV4PathTagLinksAsync() {

        //mirrors desktop DbAdapterV4c.GetPathTagLinks()

        ArrayList<PathTagLink> allLinks = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                NwdContract.MNEMOSYNE_V4_GET_PATH_TAG_LINKS,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_PATH_VALUE,
                            NwdContract.COLUMN_TAG_VALUE
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                    String pathValue =
                            record.get(NwdContract.COLUMN_PATH_VALUE);

                    String tagValue =
                            record.get(NwdContract.COLUMN_TAG_VALUE);


                    PathTagLink ptl = new PathTagLink();
                    ptl.setPathValue(pathValue);
                    ptl.setTagValue(tagValue);

                    allLinks.add(ptl);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return allLinks;
    }

    public void ensureMediaTags(HashSet<String> allTags) {

        //mirrors desktop MediaV5SubsetDb.EnsureMediaTags()

        for(String tag : allTags) {

            db.execSQL(NwdContract.INSERT_MEDIA_TAG_X,
                    new String[]{tag});
        }
    }

    public HashMap<String, Tag> getAllMediaTags() {

        //mirrors desktop MediaV5SubsetDb.GetAllMediaTags()

        HashMap<String, Tag> allTags = new HashMap<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                NwdContract.SELECT_MEDIA_TAG_ID_VALUE,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_MEDIA_TAG_ID,
                            NwdContract.COLUMN_MEDIA_TAG_VALUE
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                    int tagId =
                            Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_TAG_ID));

                    String tagValue =
                            record.get(NwdContract.COLUMN_MEDIA_TAG_VALUE);

                    Tag tag = new Tag();
                    tag.setTagValue(tagValue);
                    tag.setTagId(tagId);

                    allTags.put(tagValue, tag);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return allTags;
    }

    public void storeHashForPath(int mediaDeviceId, String path, String hash) {

        //mirrors desktop MediaV5SubsetDb.StoreHashForPath()

        db.beginTransaction();

        try {

            int mediaId = getMediaIdForHash(hash, db);

            if(mediaId < 1){

                mediaId = ensureMediaHash(hash, db);

            }else{

                updateHashForMediaId(hash, mediaId, db);
            }

            int mediaPathId = ensureMediaPath(path, db);

            storeMediaAtDevicePath(mediaId, mediaDeviceId, mediaPathId, db);

            db.setTransactionSuccessful();

        }finally {

            db.endTransaction();
        }
    }

    private void storeMediaAtDevicePath(int mediaId,
                                        int mediaDeviceId,
                                        int mediaPathId,
                                        SQLiteDatabase db) {

        String[] args = new String[] {
                Integer.toString(mediaId),
                Integer.toString(mediaDeviceId),
                Integer.toString(mediaPathId)
        };

        db.execSQL(NwdContract.INSERT_MEDIA_DEVICE_PATH_MID_DID_PID, args);
    }

    private void updateHashForMediaId(String hash, int mediaId, SQLiteDatabase db) {

        db.execSQL(NwdContract.UPDATE_HASH_FOR_MEDIA_ID_X_Y,
                new String[]{ hash, Integer.toString(mediaId) });
    }

    private int ensureMediaHash(String hash, SQLiteDatabase db) {

        int id = getMediaIdForHash(hash, db);

        if(id < 1){

            insertOrIgnoreHashForMedia(hash, db);
            id = getMediaIdForHash(hash, db);
        }

        return id;
    }

    private int ensureMediaPath(String path, SQLiteDatabase db) {

        int id = getMediaPathId(path, db);

        if(id < 1){

            insertOrIgnoreMediaPath(path, db);
            id = getMediaPathId(path, db);
        }

        return id;
    }

    private int getMediaPathId(String path, SQLiteDatabase db) {

        int id = -1;

        String[] args = new String[]{ path };

        Cursor cursor =
                db.rawQuery(
            NwdContract.SELECT_MEDIA_PATH_ID_FOR_PATH_X,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_MEDIA_PATH_ID
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                    cursorToRecord(cursor, columnNames);

                id = Integer.parseInt(
                        record.get(NwdContract.COLUMN_MEDIA_PATH_ID));

            } while (cursor.moveToNext());

            cursor.close();
        }

        return id;
    }

    private void insertOrIgnoreMediaPath(String path, SQLiteDatabase db) {

        db.execSQL(NwdContract.INSERT_MEDIA_PATH_X, new String[]{ path });
    }

    private void insertOrIgnoreHashForMedia(String hash, SQLiteDatabase db) {

        db.execSQL(NwdContract.INSERT_MEDIA_HASH_X, new String[]{ hash });
    }

    private int getMediaIdForHash(String hash, SQLiteDatabase db) {

        int id = -1;

        Media media = getMediaForHash(hash, db);

        if(media != null){

            id = media.getMediaId();
        }

        return id;
    }

    private Media getMediaForHash(String hash, SQLiteDatabase db) {

        Media m = new Media();

        m.setMediaHash(hash);

        populateMediaByHash(m, db);

        return m;
    }

    private void populateMediaByHash(Media m, SQLiteDatabase db) {

        String[] args =
                new String[]{ m.getMediaHash() };

        Cursor cursor =
                db.rawQuery(
            NwdContract.SELECT_MEDIA_FOR_HASH_X,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_MEDIA_ID,
                        NwdContract.COLUMN_MEDIA_FILE_NAME,
                        NwdContract.COLUMN_MEDIA_DESCRIPTION,
                        NwdContract.COLUMN_MEDIA_HASH
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                    cursorToRecord(cursor, columnNames);

                int mediaId =
                        Integer.parseInt(
                            record.get(
                                    NwdContract.COLUMN_MEDIA_ID));

                String mediaFileName =
                    record.get(NwdContract.COLUMN_MEDIA_FILE_NAME);

                String mediaDescription =
                    record.get(NwdContract.COLUMN_MEDIA_DESCRIPTION);

                m.setMediaId(mediaId);
                m.setMediaFileName(mediaFileName);
                m.setMediaDescription(mediaDescription);

            } while (cursor.moveToNext());

            cursor.close();
        }
    }

    public HashMap<String, Media> getAllMedia() {

        //mirrors desktop MediaV5SubsetDb.GetAllMedia()

        HashMap<String, Media> allMedia = new HashMap<>();

        String[] args = new String[]{};

        Cursor cursor =
                db.rawQuery(
            NwdContract.SELECT_MEDIA_WHERE_HASH_NOT_NULL_OR_WHITESPACE,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_MEDIA_ID,
                        NwdContract.COLUMN_MEDIA_FILE_NAME,
                        NwdContract.COLUMN_MEDIA_DESCRIPTION,
                        NwdContract.COLUMN_MEDIA_HASH
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                    cursorToRecord(cursor, columnNames);

                int mediaId =
                        Integer.parseInt(
                            record.get(
                                    NwdContract.COLUMN_MEDIA_ID));

                String mediaFileName =
                    record.get(NwdContract.COLUMN_MEDIA_FILE_NAME);

                String mediaDescription =
                    record.get(NwdContract.COLUMN_MEDIA_DESCRIPTION);

                String mediaHash =
                    record.get(NwdContract.COLUMN_MEDIA_HASH);

                Media m = new Media();

                m.setMediaId(mediaId);
                m.setMediaDescription(mediaDescription);
                m.setMediaFileName(mediaFileName);
                m.setMediaHash(mediaHash);

                allMedia.put(m.getMediaHash(), m);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return allMedia;
    }

    public void ensureMediaTaggings(ArrayList<MediaTagging> taggings)
            throws Exception {

        //mirrors desktop MediaV5SubsetDb.EnsureMediaTaggings()

        db.beginTransaction();

        try {

            ensureMediaTaggings(taggings, db);

            db.setTransactionSuccessful();

        }finally {

            db.endTransaction();
        }
    }

    private void ensureMediaTaggings(ArrayList<MediaTagging> taggings,
                                     SQLiteDatabase db)
            throws Exception {

        for(MediaTagging mt : taggings){

            if(mt.getMediaId() < 1 || mt.getMediaTagId() < 1){

                throw new Exception("Unable to ensure MediaTagging: " +
                        "MediaId and/or MediaTagId not set.");
            }

            insertOrIgnoreMediaTagging(mt, db);
            updateOrIgnoreMediaTagging(mt, db);
        }
    }

    private void updateOrIgnoreMediaTagging(MediaTagging mt, SQLiteDatabase db) {

        String taggedAt =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(mt.getTaggedAt());

        String untaggedAt =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(mt.getUntaggedAt());

        String mediaId = Integer.toString(mt.getMediaId());

        String mediaTagId = Integer.toString(mt.getMediaTagId());

        String[] args = new String[]{
                taggedAt, untaggedAt, mediaId, mediaTagId
        };

        db.execSQL(NwdContract.UPDATE_MEDIA_TAGGING_TAGGED_UNTAGGED_WHERE_MEDIA_ID_AND_TAG_ID_W_X_Y_Z ,
                args);
    }

    private void insertOrIgnoreMediaTagging(MediaTagging mt, SQLiteDatabase db) {

        String[] args = new String[]{

                Integer.toString(mt.getMediaId()),
                Integer.toString(mt.getMediaTagId())
        };

        db.execSQL(NwdContract.INSERT_OR_IGNORE_MEDIA_TAGGING_X_Y, args);
    }


    //region templates

//    public List<Object> template(Context c) {
//
//        ArrayList<Object> lst = new ArrayList<>();
//
//        db.beginTransaction();
//
//        try{
//
//            String[] args =
//                    new String[]{
//                            ARG_1,
//                            ARG_2,
//                            etc
//                    };
//
//            Cursor cursor =
//                    db.rawQuery(
//                        NwdContract.SOME_QUERY,
//                        args);
//
//            String[] columnNames =
//                    new String[]{
//                            NwdContract.RESULT_SET_COLUMN1,
//                            NwdContract.RESULT_SET_COLUMN2,
//                            etc
//                    };
//
//            if(cursor.getCount() > 0){
//
//                cursor.moveToFirst();
//
//                do {
//
//                    Map<String, String> record =
//                            cursorToRecord(cursor, columnNames);
//
//                } while (cursor.moveToNext());
//
//                cursor.close();
//            }
//
//            db.setTransactionSuccessful();
//
//        }catch (Exception ex){
//
//            Utils.toast(c, "Exception: " +
//                    ex.getMessage());
//
//        }finally {
//
//            db.endTransaction();
//        }
//
//        return lst;
//    }

    //endregion
}
