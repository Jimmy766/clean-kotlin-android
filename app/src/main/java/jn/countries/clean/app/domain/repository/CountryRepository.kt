package jn.countries.clean.app.domain.repository

import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getAllCountries(): Flow<Resource<List<Country>>>
    fun searchCountries(query: String): Flow<Resource<List<Country>>>
    fun getCountryByCode(code: String): Flow<Resource<Country?>>
    fun getFavoriteCountries(): Flow<Resource<List<Country>>>
    fun addToFavorites(country: Country): Flow<Resource<Boolean>>
    fun removeFromFavorites(countryCode: String): Flow<Resource<Boolean>>
    fun isCountryFavorite(countryCode: String): Flow<Boolean>
    suspend fun clearCache()
}
