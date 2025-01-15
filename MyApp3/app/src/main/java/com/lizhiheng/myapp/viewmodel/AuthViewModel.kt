package com.lizhiheng.myapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizhiheng.myapp.repository.UserRepository
import com.lizhiheng.myapp.sharepreferences.SharedPreferencesHelper
import com.lizhiheng.myapp.user_room.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferencesHelper: SharedPreferencesHelper
) : ViewModel() {

    val usernameLiveData = MutableLiveData<String?>()

    init {
        usernameLiveData.value = sharedPreferencesHelper.getUsername()
    }

    fun login(username: String, password: String, callback: (Boolean, Boolean) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.login(username, password)
            if (user != null) {
                sharedPreferencesHelper.saveUsername(username)
                usernameLiveData.value = username
                callback(true, true)
            } else {
                val exists = userRepository.checkUsernameExists(username)
                callback(false, exists)
            }
        }
    }

    fun checkUsernameExists(username: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            val exists = userRepository.checkUsernameExists(username)
            callback(exists)
        }
    }

    fun register(username: String, password: String, callback: () -> Unit) {
        viewModelScope.launch {
            userRepository.register(User(username, password))
            sharedPreferencesHelper.saveUsername(username)
            usernameLiveData.value = username
            callback()
        }
    }

    fun clearUsername() {
        sharedPreferencesHelper.clearUsername()
        usernameLiveData.value = null
    }
    fun logout(){
        clearUsername()
    }
    fun punchIn(callback: (Boolean, Boolean) -> Unit) { // (success, alreadyPunchedInToday)
        val username = usernameLiveData.value
        if (username != null) {
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val lastPunchInDate = sharedPreferencesHelper.getLastPunchInDate(username)

            if (lastPunchInDate == currentDate) {
                callback(false, true)
            } else {
                sharedPreferencesHelper.setLastPunchInDate(username, currentDate)
                sharedPreferencesHelper.incrementPunchInDays(username)
                sharedPreferencesHelper.incrementEffortPoints(username)
                callback(true, false)
            }
        } else {
            callback(false, false)
        }
    }

    fun getPunchInDays(username: String): Int {
        return sharedPreferencesHelper.getPunchInDays(username)
    }

    fun getEffortPoints(username: String): Int {
        return sharedPreferencesHelper.getEffortPoints(username)
    }
}

