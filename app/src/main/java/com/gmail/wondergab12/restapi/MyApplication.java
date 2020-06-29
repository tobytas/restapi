package com.gmail.wondergab12.restapi;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.wondergab12.restapi.dependencies.ApplicationGraph;
import com.gmail.wondergab12.restapi.dependencies.DaggerApplicationGraph;
import com.gmail.wondergab12.restapi.dependencies.MainViewModelSubGraph;

public class MyApplication extends Application {

    private ApplicationGraph applicationGraph;
    private MainViewModelSubGraph mainViewModelSubGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationGraph = DaggerApplicationGraph
                .builder()
                .withApplication(this)
                .build();
    }

    public ApplicationGraph getApplicationGraph(AppCompatActivity activity) {
        mainViewModelSubGraph = applicationGraph
                .mainViewModelSubGraphBuilder()
                .withActivity(activity)
                .build();
        return applicationGraph;
    }

    public MainViewModelSubGraph getMainViewModelSubGraph() {
        return mainViewModelSubGraph;
    }
}
