package com.nineworldsdeep.gauntlet.synergy.v6

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "synergy_item_table")
data class SynergyItem (
        @PrimaryKey @ColumnInfo(name = "synergy_item_id") val itemId: String,
        @ColumnInfo(name = "synergy_item_value") var itemValue: String,
        @ColumnInfo(name = "synergy_item_type") var itemType: String,
        @ColumnInfo(name = "synergy_item_last_modified") var itemLastModified: String?
)