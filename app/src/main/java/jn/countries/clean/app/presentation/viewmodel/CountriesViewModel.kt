package jn.countries.clean.app.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.usecase.AddToFavoritesUseCase
import jn.countries.clean.app.domain.usecase.GetAllCountriesUseCase
import jn.countries.clean.app.domain.usecase.GetFavoriteCountriesUseCase
import jn.countries.clean.app.domain.usecase.RemoveFromFavoritesUseCase
import jn.countries.clean.app.domain.usecase.SearchCountriesUseCase
import jn.countries.clean.app.domain.util.Resource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class CountriesViewModel @Inject constructor(
  private val getAllCountriesUseCase: GetAllCountriesUseCase,
  private val getFavoriteCountriesUseCase: GetFavoriteCountriesUseCase,
  private val addToFavoritesUseCase: AddToFavoritesUseCase,
  private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
  private val searchCountriesUseCase: SearchCountriesUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(CountriesUiState())
  val uiState: StateFlow<CountriesUiState> = _uiState.asStateFlow()

  private val _uiFavoriteState = MutableStateFlow(FavoriteCountryState())
  val uiFavoriteState: StateFlow<FavoriteCountryState> = _uiFavoriteState.asStateFlow()

  private val _uiSearchState = MutableStateFlow("")
  val uiSearchState: StateFlow<String> = _uiSearchState.asStateFlow()

  init {
    viewModelScope.launch {
      _uiSearchState.debounce(500L).distinctUntilChanged().collectLatest {
        if (it.isNotBlank()) {
          searchCountries(it)
        } else {
          loadCountries()
        }
      }
    }
  }

  fun loadCountries() {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true, error = null)
      getFavoriteCountriesUseCase().zip(getAllCountriesUseCase()) { favoriteCountries, allCountries ->
        if (favoriteCountries !is Resource.Success) {
          Log.e("CountriesViewModel", "loadCountries: Error fetching favorite countries")
          return@zip allCountries
        }
        val indexedFavorites = favoriteCountries.data.associateBy { it.code }
        when (allCountries) {
            is Resource.Success -> {
              allCountries.data.forEach { country ->
                country.isFavorite = indexedFavorites[country.code] != null
              }
                Log.d("CountriesViewModel", "loadCountries: Success")
            }
            else -> {
                Log.e("CountriesViewModel", "loadCountries: Error ")
            }
        }
        allCountries

      }.collect {

        when (it) {
            is Resource.Error -> {
                _uiState.value = _uiState.value.copy(
                countries = emptyList(),
                isLoading = false,
                error = it.message
                )
                Log.e("CountriesViewModel", "loadCountries: ${it.message}")
            }
            is Resource.Loading -> {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            }
            is Resource.Success -> {
              _uiState.value = _uiState.value.copy(
                countries = it.data,
                isLoading = false,
                error = null
              )
                Log.d("CountriesViewModel", "loadCountries: Success")
            }
        }

      }
    }
  }

  fun setSearchQuery(query: String) {
    _uiSearchState.value = query
  }

  fun addToFavorites(country: Country) {
    viewModelScope.launch {
      addToFavoritesUseCase(country)
        .collect {
          when (it) {
            is Resource.Error -> {
              Log.e("CountriesViewModel", "addToFavorites: ${it.message}")
            }
            is Resource.Loading -> {
              Log.d("CountriesViewModel", "addToFavorites: Loading")
            }
            is Resource.Success -> {
              _uiFavoriteState.value = _uiFavoriteState.value.copy(
                code = country.code,
                isFavorite = true
              )
              Log.d("CountriesViewModel", "addToFavorites: Success")
            }
          }
          Log.d("CountriesViewModel", "addToFavorites: ${country.code}")
        }
    }
  }

  fun removeFromFavorites(countryCode: String) {
    viewModelScope.launch {
      removeFromFavoritesUseCase(countryCode)
        .collect {
            when (it) {
                is Resource.Error -> {
                Log.e("CountriesViewModel", "removeFromFavorites: ${it.message}")
                }
                is Resource.Loading -> {
                Log.d("CountriesViewModel", "removeFromFavorites: Loading")
                }
                is Resource.Success -> {
                  _uiFavoriteState.value = _uiFavoriteState.value.copy(
                    code = countryCode,
                    isFavorite = false
                  )
                Log.d("CountriesViewModel", "removeFromFavorites: Success")
                }
            }
          Log.d("CountriesViewModel", "removeFromFavorites: $countryCode")
        }
    }
  }

  fun searchCountries(query: String) {
    viewModelScope.launch {
      if (query.isBlank()) {
        loadCountries()
        return@launch
      }
      _uiState.value = _uiState.value.copy(isLoading = true, error = null)
      searchCountriesUseCase(query).collect { result ->

        when (result) {
            is Resource.Error -> {
                _uiState.value = _uiState.value.copy(
                countries = emptyList(),
                isLoading = false,
                error = result.message
                )
                Log.e("CountriesViewModel", "searchCountries: ${result.message}")
            }
            is Resource.Loading -> {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                Log.d("CountriesViewModel", "searchCountries: Loading")
            }
            is Resource.Success -> {
              _uiState.value = _uiState.value.copy(
                countries = result.data,
                isLoading = false,
                error = null
              )
                Log.d("CountriesViewModel", "searchCountries: Success")
            }
        }
      }
    }
  }
}

data class CountriesUiState(
  val countries: List<Country> = emptyList(),
  val isLoading: Boolean = false,
  val error: String? = null
)
data class FavoriteCountryState(
  val code: String = "",
  val isFavorite: Boolean = false
)