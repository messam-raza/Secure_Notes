package com.messamraza.securenotes

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.messamraza.securenotes.databinding.ActivityAddEditNoteBinding
import com.messamraza.securenotes.model.Note
import com.messamraza.securenotes.viewmodel.NotesViewModel
import kotlinx.coroutines.launch

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditNoteBinding
    private lateinit var viewModel: NotesViewModel
    private var noteId: Long = -1
    private var currentNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupToolbar()
            setupViewModel()

            noteId = intent.getLongExtra("note_id", -1)
            if (noteId != -1L) {
                loadNote()
            }
        } catch (e: Exception) {
            Log.e("AddEditNoteActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

//    private fun setupToolbar() {
//        try {
//            setSupportActionBar(binding.toolbar)
//            supportActionBar?.setDisplayHomeAsUpEnabled(true)
//            supportActionBar?.title = if (noteId == -1L) "Add Note" else "Edit Note"
//        } catch (e: Exception) {
//            Log.e("AddEditNoteActivity", "Error setting up toolbar: ${e.message}", e)
//        }
//    }

    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = if (noteId == -1L) "Add Note" else "Edit Note"
        } catch (e: Exception) {
            Log.e("AddEditNoteActivity", "Error setting up toolbar: ${e.message}", e)
        }
    }

    private fun setupViewModel() {
        try {
            viewModel = ViewModelProvider(this)[NotesViewModel::class.java]
        } catch (e: Exception) {
            Log.e("AddEditNoteActivity", "Error setting up ViewModel: ${e.message}", e)
        }
    }

    private fun loadNote() {
        try {
            lifecycleScope.launch {
                try {
                    currentNote = viewModel.getNoteById(noteId)
                    currentNote?.let { note ->
                        binding.etTitle.setText(note.title)
                        binding.etContent.setText(note.content)
                    }
                } catch (e: Exception) {
                    Log.e("AddEditNoteActivity", "Error loading note: ${e.message}", e)
                    Toast.makeText(this@AddEditNoteActivity, "Error loading note: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("AddEditNoteActivity", "Error launching coroutine: ${e.message}", e)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_add_edit_note, menu)
            return true
        } catch (e: Exception) {
            Log.e("AddEditNoteActivity", "Error creating options menu: ${e.message}", e)
            return false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return try {
            when (item.itemId) {
                android.R.id.home -> {
                    finish()
                    true
                }
                R.id.action_save -> {
                    saveNote()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        } catch (e: Exception) {
//            Log("AddEditNoteActivity", "Error handling menu item: ${e.message}", e)
            false
        }
    }

    private fun saveNote() {
        try {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            if (title.isEmpty()) {
                binding.etTitle.error = "Title is required"
                return
            }

            if (content.isEmpty()) {
                binding.etContent.error = "Content is required"
                return
            }

            val note = if (noteId == -1L) {
                Note(
                    title = title,
                    content = content,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            } else {
                currentNote?.copy(
                    title = title,
                    content = content,
                    updatedAt = System.currentTimeMillis()
                ) ?: return
            }

            lifecycleScope.launch {
                try {
                    if (noteId == -1L) {
                        viewModel.insertNote(note)
                    } else {
                        viewModel.updateNote(note)
                    }
                    Toast.makeText(this@AddEditNoteActivity, "Note saved", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Log.e("AddEditNoteActivity", "Error saving note: ${e.message}", e)
                    Toast.makeText(this@AddEditNoteActivity, "Error saving note: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        } catch (e: Exception) {
            Log.e("AddEditNoteActivity", "Error in saveNote: ${e.message}", e)
            Toast.makeText(this, "Error saving note: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}