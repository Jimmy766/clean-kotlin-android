package jn.countries.clean.app.data.local.dao

import androidx.room.*
import jn.countries.clean.app.data.local.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {


    @Query("SELECT * FROM favorite_countries ORDER BY commonName ASC")
    fun getAllFavoriteCountries(): Flow<List<CountryEntity>>
    

    @Query("SELECT * FROM favorite_countries WHERE code = :countryCode")
    suspend fun getFavoriteCountryByCode(countryCode: String): CountryEntity?
    

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_countries WHERE code = :countryCode)")
    fun isCountryFavorite(countryCode: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteCountry(country: CountryEntity)

    @Query("DELETE FROM favorite_countries WHERE code = :countryCode")
    suspend fun deleteFavoriteCountry(countryCode: String)

    @Query("DELETE FROM favorite_countries")
    suspend fun deleteAllFavoriteCountries()

    @Query("SELECT COUNT(*) FROM favorite_countries")
    suspend fun getFavoriteCountriesCount(): Int
}