package jn.countries.clean.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car (
    val signs: List<String> = emptyList(),
    val side: String? = null
) : Parcelable