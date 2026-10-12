package com.example.habittracker

import android.app.Application
import com.example.habittracker.data.HabitDatabase

class HabitApp : Application() {
    val database: HabitDatabase by lazy { HabitDatabase.create(this) }
}
