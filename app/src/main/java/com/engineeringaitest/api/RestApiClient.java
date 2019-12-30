package com.engineeringaitest.api;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Rest Api Client for create/get instance of Retrofit
 */
public class RestApiClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "http://sd2-hiring.herokuapp.com/api/";
    private static final int TIMEOUT = 60 * 3;// minutes

    public static ApiInterface getApiInterface() {
        return createRetrofit().create(ApiInterface.class);
    }

    private static Retrofit createRetrofit() {
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = getBuilder();
            httpClient.protocols(Arrays.asList(Protocol.HTTP_1_1));
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
        }
        return retrofit;
    }

    @NonNull
    private static OkHttpClient.Builder getBuilder() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        // add logging interceptor for add logs.
        httpClient.addInterceptor(logging);
        return httpClient;
    }
}
