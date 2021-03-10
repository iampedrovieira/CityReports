package com.example.cityreports.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.cityreports.db.NoteDB

import com.example.cityreports.db.NoteRepository
import com.example.cityreports.entities.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel (application: Application) : AndroidViewModel(application){
    private val repository: NoteRepository

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allNotes: LiveData<List<Note>>

    init {
        val noteDao = NoteDB.getDatabase(application,viewModelScope).noteDao()
        repository = NoteRepository(noteDao)
        allNotes = repository.allNotes
    }
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    
    fun insert(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(note)
    }
    // delete all
    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun deleteNote(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(id)
    }

    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(note)
    }

    suspend fun getLastNote():Note {
      return repository.getLastNote()
    }

}
