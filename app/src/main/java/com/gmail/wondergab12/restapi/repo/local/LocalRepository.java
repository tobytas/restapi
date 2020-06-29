package com.gmail.wondergab12.restapi.repo.local;

import com.gmail.wondergab12.restapi.repo.model.Cat;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import javax.inject.Inject;

@SuppressWarnings("Convert2Lambda")
public class LocalRepository {

    private DatabaseCat database;

    @Inject
    public LocalRepository(DatabaseCat database) {
        this.database = database;
    }

    public Observable<List<Cat>> getAll() {
        return Observable.create(new ObservableOnSubscribe<List<Cat>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Cat>> emitter) {
                emitter.onNext(database.daoCat().getAll());
            }
        });
    }

    public Observable<Long> insert(Cat cat) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) {
                emitter.onNext(database.daoCat().insert(cat));
            }
        });
    }

    public Observable<Integer> delete(Cat cat) {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) {
                emitter.onNext(database.daoCat().delete(cat));
            }
        });
    }
}
