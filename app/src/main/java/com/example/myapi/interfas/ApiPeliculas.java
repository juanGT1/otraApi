package com.example.myapi.interfas;

import java.util.List;


import com.example.myapi.model.DatosApi;
import com.example.myapi.model.results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiPeliculas {



    @GET("3/movie/popular")
    Call<results> leerPeliculas(@Query("api_key") String api_key, @Query("page") int page,
                                @Query("language")String language);

@GET("3/search/movie")
Call<results> leerBusqueda(@Query("api_key") String api_key,
                                      @Query("language")String language,
                                      @Query("query") String query);

    @GET("3/movie/upcoming")
    Call<results> leerpopular(@Query("api_key") String api_key, @Query("page") int page,
                                    @Query("language")String language);


    @GET("3/movie/top_rated")
    Call<results> leermastop(@Query("api_key") String api_key, @Query("page") int page,
                              @Query("language")String language);


}



