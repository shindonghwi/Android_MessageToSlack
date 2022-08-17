package io.orot.messagetoslack.model

import com.google.gson.annotations.SerializedName

data class ResponseMsg(

    @SerializedName("error")
    val error: String,

    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("response_metadata")
    val response_metadata: ResponseMetaData

)

data class ResponseMetaData(
    val messages: List<String>
)

