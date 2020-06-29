package com.gmail.wondergab12.restapi.repo.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.gmail.wondergab12.restapi.repo.model.Cat;

import java.util.List;

@Dao
public interface CatDao {

    @Query("SELECT * FROM cats")
    List<Cat> getAll();

    @Insert
    long insert(Cat cat);

    @Delete
    int delete(Cat cat);
}
