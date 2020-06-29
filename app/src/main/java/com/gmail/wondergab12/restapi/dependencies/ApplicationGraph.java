package com.gmail.wondergab12.restapi.dependencies;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationGraph {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder withApplication(Application application);

        ApplicationGraph build();
    }

    MainViewModelSubGraph.Builder mainViewModelSubGraphBuilder();

    MainActivitySubGraph.Builder mainActivitySubGraphBuilder();
}
