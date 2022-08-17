package io.orot.messagetoslack.model

import com.google.gson.annotations.SerializedName

data class RequestMsg(
    @SerializedName("msg")
    val msg: String
)