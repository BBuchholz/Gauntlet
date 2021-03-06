package com.nineworldsdeep.gauntlet.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.nineworldsdeep.gauntlet.MultiMap;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSource;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerpt;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceExcerptTagging;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceLocation;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceLocationEntry;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceLocationSubset;
import com.nineworldsdeep.gauntlet.archivist.v5.ArchivistSourceType;
import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.MultiMapString;
import com.nineworldsdeep.gauntlet.Utils;
import com.nineworldsdeep.gauntlet.core.TimeStamp;
import com.nineworldsdeep.gauntlet.hive.HiveRoot;
import com.nineworldsdeep.gauntlet.mnemosyne.FileHashFragment;
import com.nineworldsdeep.gauntlet.mnemosyne.FileListItem;
import com.nineworldsdeep.gauntlet.mnemosyne.v4.PathTagLink;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.DevicePath;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Media;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaDevice;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaListItem;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaRoot;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.MediaTagging;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.Tag;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.TagBrowserFileItem;
import com.nineworldsdeep.gauntlet.mnemosyne.v5.TagBrowserTagItem;
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
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlLocationEntry;
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlSource;
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlSourceExcerpt;
import com.nineworldsdeep.gauntlet.xml.archivist.ArchivistXmlTag;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.ParseException;
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
     */
    private NwdDb(Context context){

        dbHelper = NwdDbOpenHelper.getInstance(context);
        dbFilePath = context.getDatabasePath(dbHelper.getDatabaseName());
        isInternalDb = true;
    }

    /**
     * Opens/Creates an external database for Gauntlet/NWD with the specified name
     * in the NWD/sqlite directory. Intended for imports and exports.
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

//            Utils.log("error linking display name [" +
//                    displayName + "] to file [" +
//                    filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    /**
     * assumes an open transaction (open and close outside of this method)
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

//            Utils.log("error linking SHA1 hash [" +
//                    sha1Hash + "] to file [" +
//                    filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    /**
     * assumes an open transaction (open and close outside of this method)
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

//            Utils.log("error linking tag [" +
//                    tag + "] to file [" +
//                    filePath + "]: " + ex.getMessage());

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

//                    Utils.log("error linking tag [" +
//                            tag + "] to file path [" +
//                            path + "]: " + ex.getMessage());

                }
            }
        }

        db.setTransactionSuccessful();

        db.endTransaction();
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

//            Utils.log("error ensuring file for device [" +
//                    deviceName + "] and path [" +
//                    filePath + "]: " + ex.getMessage());

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

//            Utils.log("error updating audio transcript [" + transcription + "] " +
//                    "for device [" + deviceName + "] " +
//                    "and path [" + filePath + "]: " + ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }

    /**
     * assumes an open transaction (open and close outside of this method)
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

//            Utils.log("error updating local config key [" + key + "] " +
//                    "with value [" + val + "]: " + ex.getMessage());

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
//
//            Utils.log("error retrieving path display names " +
//                    "for current device: " + ex.getMessage());

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

//                Utils.log("error linking display name [" +
//                        displayName + "] to file [" +
//                        filePath + "]: " + ex.getMessage());

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

    public List<Map<String, String>> getActiveV5PathTagRecordsForCurrentDevice(){

        List<Map<String,String>> pathTags =
        new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{TapestryUtils.getCurrentDeviceName()};

            Cursor c =
                    db.rawQuery(
                NwdContract.MNEMOSYNE_V5_GET_ACTIVE_TAGS_FOR_PATHS_FOR_DEVICE_NAME_X,
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

//            Utils.log("error deleting tags " +
//                    "for path [" + path + "]: " + ex.getMessage());

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

    public void syncByName(Context context, HiveRoot hiveRoot) {


        if(hiveRoot.getHiveRootId() < 1) {

            ensureHiveRootName(hiveRoot.getHiveRootName());
        }

        updateHiveRootTimeStampsByName(hiveRoot);

        populateIdAndTimeStampsForHiveRootName(context, hiveRoot);
    }


    public void populateIdAndTimeStampsForHiveRootName(Context c,
                                                       HiveRoot hiveRoot){

        db.beginTransaction();

        try{

            String hiveRootName = hiveRoot.getHiveRootName();

            String[] args =
                    new String[]{
                           hiveRootName
                    };

            Cursor cursor =
                    db.rawQuery(
        NwdContract.HIVE_ROOT_SELECT_ID_ACTIVATED_AT_DEACTIVATED_AT_FOR_NAME,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_HIVE_ROOT_ID,
                            NwdContract.COLUMN_HIVE_ROOT_ACTIVATED_AT,
                            NwdContract.COLUMN_HIVE_ROOT_DEACTIVATED_AT
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    //this method will grab the last entry, if multiple returned
                    //though list name is unique, so it should never matter
                    String idString =
                            record.get(NwdContract.COLUMN_HIVE_ROOT_ID);

                    String activatedString =
                            record.get(NwdContract.COLUMN_HIVE_ROOT_ACTIVATED_AT);
                    String deactivatedString =
                            record.get(NwdContract.COLUMN_HIVE_ROOT_DEACTIVATED_AT);

                    Date activated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    activatedString);

                    Date deactivated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    deactivatedString);

                    hiveRoot.setHiveRootId(Integer.parseInt(idString));
                    hiveRoot.setTimeStamps(activated, deactivated);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception retrieving hive root id: " +
                    ex.getMessage());

        }finally {

            db.endTransaction();
        }
    }


    private void updateHiveRootTimeStampsByName(HiveRoot hiveRoot) {

        String activated =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(hiveRoot.getActivatedAt());

        String deactivated =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(hiveRoot.getDeactivatedAt());

        String hiveRootName = hiveRoot.getHiveRootName();

        db.execSQL(
 NwdContract.HIVE_ROOT_UPDATE_ACTIVATE_AT_DEACTIVATED_AT_FOR_NAME_X_Y_Z,
                    new String[]{activated, deactivated, hiveRootName});
    }


    public void save(Context context, SynergyV5List synLst) {

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


        // for each SynergyV5ListItem,
        // do the same (populate item id, ensure, etc.)
        for(int i = 0; i < synLst.getAllItems().size(); i++){

            SynergyV5ListItem sli = synLst.get(i);
            save(context, synLst, sli, i);
        }

    }


    private void save(Context context,
                      SynergyV5List lst,
                      SynergyV5ListItem sli,
                      int position){

        if(lst.getListId() < 1){

            Utils.toast(context, "SynergyV5List Id not set, save item skipped");
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

        //need to check for SynergyToDo and save
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

    /**
     * loads just the id and timestamps for given list name
     */
    public void loadCore(Context context, SynergyV5List synLst){

        populateIdAndTimeStampsForSynergyV5ListName(context, synLst);
    }

    /**
     * loads active list items from db and merges them with the current list
     * items
     *
     */
    public void loadActive(Context context, SynergyV5List synLst) {

        synergyV5PopulateActiveListItems(context, synLst);
    }

    /**
     * loads archived list items from db and merges them with the current list
     * items
     *
     */
    public void loadArchived(Context context, SynergyV5List synLst) {

        synergyV5PopulateArchivedListItems(context, synLst);
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

    public void synergyV5PopulateActiveListItems(Context c,
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
            .SYNERGY_V5_SELECT_ACTIVE_ITEMS_AND_TODOS_BY_POSITION_FOR_LIST_ID_X,
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

                    int position = Integer.parseInt(positionString);

                    if(lst.size() > position){

                        lst.add(position, sli);

                    }else{

                        lst.add(sli);
                    }
                    //lst.add(Integer.parseInt(positionString), sli);


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
    public void synergyV5PopulateArchivedListItems(Context c,
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
            .SYNERGY_V5_SELECT_ARCHIVED_ITEMS_AND_TODOS_BY_POSITION_FOR_LIST_ID_X,
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

                    int position = Integer.parseInt(positionString);

                    if(lst.size() > position){

                        lst.add(position, sli);

                    }else{

                        lst.add(sli);
                    }
                    //lst.add(Integer.parseInt(positionString), sli);

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
     */
    public String getLocalMediaDeviceDescription(Context c) {

        String localMediaDeviceDescription = null;

        db.beginTransaction();

        try{

//            String configKey = LOCAL_CONFIG_KEY_DEVICE_DESCRIPTION;
//
//            String[] args =
//                    new String[]{
//                           configKey
//                    };
//
//            Cursor cursor =
//                    db.rawQuery(
//                        NwdContract.LOCAL_CONFIG_GET_VALUE_FOR_KEY_X,
//                        args);
//
//            String[] columnNames =
//                    new String[]{
//                            NwdContract.COLUMN_LOCAL_CONFIG_VALUE
//                    };
//
//            if(cursor.getCount() > 0){
//
//                cursor.moveToFirst();
//
//                Map<String, String> record =
//                        cursorToRecord(cursor, columnNames);
//
//                localMediaDeviceDescription =
//                        record.get(NwdContract.COLUMN_LOCAL_CONFIG_VALUE);
//
//                cursor.close();
//            }

            localMediaDeviceDescription =
                    getLocalMediaDeviceDescription(db);

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
     * will use LocalConfig table to look for
     * value associated with key "local-media-device-description"
     *
     * if that key is not found, will return null
     */
    public String getLocalMediaDeviceDescription(SQLiteDatabase db) {

        String localMediaDeviceDescription = null;

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


                md = getMediaDevice(deviceDescription, db);

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

    /**
     * will use MediaDevice table to look for
     * description associated with key "local-media-device-description"
     * in LocalConfig table
     *
     * if that key or device is not found in either table, this will return null
     * @return
     */
    public MediaDevice getLocalMediaDevice() {

        MediaDevice md = null;
        String deviceDescription = getLocalMediaDeviceDescription(db);

        if(deviceDescription != null){

            md = getMediaDevice(deviceDescription, db);
        }

        return md;
    }

    public MediaDevice getMediaDevice(
            String deviceDescription, SQLiteDatabase db) {

        MediaDevice md = null;

        if(deviceDescription != null){

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

            if(cursor.getCount() > 0) {

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
            }

            cursor.close();

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

        db.execSQL(NwdContract.INSERT_MEDIA_DEVICE_X,
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

//            db.execSQL(NwdContract.INSERT_MEDIA_TAG_X,
//                    new String[]{tag});

            ensureMediaTag(tag, db);
        }
    }

    public int ensureMediaTag(String tag, SQLiteDatabase db){

        int id = getMediaTagId(tag, db);

        if(id < 1){

            insertOrIgnoreMediaTag(tag, db);
            id = getMediaTagId(tag, db);
        }

        return id;
    }

    private int getMediaTagId(String tag, SQLiteDatabase db) {

        int id = -1;

        String[] args = new String[]{ tag };

        Cursor cursor =
                db.rawQuery(
            NwdContract.SELECT_MEDIA_TAG_ID_FOR_VALUE_X,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_MEDIA_TAG_ID
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                    cursorToRecord(cursor, columnNames);

                id = Integer.parseInt(
                        record.get(NwdContract.COLUMN_MEDIA_TAG_ID));

            } while (cursor.moveToNext());

            cursor.close();
        }

        return id;
    }

    private void insertOrIgnoreMediaTag(String tag, SQLiteDatabase db) {

        db.execSQL(NwdContract.INSERT_MEDIA_TAG_X,
        new String[]{tag});
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

                mediaId = ensureMediaIdForHash(hash, db);

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

    private int ensureMediaIdForHash(String hash, SQLiteDatabase db) {

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

            //cursor.close();
        }

        cursor.close();

        return id;
    }

    private void insertOrIgnoreMediaPath(String path, SQLiteDatabase db) {

        db.execSQL(NwdContract.INSERT_MEDIA_PATH_X, new String[]{ path });
    }

    private void insertOrIgnoreMediaDevice(String deviceName, SQLiteDatabase db){

        db.execSQL(
                NwdContract.INSERT_MEDIA_DEVICE_X,
                new String[] { deviceName });
    }

    private void insertOrIgnoreHashForMedia(String hash, SQLiteDatabase db) {

        db.execSQL(NwdContract.INSERT_MEDIA_HASH_X, new String[]{ hash });
    }

    private int getMediaIdForHash(String hash, SQLiteDatabase db) {

        int id = -1;

//        Media media = getMediaForHash(hash, db);
//
//        if(media != null){
//
//            id = media.getMediaId();
//        }

        String[] args = new String[]{ hash };

        Cursor cursor =
                db.rawQuery(
            NwdContract.SELECT_MEDIA_ID_FOR_HASH_X,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_MEDIA_ID
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                    cursorToRecord(cursor, columnNames);

                id = Integer.parseInt(
                        record.get(NwdContract.COLUMN_MEDIA_ID));

            } while (cursor.moveToNext());

            cursor.close();
        }

        return id;
    }

//    /**
//     * requires that the hash already exists in the database,
//     * will return empty media if it does not
//     * @param hash
//     * @param db
//     * @return
//     */
//    private Media getMediaForHash(String hash, SQLiteDatabase db) {
//
//        Media m = new Media();
//
//        m.setMediaHash(hash);
//
//        populateMediaByHash(m, db);
//
//        return m;
//    }

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

                m.markClean();

            } while (cursor.moveToNext());
        }

        cursor.close();

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

                m.markClean();

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

    private void ensureMediaTaggings(Media media,
                                     SQLiteDatabase db)
            throws Exception {

        //other ensure signature requires mediaId set for each
        for(MediaTagging mt : media.getMediaTaggings()){

            mt.setMediaId(media.getMediaId());
        }

        ensureMediaTaggings(media.getMediaTaggings(), db);
    }

    private void ensureMediaTaggings(ArrayList<MediaTagging> taggings,
                                     SQLiteDatabase db)
            throws Exception {

        for(MediaTagging mt : taggings){

            if(mt.getMediaTagId() < 1){

                mt.setMediaTagId(ensureMediaTag(mt.getMediaTagValue(), db));
            }

            if(mt.getMediaId() < 1 || mt.getMediaTagId() < 1){

                throw new Exception("Unable to ensure MediaTagging: " +
                        "MediaId and/or MediaTagId not set.");
            }

            insertOrIgnoreMediaTagging(mt, db);
            updateOrIgnoreMediaTagging(mt, db);
        }
    }

    private void ensureMediaDevicePaths(Media media,
                                        SQLiteDatabase db)
            throws Exception {

        //other ensure signature requires mediaId set for each
        for(DevicePath dp : media.getDevicePaths().getAll()){

            dp.setMediaId(media.getMediaId());
        }

        ensureMediaDevicePaths(media.getDevicePaths(), db);
    }

    private void ensureMediaDevicePaths(
            MultiMap<String, DevicePath> devicePaths,
            SQLiteDatabase db) throws Exception {

        //need MediaId, MediaDeviceId, MediaPathId

        //cache device ids
        HashMap<String, Integer> deviceDescriptionToId =
                new HashMap<>();

        for(String deviceName : devicePaths.keySet()) {

            // if deviceName is "", keyName will allow access in devicePaths
            // after we change deviceName to local value
            String keyName = deviceName;

            if(Utils.stringIsNullOrWhitespace(deviceName)){

                //default to local device
                MediaDevice localDevice = getLocalMediaDevice();

                if(localDevice != null) {
                    deviceName = localDevice.getMediaDeviceDescription();
                }

                if(Utils.stringIsNullOrWhitespace(deviceName)){

                    throw new Exception("unable to retrieve local device name");
                }
            }

            for (DevicePath dp : devicePaths.get(keyName)) {

                if (dp.getMediaId() < 1) {

                    throw new Exception("media id must be set to ensure device paths");
                }

                if(!deviceDescriptionToId.containsKey(deviceName)){

                    deviceDescriptionToId.put(
                            deviceName,
                            ensureMediaDevice(deviceName, db)
                                    .getMediaDeviceId());
                }

                dp.setMediaDeviceId(deviceDescriptionToId.get(deviceName));

                if(dp.getMediaPathId() < 1) {

                    dp.setMediaPathId(ensureMediaPath(dp.getPath(), db));
                }

                insertOrIgnoreMediaDevicePath(dp, db);
            }
        }
    }

    private void insertOrIgnoreMediaDevicePath(DevicePath dp, SQLiteDatabase db) {


        String[] args = new String[]{

            Integer.toString(dp.getMediaId()),
            Integer.toString(dp.getMediaDeviceId()),
            Integer.toString(dp.getMediaPathId())
        };

        db.execSQL(NwdContract.INSERT_OR_IGNORE_MEDIA_DEVICE_PATH_X_Y_Z, args);
    }

    private MediaDevice ensureMediaDevice(
            String deviceName, SQLiteDatabase db) throws Exception {

        if(Utils.stringIsNullOrWhitespace(deviceName)){

            throw new Exception("device name must be set to ensure device");
        }

        MediaDevice md = getMediaDevice(deviceName, db);

        if(md == null) {

            insertOrIgnoreMediaDevice(deviceName, db);
            md = getMediaDevice(deviceName, db);
        }

        return md;
    }

    private void updateOrIgnoreMediaTagging(MediaTagging mt, SQLiteDatabase db) {

        String taggedAt =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(mt.getTaggedAt());

        String untaggedAt =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(mt.getUntaggedAt());

        String mediaId = Integer.toString(mt.getMediaId());

        String mediaTagId = Integer.toString(mt.getMediaTagId());

//        String[] args = new String[]{
//                taggedAt, untaggedAt, mediaId, mediaTagId
//        };
//
//        db.execSQL(NwdContract.UPDATE_MEDIA_TAGGING_TAGGED_UNTAGGED_WHERE_MEDIA_ID_AND_TAG_ID_W_X_Y_Z ,
//                args);

        SQLiteStatement updateStatement = db.compileStatement(
            NwdContract.UPDATE_MEDIA_TAGGING_TAGGED_UNTAGGED_WHERE_MEDIA_ID_AND_TAG_ID_W_X_Y_Z
        );

        //will bind null by default (desired behavior for timestamp comparisons)
        if(!Utils.stringIsNullOrWhitespace(taggedAt)){
            updateStatement.bindString(1, taggedAt);
        }

        //will bind null by default (desired behavior for timestamp comparisons)
        if(!Utils.stringIsNullOrWhitespace(untaggedAt)){
            updateStatement.bindString(2, untaggedAt);
        }

        updateStatement.bindString(3, mediaId);
        updateStatement.bindString(4, mediaTagId);

        updateStatement.execute();
    }

    private void insertOrIgnoreMediaTagging(MediaTagging mt, SQLiteDatabase db) {

        String[] args = new String[]{

                Integer.toString(mt.getMediaId()),
                Integer.toString(mt.getMediaTagId())
        };

        db.execSQL(NwdContract.INSERT_OR_IGNORE_MEDIA_TAGGING_X_Y, args);
    }

    public void sync(Media media) throws Exception {

        db.beginTransaction();

        try {

            sync(media, db);

            db.setTransactionSuccessful();

        }finally {

            db.endTransaction();
        }
    }

    /**
     * assumes media is already populated from media table
     * (mediaId, mediaHash, mediaDescription, and mediaFilename)
     * this just populates taggings and device paths and it
     * relies on both mediaId and mediaHash already being set
     */
    public void populateTaggingsAndDevicePaths(ArrayList<Media> lst) throws Exception {

        db.beginTransaction();

        try {

            for(Media media : lst) {

                if(Utils.stringIsNullOrWhitespace(media.getMediaHash()) ||
                        media.getMediaId() < 1){

                    throw new Exception("both media id and media hash must " +
                            "be set to populate taggings and device paths");
                }

                populateMediaTaggingsByHash(media, db);
                populateMediaDevicePathsByMediaId(media, db);
            }

            db.setTransactionSuccessful();

        }finally {

            db.endTransaction();
        }
    }

    public void populateTagStringForFirstFoundFilename(TagBrowserFileItem tagBrowserFileItem){

        String hash = getHashForFirstFoundFilename(tagBrowserFileItem.getFilename(), db);

        String tagString = "unable to load tag string";

        if(!Utils.stringIsNullOrWhitespace(hash)) {

            Media tempMedia = new Media();
            tempMedia.setMediaHash(hash);

            try {

                sync(tempMedia);
                MediaListItem tempMediaListItem = new MediaListItem(tempMedia);
                tagString = tempMediaListItem.getTags();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        tagBrowserFileItem.setTagString(tagString);
    }

    public String getHashForFirstFoundFilename(String filename, SQLiteDatabase db){

        String hash = "";

        String[] args =
                new String[]{ "%" + filename };

        Cursor cursor =
                db.rawQuery(
                        NwdContract.SELECT_MEDIA_PATH_AND_HASH_WHERE_PATH_ENDS_WITH_X,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_MEDIA_PATH_VALUE,
                        NwdContract.COLUMN_MEDIA_HASH
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                hash = record.get(NwdContract.COLUMN_MEDIA_HASH);

            } while (cursor.moveToNext());

            cursor.close();
        }

        return hash;
    }

    public void sync(Media media, SQLiteDatabase db) throws Exception {

        if(Utils.stringIsNullOrWhitespace(media.getMediaHash())){

            throw new Exception("media hash must be set for media object sync to be possible");
        }

        media.setMediaId(ensureMediaIdForHash(media.getMediaHash(), db));

        if(!Utils.stringIsNullOrWhitespace(media.getMediaFileName())){

            updateMediaFileNameForHash(
                    media.getMediaHash(),
                    media.getMediaFileName(),
                    db);
        }

        if(!Utils.stringIsNullOrWhitespace(media.getMediaDescription())){

            updateMediaDescriptionForHash(
                    media.getMediaHash(),
                    media.getMediaDescription(),
                    db);
        }

        populateMediaByHash(media, db);

        ensureMediaTaggings(media, db);

        ensureMediaDevicePaths(media, db);

        populateMediaTaggingsByHash(media, db);

        populateMediaDevicePathsByMediaId(media, db);

        media.markClean();
    }


    private void populateMediaDevicePathsByMediaId(Media media, SQLiteDatabase db)
            throws Exception {

        if(media.getMediaId() < 1 ){

            throw new Exception("media id must be set to populate device paths");
        }

        String[] args =
            new String[]{ Integer.toString(media.getMediaId()) };

        Cursor cursor =
                db.rawQuery(
            NwdContract.SELECT_MEDIA_DEVICE_PATHS_FOR_MEDIA_ID_X,
                        args);

        String[] columnNames =
                new String[]{
                    NwdContract.COLUMN_MEDIA_DEVICE_PATH_ID,
                    NwdContract.COLUMN_MEDIA_ID,
                    NwdContract.COLUMN_MEDIA_DEVICE_ID,
                    NwdContract.COLUMN_MEDIA_PATH_ID,
                    NwdContract.COLUMN_MEDIA_PATH_VALUE,
                    NwdContract.COLUMN_MEDIA_DEVICE_DESCRIPTION,
                    NwdContract.COLUMN_MEDIA_DEVICE_PATH_VERIFIED_PRESENT,
                    NwdContract.COLUMN_MEDIA_DEVICE_PATH_VERIFIED_MISSING
                };

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                int devicePathId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_DEVICE_PATH_ID));

                int mediaId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_ID));

                int mediaDeviceId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_DEVICE_ID));

                int mediaPathId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_PATH_ID));

                String path =
                        record.get(NwdContract.COLUMN_MEDIA_PATH_VALUE);

                String deviceName =
                        record.get(NwdContract.COLUMN_MEDIA_DEVICE_DESCRIPTION);

                String verifiedPresentString =
                        record.get(NwdContract.COLUMN_MEDIA_DEVICE_PATH_VERIFIED_PRESENT);

                String verifiedMissingString =
                        record.get(NwdContract.COLUMN_MEDIA_DEVICE_PATH_VERIFIED_MISSING);


                Date verifiedPresent =
                        TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(verifiedPresentString);

                Date verifiedMissing =
                        TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(verifiedMissingString);

                DevicePath dp = media.getDevicePath(deviceName, path);
                dp.setDevicePathId(devicePathId);
                dp.setMediaId(mediaId);
                dp.setMediaDeviceId(mediaDeviceId);
                dp.setMediaPathId(mediaPathId);

                dp.setTimeStamps(verifiedPresent, verifiedMissing);

            } while (cursor.moveToNext());

        }

        cursor.close();

    }

    private void populateMediaTaggingsByHash(Media media, SQLiteDatabase db)
            throws Exception {

        if(Utils.stringIsNullOrWhitespace(media.getMediaHash())){

            throw new Exception("media hash must be set to populate taggings");
        }

        String[] args =
            new String[]{ media.getMediaHash() };

        Cursor cursor =
                db.rawQuery(
            NwdContract.SELECT_MEDIA_TAGGINGS_FOR_HASH_X,
                        args);

        String[] columnNames =
                new String[]{
                    NwdContract.COLUMN_MEDIA_TAG_ID,
                    NwdContract.COLUMN_MEDIA_TAGGING_ID,
                    NwdContract.COLUMN_MEDIA_ID,
                    NwdContract.COLUMN_MEDIA_TAG_VALUE,
                    NwdContract.COLUMN_MEDIA_HASH,
                    NwdContract.COLUMN_MEDIA_TAGGING_TAGGED_AT,
                    NwdContract.COLUMN_MEDIA_TAGGING_UNTAGGED_AT
                };

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                int mediaTagId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_TAG_ID));

                int mediaTaggingId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_TAGGING_ID));

                int mediaId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_ID));

                String mediaTagValue =
                        record.get(NwdContract.COLUMN_MEDIA_TAG_VALUE);

                String mediaHash =
                        record.get(NwdContract.COLUMN_MEDIA_HASH);

                String taggedAtString =
                        record.get(NwdContract.COLUMN_MEDIA_TAGGING_TAGGED_AT);

                String untaggedAtString =
                        record.get(NwdContract.COLUMN_MEDIA_TAGGING_UNTAGGED_AT);

                Date taggedAt =
                        TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(taggedAtString);

                Date untaggedAt =
                        TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(untaggedAtString);


                //getTag() will create a new one if it doesn't exist already
                MediaTagging mt = media.getTag(mediaTagValue);

                mt.setMediaTagId(mediaTagId);
                mt.setMediaTaggingId(mediaTaggingId);
                mt.setMediaId(mediaId);
                mt.setMediaHash(mediaHash);
                mt.setTimeStamps(taggedAt, untaggedAt);

            } while (cursor.moveToNext());

        }

        cursor.close();
    }

    private void updateMediaDescriptionForHash(String mediaHash, String mediaDescription, SQLiteDatabase db) {

        String[] args = new String[]{
            mediaDescription,
            mediaHash
        };

        db.execSQL(NwdContract.UPDATE_MEDIA_DESCRIPTION_FOR_HASH_X_Y , args);
    }

    private void updateMediaFileNameForHash(String mediaHash, String mediaFileName, SQLiteDatabase db) {

        String[] args = new String[]{
                mediaFileName,
                mediaHash
        };

        db.execSQL(NwdContract.UPDATE_MEDIA_FILE_NAME_FOR_HASH_X_Y , args);
    }

    public void beginTransaction() {

        db.beginTransaction();
    }

    public SQLiteDatabase getSqliteDatabase() {

        return db;
    }

    public void setTransactionSuccessful() {

        db.setTransactionSuccessful();
    }

    public void endTransaction() {

        db.endTransaction();
    }

    public ArrayList<TagBrowserFileItem> getTagBrowserFileItemsForTagId(int tagId) {

        ArrayList<TagBrowserFileItem> tagBrowserFileItems = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{ Integer.toString(tagId) };

            //using query from desktop, only one column actually used
            //THIS USES JOINS, AND WE CAN MODIFY IF ITS SLOW, JUST BEING QUICK
            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_MEDIA_PATH_FOR_TAG_ID_X,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_MEDIA_PATH_VALUE,
                            NwdContract.COLUMN_MEDIA_HASH
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    String path = record.get(NwdContract.COLUMN_MEDIA_PATH_VALUE);
                    String hash = record.get(NwdContract.COLUMN_MEDIA_HASH);

                    String fileNameFromPath = FilenameUtils.getName(path);

                    TagBrowserFileItem tagBrowserFileItem =
                            new TagBrowserFileItem(fileNameFromPath);

                    tagBrowserFileItem.setHash(hash);

                    tagBrowserFileItems.add(tagBrowserFileItem);

                } while (cursor.moveToNext());

