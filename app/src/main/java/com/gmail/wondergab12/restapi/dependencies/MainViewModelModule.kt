package com.gmail.wondergab12.restapi.dependencies

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.gmail.wondergab12.restapi.repo.local.DatabaseCat
import com.gmail.wondergab12.restapi.repo.model.Cat
import com.gmail.wondergab12.restapi.repo.network.TheCatApiService
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class MainViewModelModule {

    @Provides
    fun provideMutableListCatLiveData(): MutableLiveData<MutableList<Cat>> {
        return MutableLiveData()
    }

    @Provides
    fun provideMutableThrowableLiveData(): MutableLiveData<Throwable> {
        return MutableLiveData()
    }

    @Provides
    fun provideTheCatApiService(): TheCatApiService {
        return Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(TheCatApiService::class.java)
    }

    @Provides
    fun provideKDatabaseCat(activity: AppCompatActivity): DatabaseCat {
        return Room.databaseBuilder(activity, DatabaseCat::class.java, "database")
                .build()
    }

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}
