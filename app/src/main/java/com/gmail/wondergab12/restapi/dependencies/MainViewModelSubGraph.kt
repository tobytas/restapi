package com.gmail.wondergab12.restapi.dependencies

import androidx.appcompat.app.AppCompatActivity
import com.gmail.wondergab12.restapi.vm.MainViewModel
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [MainViewModelModule::class])
interface MainViewModelSubGraph {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun withActivity(activity: AppCompatActivity): Builder

        fun build(): MainViewModelSubGraph
    }

    fun inject(viewModel: MainViewModel)
}
