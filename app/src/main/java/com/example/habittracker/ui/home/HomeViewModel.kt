package com.example.habittracker.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.habittracker.HabitApp
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitDao
import com.example.habittracker.data.HabitSummary
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: HabitDao) : ViewModel() {
    /** Null until the first database read, so the UI doesn't flash the empty state. */
    val habits: StateFlow<List<HabitSummary>?> = dao.observeSummaries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun addHabit(name: String, unit: String) {
        viewModelScope.launch { dao.insertHabit(Habit(name = name.trim(), unit = unit.trim())) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer { HomeViewModel((this[APPLICATION_KEY] as HabitApp).database.habitDao()) }
        }
    }
}
