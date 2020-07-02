package com.gmail.wondergab12.restapi

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.gmail.wondergab12.restapi.dependencies.ApplicationGraph
import com.gmail.wondergab12.restapi.dependencies.DaggerApplicationGraph
import com.gmail.wondergab12.restapi.dependencies.MainViewModelSubGraph

class MyApplication : Application() {

    private lateinit var applicationGraph: ApplicationGraph
    private lateinit var mainViewModelSubGraph: MainViewModelSubGraph

    override fun onCreate() {
        super.onCreate()
        applicationGraph = DaggerApplicationGraph.builder()
                .withApplication(this)
                .build()
    }

    fun getApplicationGraph(activity: AppCompatActivity): ApplicationGraph {
        mainViewModelSubGraph = applicationGraph.mainViewModelSubGraphBuilder()
                .withActivity(activity)
                .build()
        return applicationGraph
    }

    fun getMainViewModelSubGraph(): MainViewModelSubGraph {
        return mainViewModelSubGraph
    }
}
