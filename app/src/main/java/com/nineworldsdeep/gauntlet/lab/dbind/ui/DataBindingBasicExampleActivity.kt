package com.nineworldsdeep.gauntlet.lab.dbind.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nineworldsdeep.gauntlet.R
import com.nineworldsdeep.gauntlet.databinding.DataBindingBasicExampleActivityBinding


/**
 * Shows a menu.
 */
class DataBindingBasicExampleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // The layout for this activity is a Data Binding layout so it needs to be inflated using
        // DataBindingUtil.
        val binding : DataBindingBasicExampleActivityBinding = DataBindingUtil.setContentView(
                this, R.layout.data_binding_basic_example_activity)

        // The returned binding has references to all the Views with an ID.
        binding.observableFieldsActivityButton.setOnClickListener {
            startActivity(Intent(this, ObservableFieldActivity::class.java))
        }
        binding.viewmodelActivityButton.setOnClickListener {
            startActivity(Intent(this, ViewModelActivity::class.java))
        }
    }
}
