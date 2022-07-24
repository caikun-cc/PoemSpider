package site.caikun.poem

import okhttp3.OkHttpClient
import okhttp3.Request

object Http {

    private val client = OkHttpClient()

    fun request(url: String): String {
        var result = ""
        runCatching {
            val request = Request.Builder()
                .url(url)
                .build()
            val response = client.newCall(request).execute()
            result = response.body.string()
        }.onFailure {

        }
        return result
    }
}