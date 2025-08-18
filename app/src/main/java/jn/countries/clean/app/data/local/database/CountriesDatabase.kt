package jn.countries.clean.app.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import jn.countries.clean.app.data.local.dao.CountryDao
import jn.countries.clean.app.data.local.entity.CountryEntity

@Database(
    entities = [CountryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CountriesDatabase : RoomDatabase() {
    
    abstract fun countryDao(): CountryDao
    
    companion object {
        const val DATABASE_NAME = "countries_database"
        
        @Volatile
        private var INSTANCE: CountriesDatabase? = null
        
        fun getDatabase(context: Context): CountriesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountriesDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}