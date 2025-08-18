package jn.countries.clean.app.domain.usecase

import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.repository.CountryRepository
import jn.countries.clean.app.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFavoriteCountriesUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) {

    operator fun invoke(): Flow<Resource<List<Country>>> {
        return countryRepository.getFavoriteCountries().flowOn(Dispatchers.IO)
    }
}