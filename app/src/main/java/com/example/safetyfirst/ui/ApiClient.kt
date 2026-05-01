package com.example.safetyfirst.ui

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.util.concurrent.TimeUnit

data class GatewayEvent(
    val ts: Long,
    val type: String,
    val domain: String,
    val verdict: String,
)

object ApiClient {

    private const val GATEWAY_HOST = "20.3.103.96"
    private const val EVENTS_PORT = 9999

    private val client = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    fun fetchEvents(callback: (List<GatewayEvent>?) -> Unit) {
        val request = Request.Builder()
            .url("http://$GATEWAY_HOST:$EVENTS_PORT/events")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.w("API_TEST", "fetchEvents failed: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        Log.w("API_TEST", "fetchEvents HTTP ${it.code}")
                        callback(null)
                        return
                    }
                    val body = it.body?.string().orEmpty()
                    callback(parseEvents(body))
                }
            }
        })
    }

    private fun parseEvents(body: String): List<GatewayEvent> {
        return try {
            val arr = JSONArray(body)
            val out = ArrayList<GatewayEvent>(arr.length())
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                out.add(
                    GatewayEvent(
                        ts = o.optLong("ts"),
                        type = o.optString("type"),
                        domain = o.optString("domain"),
                        verdict = o.optString("verdict"),
                    )
                )
            }
            out
        } catch (e: Exception) {
            Log.w("API_TEST", "parseEvents failed: ${e.message}")
            emptyList()
        }
    }
}
