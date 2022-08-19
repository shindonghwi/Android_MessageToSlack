package io.orot.messagetoslack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
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
import kotlin.math.log

class SMSReceiver : BroadcastReceiver() {

    var compositeDisposable = CompositeDisposable()
    val TAG = "onReceive"

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d(TAG, "onReceive: ")
        if (!intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
        Log.d(TAG, "SMS called: ")

        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        var totalMsg = ""
        var phone = ""

        extractMessages.forEach { smsMessage ->

            Log.d(TAG, "DisplayMEssageBody :: " + smsMessage.displayMessageBody);
            Log.d(TAG, "DisplayOrinatingAddress" + smsMessage.displayOriginatingAddress);
            Log.d(TAG, "EmailBody :: " + smsMessage.emailBody);
            Log.d(TAG, "originatingAddress :: " + smsMessage.originatingAddress);
            Log.d(TAG, "MessageBody :: " + smsMessage.messageBody);
            Log.d(TAG, "serviceCenterAddress :: " + smsMessage.serviceCenterAddress);
            Log.d(TAG, "time :: " + smsMessage.timestampMillis);

            phone = smsMessage.displayOriginatingAddress
            totalMsg += smsMessage.messageBody
        }

        (context?.applicationContext as App).db?.let { database ->
            CoroutineScope(Dispatchers.IO).launch {

                var sendingTransmissionPhoneStatus = false
                var sendingTransmissionCharacterStatus = false

                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    if (database.slackDao().getPhone(phone) != null){
                        sendingTransmissionPhoneStatus = true
                    }

                    val charactersList = database.slackDao().getIncludeCharacters()

                    for(charEntity in charactersList){
                        if (totalMsg.contains(charEntity.character.toString())){
                            sendingTransmissionCharacterStatus = true
                            break
                        }
                    }
                }

                if (sendingTransmissionPhoneStatus && sendingTransmissionCharacterStatus) {
                        compositeDisposable.add(
                            SlackRepository
                                .sendMsgToSlack(RequestMsg(msg = totalMsg))
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .subscribe()
                        )
                }
            }
        }
    }
}