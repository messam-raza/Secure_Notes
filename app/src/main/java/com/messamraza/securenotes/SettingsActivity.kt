package com.messamraza.securenotes

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.messamraza.securenotes.databinding.ActivitySettingsBinding
import com.messamraza.securenotes.utils.PreferenceManager
import androidx.lifecycle.ViewModelProvider
import com.messamraza.securenotes.viewmodel.NotesViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var viewModel: NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivitySettingsBinding.inflate(layoutInflater)
            setContentView(binding.root)

            preferenceManager = PreferenceManager(this)
            viewModel = ViewModelProvider(this)[NotesViewModel::class.java]

            setupToolbar()
            setupSettings()
            setupClickListeners()
        } catch (e: Exception) {
            Log.e("SettingsActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing settings: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupToolbar() {
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Settings"
        } catch (e: Exception) {
            Log.e("SettingsActivity", "Error setting up toolbar: ${e.message}", e)
        }
    }

    private fun setupSettings() {
        try {
            binding.switchDarkMode.isChecked = preferenceManager.isDarkMode()
        } catch (e: Exception) {
            Log.e("SettingsActivity", "Error setting up settings: ${e.message}", e)
        }
    }

    private fun setupClickListeners() {
        try {
            binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
                try {
                    preferenceManager.setDarkMode(isChecked)
                    if (isChecked) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    }
                    recreate()
                } catch (e: Exception) {
                    Log.e("SettingsActivity", "Error toggling dark mode: ${e.message}", e)
                    Toast.makeText(this, "Error changing theme: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnChangePin.setOnClickListener {
                try {
                    showChangePinDialog()
                } catch (e: Exception) {
                    Log.e("SettingsActivity", "Error opening PIN dialog: ${e.message}", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnBackup.setOnClickListener {
                try {
                    viewModel.backupNotes(this)
                } catch (e: Exception) {
                    Log.e("SettingsActivity", "Error in backup: ${e.message}", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("SettingsActivity", "Error setting up click listeners: ${e.message}", e)
        }
    }

    private fun showChangePinDialog() {
        try {
            val dialog = PinDialogFragment { newPin ->
                try {
                    preferenceManager.savePin(newPin)
                    Toast.makeText(this, "PIN changed successfully", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Log.e("SettingsActivity", "Error saving PIN: ${e.message}", e)
                    Toast.makeText(this, "Error saving PIN: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show(supportFragmentManager, "ChangePinDialog")
        } catch (e: Exception) {
            Log.e("SettingsActivity", "Error showing PIN dialog: ${e.message}", e)
            Toast.makeText(this, "Error showing PIN dialog: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        try {
            finish()
            return true
        } catch (e: Exception) {
            Log.e("SettingsActivity", "Error navigating up: ${e.message}", e)
            return false
        }
    }
}