package com.lizhiheng.myapp.repository

import com.lizhiheng.myapp.user_room.User
import com.lizhiheng.myapp.user_room.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun login(username: String, password: String): User? {
        return userDao.login(username, password)
    }

    suspend fun checkUsernameExists(username: String): Boolean {
        return userDao.getUserByUsername(username) != null
    }

    suspend fun register(user: User) {
        userDao.register(user)
    }
}
