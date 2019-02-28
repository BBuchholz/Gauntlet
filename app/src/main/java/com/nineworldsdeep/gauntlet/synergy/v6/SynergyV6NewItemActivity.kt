package com.nineworldsdeep.gauntlet.synergy.v6

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.nineworldsdeep.gauntlet.R
import java.util.*

class SynergyV6NewItemActivity : AppCompatActivity() {

    private var tvItemId: TextView? = null
    private var etItemValue: EditText? = null
    private var etItemType: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_synergy_v6_new_item)
        tvItemId = findViewById(R.id.tvItemId)
        etItemValue = findViewById(R.id.etItemValue)
        etItemType = findViewById(R.id.etItemType)

        tvItemId?.text = UUID.randomUUID().toString()
        etItemType?.hint = "Enter type (default is: [Raw Text])"

        val button = findViewById<Button>(R.id.btnSave)
        button.setOnClickListener {

            val replyIntent = Intent()

            if (TextUtils.isEmpty(etItemValue!!.text)) {

                setResult(Activity.RESULT_CANCELED, replyIntent)

            } else {

                val itemId = tvItemId!!.text.toString()
                val itemValue = etItemValue!!.text.toString()
                var itemType = "Raw Text"

                if(!TextUtils.isEmpty(etItemType!!.text.toString().trim())){

                    val trimmedVersion = etItemType!!.text.toString().trim()
                    itemType = trimmedVersion.split(' ').joinToString(" ") { it.capitalize() }
                }

                replyIntent.putExtra(EXTRA_ITEM_ID, itemId)
                replyIntent.putExtra(EXTRA_ITEM_VALUE, itemValue)
                replyIntent.putExtra(EXTRA_ITEM_TYPE, itemType)

                setResult(Activity.RESULT_OK, replyIntent)
            }

            finish()
        }
    }

    companion object {

        val EXTRA_ITEM_ID = "com.nineworldsdeep.synergy.synergy_item_id"
        val EXTRA_ITEM_VALUE = "com.nineworldsdeep.synergy.synergy_item_value"
        val EXTRA_ITEM_TYPE = "com.nineworldsdeep.synergy.synergy_item_type"
    }
}
