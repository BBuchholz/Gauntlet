package com.nineworldsdeep.gauntlet.synergy.v6

import android.app.Application
import android.arch.lifecycle.LiveData
import android.os.AsyncTask

class SynergyItemRepository(application: Application) {

    private val synergyItemDao: SynergyItemDao
    private val allItems: LiveData<List<SynergyItem>>

    init {
        val synergyRoomDatabase = SynergyRoomDatabase.getDatabase(application)
        synergyItemDao = synergyRoomDatabase?.synergyItemDao()!!
        allItems = synergyItemDao.getAllItems()
    }

    fun getAllItems(): LiveData<List<SynergyItem>>{
        return allItems
    }

    fun insert(synergyItem: SynergyItem){
        insertAsyncTask(synergyItemDao).execute(synergyItem)
    }

    private class insertAsyncTask internal constructor(private val asyncTaskDao: SynergyItemDao) : AsyncTask<SynergyItem, Void, Void>(){

        override fun doInBackground(vararg params: SynergyItem): Void? {
            asyncTaskDao.insert(params[0])
            return null
        }
    }
}
