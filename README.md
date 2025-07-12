# Secure Notes – Encrypted Notes App

## Overview
Secure Notes is an Android application built with Kotlin that allows users to create, edit, and save personal notes with robust security features. The app encrypts notes using AES encryption and provides multiple authentication methods (PIN, pattern, or fingerprint) to ensure data privacy. It also supports dark/light mode theming and optional backup/export functionalities to local storage.

## Project Title
**Secure Notes – Encrypted Notes App**

## Objective
To develop a secure note-taking application where users can write, edit, and save encrypted notes protected by password or biometric authentication.

## Tech Stack
- **Language**: Kotlin
- **UI**: XML with ViewBinding (Jetpack Compose optional)
- **Security**: AndroidX Biometric API, AES Encryption
- **Storage**: Room Database
- **Extras**: Fingerprint unlock, Dark/Light theming

## Features
- **Add/Edit/Delete Text Notes**: Create, modify, and remove notes seamlessly.
- **App-Level Security**: Secure access with PIN, pattern, or fingerprint authentication.
- **AES Encryption**: Encrypts notes for secure storage.
- **Dark/Light Mode Toggle**: Switch between themes for user preference.
- **Backup/Export to Local Storage**: Export notes as JSON files and create backups.

## Prerequisites
- Android Studio (latest version recommended)
- Android SDK with API level 24 (Nougat) or higher
- Emulator or physical Android device for testing

## Installation

### Clone the Repository
```bash
https://github.com/messam-raza/Secure_Notes.git
cd secure-notes

Open in Android Studio
Launch Android Studio.
Select "Open an existing project" and navigate to the cloned directory.
Wait for Gradle to sync and build the project.
Add Dependencies
Ensure the following dependencies are included in build.gradle (app level):

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.biometric:biometric:1.1.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

Permissions
Add the following permissions to AndroidManifest.xml:
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
Build the Project
Click "Build" > "Rebuild Project" in Android Studio.
Resolve any missing dependencies or sync issues.
Run the App
Connect an emulator or physical device.
Click "Run" in Android Studio to launch the app.
Usage
Launch the App: Open the app on your device; you will be prompted to set up a PIN or use biometric authentication.
Authentication: Enter a PIN (minimum 4 digits) or use your fingerprint to log in.
Manage Notes:
Add a new note by clicking the FAB (+) in NotesActivity.
Edit or delete existing notes from the notes list.
Settings: Access SettingsActivity to toggle dark/light mode or change the PIN.
Export/Backup: Use the menu options in NotesActivity to export notes or create a backup (saved to the Documents directory).
Project Structure
text



app/
├── manifests/
│   └── AndroidManifest.xml
├── java/com.messamraza.securenotes/
│   ├── adapter/NotesAdapter.kt
│   ├── database/
│   │   ├── NoteDao.kt
│   │   ├── NoteDatabase.kt
│   ├── model/Note.kt
│   ├── repository/NoteRepository.kt
│   ├── utils/
│   │   ├── EncryptionUtils.kt
│   │   ├── PreferenceManager.kt
│   ├── viewmodel/NotesViewModel.kt
│   ├── AddEditNoteActivity.kt
│   ├── MainActivity.kt
│   ├── NotesActivity.kt
│   ├── PinDialogFragment.kt
│   └── SettingsActivity.kt


Contributing
Fork the repository.
Create a new branch (git checkout -b feature-branch).
Make your changes and commit them (git commit -m "Description of changes").
Push to the branch (git push origin feature-branch).
Open a pull request with a detailed description of your changes.
License
This project is licensed under the MIT License. Feel free to use, modify, and distribute it as per the license terms.

Acknowledgments
Android Open Source Project for AndroidX libraries.
JetBrains for Kotlin and Android Studio support.
Community resources for biometric and encryption implementations.
