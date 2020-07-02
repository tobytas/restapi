package com.gmail.wondergab12.restapi.repo.local

import com.gmail.wondergab12.restapi.repo.model.Cat
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class LocalRepository @Inject constructor(private val database: DatabaseCat) {

    fun getAll(): Observable<MutableList<Cat>> {
        return Observable.create { it.onNext(database.catDao().getAll()) }
    }

    fun insert(cat: Cat): Observable<Long> {
        return Observable.create { it.onNext(database.catDao().insert(cat)) }
    }

    fun delete(cat: Cat): Observable<Int> {
        return Observable.create { it.onNext(database.catDao().delete(cat)) }
    }
}
