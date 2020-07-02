package com.gmail.wondergab12.restapi.repo.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "cats")
class Cat (
        @PrimaryKey
        @NonNull

        @Expose
        @SerializedName("id")
        var id: String,

        @Expose
        @SerializedName("url")
        var url: String
)
