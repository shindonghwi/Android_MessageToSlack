package io.orot.messagetoslack

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import io.orot.messagetoslack.model.IncludeCharactersEntity
import io.orot.messagetoslack.model.PhoneEntity
import io.orot.messagetoslack.ui.theme.MessageToSlackTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        requestSmsPermission()
        loadSlackData(viewModel)

        setContent {
            MessageToSlackTheme {
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
                                        (applicationContext as App).db?.let { database ->
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
                            CustomTextField(caption = "수신 전화번호", keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next, isPhoneField = true, viewModel = viewModel)
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
                        PhoneList(viewModel.phoneList[index])
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
                        CharacterList(viewModel.characterList[index])
                    }
                }
            }
        }
    }

    @Composable
    private fun CharacterList(character: String) {
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
                    text = character,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )

                IconButton(
                    modifier = Modifier
                        .weight(2f)
                        .padding(4.dp)
                        .size(24.dp), onClick = {
                        (applicationContext as App).db?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                database.slackDao().deleteCharacter(character)
                                viewModel.removeCharacter(character)
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

    @Composable
    private fun PhoneList(phone: String) {
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
                        (applicationContext as App).db?.let { database ->
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

    private fun loadSlackData(viewModel: MainActivityViewModel) {
        (applicationContext as App).db?.let { database ->
            CoroutineScope(Dispatchers.IO).launch {
                val resultPhone = database.slackDao().getPhones().map { it.phone as String }.toMutableList()
                val resultCharacters = database.slackDao().getIncludeCharacters().map { it.character as String }.toMutableList()
                viewModel.addAllPhoneList(resultPhone)
                viewModel.addAllCharacterList(resultCharacters)
            }
        }
    }

    /** 뷰 모델 생성 */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTextField(caption: String, keyboardType: KeyboardType, imeAction: ImeAction, isPhoneField: Boolean, viewModel: MainActivityViewModel) {
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
                                            database.slackDao().insertCharacter(IncludeCharactersEntity(textState))
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

fun LazyListScope.gridItems(
    count: Int,
    nColumns: Int,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    itemContent: @Composable BoxScope.(Int) -> Unit,
) {
    gridItems(
        data = List(count) { it },
        nColumns = nColumns,
        horizontalArrangement = horizontalArrangement,
        itemContent = itemContent,
    )
}

fun <T> LazyListScope.gridItems(
    data: List<T>,
    nColumns: Int,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    key: ((item: T) -> Any)? = null,
    itemContent: @Composable BoxScope.(T) -> Unit,
) {
    val rows = if (data.isEmpty()) 0 else 1 + (data.count() - 1) / nColumns
    items(rows) { rowIndex ->
        Row(horizontalArrangement = horizontalArrangement) {
            for (columnIndex in 0 until nColumns) {
                val itemIndex = rowIndex * nColumns + columnIndex
                if (itemIndex < data.count()) {
                    val item = data[itemIndex]
                    androidx.compose.runtime.key(key?.invoke(item)) {
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            itemContent.invoke(this, item)
                        }
                    }
                } else {
                    Spacer(Modifier.weight(1f, fill = true))
                }
            }
        }
    }
}