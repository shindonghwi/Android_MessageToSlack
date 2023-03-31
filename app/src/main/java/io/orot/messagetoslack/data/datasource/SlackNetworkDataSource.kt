package io.orot.messagetoslack.data.datasource

import io.orot.messagetoslack.data.api.slackClient
import io.orot.messagetoslack.model.slack.RequestMsg
import io.reactivex.Single
import retrofit2.Response

class SlackNetworkDataSource {

    fun sendMsgToSlack(msg: RequestMsg): Single<Response<Void>> {
        return slackClient.sendToMsg(msg)
    }
}