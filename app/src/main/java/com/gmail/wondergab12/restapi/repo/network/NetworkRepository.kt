package com.gmail.wondergab12.restapi.repo.network

import com.gmail.wondergab12.restapi.repo.model.Cat
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class NetworkRepository @Inject constructor(private val service: TheCatApiService) {

    fun getList(count: Int): Observable<MutableList<Cat>> {
        return service.getCatList(count)
    }
}
