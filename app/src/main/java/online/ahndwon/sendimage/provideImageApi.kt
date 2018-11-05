package online.ahndwon.sendimage

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import online.ahndwon.sendimage.MainActivity.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


fun provideImageApi() : ImageApi = Retrofit.Builder().apply {
    baseUrl(BASE_URL)
    client(httpClient)
    addConverterFactory(GsonConverterFactory.create())
}.build().create(ImageApi::class.java)


val httpClient: OkHttpClient = OkHttpClient.Builder().apply {
//        addInterceptor(loggingInterceptor)
}.build()

//val loggingInterceptor = HttpLoggingInterceptor(ApiLogger()).apply {
//    level = HttpLoggingInterceptor.Level.BODY
//}


class ApiLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val logName = "ApiLogger"
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val prettyPrintJson = GsonBuilder().setPrettyPrinting()
                    .create().toJson(JsonParser().parse(message))
                Log.d(logName, prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                Log.d(logName, message)
            }
        } else {
            Log.d(logName, message)
            return
        }
    }
}