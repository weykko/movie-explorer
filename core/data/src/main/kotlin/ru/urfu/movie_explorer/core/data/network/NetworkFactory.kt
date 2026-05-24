package ru.urfu.movie_explorer.core.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.urfu.movie_explorer.core.data.BuildConfig
import ru.urfu.movie_explorer.core.data.network.api.ImdbApi
import java.util.concurrent.TimeUnit

/**
 * Фабрика сетевых клиентов: OkHttp + Retrofit с kotlinx.serialization-конвертером.
 */
object NetworkFactory {

    private const val TIMEOUT_SECONDS = 20L

    private val json: Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
    }

    fun createOkHttpClient(context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY },
            )
            builder.addInterceptor(ChuckerInterceptor.Builder(context).build())
        }
        return builder.build()
    }

    fun createRetrofit(client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(ImdbApi.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    fun createImdbApi(retrofit: Retrofit): ImdbApi = retrofit.create(ImdbApi::class.java)
}
