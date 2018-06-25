package com.nineworldsdeep.gauntlet.lab.dbind.data

import android.databinding.ObservableInt

/**
 * Used as a layout variable to provide static properties (name and lastName) and an observable
 * one (likes).
 */
data class ObservableFieldProfile(
        val name: String,
        val lastName: String,
        val likes: ObservableInt
)