package io.orot.messagetoslack.model.slack

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class IncludeCharactersEntity(
    val character: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
