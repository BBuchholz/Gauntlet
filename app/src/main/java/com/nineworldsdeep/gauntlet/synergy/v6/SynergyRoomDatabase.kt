package com.nineworldsdeep.gauntlet.synergy.v6

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.os.AsyncTask
import android.arch.persistence.db.SupportSQLiteDatabase
import android.support.annotation.NonNull
import java.util.*


@Database(entities = arrayOf(SynergyItem::class), version = 1)
abstract class SynergyRoomDatabase : RoomDatabase() {

    abstract fun synergyItemDao(): SynergyItemDao

    companion object {
        private var INSTANCE: SynergyRoomDatabase? = null

        fun getDatabase(context: Context): SynergyRoomDatabase? {
            if(INSTANCE == null){
                synchronized(SynergyRoomDatabase::class){
                    if(INSTANCE == null){
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                SynergyRoomDatabase::class.java, "synergy_database")
                                .addCallback(object : RoomDatabase.Callback() {

                                    override fun onOpen(db: SupportSQLiteDatabase) {
                                        super.onOpen(db)
                                        PopulateDbAsync(INSTANCE!!).execute()
                                    }
                                })
                                .build()
                    }
                }
            }
            return INSTANCE
        }
    }

    private class PopulateDbAsync internal constructor(db: SynergyRoomDatabase) : AsyncTask<Void, Void, Void>() {

        private val mDao: SynergyItemDao

        init {
            mDao = db.synergyItemDao()
        }

        override fun doInBackground(vararg params: Void): Void? {

            //just for dev, clear all data
            mDao.deleteAll()

            //generate demo data for dev
            var newItemId = UUID.randomUUID().toString()
            var newItemTimeStamp = TimeStampV6.nowString()
            var synergyItem = SynergyItem(newItemId, "List Name Goes Here", "Internal List", newItemTimeStamp)
            mDao.insert(synergyItem)

            for(i in 2..9){


                newItemId = UUID.randomUUID().toString()
                newItemTimeStamp = TimeStampV6.nowString()

                synergyItem = SynergyItem(newItemId, "a list item", "Raw Text", newItemTimeStamp)
                mDao.insert(synergyItem)
            }


            return null
        }
    }
}
