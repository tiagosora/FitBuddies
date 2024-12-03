package com.example.fitbuddies.data.remote

import com.example.fitbuddies.data.models.Challenge
import com.example.fitbuddies.data.models.ChallengeMedia
import com.example.fitbuddies.data.models.Dare
import com.example.fitbuddies.data.models.Friendship
import com.example.fitbuddies.data.models.User
import retrofit2.http.*
import retrofit2.Response

interface SupabaseService {

    // User Endpoints

    @POST("users")
    suspend fun insertUser(@Body user: User): Response<Unit>

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") userId: String): Response<Unit>

    // Friendship Endpoints

    @POST("friendships/{userId}")
    suspend fun getFriendshipsForUser(@Path("userId") userId: String): Response<List<Friendship>>

    @POST("friendships")
    suspend fun insertFriendship(@Body friendship: Friendship): Response<Unit>

    @PATCH("friendships/accept/{userId}/{friendId}")
    suspend fun acceptFriendRequest(@Path("userId") userId: String, @Path("friendId") friendId: String): Response<Unit>

    @DELETE("friendships")
    suspend fun deleteFriendship(@Body friendship: Friendship): Response<Unit>

    // Dare Endpoints

    @POST("dares")
    suspend fun insertDare(@Body dare: Dare): Response<Unit>

    @PATCH("dares/accept/{challengeId}/{userId}")
    suspend fun acceptDare(@Path("challengeId") challengeId: String, @Path("userId") userId: String): Response<Unit>

    @DELETE("dares/{id}")
    suspend fun deleteDare(@Path("id") dareId: String): Response<Unit>

    // Challenge Endpoints

    @POST("challenges")
    suspend fun insertChallenge(@Body challenge: Challenge): Response<Unit>

    @DELETE("challenges/{id}")
    suspend fun deleteChallenge(@Path("id") challengeId: String): Response<Unit>

    // Challenge Media Endpoints

    @POST("challenge_media")
    suspend fun insertMedia(@Body media: ChallengeMedia): Response<Unit>

    @DELETE("challenge_media/{id}")
    suspend fun deleteMedia(@Path("id") mediaId: String): Response<Unit>
}
