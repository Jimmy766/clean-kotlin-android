package jn.countries.clean.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.usecase.GetCountryByCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryDetailViewModel @Inject constructor(
  private val getCountryByCodeUseCase: GetCountryByCodeUseCase,
) : ViewModel() {

  private val _uiState = MutableStateFlow(CountryDetailUiState())
  val uiState: StateFlow<CountryDetailUiState> = _uiState.asStateFlow()

  fun loadCountryDetails(countryCode: String) {
    viewModelScope.launch {
      _uiState.value = _uiState.value.copy(isLoading = true, error = null)

      getCountryByCodeUseCase(countryCode)
        .catch { exception ->
          _uiState.value = _uiState.value.copy(
            isLoading = false,
            error = exception.message ?: "Error al cargar país"
          )
        }
        .collect { country ->
          _uiState.value = _uiState.value.copy(
            country = country,
            isLoading = false,
            error = null
          )
        }
    }
  }

  fun clearError() {
    _uiState.value = _uiState.value.copy(error = null)
  }
}

data class CountryDetailUiState(
  val country: Country? = null,
  val isLoading: Boolean = false,
  val error: String? = null
)