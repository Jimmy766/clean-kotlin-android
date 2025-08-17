package jn.countries.clean.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryName(
    val common: String,
    val official: String
) : Parcelable