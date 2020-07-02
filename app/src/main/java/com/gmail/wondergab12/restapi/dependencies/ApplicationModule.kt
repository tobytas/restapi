package com.gmail.wondergab12.restapi.dependencies

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.gmail.wondergab12.restapi.vm.MainViewModel
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule {

    @ApplicationScope
    @Provides
    fun provideMainViewModel(application: Application): MainViewModel {
        return ViewModelProvider.AndroidViewModelFactory
                .getInstance(application)
                .create(MainViewModel::class.java)
    }
}
