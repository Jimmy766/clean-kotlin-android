package jn.countries.clean.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jn.countries.clean.app.data.remote.api.CountriesApiService
import jn.countries.clean.app.data.repository.CountryRepositoryImpl
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.usecase.GetAllCountriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CountriesViewModel (
) : ViewModel() {


    private val repository = CountryRepositoryImpl(getApiInstance())
    private val getAllCountriesUseCase: GetAllCountriesUseCase = GetAllCountriesUseCase(repository)
    private val _uiState = MutableStateFlow(CountriesUiState())
    val uiState: StateFlow<CountriesUiState> = _uiState.asStateFlow()

    init {
        loadCountries()
    }

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private fun getApiInstance(): CountriesApiService{
        return Retrofit.Builder()
            .baseUrl(CountriesApiService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CountriesApiService::class.java)
    }

    fun loadCountries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getAllCountriesUseCase()
                .catch {
                    exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar países"
                    )
                }
                .collect{
                countries ->
                    _uiState.value = _uiState.value.copy(
                        countries = countries,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
}
data class CountriesUiState(
    val countries: List<Country> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)