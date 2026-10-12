package com.example.habittracker.ui.habit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.habittracker.HabitApp
import com.example.habittracker.data.Entry
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(private val dao: HabitDao, savedStateHandle: SavedStateHandle) : ViewModel() {
    private val habitId: Long = checkNotNull(savedStateHandle["habitId"])

    val habit: StateFlow<Habit?> = dao.observeHabit(habitId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val entries: StateFlow<List<Entry>?> = dao.observeEntries(habitId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Replaces any existing entry for the same date (unique index on habitId + date). */
    fun saveEntry(date: LocalDate, value: Double) {
        viewModelScope.launch { dao.upsertEntry(Entry(habitId = habitId, date = date, value = value)) }
    }

    fun deleteHabit() {
        viewModelScope.launch { dao.deleteHabit(habitId) }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                HabitViewModel(
                    (this[APPLICATION_KEY] as HabitApp).database.habitDao(),
                    createSavedStateHandle(),
                )
            }
        }
    }
}
