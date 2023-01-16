package com.example.myapi.interfas;

import com.example.myapi.model.results;

import retrofit2.Call;

public interface Direccionpresenter {
    void ResultBusqueda(Call<results> call);
    void leerpelis(String g);

    void error(String error);
}
