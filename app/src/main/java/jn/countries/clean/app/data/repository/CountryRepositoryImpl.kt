package jn.countries.clean.app.data.repository

import jn.countries.clean.app.data.remote.api.CountriesApiService
import jn.countries.clean.app.data.remote.dto.toDomain
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryRepositoryImpl (
    private val apiService: CountriesApiService,
) : CountryRepository {
    
    override fun getAllCountries():Flow<List<Country>> = flow {
        val result = apiService.getAllCountries()
        if(result.isSuccessful && result.body() != null) {
            val dtos = result.body()!!
            emit(dtos.map { it.toDomain() })
        } else {
            emit(emptyList<Country>())
        }
    }
    
    override suspend fun searchCountries(query: String): Result<List<Country>> {
        TODO("Not yet implemented")
    }
    
    override suspend fun getCountryByCode(code: String): Result<Country?> {
        TODO("Not yet implemented")
    }
    
    override fun getFavoriteCountries(): Flow<List<Country>> {
        TODO("Not yet implemented")
    }
    
    override suspend fun addToFavorites(country: Country) {
        TODO("Not yet implemented")
    }
    
    override suspend fun removeFromFavorites(countryCode: String) {
        TODO("Not yet implemented")
    }
    
    override fun isCountryFavorite(countryCode: String): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun clearCache() {
        TODO("Not yet implemented")
    }
}