package jn.countries.clean.app.domain.usecase

import jn.countries.clean.app.domain.repository.CountryRepository
import jn.countries.clean.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
  private val countryRepository: CountryRepository
) {
  operator fun invoke(countryCode: String):Flow<Resource<Boolean>> {
    return countryRepository.removeFromFavorites(countryCode.trim().uppercase())
  }
}