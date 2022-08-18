package io.orot.messagetoslack.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.orot.messagetoslack.App
import io.orot.messagetoslack.MainVM
import io.orot.messagetoslack.model.IncludeCharactersEntity
import io.orot.messagetoslack.model.PhoneEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTextField(caption: String, keyboardType: KeyboardType, imeAction: ImeAction, isPhoneField: Boolean, viewModel: MainVM) {
    val app = LocalContext.current.applicationContext as App
    var textState by remember { mutableStateOf("") }
    val kc = LocalSoftwareKeyboardController.current

    Column {
        val maxLength = 110
        val lightBlue = Color(0xffd8e6ff)
        val blue = Color(0xff76a9ff)
        Text(
            text = caption,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            textAlign = TextAlign.Start,
            color = blue
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = lightBlue,
                cursorColor = Color.Black,
                disabledLabelColor = lightBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            onValueChange = {
                if (it.length <= maxLength) textState = it
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
            keyboardActions = KeyboardActions(onDone = { kc?.hide() }),
            trailingIcon = {
                IconButton(onClick = {
                    app.db?.let { database ->
                        CoroutineScope(Dispatchers.IO).launch {
                            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                                if (textState.trim().isNotEmpty()) {
                                    if (isPhoneField) {
                                        if (!viewModel.phoneList.contains(textState)) {
                                            database.slackDao().insertPhone(PhoneEntity(textState))
                                            viewModel.addPhone(textState)
                                        }
                                    } else {
                                        if (!viewModel.characterList.contains(textState)) {
                                            database.slackDao().insertCharacter(
                                                IncludeCharactersEntity(textState)
                                            )
                                            viewModel.addCharacter(textState)
                                        }
                                    }
                                }
                                textState = ""
                            }
                        }
                    }
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = null
                    )
                }
            }
        )
    }
}
