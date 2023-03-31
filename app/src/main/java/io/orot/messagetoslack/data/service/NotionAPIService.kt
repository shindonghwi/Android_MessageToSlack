package io.orot.messagetoslack.data.service

import io.orot.messagetoslack.model.notion.NotionTransaction
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotionAPIService {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer secret_gWfN6vi4JGysdJYHzlshIbzANZ5hBFHYFEBsmZM7RGq",
        "Notion-Version: 2022-06-28",
    )
    @POST("v1/pages/")
    fun registerTransaction(
        @Body msg: NotionTransaction,
    ): Single<Response<Void>>
}