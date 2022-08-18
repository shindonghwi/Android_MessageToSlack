package io.orot.messagetoslack.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.SYSTEM_HIGH_PRIORITY
import android.os.IBinder
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.Task
import io.orot.messagetoslack.SMSReceiver


class SmsProcessService : Service() {
    var smsReceiver: SMSReceiver? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val mClient = SmsRetriever.getClient(this)
        val mTask: Task<Void> = mClient.startSmsRetriever()
        mTask.addOnSuccessListener {
            Log.d("Asdasd", "onStartCommand: ")
            initSmsListener()
        }

        return START_STICKY
    }

    /** sms retriever 초기화 */
    private fun Service.initSmsListener() {
        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()

        task.addOnSuccessListener {
            Log.d("Asdasd", "task Success: ")

            if (smsReceiver != null) {
                Log.d("Asdasd", "unregisterReceiver: ")
                unregisterReceiver(smsReceiver)
            }
            smsReceiver = SMSReceiver()
            registerReceiver(smsReceiver, IntentFilter().apply {
                addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
                 priority = SYSTEM_HIGH_PRIORITY
            })

            smsReceiver?.setOTPListener(object : SMSReceiver.OTPReceiveListener {
                override fun onOTPReceived(otp: String?) {
                    Log.d("ASDasdasd", "onOTPReceived: $otp")
                    initSmsListener()
                }
            })
        }
    }
}