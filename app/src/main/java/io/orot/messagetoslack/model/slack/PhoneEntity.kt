package io.orot.messagetoslack.model.slack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhoneEntity(
    val phone: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
