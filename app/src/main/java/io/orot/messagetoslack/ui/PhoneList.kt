package io.orot.messagetoslack.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.orot.messagetoslack.App
import io.orot.messagetoslack.MainVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PhoneList(phone: String, viewModel: MainVM) {

    val app = LocalContext.current.applicationContext as App

    Card(
        modifier = Modifier.padding(top = 8.dp, start = 6.dp, end = 6.dp),
        backgroundColor = Color(0xff1e97f3)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(8f),
                text = phone,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            IconButton(
                modifier = Modifier
                    .weight(2f)
                    .padding(4.dp)
                    .size(24.dp), onClick = {
                    app.db?.let { database ->
                        CoroutineScope(Dispatchers.IO).launch {
                            database.slackDao().deletePhone(phone)
                            viewModel.removePhone(phone)
                        }
                    }
                }) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}