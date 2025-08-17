package jn.countries.clean.app.domain.repository

import jn.countries.clean.app.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    
    fun getAllCountries(): Flow<List<Country>>

    suspend fun searchCountries(query: String): Result<List<Country>>

    suspend fun getCountryByCode(countryCode: String): Result<Country?>

    fun getFavoriteCountries(): Flow<List<Country>>

    suspend fun addToFavorites(country: Country)

    suspend fun removeFromFavorites(countryCode: String)
    
    fun isCountryFavorite(countryCode: String): Flow<Boolean>
    
    suspend fun clearCache()
}