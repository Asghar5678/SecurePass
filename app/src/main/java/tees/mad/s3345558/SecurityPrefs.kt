package tees.mad.s3345558

import android.content.Context


object SecurityPrefs {

    private const val PREF_NAME = "secure_pass_prefs"
    private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"

    private const val LAST_STRENGTH_STATUS = "strength"
    private const val LAST_BREACH_STATUS = "breach"

    fun isBiometricEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_BIOMETRIC_ENABLED, false)
    }

    fun setBiometricEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply()
    }

    fun getLastStrengthStatus(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(LAST_STRENGTH_STATUS, "") ?: ""

    }

    fun setLastStrengthStatus(context: Context, status: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(LAST_STRENGTH_STATUS, status).apply()
    }

    fun getLastBreachStatus(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(LAST_BREACH_STATUS, "") ?: ""

    }

    fun setLastBreachStatus(context: Context, status: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(LAST_BREACH_STATUS, status).apply()
    }
}
