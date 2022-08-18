package io.orot.messagetoslack

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainVM : ViewModel() {

    var phoneList = mutableStateListOf<String>()
    var characterList = mutableStateListOf<String>()

    fun addAllPhoneList(items: List<String>){
        phoneList.addAll(items)
    }
    fun addAllCharacterList(items: List<String>){
        characterList.addAll(items)
    }

    fun clearPhoneList(){
        phoneList.clear()
    }
    fun clearCharacterList(){
        characterList.clear()
    }

    fun addPhone(item: String){
        phoneList.add(item)
    }

    fun addCharacter(item: String){
        characterList.add(item)
    }

    fun removePhone(item: String){
        phoneList.remove(item)
    }

    fun removeCharacter(item: String){
        characterList.remove(item)
    }

}