package com.gmail.wondergab12.restapi.repo.network

import com.gmail.wondergab12.restapi.repo.model.Cat
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface TheCatApiService {
    
    @GET("/v1/images/search")
    fun getCatList(@Query("limit") count: Int): Observable<MutableList<Cat>>
}
