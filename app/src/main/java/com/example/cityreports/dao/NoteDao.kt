package com.example.cityreports.dao

import androidx.lifecycle.LiveData
import androidx.room.*

import com.example.cityreports.entities.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun getAllNotes():LiveData<List<Note>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("DELETE FROM note_table where id == :id")
    suspend fun deleteNote(id: Int)

    @Update
    suspend fun updateCity(note: Note)
}