package jn.countries.clean.app.presentation

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class CountriesApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}