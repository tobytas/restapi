package com.gmail.wondergab12.restapi.dependencies

import androidx.appcompat.app.AppCompatActivity
import com.gmail.wondergab12.restapi.MainActivity
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [MainActivityModule::class])
interface MainActivitySubGraph {

    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun with(activity: AppCompatActivity): Builder

        fun build(): MainActivitySubGraph
    }

    fun inject(mainActivity: MainActivity)
}
