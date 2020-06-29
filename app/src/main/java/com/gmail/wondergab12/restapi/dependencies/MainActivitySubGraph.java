package com.gmail.wondergab12.restapi.dependencies;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.wondergab12.restapi.MainActivity;

import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent(modules = MainActivityModule.class)
public interface MainActivitySubGraph {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        Builder with(AppCompatActivity activity);

        MainActivitySubGraph build();
    }

    void inject(MainActivity mainActivity);
}
