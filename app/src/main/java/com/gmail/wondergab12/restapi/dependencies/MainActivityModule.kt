package com.gmail.wondergab12.restapi.dependencies

import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.wondergab12.restapi.R
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    private var remote: Boolean = true

    @Provides
    fun provideButton(activity: AppCompatActivity): Button {
        if (remote) {
            remote = false
            return activity.findViewById(R.id.button)
        }
        return activity.findViewById(R.id.button2)
    }

    @Provides
    fun provideRecyclerView(activity: AppCompatActivity): RecyclerView {
        val recyclerView: RecyclerView = activity.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        return recyclerView
    }

    @Provides
    fun providesProgressBar(activity: AppCompatActivity): ProgressBar {
        return activity.findViewById(R.id.progressBar)
    }
}