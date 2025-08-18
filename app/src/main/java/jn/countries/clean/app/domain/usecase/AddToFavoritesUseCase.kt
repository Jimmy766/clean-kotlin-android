package jn.countries.clean.app.domain.usecase

import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.repository.CountryRepository
import jn.countries.clean.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToFavoritesUseCase @Inject constructor(
  private val countryRepository: CountryRepository
) {

  operator fun invoke(country: Country): Flow<Resource<Boolean>> {
    return countryRepository.addToFavorites(country)
  }
}