//                cursor.close();
            }

            cursor.close();

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return tagBrowserFileItems;
    }


    public ArrayList<TagBrowserTagItem> getAllTagBrowserTagItems(Context c) {

        ArrayList<TagBrowserTagItem> tagBrowserTagItems = new ArrayList<>();

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

                    TagBrowserTagItem tagBrowserTagItem = new TagBrowserTagItem(
                            Integer.parseInt(record.get(NwdContract.COLUMN_MEDIA_TAG_ID)),
                            record.get(NwdContract.COLUMN_MEDIA_TAG_VALUE)
                    );



                    tagBrowserTagItems.add(tagBrowserTagItem);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            Utils.toast(c, "Exception: " +
                    ex.getMessage());

            //ex.printStackTrace();

        }finally {

            db.endTransaction();
        }

        return tagBrowserTagItems;
    }

    public ArrayList<HiveRoot> getAllHiveRoots(Context c) {

        ArrayList<HiveRoot> roots = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                        NwdContract.SELECT_HIVE_ROOTS,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_HIVE_ROOT_ID,
                            NwdContract.COLUMN_HIVE_ROOT_NAME,
                            NwdContract.COLUMN_HIVE_ROOT_ACTIVATED_AT,
                            NwdContract.COLUMN_HIVE_ROOT_DEACTIVATED_AT
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    HiveRoot root = new HiveRoot(
                            Integer.parseInt(record.get(NwdContract.COLUMN_HIVE_ROOT_ID)),
                            record.get(NwdContract.COLUMN_HIVE_ROOT_NAME)
                    );

                    String activatedString =
                            record.get(
                                NwdContract.COLUMN_HIVE_ROOT_ACTIVATED_AT);

                    String deactivatedString =
                            record.get(
                                NwdContract.COLUMN_HIVE_ROOT_DEACTIVATED_AT);

                    Date activated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    activatedString);

                    Date deactivated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    deactivatedString);

                    root.setTimeStamps(activated, deactivated);

                    roots.add(root);

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

        return roots;
    }


    public ArrayList<HiveRoot> getDeactivatedHiveRoots(Context c) {

        ArrayList<HiveRoot> roots = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                        NwdContract.SELECT_DEACTIVATED_HIVE_ROOTS,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_HIVE_ROOT_ID,
                            NwdContract.COLUMN_HIVE_ROOT_NAME,
                            NwdContract.COLUMN_HIVE_ROOT_ACTIVATED_AT,
                            NwdContract.COLUMN_HIVE_ROOT_DEACTIVATED_AT
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    HiveRoot root = new HiveRoot(
                            Integer.parseInt(record.get(NwdContract.COLUMN_HIVE_ROOT_ID)),
                            record.get(NwdContract.COLUMN_HIVE_ROOT_NAME)
                    );

                    String activatedString =
                            record.get(
                                NwdContract.COLUMN_HIVE_ROOT_ACTIVATED_AT);

                    String deactivatedString =
                            record.get(
                                NwdContract.COLUMN_HIVE_ROOT_DEACTIVATED_AT);

                    Date activated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    activatedString);

                    Date deactivated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    deactivatedString);

                    root.setTimeStamps(activated, deactivated);

                    roots.add(root);

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

        return roots;
    }


    public ArrayList<HiveRoot> getActiveHiveRoots(Context c) {

        ArrayList<HiveRoot> roots = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                        NwdContract.SELECT_ACTIVE_HIVE_ROOTS,
                        args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_HIVE_ROOT_ID,
                            NwdContract.COLUMN_HIVE_ROOT_NAME,
                            NwdContract.COLUMN_HIVE_ROOT_ACTIVATED_AT,
                            NwdContract.COLUMN_HIVE_ROOT_DEACTIVATED_AT
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    HiveRoot root = new HiveRoot(
                            Integer.parseInt(record.get(NwdContract.COLUMN_HIVE_ROOT_ID)),
                            record.get(NwdContract.COLUMN_HIVE_ROOT_NAME)
                    );

                    /////////////////////////////////////////////////////////////////////////////
                    ////copying this block from getDeactivatedHiveRoots() without testing, nothing was here before, if it breaks just delete to the end comment
                    String activatedString =
                            record.get(
                                NwdContract.COLUMN_HIVE_ROOT_ACTIVATED_AT);

                    String deactivatedString =
                            record.get(
                                NwdContract.COLUMN_HIVE_ROOT_DEACTIVATED_AT);

                    Date activated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    activatedString);

                    Date deactivated =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    deactivatedString);

                    root.setTimeStamps(activated, deactivated);
                    ///////////////////////end block copy////////////////////////////////////////////////////////////////


                    roots.add(root);

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

        return roots;
    }

    public void ensureHiveRootName(String name){

        db.execSQL(NwdContract.INSERT_HIVE_ROOT_NAME_X, new String[]{ name });
    }

    public void ensureArchivistSourceTypeName(String sourceTypeName) {

        db.execSQL(NwdContract.INSERT_OR_IGNORE_SOURCE_TYPE_VALUE, new String[]{sourceTypeName});
    }

    public void insertOrIgnoreArchivistSource(ArchivistSource archivistSource) {

        String[] args = new String[]{

                Integer.toString(archivistSource.getSourceTypeId()),
                archivistSource.getSourceTitle(),
                archivistSource.getSourceAuthor(),
                archivistSource.getSourceDirector(),
                archivistSource.getSourceYear(),
                archivistSource.getSourceUrl(),
                archivistSource.getSourceRetrievalDate()
        };

        db.execSQL(NwdContract.INSERT_SOURCE_T_U_V_W_X_Y_Z, args);
    }

    public void insertOrIgnoreArchivistSourceExcerpt(ArchivistSourceExcerpt ase){

        String[] args = new String[]{

                Integer.toString(ase.getSourceId()),
                ase.getExcerptValue(),
                ase.getExcerptBeginTime(),
                ase.getExcerptEndTime(),
                ase.getExcerptPages()
        };

        db.execSQL(NwdContract.INSERT_OR_IGNORE_SOURCE_EXCERPT_SRCID_EXVAL_BTIME_ETIME_PGS_V_W_X_Y_Z, args);
    }

    public void insertOrIgnoreArchivistSourceExcerpt(int sourceId,
                                                     String excerptValue,
                                                     String beginTime,
                                                     String endTime,
                                                     String pages,
                                                     SQLiteDatabase db){

        String[] args = new String[]{

                Integer.toString(sourceId),
                excerptValue,
                beginTime,
                endTime,
                pages
        };

        db.execSQL(NwdContract.INSERT_OR_IGNORE_SOURCE_EXCERPT_SRCID_EXVAL_BTIME_ETIME_PGS_V_W_X_Y_Z, args);
    }

    public void ensureArchivistSourceExcerptTaggings(
            ArchivistSourceExcerpt ase)
            throws Exception {

        //mirrors this.ensureMediaTaggings()

        db.beginTransaction();

        try {

            ensureArchivistSourceExcerptTaggings(ase, db);

            db.setTransactionSuccessful();

        }finally {

            db.endTransaction();
        }
    }

    private void ensureArchivistSourceExcerptTaggings(
            ArchivistSourceExcerpt ase,
            SQLiteDatabase db)
                throws Exception {

        for(ArchivistSourceExcerptTagging aset : ase.getExcerptTaggings()){

            if(aset.getMediaTagId() < 1){

                aset.setMediaTagId(ensureMediaTag(aset.getMediaTagValue(), db));
            }

            if(aset.getSourceExcerptId() < 1){

                aset.setSourceExcerptId(ase.getExcerptId());
            }

            if(aset.getSourceExcerptId() < 1 || aset.getMediaTagId() < 1){

                throw new Exception("Unable to ensure ExcerptTagging: " +
                        "excerptId and/or mediaId not set.");
            }

            insertOrIgnoreArchivistExcerptTagging(aset, db);
            updateOrIgnoreArchivistSourceExcerptTagging(aset, db);
        }
    }

    private void insertOrIgnoreArchivistExcerptTagging(
            int sourceExcerptId,
            int mediaTagId,
            SQLiteDatabase db) {

        String[] args = new String[]{

                Integer.toString(sourceExcerptId),
                Integer.toString(mediaTagId)
        };

        //should mirror INSERT_OR_IGNORE_MEDIA_TAGGING_X_Y
        db.execSQL(NwdContract.INSERT_OR_IGNORE_EXCERPT_TAGGING_X_Y, args);
    }

    private void updateOrIgnoreArchivistSourceExcerptTagging(
            String taggedAt,
            String untaggedAt,
            int excerptId,
            int mediaTagId,
            SQLiteDatabase db) {

        String excerptIdString = Integer.toString(excerptId);

        String mediaTagIdString = Integer.toString(mediaTagId);

        //should mirror UPDATE_MEDIA_TAGGING_TAGGED_UNTAGGED_WHERE_MEDIA_ID_AND_TAG_ID_W_X_Y_Z
        SQLiteStatement updateStatement = db.compileStatement(
                NwdContract.UPDATE_EXCERPT_TAGGING_TAGGED_UNTAGGED_WHERE_EXID_AND_TGID_W_X_Y_Z
        );

        //will bind null by default (desired behavior for timestamp comparisons)
        if(!Utils.stringIsNullOrWhitespace(taggedAt)){
            updateStatement.bindString(1, taggedAt);
        }

        //will bind null by default (desired behavior for timestamp comparisons)
        if(!Utils.stringIsNullOrWhitespace(untaggedAt)){
            updateStatement.bindString(2, untaggedAt);
        }

        updateStatement.bindString(3, excerptIdString);
        updateStatement.bindString(4, mediaTagIdString);

        updateStatement.execute();
    }

    private void insertOrIgnoreArchivistExcerptTagging(
            ArchivistSourceExcerptTagging aset,
            SQLiteDatabase db) {

        String[] args = new String[]{

                Integer.toString(aset.getSourceExcerptId()),
                Integer.toString(aset.getMediaTagId())
        };

        //should mirror INSERT_OR_IGNORE_MEDIA_TAGGING_X_Y
        db.execSQL(NwdContract.INSERT_OR_IGNORE_EXCERPT_TAGGING_X_Y, args);
    }

    private void updateOrIgnoreArchivistSourceExcerptTagging(
            ArchivistSourceExcerptTagging aset, SQLiteDatabase db) {

        String taggedAt =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(aset.getTaggedAt());

        String untaggedAt =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(aset.getUntaggedAt());

        String excerptId = Integer.toString(aset.getSourceExcerptId());

        String mediaTagId = Integer.toString(aset.getMediaTagId());

        //should mirror UPDATE_MEDIA_TAGGING_TAGGED_UNTAGGED_WHERE_MEDIA_ID_AND_TAG_ID_W_X_Y_Z
        SQLiteStatement updateStatement = db.compileStatement(
                NwdContract.UPDATE_EXCERPT_TAGGING_TAGGED_UNTAGGED_WHERE_EXID_AND_TGID_W_X_Y_Z
        );

        //will bind null by default (desired behavior for timestamp comparisons)
        if(!Utils.stringIsNullOrWhitespace(taggedAt)){
            updateStatement.bindString(1, taggedAt);
        }

        //will bind null by default (desired behavior for timestamp comparisons)
        if(!Utils.stringIsNullOrWhitespace(untaggedAt)){
            updateStatement.bindString(2, untaggedAt);
        }

        updateStatement.bindString(3, excerptId);
        updateStatement.bindString(4, mediaTagId);

        updateStatement.execute();
    }

    /**
     * assumes excerpt is already populated this just populates taggings
     * and relies on sourceExcerptId already being set
     */
    public void populateArchivistSourceExcerptTaggings(
            ArrayList<ArchivistSourceExcerpt> lst) throws Exception {

        db.beginTransaction();

        try {

            for(ArchivistSourceExcerpt ase : lst) {

                if(ase.getExcerptId() < 1){

                    throw new Exception("excerpt id must " +
                            "be set to populate taggings");
                }

                populateArchivistSourceExcerptTaggingsByExcerptId(ase, db);
            }

            db.setTransactionSuccessful();

        }finally {

            db.endTransaction();
        }
    }

    private void populateArchivistSourceExcerptTaggingsByExcerptId(
            ArchivistSourceExcerpt ase,
            SQLiteDatabase db)
                throws Exception {

        //mimics this.populateMediaTaggingsByHash()

        String excerptIdString = Integer.toString(ase.getExcerptId());

        if(Utils.stringIsNullOrWhitespace(excerptIdString)){

            throw new Exception("excerpt id must be set to populate taggings");
        }

        String[] args =
                new String[]{ excerptIdString };

        //mirrors SELECT_MEDIA_TAGGINGS_FOR_HASH_X
        Cursor cursor =
                db.rawQuery(
                        NwdContract.SELECT_ARCHIVIST_SOURCE_EXCERPT_TAGGINGS_FOR_EXID,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_MEDIA_TAG_ID,
                        NwdContract.COLUMN_SOURCE_EXCERPT_TAGGING_ID,
                        NwdContract.COLUMN_SOURCE_EXCERPT_ID,
                        NwdContract.COLUMN_MEDIA_TAG_VALUE,
                        NwdContract.COLUMN_SOURCE_EXCERPT_TAGGING_TAGGED_AT,
                        NwdContract.COLUMN_SOURCE_EXCERPT_TAGGING_UNTAGGED_AT
                };

        if(cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                int mediaTagId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_MEDIA_TAG_ID));

                int sourceExcerptTaggingId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_SOURCE_EXCERPT_TAGGING_ID));

                int sourceExcerptId =
                        Integer.parseInt(
                                record.get(NwdContract.COLUMN_SOURCE_EXCERPT_ID));

                String mediaTagValue =
                        record.get(NwdContract.COLUMN_MEDIA_TAG_VALUE);

                String taggedAtString =
                        record.get(NwdContract.COLUMN_SOURCE_EXCERPT_TAGGING_TAGGED_AT);

                String untaggedAtString =
                        record.get(NwdContract.COLUMN_SOURCE_EXCERPT_TAGGING_UNTAGGED_AT);

                Date taggedAt =
                        TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(taggedAtString);

                Date untaggedAt =
                        TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(untaggedAtString);


                //getTag() will create a new one if it doesn't exist already
                ArchivistSourceExcerptTagging aset =
                        ase.getTag(mediaTagValue);

                aset.setMediaTagId(mediaTagId);
                aset.setSourceExcerptTaggingId(sourceExcerptTaggingId);
                aset.setSourceExcerptId(sourceExcerptId);
                aset.setTimeStamps(taggedAt, untaggedAt);

            } while (cursor.moveToNext());

        }

        cursor.close();
    }

    public ArrayList<ArchivistSourceType> getArchivistSourceTypes() {

        ArrayList<ArchivistSourceType> allSourceTypes = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_TYPE_ID_TYPE_VALUE_FROM_SOURCE_TYPE,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_TYPE_ID,
                            NwdContract.COLUMN_SOURCE_TYPE_VALUE
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    int sourceTypeId =
                            Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_TYPE_ID));

                    String sourceTypeValue =
                            record.get(NwdContract.COLUMN_SOURCE_TYPE_VALUE);


                    ArchivistSourceType sourceType =
                            new ArchivistSourceType(sourceTypeId, sourceTypeValue);

                    allSourceTypes.add(sourceType);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return allSourceTypes;
    }


    public ArrayList<ArchivistSource> getAllArchivistSources() {

        ArrayList<ArchivistSource> allSources = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_SOURCES, //query is in NwdSql, just remove where clause
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_ID,
                            NwdContract.COLUMN_SOURCE_TYPE_ID,
                            NwdContract.COLUMN_SOURCE_TITLE,
                            NwdContract.COLUMN_SOURCE_AUTHOR,
                            NwdContract.COLUMN_SOURCE_DIRECTOR,
                            NwdContract.COLUMN_SOURCE_YEAR,
                            NwdContract.COLUMN_SOURCE_URL,
                            NwdContract.COLUMN_SOURCE_RETRIEVAL_DATE
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);


                    int sourceId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_ID));
                    int sourceTypeId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_TYPE_ID));
                    String sourceTitle = record.get(NwdContract.COLUMN_SOURCE_TITLE);
                    String sourceAuthor = record.get(NwdContract.COLUMN_SOURCE_AUTHOR);
                    String sourceDirector = record.get(NwdContract.COLUMN_SOURCE_DIRECTOR);
                    String sourceYear = record.get(NwdContract.COLUMN_SOURCE_YEAR);
                    String sourceUrl = record.get(NwdContract.COLUMN_SOURCE_URL);
                    String sourceRetrievalDate = record.get(NwdContract.COLUMN_SOURCE_RETRIEVAL_DATE);

                    ArchivistSource archivistSource =
                            new ArchivistSource(
                                    sourceId,
                                    sourceTypeId,
                                    sourceTitle,
                                    sourceAuthor,
                                    sourceDirector,
                                    sourceYear,
                                    sourceUrl,
                                    sourceRetrievalDate);

                    allSources.add(archivistSource);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return allSources;
    }

    public ArrayList<ArchivistSource> getArchivistSourcesForTypeId(int sourceTypeId) {


        ArrayList<ArchivistSource> sourcesForTypeId = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{
                            Integer.toString(sourceTypeId)
                    };

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_SOURCES_BY_TYPE_ID_X,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_ID,
                            NwdContract.COLUMN_SOURCE_TYPE_ID,
                            NwdContract.COLUMN_SOURCE_TITLE,
                            NwdContract.COLUMN_SOURCE_AUTHOR,
                            NwdContract.COLUMN_SOURCE_DIRECTOR,
                            NwdContract.COLUMN_SOURCE_YEAR,
                            NwdContract.COLUMN_SOURCE_URL,
                            NwdContract.COLUMN_SOURCE_RETRIEVAL_DATE
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);


                    int sourceId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_ID));
                    //int sourceTypeId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_TYPE_ID));
                    String sourceTitle = record.get(NwdContract.COLUMN_SOURCE_TITLE);
                    String sourceAuthor = record.get(NwdContract.COLUMN_SOURCE_AUTHOR);
                    String sourceDirector = record.get(NwdContract.COLUMN_SOURCE_DIRECTOR);
                    String sourceYear = record.get(NwdContract.COLUMN_SOURCE_YEAR);
                    String sourceUrl = record.get(NwdContract.COLUMN_SOURCE_URL);
                    String sourceRetrievalDate = record.get(NwdContract.COLUMN_SOURCE_RETRIEVAL_DATE);

                    ArchivistSource archivistSource =
                            new ArchivistSource(
                                    sourceId,
                                    sourceTypeId,
                                    sourceTitle,
                                    sourceAuthor,
                                    sourceDirector,
                                    sourceYear,
                                    sourceUrl,
                                    sourceRetrievalDate);

                    sourcesForTypeId.add(archivistSource);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return sourcesForTypeId;
    }

    public ArrayList<ArchivistSourceExcerpt> getArchivistSourceExcerptsForSourceId(int sourceId) {

        ArrayList<ArchivistSourceExcerpt> sourceExcerptsForSourceId = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{
                            Integer.toString(sourceId)
                    };

            //////////////////////////////////////////////////////////////////////
            // NOTE: NwdSql also has other versions of this query,
            // if you come here looking for slightly different
            // functionality, don't reinvent the wheel
            //////////////////////////////////////////////////////////////////////
            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_SOURCE_EXCERPTS_FOR_SOURCE_ID_X,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_EXCERPT_ID,
                            NwdContract.COLUMN_SOURCE_ID,
                            NwdContract.COLUMN_SOURCE_EXCERPT_VALUE,
                            NwdContract.COLUMN_SOURCE_EXCERPT_PAGES,
                            NwdContract.COLUMN_SOURCE_EXCERPT_BEGIN_TIME,
                            NwdContract.COLUMN_SOURCE_EXCERPT_END_TIME
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);


                    int sourceExcerptId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_EXCERPT_ID));
                    //int sourceId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_ID));
                    String sourceExcerptValue = record.get(NwdContract.COLUMN_SOURCE_EXCERPT_VALUE);
                    String sourceExcerptPages = record.get(NwdContract.COLUMN_SOURCE_EXCERPT_PAGES);
                    String sourceExcerptBeginTime = record.get(NwdContract.COLUMN_SOURCE_EXCERPT_BEGIN_TIME);
                    String sourceExcerptEndTime = record.get(NwdContract.COLUMN_SOURCE_EXCERPT_END_TIME);

                    ArchivistSourceExcerpt ase =
                            new ArchivistSourceExcerpt(
                                    sourceExcerptId,
                                    sourceId,
                                    sourceExcerptValue,
                                    sourceExcerptPages,
                                    sourceExcerptBeginTime,
                                    sourceExcerptEndTime
                            );

                    sourceExcerptsForSourceId.add(ase);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return sourceExcerptsForSourceId;
    }

    public void ensureArchivistSourceLocationValue(String locationName) {
        db.execSQL(NwdContract.INSERT_OR_IGNORE_SOURCE_LOCATION_VALUE, new String[]{ locationName });
    }

    public int ensureArchivistSourceLocationValue(String locationName, SQLiteDatabase db) {

        int id = getIdForArchivistSourceLocationValue(locationName, db);

        if(id < 1){
            insertOrIgnoreArchivistSourceLocationValue(locationName, db);
            id = getIdForArchivistSourceLocationValue(locationName, db);
        }

        return id;
    }

    private int getIdForArchivistSourceLocationValue(String locationName, SQLiteDatabase db) {

        int id = -1;

        String[] args =
                new String[]{
                        locationName
                };

        Cursor cursor =
                db.rawQuery(
                        NwdContract.SELECT_SOURCE_LOCATION_ID_FOR_VALUE_X,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_SOURCE_LOCATION_ID
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                id = Integer.parseInt(
                        record.get(NwdContract.COLUMN_SOURCE_LOCATION_ID));

            } while (cursor.moveToNext());

            cursor.close();
        }

        return id;
    }

    public void insertOrIgnoreArchivistSourceLocationValue(String locationName, SQLiteDatabase db){
        db.execSQL(NwdContract.INSERT_OR_IGNORE_SOURCE_LOCATION_VALUE, new String[]{ locationName });
    }

    public ArrayList<ArchivistSourceLocation> getAllArchivistSourceLocations() {

        ArrayList<ArchivistSourceLocation> allSourceLocations = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{};

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_LOCATION_ID_LOCATION_VALUE_FROM_SOURCE_LOCATION,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_LOCATION_ID,
                            NwdContract.COLUMN_SOURCE_LOCATION_VALUE
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);


                    int sourceLocationId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_LOCATION_ID));
                    String sourceLocationValue = record.get(NwdContract.COLUMN_SOURCE_LOCATION_VALUE);

                    ArchivistSourceLocation asl =
                            new ArchivistSourceLocation(
                                    sourceLocationId,
                                    sourceLocationValue
                            );

                    allSourceLocations.add(asl);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return allSourceLocations;
    }

    public void ensureArchivistSourceLocationSubset(int sourceLocationId, String subsetName) {

        db.execSQL(NwdContract.INSERT_OR_IGNORE_SOURCE_LOCATION_SUBSET_FOR_LOCATION_ID_AND_SUBSET_VALUE_X_Y,
                new String[]{Integer.toString(sourceLocationId), subsetName});
    }

    public int ensureArchivistSourceLocationSubset(int sourceLocationId, String subsetName, SQLiteDatabase db) {

        int id = getIdForArchivistSourceLocationSubset(sourceLocationId, subsetName, db);

        if(id < 1){
            insertOrIgnoreArchivistSourceLocationSubset(sourceLocationId, subsetName, db);
            id = getIdForArchivistSourceLocationSubset(sourceLocationId, subsetName, db);
        }

        return id;
    }

    private int getIdForArchivistSourceLocationSubset(int sourceLocationId, String subsetName, SQLiteDatabase db) {

        int id = -1;

        String[] args =
                new String[]{
                        Integer.toString(sourceLocationId),
                        subsetName
                };

        Cursor cursor =
                db.rawQuery(
                        NwdContract.SELECT_SOURCE_LOCATION_SUBSET_ID_FOR_LOCATION_ID_AND_SUBSET_VALUE_X_Y,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ID
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            Map<String, String> record =
                    cursorToRecord(cursor, columnNames);

            id = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ID));

            cursor.close();
        }

        return id;
    }

    public void insertOrIgnoreArchivistSourceLocationSubset(int sourceLocationId, String subsetName, SQLiteDatabase db) {

        db.execSQL(NwdContract.INSERT_OR_IGNORE_SOURCE_LOCATION_SUBSET_FOR_LOCATION_ID_AND_SUBSET_VALUE_X_Y,
                new String[]{Integer.toString(sourceLocationId), subsetName});
    }

    public ArrayList<ArchivistSourceLocationSubset> getArchivistSourceLocationSubsetsForLocationId(int sourceLocationId) {

        ArrayList<ArchivistSourceLocationSubset> subsets = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{ Integer.toString(sourceLocationId) };

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_SOURCE_LOCATION_SUBSETS_BY_LOCATION_ID_X,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_LOCATION_ID,
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ID,
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_VALUE
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    sourceLocationId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_LOCATION_ID));
                    int sourceLocationSubsetId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ID));
                    String sourceLocationSubsetValue = record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_VALUE);

                    ArchivistSourceLocationSubset asls =
                            new ArchivistSourceLocationSubset(
                                    sourceLocationSubsetId,
                                    sourceLocationId,
                                    sourceLocationSubsetValue
                            );

                    subsets.add(asls);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return subsets;
    }

    public void insertOrIgnoreArchivistSourceLocationSubsetEntry(
            int sourceLocationSubsetId,
            int sourceId,
            String sourceLocationSubsetEntryValue
    ) {

        String[] args = new String[]{
                Integer.toString(sourceLocationSubsetId),
                Integer.toString(sourceId),
                sourceLocationSubsetEntryValue
        };

        db.execSQL(
                NwdContract.INSERT_OR_IGNORE_INTO_SOURCE_LOCATION_SUBSET_ENTRY_VALUES_SUBSET_ID_SOURCE_ID_ENTRY_VALUE_X_Y_Z,
                args);
    }

    public void insertOrIgnoreArchivistSourceLocationSubsetEntry(
            int sourceLocationSubsetId,
            int sourceId,
            String sourceLocationSubsetEntryValue,
            SQLiteDatabase db
    ) {

        String[] args = new String[]{
                Integer.toString(sourceLocationSubsetId),
                Integer.toString(sourceId),
                sourceLocationSubsetEntryValue
        };

        db.execSQL(
                NwdContract.INSERT_OR_IGNORE_INTO_SOURCE_LOCATION_SUBSET_ENTRY_VALUES_SUBSET_ID_SOURCE_ID_ENTRY_VALUE_X_Y_Z,
                args);
    }

    private int ensureArchivistSourceLocationSubsetEntry(
            int sourceLocationSubsetId,
            int sourceId,
            String sourceLocationSubsetEntryValue,
            SQLiteDatabase db
    ) throws ParseException {

        int entryId =
                getSourceLocationEntryId(
                        sourceLocationSubsetId,
                        sourceId,
                        sourceLocationSubsetEntryValue,
                        db);

        if(entryId < 1){

            insertOrIgnoreArchivistSourceLocationSubsetEntry(
                    sourceLocationSubsetId,
                    sourceId,
                    sourceLocationSubsetEntryValue,
                    db
            );
        }

        entryId = getSourceLocationEntryId(
                sourceLocationSubsetId,
                sourceId,
                sourceLocationSubsetEntryValue,
                db
        );

        return entryId;
    }

    public ArrayList<ArchivistSourceLocationEntry> getArchivistSourceLocationSubsetEntriesForSourceId(
            int sourceId) throws ParseException {

        ArrayList<ArchivistSourceLocationEntry> entries = new ArrayList<>();

        db.beginTransaction();

        try{

            String[] args =
                    new String[]{ Integer.toString(sourceId) };

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_SOURCE_LOCATION_SUBSET_ENTRIES_FOR_SOURCE_ID_X,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID,
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ID,
                            NwdContract.COLUMN_SOURCE_LOCATION_VALUE,
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_VALUE,
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VALUE,
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_AT,
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_MISSING_AT
                    };

            if(cursor.getCount() > 0){

                cursor.moveToFirst();

                do {

                    Map<String, String> record =
                            cursorToRecord(cursor, columnNames);

                    int sourceLocationSubsetEntryId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID));
                    int sourceLocationSubsetId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ID));
                    String sourceLocationValue = record.get(NwdContract.COLUMN_SOURCE_LOCATION_VALUE);
                    String sourceLocationSubsetValue = record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_VALUE);
                    String sourceLocationSubsetEntryValue = record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VALUE);
                    String sourceLocationSubsetEntryVerifiedPresentAt = record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_AT);
                    String sourceLocationSubsetEntryVerifiedMissingAt = record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_MISSING_AT);

                    Date verifiedPresent =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    sourceLocationSubsetEntryVerifiedPresentAt);

                    Date verifiedMissing =
                            TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                                    sourceLocationSubsetEntryVerifiedMissingAt);


                    ArchivistSourceLocationEntry asle =
                            new ArchivistSourceLocationEntry(
                                    sourceId,
                                    sourceLocationSubsetId,
                                    sourceLocationSubsetEntryId,
                                    sourceLocationValue,
                                    sourceLocationSubsetValue,
                                    sourceLocationSubsetEntryValue
                            );

                    asle.setTimeStamps(verifiedPresent, verifiedMissing);

                    entries.add(asle);

                } while (cursor.moveToNext());

                cursor.close();
            }

            db.setTransactionSuccessful();

        }catch (Exception ex){

            throw ex;

        }finally {

            db.endTransaction();
        }

        return entries;
    }

    public int getSourceLocationEntryId(
            int sourceLocationsSubsetId,
            int sourceId,
            String entryName) throws ParseException {

        int sourceLocationSubsetEntryId = -1;

        if(sourceLocationsSubsetId > 0 &&
                sourceId > 0 &&
                entryName != null){

            String[] args =
                    new String[]{
                            Integer.toString(sourceLocationsSubsetId),
                            Integer.toString(sourceId),
                            entryName
                    };

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_SOURCE_LOCATION_SUBSET_ENTRY_ID_FOR_SUBSET_ID_AND_SOURCE_ID_AND_ENTRY_VALUE_X_Y_Z,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID
                    };

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                sourceLocationSubsetEntryId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID));
            }

            cursor.close();

        }

        return sourceLocationSubsetEntryId;
    }

    private int getSourceLocationEntryId(
            int sourceLocationsSubsetId,
            int sourceId,
            String entryName,
            SQLiteDatabase db) throws ParseException {

        int sourceLocationSubsetEntryId = -1;

        if(sourceLocationsSubsetId > 0 &&
                sourceId > 0 &&
                entryName != null){

            String[] args =
                    new String[]{
                            Integer.toString(sourceLocationsSubsetId),
                            Integer.toString(sourceId),
                            entryName
                    };

            Cursor cursor =
                    db.rawQuery(
                            NwdContract.SELECT_SOURCE_LOCATION_SUBSET_ENTRY_ID_FOR_SUBSET_ID_AND_SOURCE_ID_AND_ENTRY_VALUE_X_Y_Z,
                            args);

            String[] columnNames =
                    new String[]{
                            NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID
                    };

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                sourceLocationSubsetEntryId = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_LOCATION_SUBSET_ENTRY_ID));
            }

            cursor.close();

        }

        return sourceLocationSubsetEntryId;
    }


    public void updateArchivistSourceLocationSubsetEntryTimeStamps(
            int sourceLocationSubsetEntryId,
            Date verifiedPresentAt,
            Date verifiedMissingAt
    ) {

        String verifiedPresent =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(verifiedPresentAt);

        String verifiedMissing =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(verifiedMissingAt);

        String[] args = new String[]{
              verifiedPresent,
              verifiedMissing,
              Integer.toString(sourceLocationSubsetEntryId)
        };

        db.execSQL(NwdContract.UPDATE_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_VERIFIED_MISSING_FOR_ID_X_Y_Z, args);
    }

    private void updateArchivistSourceLocationSubsetEntryTimeStamps(
            int sourceLocationSubsetEntryId,
            Date verifiedPresentAt,
            Date verifiedMissingAt,
            SQLiteDatabase db
    ) {

        String verifiedPresent =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(verifiedPresentAt);

        String verifiedMissing =
                TimeStamp.to_UTC_Yyyy_MM_dd_hh_mm_ss(verifiedMissingAt);

        String[] args = new String[]{
                verifiedPresent,
                verifiedMissing,
                Integer.toString(sourceLocationSubsetEntryId)
        };

        db.execSQL(NwdContract.UPDATE_SOURCE_LOCATION_SUBSET_ENTRY_VERIFIED_PRESENT_VERIFIED_MISSING_FOR_ID_X_Y_Z, args);
    }

    public void deleteArchivistSourceExcerptAnnotationsByExcerptId(int excerptId) {

        String[] args = {Integer.toString(excerptId)};

        db.execSQL(NwdContract.DELETE_ARCHIVIST_SOURCE_EXCERPT_ANNOTATIONS_FOR_EXID, args);
    }

    public void deleteArchivistSourceExcerptTaggingsByExcerptId(int excerptId) {

        String[] args = {Integer.toString(excerptId)};

        db.execSQL(NwdContract.DELETE_ARCHIVIST_SOURCE_EXCERPT_TAGGINGS_FOR_EXID, args);
    }

    public void deleteArchivistSourceExcerptsBySourceId(int sourceId) {

        String[] args = {Integer.toString(sourceId)};

        db.execSQL(NwdContract.DELETE_ARCHIVIST_SOURCE_EXCERPTS_FOR_SOURCE_ID, args);
    }

    public void deleteArchivistSourceLocationSubsetEntriesBySourceId(int sourceId) {

        String[] args = {Integer.toString(sourceId)};

        db.execSQL(NwdContract.DELETE_ARCHIVIST_SOURCE_LOCATION_SUBSET_ENTRIES_FOR_SOURCE_ID, args);
    }

    public void deleteArchivistSourceBySourceId(int sourceId) {

        String[] args = {Integer.toString(sourceId)};

        db.execSQL(NwdContract.DELETE_ARCHIVIST_SOURCE_FOR_SOURCE_ID, args);
    }

    public void save(ArchivistXmlSource axs, SQLiteDatabase db) throws ParseException {

        //mimic sync(Media media, db);
        int sourceTypeId = ensureArchivistSourceTypeName(axs.getSourceType(), db);

        int sourceId = ensureArchivistXmlSource(sourceTypeId, axs, db);

        for(ArchivistXmlLocationEntry axle : axs.getLocationEntries()){

            int locationId = ensureArchivistSourceLocationValue(axle.getLocation(), db);

            int subsetId = ensureArchivistSourceLocationSubset(
                    locationId, axle.getLocationSubset(), db);

            int entryId = ensureArchivistSourceLocationSubsetEntry(
                    subsetId, sourceId, axle.getLocationSubsetEntry(), db);

            Date verifiedPresentAt =
                    TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                            axle.getVerifiedPresent());

            Date verifiedMissingAt =
                    TimeStamp.yyyy_MM_dd_hh_mm_ss_UTC_ToDate(
                            axle.getVerifiedMissing()
                    );

            updateArchivistSourceLocationSubsetEntryTimeStamps(
                    entryId,
                    verifiedPresentAt,
                    verifiedMissingAt,
                    db
            );
        }

        for(ArchivistXmlSourceExcerpt axse : axs.getExcerpts()){

            //ensure excerpt
            int excerptId =
                    ensureArchivistSourceExcerpt(
                            sourceId,
                            axse.getExcerptValue(),
                            axse.getBeginTime(),
                            axse.getEndTime(),
                            axse.getPages(),
                            db
                    );

            for(ArchivistXmlTag axt : axse.getTags()){

                int tagId = ensureMediaTag(axt.getTagValue(), db);

                //upsert excerpt tagging
                insertOrIgnoreArchivistExcerptTagging(excerptId, tagId, db);
                updateOrIgnoreArchivistSourceExcerptTagging(
                        axt.getTaggedAt(), axt.getUntaggedAt(), excerptId, tagId, db);
            }
        }
    }

    private int ensureArchivistSourceExcerpt(int sourceId,
                                             String excerptValue,
                                             String beginTime,
                                             String endTime,
                                             String pages,
                                             SQLiteDatabase db) {

        int id = getIdForArchivistSourceExcerpt(sourceId,
                                                excerptValue,
                                                beginTime,
                                                endTime,
                                                pages,
                                                db);

        if(id < 1){

            insertOrIgnoreArchivistSourceExcerpt(sourceId,
                                                 excerptValue,
                                                 beginTime,
                                                 endTime,
                                                 pages,
                                                 db);

            id = getIdForArchivistSourceExcerpt(sourceId,
                                                excerptValue,
                                                beginTime,
                                                endTime,
                                                pages,
                                                db);
        }

        return id;
    }

    private int getIdForArchivistSourceExcerpt(int sourceId,
                                               String excerptValue,
                                               String beginTime,
                                               String endTime,
                                               String pages,
                                               SQLiteDatabase db) {

        int id = -1;

        String[] args =
                new String[]{
                        Integer.toString(sourceId),
                        excerptValue,
                        beginTime,
                        endTime,
                        pages
                };

        Cursor cursor =
                db.rawQuery(
                        NwdContract.SELECT_SOURCE_EXCERPT_ID_FOR_SRCID_EXVAL_BTIME_ETIME_PGS_V_W_X_Y_Z,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_SOURCE_EXCERPT_ID
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            Map<String, String> record =
                    cursorToRecord(cursor, columnNames);

            id = Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_EXCERPT_ID));

            cursor.close();
        }

        return id;
    }

    private int ensureArchivistXmlSource(int sourceTypeId, ArchivistXmlSource axs, SQLiteDatabase db) {

        int id = getIdForArchivistXmlSource(sourceTypeId, axs, db);

        if(id < 1){
            insertOrIgnoreArchivistXmlSource(sourceTypeId, axs, db);
            id = getIdForArchivistXmlSource(sourceTypeId, axs, db);
        }

        return id;
    }

    private int getIdForArchivistXmlSource(int sourceTypeId, ArchivistXmlSource axs, SQLiteDatabase db) {

        int sourceId = -1;

        String[] args =
                new String[]{
                        Integer.toString(sourceTypeId),
                        axs.getTitle(),
                        axs.getAuthor(),
                        axs.getDirector(),
                        axs.getYear(),
                        axs.getUrl(),
                        axs.getRetrievalDate(),
                        axs.getSourceTag()
                };

        Cursor cursor =
                db.rawQuery(
                        NwdContract.SELECT_SOURCE_FOR_TID_TTL_AUT_DIR_YR_URL_RDT_TG,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_SOURCE_ID
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                sourceId = Integer.parseInt(
                        record.get(NwdContract.COLUMN_SOURCE_ID));

            } while (cursor.moveToNext());

            cursor.close();
        }

        return sourceId;
    }

    public void insertOrIgnoreArchivistXmlSource(int sourceTypeId,
                                              ArchivistXmlSource axs,
                                              SQLiteDatabase db) {

        String[] args = new String[]{

                Integer.toString(sourceTypeId),
                axs.getTitle(),
                axs.getAuthor(),
                axs.getDirector(),
                axs.getYear(),
                axs.getUrl(),
                axs.getRetrievalDate()
        };

        db.execSQL(NwdContract.INSERT_SOURCE_T_U_V_W_X_Y_Z, args);
    }

    private void insertOrIgnoreSourceTypeValue(String sourceTypeName,
                                               SQLiteDatabase db){

        db.execSQL(NwdContract.INSERT_OR_IGNORE_SOURCE_TYPE_VALUE,
                   new String[]{sourceTypeName});
    }

    private int getIdForSourceTypeValue(String sourceTypeValue,
                                        SQLiteDatabase db){

        int id = -1;

        String[] args =
                new String[]{};

        Cursor cursor =
                db.rawQuery(
                        NwdContract.SELECT_TYPE_ID_TYPE_VALUE_FROM_SOURCE_TYPE,
                        args);

        String[] columnNames =
                new String[]{
                        NwdContract.COLUMN_SOURCE_TYPE_ID,
                        NwdContract.COLUMN_SOURCE_TYPE_VALUE
                };

        if(cursor.getCount() > 0){

            cursor.moveToFirst();

            do {

                Map<String, String> record =
                        cursorToRecord(cursor, columnNames);

                int sourceTypeId =
                        Integer.parseInt(record.get(NwdContract.COLUMN_SOURCE_TYPE_ID));

                String sourceTypeValueForRow =
                        record.get(NwdContract.COLUMN_SOURCE_TYPE_VALUE);


                if(sourceTypeValue.equalsIgnoreCase(sourceTypeValueForRow)){

                    id = sourceTypeId;
                }

            } while (cursor.moveToNext());

            cursor.close();
        }

        return id;
    }

    private int ensureArchivistSourceTypeName(String sourceTypeName, SQLiteDatabase db) {

        int id = getIdForSourceTypeValue(sourceTypeName, db);

        if(id < 1){

            insertOrIgnoreSourceTypeValue(sourceTypeName, db);
            id = getIdForSourceTypeValue(sourceTypeName, db);
        }

        return id;
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
