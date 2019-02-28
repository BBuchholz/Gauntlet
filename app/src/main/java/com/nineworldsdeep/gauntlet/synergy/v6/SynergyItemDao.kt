package com.nineworldsdeep.gauntlet.synergy.v6

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface SynergyItemDao {
    @Insert
    fun insert(item: SynergyItem)

    @Query("DELETE FROM synergy_item_table")
    fun deleteAll()

    @Query("SELECT * FROM synergy_item_table ORDER BY synergy_item_last_modified DESC")
    fun getAllItems(): LiveData<List<SynergyItem>>
}
