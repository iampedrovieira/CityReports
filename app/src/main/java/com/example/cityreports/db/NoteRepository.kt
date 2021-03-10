package com.example.cityreports.db

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.cityreports.dao.NoteDao
import com.example.cityreports.entities.Note

class NoteRepository (private val noteDao: NoteDao){

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun deleteAll(){
        noteDao.deleteAll()
    }
    suspend fun deleteNote(id:Int){
        noteDao.deleteNote(id)
    }
}