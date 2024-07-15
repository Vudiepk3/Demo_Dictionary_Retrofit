package com.example.demo_dictionary_retrofit.retrofit;

import com.example.demo_dictionary_retrofit.api.DictionaryApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Lớp Singleton để quản lý instance của Retrofit
public class RetrofitInstance {

    private static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/";

    private static Retrofit retrofit;

    // Phương thức để lấy instance của Retrofit
    private static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Instance của DictionaryApi
    public static final DictionaryApi dictionaryApi = getInstance().create(DictionaryApi.class);
}
