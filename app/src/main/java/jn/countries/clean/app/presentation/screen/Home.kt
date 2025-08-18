package jn.countries.clean.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import jn.countries.clean.app.R
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.presentation.viewmodel.CountriesViewModel


@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  onCountryClick: (String) -> Unit,
  viewModel: CountriesViewModel = hiltViewModel()
) {

  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val uiFavoriteState by viewModel.uiFavoriteState.collectAsStateWithLifecycle()


  val countries = remember(uiState.countries, uiFavoriteState) {
    uiState.countries.map { country ->
      if (country.code == uiFavoriteState.code) {
        country.copy(isFavorite = uiFavoriteState.isFavorite)
      } else {
        country
      }
    }
  }


  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Text(
      text = "Countries",
      style = MaterialTheme.typography.headlineMedium,
      fontWeight = FontWeight.Bold,
      modifier = Modifier
        .padding(top = 16.dp)
        .align(Alignment.CenterHorizontally)
    )
    InputSearchBar(
      query = searchQuery,
      onQueryChange = { searchQuery = it },
      modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.size(16.dp))

    when {
      uiState.isLoading -> {
        LoadingContent()
      }

      uiState.error != null -> {
        ErrorContent(
          error = uiState.error!!,
          onRetry = { viewModel.loadCountries() }
        )
      }

      uiState.countries.isEmpty() && searchQuery.isNotEmpty() -> {
        EmptySearchContent()
      }

      else -> {

        CountriesList(
          countries = countries,
          modifier = modifier,
          onCountryClick = { countryCode ->
            onCountryClick(countryCode)
          },
          onFavoriteClick = { country ->
            if (country.isFavorite) {
              viewModel.removeFromFavorites(country.code)
            } else {
              viewModel.addToFavorites(country)
            }
          }
        )
      }

    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputSearchBar(
  query: String,
  onQueryChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  onExpandedChange: (Boolean) -> Unit = { /* No-op */ }
) {
  SearchBar(
    modifier = modifier,
    expanded = false,
    onExpandedChange = onExpandedChange,
    inputField = {
      InputField(
        query = query,
        onQueryChange = onQueryChange,
        placeholder = {
          Text("Buscar países...")
        },
        leadingIcon = {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Buscar"
          )
        },
        trailingIcon = {
          if (query.isNotEmpty()) {
            IconButton(
              onClick = { onQueryChange("") }
            ) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Limpiar búsqueda"
              )
            }
          }
        },
        onSearch = onQueryChange,
        expanded = false,
        onExpandedChange = onExpandedChange,
      )},
    shape = RoundedCornerShape(12.dp)
  ){

  }
}

@Composable
private fun CountriesList(
  modifier: Modifier = Modifier,
  countries: List<Country>,
  onCountryClick: (String) -> Unit,
  onFavoriteClick: (Country) -> Unit
) {
  LazyColumn(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(items = countries, key = {it.code})
    { country ->
      CountryItem(
        country = country,
        onClick = { onCountryClick(country.code) },
        onFavoriteClick = { onFavoriteClick(country) }
      )
    }
  }
}

@Composable
private fun CountryItem(
  country: Country,
  onClick: () -> Unit,
  onFavoriteClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.onSurface
    ),
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AsyncImage (
        model = ImageRequest.Builder(LocalContext.current)
          .data(country.flags.svg)
          .decoderFactory(SvgDecoder.Factory())
          .crossfade(true)
          .build(),
        contentDescription = "Bandera de ${country.name.common}",
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.Fit,
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = country.name.official,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Text(
          text = country.name.common,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Text(
          text = "Capital: ${country.capital?.firstOrNull() ?: "N/A"}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      IconButton(
        onClick = onFavoriteClick,
        modifier = Modifier.size(30.dp)
      ) {
        Icon(
          imageVector = if (country.isFavorite) {
            ImageVector.vectorResource(id = R.drawable.fav_filled)
          } else {
            ImageVector.vectorResource(id = R.drawable.fav_outline)
          },
          contentDescription = if (country.isFavorite) {
            "Eliminar de favoritos"
          } else {
            "Añadir a favoritos"
          },
          tint = Color.Unspecified
        )
      }
    }
  }
}

@Composable
private fun LoadingContent() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      CircularProgressIndicator()
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "Cargando países...",
        style = MaterialTheme.typography.bodyLarge
      )
    }
  }
}

@Composable
private fun ErrorContent(
  error: String,
  onRetry: () -> Unit
) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Error: $error",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error
      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(
        onClick = onRetry
      ) {
        Text("Reintentar")
      }
    }
  }
}

@Composable
private fun EmptySearchContent() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = "No se encontraron países",
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}