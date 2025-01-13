package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.remote.Supabase.client
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration.Companion.minutes

class BucketRepository {

    companion object {
        suspend fun getImageURL(path: String): String {
            return client.storage.from("images").createSignedUrl(path, expiresIn = 10.minutes)
        }

        suspend fun uploadImage(path: String, image: ByteArray) {
            client.storage.from("images").upload(path, image) {
                upsert = true
            }
        }
    }

}