package com.messamraza.securenotes.utils

import android.content.Context

class PreferenceManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("SecureNotesPrefs", Context.MODE_PRIVATE)

    fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean("dark_mode", false)
    }

    fun setDarkMode(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean("dark_mode", isDarkMode).apply()
    }

    fun getPin(): String {
        return sharedPreferences.getString("pin", "") ?: ""
    }

    fun savePin(pin: String) {
        sharedPreferences.edit().putString("pin", pin).apply()
    }

    fun setFirstTime(isFirstTime: Boolean) {
        sharedPreferences.edit().putBoolean("first_time", isFirstTime).apply()
    }

    fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean("first_time", true)
    }
}