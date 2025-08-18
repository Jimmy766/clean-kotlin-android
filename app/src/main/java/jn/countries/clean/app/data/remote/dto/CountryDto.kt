package jn.countries.clean.app.data.remote.dto

import com.google.gson.annotations.SerializedName
import jn.countries.clean.app.domain.model.Car
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.model.CountryCoatOfArms
import jn.countries.clean.app.domain.model.CountryFlags
import jn.countries.clean.app.domain.model.CountryName
import jn.countries.clean.app.domain.model.Currency
import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    @SerializedName("cca2")
    val code: String,
    @SerializedName("name")
    val name: CountryNameDto,
    @SerializedName("flags")
    val flags: CountryFlagsDto,
    @SerializedName("population")
    val population: Long,
    @SerializedName("region")
    val region: String,
    @SerializedName("subregion")
    val subregion: String? = null,
    @SerializedName("capital")
    val capital: List<String>? = null,
    @SerializedName("area")
    val area: Double? = null,
    @SerializedName("languages")
    val languages: Map<String, String>? = null,
    @SerializedName("currencies")
    val currencies: Map<String, CurrencyDto>? = null,
    @SerializedName("independent")
    val independent: Boolean = false,
    @SerializedName("unMember")
    val unMember: Boolean = false,
    @SerializedName("timezones")
    val timezones: List<String>? = null,
    @SerializedName("coatOfArms")
    val coatOfArms : CountryCoatOfArmsDto? = null,
    @SerializedName("car")
    val car: CarDto? = null,
)

@Serializable
data class CountryCoatOfArmsDto(
    @SerializedName("png")
    val png: String? = null,
    @SerializedName("svg")
    val svg: String? = null
)

@Serializable
data class CarDto(
    @SerializedName("side")
    val side: String? = null,
    @SerializedName("signs")
    val signs: List<String> = emptyList()
)

@Serializable
data class CountryNameDto(
    @SerializedName("common")
    val common: String,
    @SerializedName("official")
    val official: String
)

@Serializable
data class CountryFlagsDto(
    @SerializedName("png")
    val png: String,
    @SerializedName("svg")
    val svg: String
)

@Serializable
data class CurrencyDto(
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String? = null
)

fun CountryDto.toDomain(): Country {
    return Country(
        code = code,
        name = CountryName(
            common = name.common,
            official = name.official
        ),
        flags = CountryFlags(
            png = flags.png,
            svg = flags.svg
        ),
        population = population,
        region = region,
        subregion = subregion,
        capital = capital,
        area = area,
        languages = languages,
        currencies = currencies?.mapValues { (_, currencyDto) ->
            Currency(
                name = currencyDto.name,
                symbol = currencyDto.symbol
            )
        },
        independent = independent,
        unMember = unMember,
        timezones = timezones,
        coatOfArms = coatOfArms?.let {
            CountryCoatOfArms(
                png = it.png,
                svg = it.svg
            )
        },
        car = car?.let {
            Car(
                side = it.side,
                signs = it.signs
            )
        }
    )
}