package io.orot.messagetoslack

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
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
import java.util.regex.Pattern

//class SMSReceiver : BroadcastReceiver() {
//
//    var compositeDisposable = CompositeDisposable()
//
//    override fun onReceive(context: Context?, intent: Intent?) {
//
//        Log.d("ASdsadsdaads", "onReceive: ")
//
////        if (!intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
////        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
////        extractMessages.forEach { smsMessage ->
////
////            val phone = smsMessage.displayOriginatingAddress
////            val message = smsMessage.displayMessageBody
////
////            (context?.applicationContext as App).db?.let { database ->
////                CoroutineScope(Dispatchers.IO).launch {
////
////                    var resultCharacter: IncludeCharactersEntity?
////                    var resultPhone: PhoneEntity?
////
////                    withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
////                        resultPhone = database.slackDao().getPhone(phone)
////                        resultCharacter = database.slackDao().getIncludeCharacter(message)
////                    }
////                    if (resultPhone != null && resultCharacter != null){
////                        compositeDisposable.add(
////                            SlackRepository
////                                .sendMsgToSlack(RequestMsg(msg = smsMessage.displayMessageBody))
////                                .subscribeOn(Schedulers.io())
////                                .observeOn(Schedulers.io())
////                                .subscribe()
////                        )
////                    }
////                }
////            }
////        }
//    }
//}


class SMSReceiver : BroadcastReceiver() {
    private var otpListener: OTPReceiveListener? = null

    fun setOTPListener(otpListener: OTPReceiveListener?) {
        this.otpListener = otpListener
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ASdsadsdaads", "onReceive: ")
//        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
//            val extras = intent.extras
//            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
//            when (status!!.statusCode) {
//                CommonStatusCodes.SUCCESS -> {
//                    val sms = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
//                    sms?.let {
//                        // val p = Pattern.compile("[0-9]+") check a pattern with only digit
//                        val p = Pattern.compile("\\d+")
//                        val m = p.matcher(it)
//                        if (m.find()) {
//                            val otp = m.group()
//                            if (otpListener != null) {
                                otpListener!!.onOTPReceived("otp")
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
    }
}