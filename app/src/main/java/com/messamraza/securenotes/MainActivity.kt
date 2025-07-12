package com.messamraza.securenotes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.messamraza.securenotes.databinding.ActivityMainBinding
import com.messamraza.securenotes.utils.PreferenceManager
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            preferenceManager = PreferenceManager(this)
            setupBiometricAuthentication()
            setupClickListeners()
            checkBiometricSupport()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing app: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupBiometricAuthentication() {
        try {
            executor = ContextCompat.getMainExecutor(this)
            biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        navigateToNotes()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                    }
                })

            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Secure Notes Authentication")
                .setSubtitle("Use your fingerprint to access your secure notes")
                .setNegativeButtonText("Cancel")
                .build()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error setting up biometric auth: ${e.message}", e)
        }
    }

    private fun setupClickListeners() {
        try {
            binding.btnBiometric.setOnClickListener {
                try {
                    val savedPin = preferenceManager.getPin()
                    if (savedPin.isEmpty()) {
                        Toast.makeText(this, "Please set up PIN first", Toast.LENGTH_SHORT).show()
                        showPinDialog()
                    } else {
                        biometricPrompt.authenticate(promptInfo)
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error in biometric click: ${e.message}", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnPin.setOnClickListener {
                try {
                    showPinDialog()
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error in PIN click: ${e.message}", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnSettings.setOnClickListener {
                try {
                    startActivity(Intent(this, SettingsActivity::class.java))
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error opening settings: ${e.message}", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error setting up click listeners: ${e.message}", e)
        }
    }

    private fun checkBiometricSupport() {
        try {
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    binding.btnBiometric.isEnabled = true
                    binding.tvBiometricStatus.text = "Biometric authentication available"
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    binding.btnBiometric.isEnabled = false
                    binding.tvBiometricStatus.text = "No biometric features available"
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    binding.btnBiometric.isEnabled = false
                    binding.tvBiometricStatus.text = "Biometric features are currently unavailable"
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    binding.btnBiometric.isEnabled = false
                    binding.tvBiometricStatus.text = "No biometric credentials enrolled"
                }
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error checking biometric support: ${e.message}", e)
            binding.btnBiometric.isEnabled = false
            binding.tvBiometricStatus.text = "Error checking biometric support"
        }
    }

    private fun showPinDialog() {
        try {
            val dialog = PinDialogFragment { enteredPin ->
                try {
                    val savedPin = preferenceManager.getPin()
                    if (savedPin.isEmpty()) {
                        if (enteredPin.length >= 4) {
                            preferenceManager.savePin(enteredPin)
                            preferenceManager.setFirstTime(false)
                            Toast.makeText(this, "PIN set successfully!", Toast.LENGTH_SHORT).show()
                            navigateToNotes()
                        } else {
                            Toast.makeText(this, "PIN must be at least 4 digits", Toast.LENGTH_SHORT).show()
                        }
                    } else if (savedPin == enteredPin) {
                        navigateToNotes()
                    } else {
                        Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error in PIN dialog callback: ${e.message}", e)
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show(supportFragmentManager, "PinDialog")
        } catch (e: Exception) {
            Log.e("MainActivity", "Error showing PIN dialog: ${e.message}", e)
            Toast.makeText(this, "Error showing PIN dialog: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToNotes() {
        try {
            startActivity(Intent(this, NotesActivity::class.java))
            finish()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error navigating to notes: ${e.message}", e)
            Toast.makeText(this, "Error navigating to notes: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}