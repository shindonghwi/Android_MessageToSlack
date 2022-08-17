package io.orot.messagetoslack.data.service

import androidx.room.*
import io.orot.messagetoslack.model.IncludeCharactersEntity
import io.orot.messagetoslack.model.PhoneEntity

@Dao
interface SlackDao {

    @Query("Select * From PhoneEntity")
    fun getPhones(): List<PhoneEntity>

    @Query("Select * From PhoneEntity WHERE phone IN (:phone)")
    fun getPhone(phone: String): PhoneEntity

    @Query("Select * From IncludeCharactersEntity")
    fun getIncludeCharacters(): List<IncludeCharactersEntity>

    @Query("Select * From IncludeCharactersEntity WHERE character IN (:character)")
    fun getIncludeCharacter(character: String): IncludeCharactersEntity

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertPhone(phone: PhoneEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCharacter(character: IncludeCharactersEntity): Long

    @Query("DELETE FROM PhoneEntity WHERE phone = (:phone)")
    fun deletePhone(phone: String)

    @Query("DELETE FROM IncludeCharactersEntity WHERE character = (:character)")
    fun deleteCharacter(character: String)

    @Query("DELETE FROM PhoneEntity")
    fun deleteAllPhone()

    @Query("DELETE FROM IncludeCharactersEntity")
    fun deleteAllCharacter()



}