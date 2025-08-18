package jn.countries.clean.app.domain.usecase

import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.repository.CountryRepository
import jn.countries.clean.app.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class GetCountryByCodeUseCase @Inject constructor(
  private val countryRepository: CountryRepository
) {

  operator fun invoke(countryCode: String): Flow<Resource<Country?>> {
    return countryRepository.getCountryByCode(countryCode.trim().uppercase()).flowOn(Dispatchers.IO)
  }
}