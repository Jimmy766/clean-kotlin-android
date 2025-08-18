package jn.countries.clean.app.presentation.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

object CountriesDestinations {

  const val COUNTRIES_LIST = "countries_list"
  const val COUNTRY_DETAIL = "country_detail/{countryCode}"
  const val COUNTRY_FAVORITES = "country_favorites"

  val countryDetailArguments: List<NamedNavArgument> = listOf(
    navArgument("countryCode") {
      type = NavType.StringType
      nullable = false
    }
  )

  fun countryDetail(countryCode: String): String {
    return "country_detail/$countryCode"
  }
}