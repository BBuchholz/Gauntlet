package com.nineworldsdeep.gauntlet.lab.dbind.data

import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.nineworldsdeep.gauntlet.BR
import com.nineworldsdeep.gauntlet.lab.dbind.util.ObservableViewModel

/**
 * This class is used as a variable in the XML layout and it's fully observable, meaning that
 * changes to any of the public fields automatically refresh the UI.
 *
 * `Popularity` is exposed here as a `@Bindable` attribute, see the
 * [ProfileObservableFieldsViewModel] for an alternative using Observable fields.
 */
class ProfileObservableViewModel : ObservableViewModel() {
    val name = ObservableField("Ada")
    val lastName = ObservableField("Lovelace")
    val likes =  ObservableInt(0)

    fun onLike() {
        likes.increment()
        // You control when the @Bindable properties are updated using `notifyPropertyChanged()`.
        notifyPropertyChanged(BR.popularity)
    }

    @Bindable
    fun getPopularity(): Popularity {
        return likes.get().let {
            when {
                it > 9 -> Popularity.STAR
                it > 4 -> Popularity.POPULAR
                else -> Popularity.NORMAL
            }
        }
    }
}

/**
 * As an alternative, the @Bindable attribute can be replaced with an
 * `ObservableField`. In this case 'popularity' is an `ObservableField` which has to be computed when
 * `likes` change.
 */
class ProfileObservableFieldsViewModel : ViewModel() {
    val name = ObservableField("Ada")
    val lastName = ObservableField("Lovelace")
    val likes =  ObservableInt(0)

    // popularity is exposed as an ObservableField instead of a @Bindable property.
    val popularity = ObservableField<Popularity>(Popularity.NORMAL)

    fun onLike() {
        likes.set(likes.get() + 1)

        popularity.set(likes.get().let {
            if (it > 9) Popularity.STAR
            if (it > 4) Popularity.POPULAR
            Popularity.NORMAL
        })
    }
}

enum class Popularity {
    NORMAL,
    POPULAR,
    STAR
}

private fun ObservableInt.increment() {
    set(get() + 1)
}