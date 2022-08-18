package io.orot.messagetoslack.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.orot.messagetoslack.App
import io.orot.messagetoslack.MainVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MainView(viewModel: MainVM){
    val app = LocalContext.current.applicationContext as App

    LazyColumn(modifier = Modifier.padding(top = 12.dp)) {

        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "오롯코드")
                    Text(modifier = Modifier
                        .clickable {
                            app.db?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database
                                        .slackDao()
                                        .deleteAllCharacter()
                                    database
                                        .slackDao()
                                        .deleteAllPhone()
                                    viewModel.clearPhoneList()
                                    viewModel.clearCharacterList()
                                }
                            }
                        }
                        .padding(8.dp), text = "전체삭제")
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                CustomTextField(caption = "수신 전화번호", keyboardType = KeyboardType.Text, imeAction = ImeAction.Next, isPhoneField = true, viewModel = viewModel)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                CustomTextField(caption = "포함문자", keyboardType = KeyboardType.Text, imeAction = ImeAction.Done, isPhoneField = false, viewModel = viewModel)
            }
            Divider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xff4f5867))
            )
        }

        item {
            Text(modifier = Modifier.padding(horizontal = 16.dp), text = "등록된 전화번호")
        }

        gridItems(viewModel.phoneList.size, nColumns = 2) { index ->
            PhoneList(viewModel.phoneList[index], viewModel)
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            Divider(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(color = Color(0xff4f5867))
            )
        }

        item {
            Text(modifier = Modifier.padding(horizontal = 16.dp), text = "등록된 문자")
        }

        gridItems(viewModel.characterList.size, nColumns = 2) { index ->
            CharacterList(viewModel.characterList[index], viewModel)
        }
    }
}