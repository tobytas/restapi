package com.gmail.wondergab12.restapi.repo.network;

import com.gmail.wondergab12.restapi.repo.model.Cat;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TheCatApiService {

    @GET("/v1/images/search")
    Observable<List<Cat>> getCatList(@Query("limit") int count);
}
