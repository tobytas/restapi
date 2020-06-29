package com.gmail.wondergab12.restapi.repo.network;

import com.gmail.wondergab12.restapi.repo.model.Cat;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import javax.inject.Inject;

public class NetworkRepository {

    private TheCatApiService service;

    @Inject
    public NetworkRepository(TheCatApiService service) {
        this.service = service;
    }

    public Observable<List<Cat>> getList(int count) {
        return service.getCatList(count);
    }
}
