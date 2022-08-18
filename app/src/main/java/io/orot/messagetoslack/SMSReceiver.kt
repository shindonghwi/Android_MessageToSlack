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

class SMSReceiver : BroadcastReceiver() {

    var compositeDisposable = CompositeDisposable()

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("ASdsadsdaads", "onReceive: ")
        if (!intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
        Log.d("ASdsadsdaads", "called: ")
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach { smsMessage ->
            val TAG = "ASdkjl;ass"
            Log.d(TAG, "DisplayMEssageBody :: " + smsMessage.getDisplayMessageBody());
            Log.d(TAG, "DisplayOrinatingAddress" + smsMessage.displayOriginatingAddress);
            Log.d(TAG, "EmailBody :: " + smsMessage.getEmailBody());
            Log.d(TAG, "originatingAddress :: " + smsMessage.getOriginatingAddress());
            Log.d(TAG, "MessageBody :: " + smsMessage.getMessageBody());
            Log.d(TAG, "serviceCenterAddress :: " + smsMessage.getServiceCenterAddress());
            Log.d(TAG, "time :: " + smsMessage.getTimestampMillis());

            val phone = smsMessage.displayOriginatingAddress
            val message = smsMessage.displayMessageBody

            Log.d("ASdsadsdaads", "$phone ")
            Log.d("ASdsadsdaads", "$message ")

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
                            if (message.contains(charEntity.character.toString())){
                                sendingTransmissionCharacterStatus = true
                                break
                            }
                        }
                    }

                    if (sendingTransmissionPhoneStatus && sendingTransmissionCharacterStatus) {
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