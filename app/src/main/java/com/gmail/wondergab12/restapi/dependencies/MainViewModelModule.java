package com.gmail.wondergab12.restapi.dependencies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.gmail.wondergab12.restapi.repo.model.Cat;
import com.gmail.wondergab12.restapi.repo.network.TheCatApiService;
import com.gmail.wondergab12.restapi.repo.local.DatabaseCat;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MainViewModelModule {

    @Provides
    public MutableLiveData<List<Cat>> provideMutableListCatLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public MutableLiveData<Throwable> provideMutableThrowableLiveData() {
        return new MutableLiveData<>();
    }

    @Provides
    public TheCatApiService provideTheCatApiService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.thecatapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .create(TheCatApiService.class);
    }

    @MainViewModelScope
    @Provides
    public DatabaseCat provideDatabaseCat(AppCompatActivity activity) {
        return Room.databaseBuilder(activity, DatabaseCat.class, "database")
                .build();
    }

    @Provides
    public CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }
}
