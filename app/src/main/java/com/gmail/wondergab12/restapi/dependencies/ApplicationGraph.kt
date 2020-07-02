package com.gmail.wondergab12.restapi.dependencies

import android.app.Application
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class])
interface ApplicationGraph {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withApplication(application: Application): Builder

        fun build(): ApplicationGraph
    }

    fun mainViewModelSubGraphBuilder(): MainViewModelSubGraph.Builder

    fun mainActivitySubGraphBuilder(): MainActivitySubGraph.Builder
}
