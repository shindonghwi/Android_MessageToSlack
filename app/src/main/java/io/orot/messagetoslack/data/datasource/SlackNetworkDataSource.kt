package io.orot.messagetoslack.data.datasource

import io.orot.messagetoslack.data.api.slackClient
import io.orot.messagetoslack.model.RequestMsg
import io.orot.messagetoslack.model.ResponseMsg
import io.reactivex.Single
import retrofit2.Response

class SlackNetworkDataSource {

    fun sendMsgToSlack(msg: RequestMsg): Single<Response<Void>> {
        return slackClient.sendToMsg(msg)
    }
}