package jn.countries.clean.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jn.countries.clean.app.presentation.screen.CountryDetailScreen
import jn.countries.clean.app.presentation.screen.HomeScreen

@Composable
fun CountriesNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = CountriesDestinations.COUNTRIES_LIST
    ) {

        composable(CountriesDestinations.COUNTRIES_LIST) {
            HomeScreen(
                onCountryClick = { countryCode ->
                    navController.navigate(
                        CountriesDestinations.countryDetail(countryCode)
                    )
                }
            )
        }

        composable(
            route = CountriesDestinations.COUNTRY_DETAIL,
            arguments = CountriesDestinations.countryDetailArguments
        ) { backStackEntry ->
            val countryCode = backStackEntry.arguments?.getString("countryCode") ?: ""
            CountryDetailScreen(
                countryCode = countryCode,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}