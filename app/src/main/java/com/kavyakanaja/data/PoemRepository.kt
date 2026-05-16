package com.kavyakanaja.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kavyakanaja.model.Poem
import java.io.InputStreamReader

class PoemRepository(private val context: Context) {

    fun getPoems(): List<Poem> {
        return try {
            val inputStream = context.assets.open("poems.json")
            val reader = InputStreamReader(inputStream)
            val listType = object : TypeToken<List<Poem>>() {}.type
            Gson().fromJson(reader, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun getPoemOfTheDay(): Poem? {
        val poems = getPoems()
        if (poems.isEmpty()) return null
        
        // Simple logic to pick a poem based on the day of the year
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        val index = dayOfYear % poems.size
        return poems[index]
    }
    fun getPoemById(id: Int): Poem? {
        val poems = getPoems()
        return poems.find { it.id == id }
    }
}
