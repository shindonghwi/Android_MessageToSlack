package io.orot.messagetoslack.data.api

import io.orot.messagetoslack.data.service.NotionAPIService
import io.orot.messagetoslack.data.service.SlackAPIService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClient {

    /** API 통신 시 필요한 공통 헤더 정보 */
    private val headerInterceptor: Interceptor by lazy {
        Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .build()
            return@Interceptor chain.proceed(request)
        }
    }

    /** okHttp Builder 생성  */
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addInterceptor(headerInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    fun getSlackClient(): SlackAPIService {
        return Retrofit.Builder()
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://hooks.slack.com/")
            .build()
            .create(SlackAPIService::class.java)
    }

    fun getNotionClient(): NotionAPIService {
        return Retrofit.Builder()
            .client(getOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.notion.com/")
            .build()
            .create(NotionAPIService::class.java)
    }
}

@JvmField
val slackClient = APIClient.getSlackClient()

@JvmField
val notionClient = APIClient.getNotionClient()