package io.orot.messagetoslack

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import io.orot.messagetoslack.service.SmsProcessService
import io.orot.messagetoslack.ui.MainView
import io.orot.messagetoslack.ui.theme.MessageToSlackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainVM
    lateinit var smsServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        requestSmsPermission()
        loadSlackData(viewModel)

        setContent {
            MessageToSlackTheme {
                MainView(viewModel = viewModel)
            }
        }
        smsServiceIntent = Intent(this, SmsProcessService::class.java)
        startService(smsServiceIntent)
    }

    private fun loadSlackData(viewModel: MainVM) {
        (applicationContext as App).db?.let { database ->
            CoroutineScope(Dispatchers.IO).launch {
                val resultPhone =
                    database.slackDao().getPhones().map { it.phone as String }.toMutableList()
                val resultCharacters =
                    database.slackDao().getIncludeCharacters().map { it.character as String }
                        .toMutableList()
                viewModel.addAllPhoneList(resultPhone)
                viewModel.addAllCharacterList(resultCharacters)
            }
        }
    }

    /** 뷰 모델 생성 */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MainVM::class.java]
    }

    /** SMS 퍼미션 체크 */
    private fun requestSmsPermission() {
        val permission = Manifest.permission.RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 1000)
        }
    }
}
