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
        val indexedFavorites = favoriteCountries.associateBy { it.code }
        allCountries.forEach { country ->
          country.isFavorite = indexedFavorites[country.code] != null
        }
        allCountries

      }.collect {
        _uiState.value = _uiState.value.copy(
          countries = it,
          isLoading = false,
          error = null
        )
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
          if (it) {
            _uiFavoriteState.value = _uiFavoriteState.value.copy(
              code = country.code,
              isFavorite = true
            )
          }
          Log.d("CountriesViewModel", "addToFavorites: ${country.code}")
        }
    }
  }

  fun removeFromFavorites(countryCode: String) {
    viewModelScope.launch {
      removeFromFavoritesUseCase(countryCode)
        .collect {
          if (it) {
            _uiFavoriteState.value = _uiFavoriteState.value.copy(
              code = countryCode,
              isFavorite = false
            )
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
        _uiState.value = _uiState.value.copy(
          countries = result,
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
data class FavoriteCountryState(
  val code: String = "",
  val isFavorite: Boolean = false
)