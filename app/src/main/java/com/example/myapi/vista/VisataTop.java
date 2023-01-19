package com.example.myapi.vista;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.myapi.Lista_y_Adapters.AdapterPeliculas;
import com.example.myapi.R;
import com.example.myapi.interfas.ApiPeliculas;
import com.example.myapi.model.DatosApi;
import com.example.myapi.model.LlamaApi;
import com.example.myapi.model.results;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisataTop extends AppCompatActivity  {
    private List<DatosApi> lista = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterPeliculas adapterPeliculas;
    private int page;
    private boolean cargaPagina;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visata_top);
        recyclerView = (RecyclerView) findViewById(R.id.recyclertop);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0){

                    int pastVertical = layoutManager.findLastVisibleItemPosition();
                    if (cargaPagina &&  pastVertical==5){
                        if (page==1){

                        } else  {
                            cargaPagina = false;
                            page-=1;
                            optenDatos(page);
                        }
                    }

                }


                if (dy>0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (cargaPagina){
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount){
                            Log.i(TAG,"lllegamos al final");

                            cargaPagina = false;
                            page +=1;
                            optenDatos(page);

                        }

                    }

                }
            }
        });
        cargaPagina = true;
        page = 1;
        optenDatos(page);





    }


    public void optenDatos(int page){
        Call<results> call = LlamaApi.getllamaApi().create(ApiPeliculas.class)
                .leermastop("511174b10ded86a2c00770c3b55b5b88", page,"es-US");
        call.enqueue(new Callback<results>() {


            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<results> call, Response<results> response) {
                cargaPagina = true;
                results info = response.body();
                lista = Arrays.asList(info.getResults());


                adapterPeliculas = new AdapterPeliculas( lista, getApplicationContext(), new AdapterPeliculas.OnItemClickListener() {
                    @Override
                    public void onItemClick(DatosApi item) {
                        moveToDescripcion(item);
                    }
                });
                recyclerView.setAdapter(adapterPeliculas);
                adapterPeliculas.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<results> call, Throwable t) {
                cargaPagina = true;
                t.getMessage();
                Toast.makeText(VisataTop.this, "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
                Log.w("Prueba", " " + t.getMessage());

            }
        });

    }


    public void moveToDescripcion(DatosApi item){
        Intent intent = new Intent(this,VistaDescripcion.class);
        intent.putExtra("datosApi",item);
        startActivity(intent);
    }


}