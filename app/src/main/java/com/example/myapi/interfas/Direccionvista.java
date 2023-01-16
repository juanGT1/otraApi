package com.example.myapi.interfas;

import com.example.myapi.model.results;

import retrofit2.Call;

public interface Direccionvista {

    void ResultBusqueda(Call<results> call);

    void error(String err);
    //void leerPeliculas(String peli);

}
