package io.orot.messagetoslack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import io.orot.messagetoslack.data.repository.NotionRepository
import io.orot.messagetoslack.data.repository.SlackRepository
import io.orot.messagetoslack.model.notion.*
import io.orot.messagetoslack.model.slack.RequestMsg
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    if (database.slackDao().getPhone(phone) != null) {
                        sendingTransmissionPhoneStatus = true
                    }

                    val charactersList = database.slackDao().getIncludeCharacters()

                    for (charEntity in charactersList) {
                        if (totalMsg.contains(charEntity.character.toString())) {
                            sendingTransmissionCharacterStatus = true
                            break
                        }
                    }
                }

                if (sendingTransmissionPhoneStatus && sendingTransmissionCharacterStatus) {
                    compositeDisposable.add(
                        SlackRepository.sendMsgToSlack(RequestMsg(msg = totalMsg))
                            .subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe()
                    )

                    registerTransactionInNotion(totalMsg)

                }
            }
        }
    }

    private fun registerTransactionInNotion(msg: String) {
        try{
            val msgSplit = msg.split("\n")
            val isPriceMinus = msgSplit[2].split(" ").first() == "출금"

            if (isPriceMinus) {
                val date = msgSplit[1].split(" ").first().replace("/", "-")
                val regex = Regex("\\d+")
                val isPriceString = msgSplit[2].split(" ").last()
                val price =
                    regex.findAll(isPriceString).joinToString(separator = "") { it.value }.toInt()
                val place = msgSplit[4].trim()

                compositeDisposable.add(
                    NotionRepository.registerTransaction(
                        NotionTransaction(
                            parent = NotionTransactionParent(),
                            properties = NotionTransactionProperties(
                                NotionTransactionPropertiesDate(
                                    NotionTransactionPropertiesDateDetail(date)
                                ),
                                NotionTransactionPropertiesPrice(price),
                                NotionTransactionPropertiesContent(
                                    listOf(
                                        NotionTransactionPropertiesContentInfo(
                                            type = "text",
                                            text = NotionTransactionPropertiesContentInfoText(
                                                place
                                            )
                                        )
                                    )
                                ),
                                NotionTransactionPropertiesPayment(
                                    select = NotionTransactionPropertiesPaymentInfo(
                                        name = "카드"
                                    )
                                )
                            )
                        )
                    ).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe()
                )
            }
        }catch (e: Exception){
            compositeDisposable.add(
                SlackRepository.sendMsgToSlack(RequestMsg(msg = "출금 내역 등록에러"))
                    .subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe()
            )
        }

    }
}
