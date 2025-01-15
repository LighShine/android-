package com.lizhiheng.myapp.sharepreferences

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class SharedPreferencesHelper @Inject constructor(@ApplicationContext context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun getUsername(): String? {
        return sharedPreferences.getString("username", null)
    }

    fun saveUsername(username: String) {
        sharedPreferences.edit().putString("username", username).apply()
    }

    fun clearUsername() {
        sharedPreferences.edit().remove("username").apply()
    }

    fun getPunchInDays(username: String): Int {
        return sharedPreferences.getInt("${username}_punch_in_days", 0)
    }

    fun getEffortPoints(username: String): Int {
        return sharedPreferences.getInt("${username}_effort_points", 0)
    }

    fun getLastPunchInDate(username: String): String? {
        return sharedPreferences.getString("${username}_last_punch_in_date", null)
    }

    fun setLastPunchInDate(username: String, date: String) {
        sharedPreferences.edit().putString("${username}_last_punch_in_date", date).apply()
    }

    fun incrementPunchInDays(username: String) {
        val days = getPunchInDays(username) + 1
        sharedPreferences.edit().putInt("${username}_punch_in_days", days).apply()
    }

    fun incrementEffortPoints(username: String) {
        val points = getEffortPoints(username) + 1
        sharedPreferences.edit().putInt("${username}_effort_points", points).apply()
    }
}
