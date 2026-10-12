package com.example.habittracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Habit::class, Entry::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao

    companion object {
        fun create(context: Context): HabitDatabase =
            Room.databaseBuilder(context, HabitDatabase::class.java, "habits.db").build()
    }
}
