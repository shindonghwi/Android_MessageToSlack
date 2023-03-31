package io.orot.messagetoslack.data.datasource

import io.orot.messagetoslack.data.api.notionClient
import io.orot.messagetoslack.data.api.slackClient
import io.orot.messagetoslack.model.notion.NotionTransaction
import io.orot.messagetoslack.model.slack.RequestMsg
import io.reactivex.Single
import retrofit2.Response

class NotionNetworkDataSource {

    fun registerTransaction(msg: NotionTransaction): Single<Response<Void>> {
        return notionClient.registerTransaction(msg)
    }
}