package com.nineworldsdeep.gauntlet.lab.dbind.ui

import android.databinding.DataBindingUtil
import android.databinding.ObservableInt
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.nineworldsdeep.gauntlet.R
import com.nineworldsdeep.gauntlet.databinding.ObservableFieldProfileBinding
import com.nineworldsdeep.gauntlet.lab.dbind.data.ObservableFieldProfile

/**
 * This activity shows the same data as [PojoActivity] but it lets the user increment the
 * number of likes by clicking a button. See [ViewModelActivity] for a better implementation.
 */
class ObservableFieldActivity : AppCompatActivity() {

    private val observableFieldProfile = ObservableFieldProfile("Ada", "Lovelace", ObservableInt(0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ObservableFieldProfileBinding =
                DataBindingUtil.setContentView(this, R.layout.observable_field_profile)
        binding.user = observableFieldProfile
    }

    /**
     * This method is triggered by the `android:onclick` attribute in the layout. It puts business
     * logic in the activity, which is not ideal. See {@link ViewModelActivity} for a better
     * solution.
     */
    fun onLike(view: View) {
        observableFieldProfile.likes.set(observableFieldProfile.likes.get() + 1)
    }
}
