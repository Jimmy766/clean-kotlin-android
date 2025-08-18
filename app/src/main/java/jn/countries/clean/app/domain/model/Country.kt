package jn.countries.clean.app.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.NumberFormat
import java.util.Locale
import kotlin.text.format

@Parcelize
data class Country(
    val code: String,
    val name: CountryName,
    val flags: CountryFlags,
    val population: Long,
    val region: String,
    val subregion: String?,
    val capital: List<String>?,
    val area: Double?,
    val languages: Map<String, String>?,
    val currencies: Map<String, Currency>?,
    var isFavorite: Boolean = false,
    val independent: Boolean = false,
    val unMember: Boolean = false,
    val timezones: List<String>? = null,
    val coatOfArms : CountryCoatOfArms? = null,
    val car: Car? = null,
) : Parcelable {

    fun getLanguagesString(): String? {
        return languages?.values?.joinToString(", ")
    }

    fun getCurrenciesString(): String? {
        return currencies?.values?.joinToString(", ") { currency ->
            "${currency.name} (${currency.symbol ?: "N/A"})"
        }
    }

    fun getFormattedPopulation(): String {
        return when {
            population >= 1_000_000 -> "${String.format("%.1f", population / 1_000_000.0)}M"
            population >= 1_000 -> "${String.format("%.1f", population / 1_000.0)}K"
            else -> population.toString()
        }
    }
    
    fun getFormattedArea(): String? {
        return area?.let {
            val formatted = NumberFormat.getNumberInstance(Locale.getDefault()).format(it)
            "$formatted km²"
        }
    }
}