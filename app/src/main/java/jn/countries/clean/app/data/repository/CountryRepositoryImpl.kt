package jn.countries.clean.app.data.repository

import jn.countries.clean.app.data.local.dao.CountryDao
import jn.countries.clean.app.data.local.entity.toDomain
import jn.countries.clean.app.data.local.entity.toEntity
import jn.countries.clean.app.data.remote.api.CountriesApiService
import jn.countries.clean.app.data.remote.dto.toDomain
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepositoryImpl @Inject constructor(
    private val apiService: CountriesApiService,
     private val countryDao: CountryDao
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
    
    override fun getCountryByCode(code: String): Flow<Country?> = flow{
        val result = apiService.getCountryByCode(code)
        if(result.isSuccessful && result.body() != null) {
            val dtos = result.body()!!
            emit(dtos.firstOrNull()?.toDomain())
        } else {
            emit(null)
        }
    }
    
    override fun getFavoriteCountries(): Flow<List<Country>> = flow {
        val favoriteCountries = countryDao.getAllFavoriteCountries()
            .map { it.map { entity -> entity.toDomain() } }
        emit(favoriteCountries.first())
    }
    
    override fun addToFavorites(country: Country): Flow<Boolean> = flow {
        val entity = country.toEntity()
        countryDao.insertFavoriteCountry(entity)
        emit(true)
    }
    
    override fun removeFromFavorites(countryCode: String): Flow<Boolean> = flow {
        val existingCountry = countryDao.getFavoriteCountryByCode(countryCode)
        if (existingCountry != null) {
            countryDao.deleteFavoriteCountry(countryCode)
            emit(true)
        } else {
            emit(false)
        }
    }
    
    override fun isCountryFavorite(countryCode: String): Flow<Boolean> = flow {
        val favoriteCountry = countryDao.getFavoriteCountryByCode(countryCode)
        emit(favoriteCountry != null)
    }

    override suspend fun clearCache() {
        TODO("Not yet implemented")
    }
}