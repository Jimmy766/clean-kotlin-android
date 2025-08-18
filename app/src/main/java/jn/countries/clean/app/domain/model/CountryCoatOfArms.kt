package jn.countries.clean.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryCoatOfArms (
  val png: String? = null,
  val svg: String? = null
) : Parcelable