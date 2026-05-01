package com.example.safetyfirst.ui

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import android.util.Log

object ApiClient {

    private val client = OkHttpClient()

    fun checkDomain(domain: String, callback: (String?) -> Unit) {

//        val json = JSONObject()
//        json.put("domain", domain)
//        val body = json.toString()
//            .toRequestBody("application/json".toMediaType())
//        val request = Request.Builder()
//            .url("http://4.154.154.5:8080/check-domain")
//            .post(body)
//            .build()
//
//
//        client.newCall(request).enqueue(object : Callback {
//
//            override fun onFailure(call: Call, e: IOException) {
//                Log.e("API_TEST", "Error: ${e.message}")
//                callback(null)
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                callback(response.body?.string())
//            }
//        })
        // Log that API was called
        Log.d("API_TEST", "checkDomain called with: $domain (mocked response)")

        // Simulate a network call delay
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val fakeResponse = """{"domain":"$domain","status":"safe"}"""
            callback(fakeResponse)
        }, 500) // half-second delay





    }

}
















