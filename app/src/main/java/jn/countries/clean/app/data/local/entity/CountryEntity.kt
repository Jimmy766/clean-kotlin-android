package jn.countries.clean.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import jn.countries.clean.app.domain.model.Car
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.model.CountryCoatOfArms
import jn.countries.clean.app.domain.model.CountryFlags
import jn.countries.clean.app.domain.model.CountryName
import jn.countries.clean.app.domain.model.Currency
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

@Entity(tableName = "favorite_countries")
data class CountryEntity(
    @PrimaryKey
    val code: String,
    val commonName: String,
    val officialName: String,
    val flagPng: String,
    val flagSvg: String,
    val population: Long,
    val region: String,
    val subregion: String?,
    val capital: String?,
    val area: Double?,
    val languages: String?,
    val currencies: String?,
    val independent: Boolean = false,
    val unMember: Boolean = false,
    val timezones: String? = null,
    val coatOfArmsPng : String? = null,
    val coatOfArmsSvg : String? = null,
    val carSide: String? = null,
    val carSigns: String? = null
)

fun CountryEntity.toDomain(): Country {
    return Country(
        code = code,
        name = CountryName(
            common = commonName,
            official = officialName
        ),
        flags = CountryFlags(
            png = flagPng,
            svg = flagSvg
        ),
        population = population,
        region = region,
        subregion = subregion,
        capital = capital?.let {
            try {
                Json.decodeFromString<List<String>>(it)
            } catch (e: Exception) {
                null
            }
        },
        area = area,
        languages = languages?.let {
            try {
                Json.decodeFromString<Map<String, String>>(it)
            } catch (e: Exception) {
                null
            }
        },
        currencies = currencies?.let {
            try {
                Json.decodeFromString<Map<String, Currency>>(it)
            } catch (e: Exception) {
                null
            }
        },
        independent = independent,
        unMember = unMember,
        timezones = timezones?.let {
            try {
                Json.decodeFromString<List<String>>(it)
            } catch (e: Exception) {
                null
            }
        },
        coatOfArms = coatOfArmsPng?.let { png ->
            coatOfArmsSvg?.let { svg ->
                CountryCoatOfArms(png = png, svg = svg)
            }
        },
        car = Car(
            side = carSide,
            signs = carSigns?.let {
                    Json.decodeFromString<List<String>>(it)
            }?: emptyList()
        )
    )
}

fun Country.toEntity(): CountryEntity {
    return CountryEntity(
        code = code,
        commonName = name.common,
        officialName = name.official,
        flagPng = flags.png,
        flagSvg = flags.svg,
        population = population,
        region = region,
        subregion = subregion,
        capital = capital?.let { 
            try {
                Json.encodeToString(ListSerializer(String.serializer()),it)
                
            } catch (e: Exception) {
                null
            }
        },
        area = area,
        languages = languages?.let {
            try {
                Json.encodeToString(MapSerializer(String.serializer(), String.serializer()),it)
            } catch (e: Exception) {
                null
            }
        },
        currencies = currencies?.let {
            try {
                Json.encodeToString(MapSerializer(String.serializer(), Currency.serializer()), it)
            } catch (e: Exception) {
                null
            }
        },
        independent = independent,
        unMember = unMember,
        timezones = timezones?.let {
            try {
                Json.encodeToString(ListSerializer(String.serializer()), it)
            } catch (e: Exception) {
                null
            }
        },
        coatOfArmsPng = coatOfArms?.png,
        coatOfArmsSvg = coatOfArms?.svg,
        carSide = car?.side,
        carSigns = car?.signs?.let {
            try {
                Json.encodeToString(ListSerializer(String.serializer()), it)
            } catch (e: Exception) {
                null
            }
        }
    )
}