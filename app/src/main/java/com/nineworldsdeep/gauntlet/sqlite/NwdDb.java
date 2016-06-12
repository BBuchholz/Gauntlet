package com.nineworldsdeep.gauntlet.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nineworldsdeep.gauntlet.Configuration;
import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.mnemosyne.FileHashFragment;
import com.nineworldsdeep.gauntlet.mnemosyne.FileListItem;
import com.nineworldsdeep.gauntlet.synergy.v3.SynergyUtils;
import com.nineworldsdeep.gauntlet.tapestry.TapestryUtils;

import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brent on 5/12/16.
 */
public class NwdDb {

    // Creating this separate class from the NwdDbOpenHelper per:
    // http://www.vogella.com/tutorials/AndroidSQLite/article.html
    // which does so, and I like the design better that way

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
            "AND p."+ NwdContract.COLUMN_PATH_VALUE + " = ? ; ";

    public static NwdDb getInstance(Context c){

        return getInstance(c, null);
    }

    public static NwdDb getInstance(Context c, String dbName) {

        if(Configuration.isInTestMode()){

            dbName = "test";

            if(!instances.containsKey(dbName)){

                instances.put(dbName, new NwdDb(c, dbName));
            }

        }else if(Utils.stringIsNullOrWhitespace(dbName) ||
                dbName.trim().equalsIgnoreCase("nwd")){

            dbName = "nwd";

            if(!instances.containsKey(dbName)){

                instances.put(dbName, new NwdDb(c));
            }

        } else {

            if(!instances.containsKey(dbName)){

                instances.put(dbName, new NwdDb(c, dbName));
            }
        }

        return instances.get(dbName);
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

    public void linkFileToDisplayName(String filePath, String displayName){

        linkFileToDisplayName(
                TapestryUtils.getCurrentDeviceName(),
                filePath,
                displayName);
    }

    public void linkFileToDisplayName(String deviceDescription,
                                      String filePath,
                                      String displayName){
        //open transaction
        db.beginTransaction();

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

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error linking display name [" +
                    displayName + "] to file [" +
                    filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    public void linkHashToFile(String deviceName,
                               String filePath,
                               String sha1Hash,
                               String hashedAt) {
        //open transaction
        db.beginTransaction();

        try{

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

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error linking SHA1 hash [" +
                    sha1Hash + "] to file [" +
                    filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    public void linkTagToFile(String deviceName, String filePath, String tag) {

        //open transaction
        db.beginTransaction();

        try{

            //insert or ignore device
            db.execSQL(DATABASE_ENSURE_DEVICE, new String[]{deviceName});
            //insert or ignore path
            db.execSQL(DATABASE_ENSURE_PATH, new String[]{filePath});
            //insert or ignore hash
            db.execSQL(DATABASE_ENSURE_TAG, new String[]{tag});
            //insert or ignore file tag entry
            db.execSQL(DATABASE_ENSURE_TAG_FOR_FILE,
                    new String[]{deviceName, filePath, tag});

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

                try{

                    //insert or ignore device
                    db.execSQL(DATABASE_ENSURE_DEVICE,
                            new String[]{deviceDescription});

                    //insert or ignore path
                    db.execSQL(DATABASE_ENSURE_PATH,
                            new String[]{path});

                    //insert or ignore tag
                    db.execSQL(DATABASE_ENSURE_TAG, new String[]{tag});

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

    public void updateAudioTranscriptForFile(String deviceName,
                                             String filePath,
                                             String transcription) {


        //open transaction
        db.beginTransaction();

        try{

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

            db.setTransactionSuccessful();

        }catch(Exception ex) {

            Utils.log("error updating audio transcript [" + transcription + "] " +
                    "for device [" + deviceName + "] " +
                    "and path [" + filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    public void setConfigValue(String key, String val) {

        //open transaction
        db.beginTransaction();

        try{

            //update or ignore config value (if exists)
            db.execSQL(DATABASE_UPDATE_LOCAL_CONFIG,
                    new String[]{val, key});
            //insert or ignore config value (if !exists)
            db.execSQL(DATABASE_ENSURE_LOCAL_CONFIG,
                    new String[]{key, val});

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

            record.put(colName, c.getString(c.getColumnIndex(colName)));
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
}
