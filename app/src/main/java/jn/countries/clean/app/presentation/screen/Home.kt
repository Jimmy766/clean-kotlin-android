package jn.countries.clean.app.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class Mock(
  val id: String,
  val name: String,
  val capital: String?,
  val isFavorite: Boolean = false)

val mocks =
    (1..20).map {
        Mock(
            id = it.toString(),
            name = "Country $it",
            capital = "Capital $it",
            isFavorite = it % 2 == 0
        )
    }


@Composable
fun HomeScreen(modifier: Modifier) {

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
      modifier = Modifier.padding(top= 8.dp)
        .align(Alignment.CenterHorizontally)
    )
    InputSearchBar(
      query = searchQuery,
      onQueryChange = { searchQuery = it },
      modifier = Modifier.fillMaxWidth()
    )
    CountriesList(
      countries = mocks,
      modifier = modifier
    )

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
  countries: List<Mock>,
) {
  LazyColumn(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    items(items = countries, key = {it.id})
    { country ->
      CountryItem(
        country = country,
        onClick = {  },
        onFavoriteClick = { }
      )
    }
  }
}

@Composable
private fun CountryItem(
  country: Mock,
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
      Box (
        modifier = Modifier
          .size(48.dp)
          .clip(RoundedCornerShape(4.dp)),
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = country.name,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Text(
          text = country.name,
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
        onClick = onFavoriteClick
      ) {
        Icon(
          imageVector = if (country.isFavorite) {
            Icons.Default.Favorite
          } else {
            Icons.Default.FavoriteBorder
          },
          contentDescription = if (country.isFavorite) {
            "Eliminar de favoritos"
          } else {
            "Añadir a favoritos"
          },
          tint = if (country.isFavorite) {
            MaterialTheme.colorScheme.error
          } else {
            MaterialTheme.colorScheme.onSurfaceVariant
          }
        )
      }
    }
  }
}