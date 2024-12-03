package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.models.User
import com.example.fitbuddies.data.remote.SupabaseService
import kotlinx.coroutines.flow.Flow
import android.util.Log
import com.example.fitbuddies.data.local.UserDao

class UserRepository(
    private val userDao: UserDao,
    private val supabaseService: SupabaseService
) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    suspend fun getUserById(userId: String): Flow<User?> {
        return userDao.getUserById(userId)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user) // Save locally first
        try {
            supabaseService.insertUser(user) // Sync to Supabase
        } catch (e: Exception) {
            Log.e("UserRepository", "Failed to sync user: ${user.userId}", e)
        }
    }

    suspend fun refreshUsers() {
        // TODO: Implement this method
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
