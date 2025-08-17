package jn.countries.clean.app.data.remote.api

import jn.countries.clean.app.data.remote.dto.CountryDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface CountriesApiService {

    @GET("all?fields=cca2,name,flags,population,region,subregion,capital,area,languages,currencies")
    suspend fun getAllCountries(): Response<List<CountryDto>>
    
    @GET("name/{name}?fields=cca2,name,flags,population,region,subregion,capital,area,languages,currencies")
    suspend fun searchCountriesByName(
        @Path("name") name: String
    ): Response<List<CountryDto>>
    
    @GET("alpha/{code}?fields=cca2,name,flags,population,region,subregion,capital,area,languages,currencies")
    suspend fun getCountryByCode(
        @Path("code") code: String
    ): Response<List<CountryDto>>
    
    @GET("region/{region}?fields=cca2,name,flags,population,region,subregion,capital,area,languages,currencies")
    suspend fun getCountriesByRegion(
        @Path("region") region: String
    ): Response<List<CountryDto>>
    
    companion object {
        const val BASE_URL = "https://restcountries.com/v3.1/"
    }
}