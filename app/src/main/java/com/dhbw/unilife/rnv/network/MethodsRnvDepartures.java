package com.dhbw.unilife.rnv.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MethodsRnvDepartures {
    @GET
    Call<ModelRnvDepartures> getAllData(
            @Url String url
    );
}
