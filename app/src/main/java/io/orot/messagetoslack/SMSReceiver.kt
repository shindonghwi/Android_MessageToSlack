package io.orot.messagetoslack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import io.orot.messagetoslack.data.repository.SlackRepository
import io.orot.messagetoslack.model.RequestMsg
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SMSReceiver : BroadcastReceiver() {

    var compositeDisposable = CompositeDisposable()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach { smsMessage ->
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