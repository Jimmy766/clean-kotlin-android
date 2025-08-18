package jn.countries.clean.app.di

import android.content.Context
import androidx.room.Room
import jn.countries.clean.app.data.local.dao.CountryDao
import jn.countries.clean.app.data.local.database.CountriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideCountriesDatabase(
        @ApplicationContext context: Context
    ): CountriesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CountriesDatabase::class.java,
            CountriesDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCountryDao(database: CountriesDatabase): CountryDao {
        return database.countryDao()
    }
}