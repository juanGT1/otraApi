package com.example.myapi.presenter;

import com.example.myapi.interfas.Direccionmodelo;
import com.example.myapi.interfas.Direccionpresenter;
import com.example.myapi.interfas.Direccionvista;
import com.example.myapi.model.Modelo;
import com.example.myapi.model.results;

import retrofit2.Call;

public class Presenter implements Direccionpresenter {
     private Direccionvista vista;
     private Direccionmodelo modelo;

    public Presenter(Direccionvista vista) {
        this.vista = vista;
        this.modelo = new Modelo(this);
    }



    @Override
    public void ResultBusqueda(Call<results> call) {

      if (vista != null){
          vista.ResultBusqueda(call);
      }


    }

    @Override
    public void leerpelis(String g) {
        if (modelo != null ){
            modelo.leerpelis(g);
        }else if (g.isEmpty()){
            this.vista.error("ingrese un nombre ");

        }
    }

    @Override
    public void error(String error) {
        vista.error(error);
    }
}
