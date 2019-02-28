package com.nineworldsdeep.gauntlet.synergy.v6

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData

class SynergyItemViewModel(application: Application) : AndroidViewModel(application) {

    private val synergyItemRepository: SynergyItemRepository
    internal val allItems: LiveData<List<SynergyItem>>

    init {
        synergyItemRepository = SynergyItemRepository(application)
        allItems = synergyItemRepository.getAllItems()
    }

    fun insert(synergyItem: SynergyItem){
        synergyItemRepository.insert(synergyItem)
    }

    fun getAllItems(): LiveData<List<SynergyItem>>{
        return allItems
    }
}
