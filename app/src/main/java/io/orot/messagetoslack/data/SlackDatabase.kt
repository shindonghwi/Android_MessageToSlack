package io.orot.messagetoslack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.orot.messagetoslack.data.service.SlackDao
import io.orot.messagetoslack.model.slack.IncludeCharactersEntity
import io.orot.messagetoslack.model.slack.PhoneEntity

@Database(
    entities = [PhoneEntity::class, IncludeCharactersEntity::class],
    version = 1,
    exportSchema = false
)

abstract class SlackDatabase: RoomDatabase() {
    abstract fun slackDao() : SlackDao

    companion object {
        private var instance: SlackDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SlackDatabase? {
            if (instance == null) {
                synchronized(SlackDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SlackDatabase::class.java,
                        "slack-database"
                    )
                        .build()
                }
            }
            return instance
        }
    }
}