package com.example.myapi.model;


import static com.example.myapi.constantes.Constantes.BASE_URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LlamaApi {
   private static Retrofit retrofit;
   public static Retrofit getllamaApi(){
      retrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
      return  retrofit;
   }


}