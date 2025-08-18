package jn.countries.clean.app.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeBottomBar(
  navController: NavHostController
){
  BottomAppBar {
    NavigationBar {
      NavigationBarItem(
        selected = true,
        onClick = { navController.navigate(
          CountriesDestinations.COUNTRIES_LIST
        )  },
        label = {
            Text(
                text = "Overview",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        icon = {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Countries",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      )

      NavigationBarItem(
        selected = true,
        onClick = {
          navController.navigate(CountriesDestinations.COUNTRY_FAVORITES)
        },
        label = {
          Text(
            text = "Favorites",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
          )
        },
        icon = {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = "Countries",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      )
    }
  }

}