package jn.countries.clean.app.domain.usecase

import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow

class GetAllCountriesUseCase(
    private val countryRepository: CountryRepository
) {
    operator fun invoke(): Flow<List<Country>> {
        return countryRepository.getAllCountries()
    }
}