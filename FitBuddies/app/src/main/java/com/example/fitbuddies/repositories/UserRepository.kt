package com.example.fitbuddies.repositories

import com.example.fitbuddies.models.User
import com.example.fitbuddies.remote.SupabaseService
import kotlinx.coroutines.flow.Flow
import android.util.Log
import com.example.fitbuddies.local.UserDao

class UserRepository(
    private val userDao: UserDao,
    private val supabaseService: SupabaseService
) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun insertUser(user: User) {
        userDao.insertUser(user) // Save locally first
        try {
            supabaseService.insertUser(user) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("UserRepository", "Failed to sync user: ${user.userId}", e)
        }
    }

    suspend fun refreshUsers() {
        try {
            val remoteUsers = supabaseService.getAllUsers()
            remoteUsers.forEach { userDao.insertUser(it) } // Update local cache
        } catch (e: Exception) {
            Log.e("UserRepository", "Failed to refresh users", e)
        }
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user) // Remove locally
        try {
            supabaseService.deleteUser(user.userId) // Delete remotely
        } catch (e: Exception) {
            Log.e("UserRepository", "Failed to delete user: ${user.userId}", e)
        }
    }
}
