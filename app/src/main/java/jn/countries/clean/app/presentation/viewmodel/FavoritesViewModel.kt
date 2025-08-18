package jn.countries.clean.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.domain.usecase.GetFavoriteCountriesUseCase
import jn.countries.clean.app.domain.usecase.RemoveFromFavoritesUseCase
import jn.countries.clean.app.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteCountriesUseCase: GetFavoriteCountriesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        loadFavoriteCountries()
    }

    private fun loadFavoriteCountries() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getFavoriteCountriesUseCase()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al cargar favoritos"
                    )
                }
                .collect { favoriteCountries ->
                    when (favoriteCountries) {
                        is Resource.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = null,
                                favoriteCountries = favoriteCountries.data
                            )
                            return@collect
                        }
                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = favoriteCountries.message
                            )
                            return@collect
                        }
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                        }
                    }
                }
        }
    }

    fun removeFromFavorites(countryCode: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            removeFromFavoritesUseCase(countryCode)
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Error al eliminar de favoritos"
                    )
                }.collect {
                    loadFavoriteCountries()
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun refresh() {
        loadFavoriteCountries()
    }
}

data class FavoritesUiState(
    val favoriteCountries: List<Country> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val isEmpty: Boolean
        get() = favoriteCountries.isEmpty() && !isLoading
}