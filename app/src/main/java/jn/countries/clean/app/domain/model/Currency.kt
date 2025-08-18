package jn.countries.clean.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Currency(
    val name: String,
    val symbol: String?
) : Parcelable