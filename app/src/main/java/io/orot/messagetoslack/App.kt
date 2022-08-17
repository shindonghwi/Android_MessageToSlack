package io.orot.messagetoslack

import android.app.Application
import io.orot.messagetoslack.data.SlackDatabase

class App: Application(){
    val db by lazy { SlackDatabase.getInstance(this) }
}