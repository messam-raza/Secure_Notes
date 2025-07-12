package com.messamraza.securenotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.messamraza.securenotes.adapter.NotesAdapter
import com.messamraza.securenotes.databinding.ActivityNotesBinding
import com.messamraza.securenotes.model.Note
import com.messamraza.securenotes.utils.PreferenceManager
import com.messamraza.securenotes.viewmodel.NotesViewModel

class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var viewModel: NotesViewModel
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityNotesBinding.inflate(layoutInflater)
            setContentView(binding.root)

            preferenceManager = PreferenceManager(this)
            setupToolbar()
            setupRecyclerView()
            setupViewModel()
            setupClickListeners()
            applyTheme()
        } catch (e: Exception) {
            Log.e("NotesActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing notes: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Secure Notes"
        } catch (e: Exception) {
            Log.e("NotesActivity", "Error setting up toolbar: ${e.message}", e)
        }
    }

    private fun setupRecyclerView() {
        try {
            notesAdapter = NotesAdapter(
                onNoteClick = { note ->
                    try {
                        val intent = Intent(this, AddEditNoteActivity::class.java)
                        intent.putExtra("note_id", note.id)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("NotesActivity", "Error opening note: ${e.message}", e)
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                },
                onDeleteClick = { note ->
                    try {
                        viewModel.deleteNote(note)
                    } catch (e: Exception) {
                        Log.e("NotesActivity", "Error deleting note: ${e.message}", e)
                        Toast.makeText(this, "Error deleting note: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            binding.recyclerViewNotes.apply {
                adapter = notesAdapter
                layoutManager = LinearLayoutManager(this@NotesActivity)
            }
        } catch (e: Exception) {
            Log.e("NotesActivity", "Error setting up RecyclerView: ${e.message}", e)
        }
    }

    private fun setupViewModel() {
        try {
            viewModel = ViewModelProvider(this)[NotesViewModel::class.java]
            viewModel.allNotes.observe(this) { notes ->
                try {
                    notesAdapter.submitList(notes)
                    binding.tvEmptyState.visibility = if (notes.isEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                } catch (e: Exception) {
                    Log.e("NotesActivity", "Error updating notes list: ${e.message}", e)
                }
            }
        } catch (e: Exception) {
            Log.e("NotesActivity", "Error setting up ViewModel: ${e.message}", e)
        }
    }

    private fun setupClickListeners() {
        try {
            binding.fabAddNote.setOnClickListener {
                try {
                    startActivity(Intent(this, AddEditNoteActivity::class.java))
                } catch (e: Exception) {
                    Log.e("NotesActivity", "Error opening add note: ${e.message}", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("NotesActivity", "Error setting up click listeners: ${e.message}", e)
        }
    }

    private fun applyTheme() {
        // Theme applied via manifest or PreferenceManager
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_notes, menu)
            return true
        } catch (e: Exception) {
            Log.e("NotesActivity", "Error creating options menu: ${e.message}", e)
            return false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return try {
            when (item.itemId) {
                android.R.id.home -> {
                    try {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        startActivity(intent)
                        finish()
                        true
                    } catch (e: Exception) {
                        Log.e("NotesActivity", "Error navigating to home: ${e.message}", e)
                        Toast.makeText(this, "Error navigating to home: ${e.message}", Toast.LENGTH_SHORT).show()
                        true
                    }
                }
                R.id.action_settings -> {
                    try {
                        startActivity(Intent(this, SettingsActivity::class.java))
                        true
                    } catch (e: Exception) {
                        Log.e("NotesActivity", "Error opening settings: ${e.message}", e)
                        Toast.makeText(this, "Error opening settings: ${e.message}", Toast.LENGTH_SHORT).show()
                        true
                    }
                }
                R.id.action_export -> {
                    try {
                        viewModel.exportNotes(this)
                        true
                    } catch (e: Exception) {
                        Log.e("NotesActivity", "Error exporting notes: ${e.message}", e)
                        Toast.makeText(this, "Error exporting notes: ${e.message}", Toast.LENGTH_SHORT).show()
                        true
                    }
                }
                else -> super.onOptionsItemSelected(item)
            }
        } catch (e: Exception) {
            Log.e("NotesActivity", "Error handling menu item: ${e.message}", e)
            false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        try {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
            return true
        } catch (e: Exception) {
            Log.e("NotesActivity", "Error navigating up: ${e.message}", e)
            return false
        }
    }
}