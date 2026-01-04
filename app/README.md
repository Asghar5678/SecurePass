# 🔐 Secure Pass – Privacy-First Password Security App

Secure Pass is an Android application designed to help users **evaluate password strength**, **check for data breaches**, and **improve password hygiene** in a completely **privacy-safe** manner.  
The app performs all sensitive operations locally and never stores or uploads user passwords.

---

## 📱 Features

### 🔍 Password Strength Checker
- Real-time password strength analysis
- Visual strength meter (Weak / Medium / Strong)
- Character validation (length, uppercase, lowercase, numbers, symbols)
- Predictable password detection
- Instant feedback while typing

### 🚨 Password Breach Checker
- Checks if a password appears in known data breaches
- Uses **Have I Been Pwned (HIBP)** Pwned Passwords API
- Implements **SHA-1 hash prefix (k-Anonymity)** for privacy
- No actual password is ever sent to the server
- Clear Safe / Breached result UI
- Error handling for network failures

### 🕓 History Tracking
- Stores password check results securely on the device
- Displays strength and breach status with timestamps
- User-defined labels to identify passwords (e.g., Gmail, Bank)
- Individual delete option for each history item
- Clear all history option
- **No passwords are stored**

### 🔐 Biometric Security
- Optional biometric authentication (Fingerprint / Face)
- Toggle biometric lock ON/OFF from Settings
- Uses Android’s secure `BiometricPrompt` API
- Biometric data is never accessed or stored by the app

### 👤 Profile & Settings
- View user profile (Name, Email, Country)
- Change password with old password verification
- Password strength meter inside change password dialog
- Secure logout with cleared back stack

### ℹ️ About & Contact
- App information displayed at the bottom of the Home Screen
- Developer contact details
- Privacy-first design message

---

## 🛡 Privacy & Security Principles

- ❌ Passwords are **never stored**
- ❌ Passwords are **never uploaded**
- ✅ All local data is **encrypted**
- ✅ Uses **k-Anonymity** for breach checks
- ✅ Biometric authentication handled by Android OS
- ✅ Minimal permissions (Internet & Biometrics only)

---

## 🏗 Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Architecture:** Compose-based UI with lifecycle awareness
- **Networking:** Retrofit + OkHttp
- **Security:**
    - AndroidX Biometric
    - EncryptedSharedPreferences
- **Data Serialization:** Gson
- **Concurrency:** Kotlin Coroutines

---
