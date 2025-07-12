package com.messamraza.securenotes.viewmodel

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.messamraza.securenotes.model.Note
import com.messamraza.securenotes.model.NoteDatabase
import com.messamraza.securenotes.model.NotesRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotesViewModel(application: android.app.Application) : AndroidViewModel(application) {
    private val repository: NotesRepository
    val allNotes: LiveData<List<Note>>

    init {
        val noteDao = NoteDatabase.getDatabase(application).noteDao()
        repository = NotesRepository(noteDao)
        allNotes = repository.allNotes
    }

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insert(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repository.update(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.delete(note)
    }

    suspend fun getNoteById(id: Long): Note? {
        return repository.getNoteById(id)
    }

    fun exportNotes(context: Context) = viewModelScope.launch {
        try {
            val notes = allNotes.value ?: emptyList()
            if (notes.isEmpty()) {
                Toast.makeText(context, "No notes to export", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val json = Json.encodeToString(notes)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "SecureNotes_Export_$timeStamp.json"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            FileOutputStream(file).use { outputStream ->
                outputStream.write(json.toByteArray())
            }

            Toast.makeText(context, "Notes exported to $fileName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error exporting notes: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun backupNotes(context: Context) = viewModelScope.launch {
        try {
            val notes = allNotes.value ?: emptyList()
            if (notes.isEmpty()) {
                Toast.makeText(context, "No notes to backup", Toast.LENGTH_SHORT).show()
                return@launch
            }

            val json = Json.encodeToString(notes)
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "SecureNotes_Backup_$timeStamp.json"
            val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)

            FileOutputStream(file).use { outputStream ->
                outputStream.write(json.toByteArray())
            }

            Toast.makeText(context, "Backup created: $fileName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error creating backup: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}