package io.orot.messagetoslack.data.repository

import io.orot.messagetoslack.data.datasource.NotionNetworkDataSource
import io.orot.messagetoslack.model.notion.NotionTransaction
import io.orot.messagetoslack.model.slack.RequestMsg
import io.reactivex.Single
import retrofit2.Response

object NotionRepository{

    private var notionDataSource = NotionNetworkDataSource()

    fun registerTransaction(msg: NotionTransaction): Single<Response<Void>>{
        return notionDataSource.registerTransaction(msg)
    }
}
