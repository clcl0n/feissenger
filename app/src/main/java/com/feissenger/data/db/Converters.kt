package com.feissenger.data.db

import androidx.room.TypeConverter
import com.giphy.sdk.core.models.Media
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun listToJson(value: List<String>?): String {

        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<String>? {

        val objects = Gson().fromJson(value, Array<String>::class.java) as Array<String>
        val list = objects.toList()
        return list
    }

    @TypeConverter
    fun mediaToJson(value: Media?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToMedia(value: String): Media? {
        if(value == "null")
            return null
        val obj = Gson().fromJson(value, Media::class.java) as Media
        return obj
    }
}