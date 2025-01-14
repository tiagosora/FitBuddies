package com.example.fitbuddies.data.repositories

import com.example.fitbuddies.data.remote.Supabase.client
import io.github.jan.supabase.storage.storage
import kotlin.time.Duration.Companion.minutes

class BucketRepository {

    companion object {
        suspend fun getImageURL(path: String): String {
            println("BucketRepository path: $path")

            println("BucketRepository: ${client.storage.from("images").list()}")

            val url = client.storage.from("images").createSignedUrl("mano_bro.png", expiresIn = 10.minutes)
            println("BucketRepository: $url")
            return url

        }

        suspend fun uploadImage(path: String, image: ByteArray) {
            client.storage.from("images").upload(path, image) {
                upsert = true
            }
        }
    }

}