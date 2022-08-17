package io.orot.messagetoslack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import io.orot.messagetoslack.data.repository.SlackRepository
import io.orot.messagetoslack.model.IncludeCharactersEntity
import io.orot.messagetoslack.model.PhoneEntity
import io.orot.messagetoslack.model.RequestMsg
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SMSReceiver : BroadcastReceiver() {

    var compositeDisposable = CompositeDisposable()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach { smsMessage ->

            val phone = smsMessage.displayOriginatingAddress
            val message = smsMessage.displayMessageBody

            (context?.applicationContext as App).db?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {

                    var resultCharacter: IncludeCharactersEntity?
                    var resultPhone: PhoneEntity?

                    withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                        resultPhone = database.slackDao().getPhone(phone)
                        resultCharacter = database.slackDao().getIncludeCharacter(message)
                    }
                    if (resultPhone != null && resultCharacter != null){
                        compositeDisposable.add(
                            SlackRepository
                                .sendMsgToSlack(RequestMsg(msg = smsMessage.displayMessageBody))
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe()
                        )
                    }
                }
            }
        }
    }
}