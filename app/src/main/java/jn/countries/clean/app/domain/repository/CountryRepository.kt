package jn.countries.clean.app.domain.repository

import jn.countries.clean.app.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    
    fun getAllCountries(): Flow<List<Country>>

    fun searchCountries(query: String): Flow<List<Country>>

    fun getCountryByCode(countryCode: String): Flow<Country?>

    fun getFavoriteCountries(): Flow<List<Country>>

    fun addToFavorites(country: Country): Flow<Boolean>

    fun removeFromFavorites(countryCode: String):Flow<Boolean>
    
    fun isCountryFavorite(countryCode: String): Flow<Boolean>
    
    suspend fun clearCache()
}