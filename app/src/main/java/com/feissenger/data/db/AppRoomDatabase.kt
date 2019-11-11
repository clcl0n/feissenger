package com.feissenger.data.db

import android.content.Context
import androidx.room.*
import com.feissenger.data.db.model.ContactItem
import com.feissenger.data.db.model.MessageItem
import com.feissenger.data.db.model.RoomItem
import com.feissenger.data.db.model.RoomMessageItem

@Database(
    entities = [MessageItem::class, RoomItem::class, ContactItem::class, RoomMessageItem::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun appDao(): DbDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getInstance(context: Context): AppRoomDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppRoomDatabase::class.java, "MOBV.db"
            ).fallbackToDestructiveMigration()
                .build()

    }

}