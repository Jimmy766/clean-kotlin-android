package jn.countries.clean.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jn.countries.clean.app.domain.model.Country
import jn.countries.clean.app.presentation.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
  onCountryClick: (String) -> Unit,
  viewModel: FavoritesViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text("Países Favoritos")
        }
      )
    },
    floatingActionButton = {
      if (!uiState.isEmpty && !uiState.isLoading) {
        ExtendedFloatingActionButton(
          onClick = { viewModel.refresh() },
          icon = {
            Icon(
              imageVector = Icons.Default.FavoriteBorder,
              contentDescription = "Actualizar"
            )
          },
          text = { Text("Actualizar") }
        )
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when {
        uiState.isLoading -> {
          LoadingContent()
        }

        uiState.error != null -> {
          ErrorContent(
            error = uiState.error!!,
            onRetry = { viewModel.refresh() },
            onDismissError = { viewModel.clearError() }
          )
        }

        uiState.isEmpty -> {
          EmptyFavoritesContent()
        }

        else -> {
          FavoritesList(
            favorites = uiState.favoriteCountries,
            onCountryClick = onCountryClick,
            onRemoveFromFavorites = { countryCode ->
              viewModel.removeFromFavorites(countryCode)
            },
            modifier = Modifier.padding(16.dp)
          )
        }
      }
    }
  }
}

@Composable
private fun FavoritesList(
  favorites: List<Country>,
  onCountryClick: (String) -> Unit,
  onRemoveFromFavorites: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    item {
      Text(
        text = "${favorites.size} países favoritos",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp)
      )
    }

    items(
      items = favorites,
      key = { it.code }
    ) { country ->
      FavoriteCountryItem(
        country = country,
        onClick = { onCountryClick(country.code) },
        onRemoveClick = { onRemoveFromFavorites(country.code) }
      )
    }
  }
}

@Composable
private fun FavoriteCountryItem(
  country: Country,
  onClick: () -> Unit,
  onRemoveClick: () -> Unit
) {
  var showDeleteDialog by remember { mutableStateOf(false) }

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
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(country.flags.png)
          .crossfade(true)
          .build(),
        contentDescription = "Bandera de ${country.name.common}",
        modifier = Modifier
          .size(56.dp)
          .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = country.name.common,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Text(
          text = country.name.official,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Text(
            text = "Capital: ${country.capital?.firstOrNull() ?: "N/A"}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          country.population?.let { population ->
            Text(
              text = "Población: ${formatPopulation(population)}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }


      IconButton(
        onClick = { showDeleteDialog = true }
      ) {
        Icon(
          imageVector = Icons.Default.Delete,
          contentDescription = "Eliminar de favoritos",
          tint = MaterialTheme.colorScheme.error
        )
      }
    }
  }

  if (showDeleteDialog) {
    AlertDialog(
      onDismissRequest = { showDeleteDialog = false },
      title = {
        Text("Eliminar de favoritos")
      },
      text = {
        Text("¿Estás seguro de que quieres eliminar ${country.name.common} de tus favoritos?")
      },
      confirmButton = {
        TextButton(
          onClick = {
            onRemoveClick()
            showDeleteDialog = false
          }
        ) {
          Text(
            "Eliminar",
            color = MaterialTheme.colorScheme.error
          )
        }
      },
      dismissButton = {
        TextButton(
          onClick = { showDeleteDialog = false }
        ) {
          Text("Cancelar")
        }
      }
    )
  }
}

@Composable
private fun EmptyFavoritesContent() {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(32.dp)
    ) {
      Icon(
        imageVector = Icons.Default.FavoriteBorder,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "No tienes países favoritos",
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "Explora la lista de países y marca tus favoritos tocando el ícono de corazón",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
      )
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
        text = "Cargando favoritos...",
        style = MaterialTheme.typography.bodyLarge
      )
    }
  }
}

@Composable
private fun ErrorContent(
  error: String,
  onRetry: () -> Unit,
  onDismissError: () -> Unit
) {
  Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(16.dp)
    ) {
      Text(
        text = "Error: $error",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = onDismissError
        ) {
          Text("Cerrar")
        }

        Button(
          onClick = onRetry
        ) {
          Text("Reintentar")
        }
      }
    }
  }
}

private fun formatPopulation(population: Long): String {
  return when {
    population >= 1_000_000 -> "${population / 1_000_000}M"
    population >= 1_000 -> "${population / 1_000}K"
    else -> population.toString()
  }
}