package jn.countries.clean.app.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jn.countries.clean.app.presentation.navigation.CountriesNavigation
import jn.countries.clean.app.presentation.navigation.HomeBottomBar
import jn.countries.clean.app.presentation.theme.CountriesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      CountriesTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          MainScreen()
        }
      }
    }
  }

  @Composable
  private fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
      bottomBar = {HomeBottomBar(navController = navController)},
    ) {
      Box(modifier = Modifier.padding(it)){
        CountriesNavigation(navController = navController)
      }
    }

  }
}