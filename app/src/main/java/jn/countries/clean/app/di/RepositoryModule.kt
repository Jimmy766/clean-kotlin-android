package jn.countries.clean.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jn.countries.clean.app.data.repository.CountryRepositoryImpl
import jn.countries.clean.app.domain.repository.CountryRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

  @Binds
  @Singleton
  abstract fun bindCountryRepository(
    countryRepositoryImpl: CountryRepositoryImpl
  ): CountryRepository
}