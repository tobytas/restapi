package com.gmail.wondergab12.restapi.dependencies;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.gmail.wondergab12.restapi.vm.MainViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@SuppressWarnings("WeakerAccess")
@Module
public class ApplicationModule {

    @Singleton
    @Provides
    public MainViewModel provideMainViewModel(Application application) {
        return ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
                .create(MainViewModel.class);
    }
}
