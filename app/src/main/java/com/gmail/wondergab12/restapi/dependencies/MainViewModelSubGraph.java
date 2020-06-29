package com.gmail.wondergab12.restapi.dependencies;

import androidx.appcompat.app.AppCompatActivity;

import com.gmail.wondergab12.restapi.vm.MainViewModel;

import dagger.BindsInstance;
import dagger.Subcomponent;

@MainViewModelScope
@Subcomponent(modules = MainViewModelModule.class)
public interface MainViewModelSubGraph {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        Builder withActivity(AppCompatActivity activity);

        MainViewModelSubGraph build();
    }

    void inject(MainViewModel viewModel);
}
