package com.dhbw.unilife.rnv.network;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RnvRetrofitClient {

    private static Retrofit rnv_retrofit;
    public static final String RNV_BASE_URL = "https://www.vrn.de/";
    public static Retrofit getRetrofitInstance(){ // Instanz der Retrofit Library erstellen bzw zurückgeben wenn schon existiert
        if (rnv_retrofit == null){
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            rnv_retrofit = new Retrofit.Builder()
                    .baseUrl(RNV_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return rnv_retrofit;
    }
}
