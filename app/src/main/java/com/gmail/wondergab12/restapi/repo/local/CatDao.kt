package com.gmail.wondergab12.restapi.repo.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.gmail.wondergab12.restapi.repo.model.Cat

@Dao
interface CatDao {

    @Query("SELECT * FROM cats")
    fun getAll(): MutableList<Cat>

    @Insert
    fun insert(cat: Cat): Long

    @Delete
    fun delete(cat: Cat): Int
}
