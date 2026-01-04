package tees.mad.s3345558

import android.content.Context


object UserAccountData {

    private const val PREFS_NAME = "PREFS"
    private const val KEY_IS_USER_LOGGED_IN = "KEY_IS_USER_LOGGED_IN"
    private const val NAME = "NAME"
    private const val USER_EMAIL = "USER_EMAIL"
    private const val COUNTRY = "COUNTRY"

    fun markLoginStatus(context: Context, isLoggedIn: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_IS_USER_LOGGED_IN, isLoggedIn).apply()
    }

    fun checkLoginStatus(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_IS_USER_LOGGED_IN, false)
    }

    fun saveName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(NAME, name).apply()
    }

    fun getName(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(NAME, "") ?: ""
    }

    fun saveEmail(context: Context, email: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(USER_EMAIL, email).apply()
    }

    fun getEmail(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(USER_EMAIL, "") ?: ""
    }

    fun saveCountry(context: Context, email: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(COUNTRY, email).apply()
    }

    fun getCountry(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(COUNTRY, "") ?: ""
    }
}