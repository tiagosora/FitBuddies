package com.example.fitbuddies.remote

import retrofit2.http.*
import com.example.fitbuddies.models.*
import retrofit2.Response

interface SupabaseService {

    // User Endpoints
    @GET("users")
    suspend fun getAllUsers(): List<User>

    @POST("users")
    suspend fun insertUser(@Body user: User): Response<Unit>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Response<Unit>

    // Friendship Endpoints
    @GET("friendships")
    suspend fun getAllFriendships(): List<Friendship>

    @POST("friendships")
    suspend fun insertFriendship(@Body friendship: Friendship): Response<Unit>

    @DELETE("friendships")
    suspend fun deleteFriendship(@Body friendship: Friendship): Response<Unit>

    // Dare Endpoints
    @GET("dares")
    suspend fun getAllDares(): List<Dare>

    @POST("dares")
    suspend fun insertDare(@Body dare: Dare): Response<Unit>

    @DELETE("dares/{id}")
    suspend fun deleteDare(@Path("id") dareId: String): Response<Unit>

    // Challenge Endpoints
    @GET("challenges")
    suspend fun getAllChallenges(): List<Challenge>

    @POST("challenges")
    suspend fun insertChallenge(@Body challenge: Challenge): Response<Unit>

    @DELETE("challenges/{id}")
    suspend fun deleteChallenge(@Path("id") challengeId: String): Response<Unit>

    // Challenge Media Endpoints
    @GET("challenge_media")
    suspend fun getMediaByDare(@Query("dareId") dareId: String): List<ChallengeMedia>

    @POST("challenge_media")
    suspend fun insertMedia(@Body media: ChallengeMedia): Response<Unit>

    @DELETE("challenge_media/{id}")
    suspend fun deleteMedia(@Path("id") mediaId: String): Response<Unit>
}
