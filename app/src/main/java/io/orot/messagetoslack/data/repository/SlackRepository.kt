package io.orot.messagetoslack.data.repository

import io.orot.messagetoslack.data.datasource.SlackNetworkDataSource
import io.orot.messagetoslack.model.slack.RequestMsg
import io.reactivex.Single
import retrofit2.Response

object SlackRepository{

    private var slackDataSource = SlackNetworkDataSource()

    fun sendMsgToSlack(msg: RequestMsg): Single<Response<Void>>{
        return slackDataSource.sendMsgToSlack(msg)
    }
}
