package com.nineworldsdeep.gauntlet.sqlite;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nineworldsdeep.gauntlet.core.Configuration;
import com.nineworldsdeep.gauntlet.Utils;

import java.io.File;
import java.io.FileFilter;

/**
 * // http://stackoverflow.com/questions/5332328/sqliteopenhelper-problem-with-fully-qualified-db-path-name/9168969#9168969
 */
public class NwdDbContextWrapper extends ContextWrapper {

    private static final String DEBUG_CONTEXT = "NwdDbContextWrapper";

    public NwdDbContextWrapper(Context base) {
        super(base);
    }

    /**
     * Gets a File object representing the
     * external database file with the
     * supplied name.
     * The name can be given with or without
     * the ".db" suffix.
     * @param name
     * @return
     */
    @Override
    public File getDatabasePath(String name)
    {
        File sqliteDbFile = Configuration.getSqliteDb(name);

        if (!sqliteDbFile.getParentFile().exists())
        {
            sqliteDbFile.getParentFile().mkdirs();
        }

        if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN))
        {
            Log.w(DEBUG_CONTEXT,
                    "getDatabasePath(" + name + ") = " + sqliteDbFile.getAbsolutePath());
        }

        return sqliteDbFile;
    }

    /**
     * will delete a database with the specified name
     * in the external directory (NWD/sqlite)
     * @param name
     * @return
     */
    @Override
    public boolean deleteDatabase(String name) {

        boolean successful = false;

        try {

            File f = Configuration.getSqliteDb(name);
            successful = deleteSqliteDatabase(f);

        } catch (Exception e) {

            Utils.log("error deleting database '" + name +"':" +
                    e.getMessage());
        }

        return successful;
    }

    /**
     * borrow from Android.database.sqlite.SqliteDatabase.
     * having troubles deleting external databases, putting it
     * here in case we need to tweak it (only used in
     * the deleteDatabase method in this class)
     * @param file
     * @return true if database is deleted succesfully
     */
    private boolean deleteSqliteDatabase(File file){

        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }

        boolean deleted = false;
        deleted |= file.delete();
        deleted |= new File(file.getPath() + "-journal").delete();
        deleted |= new File(file.getPath() + "-shm").delete();
        deleted |= new File(file.getPath() + "-wal").delete();

        File dir = file.getParentFile();
        if (dir != null) {
            final String prefix = file.getName() + "-mj";
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File candidate) {
                    return candidate.getName().startsWith(prefix);
                }
            });
            if (files != null) {
                for (File masterJournal : files) {
                    deleted |= masterJournal.delete();
                }
            }
        }
        return deleted;

    }

    /* this version is called for android devices >= api-11. thank to @damccull for fixing this. */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return openOrCreateDatabase(name,mode, factory);
    }

    /* this version is called for android devices < api-11 */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory)
    {
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        // SQLiteDatabase result = super.openOrCreateDatabase(name, mode, factory);
        if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN))
        {
            Log.w(DEBUG_CONTEXT,
                    "openOrCreateDatabase(" + name + ",,) = " + result.getPath());
        }
        return result;
    }
}
