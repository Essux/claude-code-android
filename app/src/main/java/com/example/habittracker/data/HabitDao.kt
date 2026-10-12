package com.example.habittracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query(
        """
        SELECT h.id, h.name, h.unit, e.value AS lastValue, e.date AS lastDate
        FROM habits h
        LEFT JOIN entries e ON e.id = (
            SELECT id FROM entries WHERE habitId = h.id ORDER BY date DESC LIMIT 1
        )
        ORDER BY h.name COLLATE NOCASE
        """
    )
    fun observeSummaries(): Flow<List<HabitSummary>>

    @Query("SELECT * FROM habits WHERE id = :id")
    fun observeHabit(id: Long): Flow<Habit?>

    @Query("SELECT * FROM entries WHERE habitId = :habitId ORDER BY date DESC")
    fun observeEntries(habitId: Long): Flow<List<Entry>>

    @Insert
    suspend fun insertHabit(habit: Habit): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEntry(entry: Entry)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabit(id: Long)
}
