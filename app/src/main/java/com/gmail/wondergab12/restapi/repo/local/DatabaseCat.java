package com.gmail.wondergab12.restapi.repo.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.gmail.wondergab12.restapi.repo.model.Cat;

@Database(entities = {Cat.class}, version = 1, exportSchema = false)
public abstract class DatabaseCat extends RoomDatabase {

    public abstract CatDao daoCat();
}
