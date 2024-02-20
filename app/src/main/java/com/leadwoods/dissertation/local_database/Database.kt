package com.leadwoods.dissertation.local_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.leadwoods.dissertation.local_database.character.Character
import com.leadwoods.dissertation.local_database.character.CharacterDao
import com.leadwoods.dissertation.local_database.character.Converters

/**
 * Database class for managing the entities [Character] and [Asset].
 *
 * @property entities | An array of entity classes managed by the database.
 * @property version  | The version of the database schema.
 */
@Database(entities = [Character::class], version = 2)
@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {

    /**
     * Abstract method to retrieve the DAO interface for the [Character] entity.
     *
     * @return The DAO interface for the [Character] entity.
     */
    abstract fun CharacterDao(): CharacterDao

    companion object{

        @Volatile
        private var INSTANCE: LocalDatabase? = null


        /**
         * Gets the singleton instance of the [LocalDatabase].
         *
         * @param context | The application context.
         *
         * @return The singleton instance of the [LocalDatabase].
         */
        fun getInstance(context: Context): LocalDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "ExerciseDatabase"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}