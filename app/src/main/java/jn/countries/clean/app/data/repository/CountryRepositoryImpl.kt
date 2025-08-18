package jn.countries.clean.app.data.repository

import jn.countries.clean.app.data.local.dao.CountryDao
import jn.countries.clean.app.data.local.entity.toDomain
import jn.countries.clean.app.data.local.entity.toEntity
import jn.countries.clean.app.data.remote.api.CountriesApiService
import jn.countries.clean.app.data.remote.dto.toDomain
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.repository.CountryRepository
import jn.countries.clean.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CountryRepositoryImpl @Inject constructor(
    private val apiService: CountriesApiService,
    private val countryDao: CountryDao
) : CountryRepository {
    
    override fun getAllCountries(): Flow<Resource<List<Country>>> = flow {
        emit(Resource.Loading())
        try {
            val result = apiService.getAllCountries()
            if(result.isSuccessful && result.body() != null) {
                val countries = result.body()!!.map { it.toDomain() }
                emit(Resource.Success(countries))
            } else {
                emit(Resource.Error("Error: ${result.code()} ${result.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión con el servidor"))
        } catch (e: IOException) {
            emit(Resource.Error("No se pudo conectar al servidor. Verifica tu conexión a internet"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    override fun searchCountries(query: String): Flow<Resource<List<Country>>> = flow {
        emit(Resource.Loading())
        try {
            val result = apiService.searchCountriesByName(query)
            if(result.isSuccessful && result.body() != null) {
                val countries = result.body()!!.map { it.toDomain() }
                emit(Resource.Success(countries))
            } else {
                emit(Resource.Error("Error: ${result.code()} ${result.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión con el servidor"))
        } catch (e: IOException) {
            emit(Resource.Error("No se pudo conectar al servidor. Verifica tu conexión a internet"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    override fun getCountryByCode(code: String): Flow<Resource<Country?>> = flow {
        emit(Resource.Loading())
        try {
            val result = apiService.getCountryByCode(code)
            if(result.isSuccessful && result.body() != null) {
                val country = result.body()!!.firstOrNull()?.toDomain()
                emit(Resource.Success(country))
            } else {
                emit(Resource.Error("Error: ${result.code()} ${result.message()}"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error("Error de conexión con el servidor"))
        } catch (e: IOException) {
            emit(Resource.Error("No se pudo conectar al servidor. Verifica tu conexión a internet"))
        } catch (e: Exception) {
            emit(Resource.Error("Error inesperado: ${e.localizedMessage}"))
        }
    }
    
    override fun getFavoriteCountries(): Flow<Resource<List<Country>>> = flow {
        emit(Resource.Loading())
        try {
            val favoriteCountries = countryDao.getAllFavoriteCountries()
                .map { entities -> entities.map { it.toDomain() } }
                .first()
            emit(Resource.Success(favoriteCountries))
        } catch (e: Exception) {
            emit(Resource.Error("Error al obtener países favoritos: ${e.localizedMessage}"))
        }
    }
    
    override fun addToFavorites(country: Country): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            val entity = country.toEntity()
            countryDao.insertFavoriteCountry(entity)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error("Error al agregar a favoritos: ${e.localizedMessage}"))
        }
    }

    override fun removeFromFavorites(countryCode: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading())
        try {
            countryDao.deleteFavoriteCountry(countryCode)
            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error("Error al eliminar de favoritos: ${e.localizedMessage}"))
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

