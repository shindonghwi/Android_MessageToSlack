package io.orot.messagetoslack.data.service
import io.orot.messagetoslack.model.RequestMsg
import io.orot.messagetoslack.model.ResponseMsg
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface SlackAPIService {

    @Headers("Content-Type: application/json")
    @POST("workflows/T03MX8Q6U80/A03UAA00STB/421382150881820421/EJxxvpOvQO5gF60ObszDH7QL")
    fun sendToMsg(
        @Body msg: RequestMsg,
    ): Single<Response<Void>>
}