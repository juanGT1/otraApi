package com.example.myapi.model;

import com.example.myapi.interfas.ApiPeliculas;
import com.example.myapi.interfas.Direccionmodelo;
import com.example.myapi.interfas.Direccionpresenter;

import retrofit2.Call;

public class Modelo implements Direccionmodelo {
    private Direccionpresenter presenter;
    public Modelo(Direccionpresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void leerpelis(String g) {
        Call<results> call = LlamaApi.getllamaApi().create(ApiPeliculas.class)
                .leerBusqueda("511174b10ded86a2c00770c3b55b5b88","es-US"," "+ g);

        presenter.ResultBusqueda(call);

    }



}
