package com.example.myapi.vista;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapi.Lista_y_Adapters.AdapterPeliculas;
import com.example.myapi.MainActivity;
import com.example.myapi.R;
import com.example.myapi.interfas.ApiPeliculas;
import com.example.myapi.model.DatosApi;
import com.example.myapi.model.LlamaApi;
import com.example.myapi.model.results;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VistaPopular extends AppCompatActivity {
    private List<DatosApi>  lista = new ArrayList<>();
    private RecyclerView recyclerView;
    private ImageView atras,wifi;
    private AdapterPeliculas adapterPeliculas;
    private int page = 1;
    private boolean cargaPagina;

    Button reintentar;
    ProgressBar p1;
    Context ctx = this;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_popular);
        atras = findViewById(R.id.atraspopular);
        p1=(ProgressBar) findViewById(R.id.progressBar2);
        reintentar= (Button) findViewById(R.id.reintentar);
        wifi = (ImageView) findViewById(R.id.wifi);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerpopular);
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



                if (dy > 0){
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

        optenDatos(page);

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        reintentar.setEnabled(true);
        atras.setEnabled(true);
    }

    public void optenDatos(int page){
        Call<results> call = LlamaApi.getllamaApi().create(ApiPeliculas.class)
                .leerPeliculas("511174b10ded86a2c00770c3b55b5b88", page,"es-US");
        call.enqueue(new Callback<results>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<results> call, Response<results> response) {
                cargaPagina = true;
                if (response.isSuccessful()) {
                    results info = response.body();
                    lista = Arrays.asList(info.getResults());
                    adapterPeliculas = new AdapterPeliculas(lista,ctx, new AdapterPeliculas.OnItemClickListener() {
                        @Override
                        public void onItemClick(DatosApi item) {
                            moveToDescripcion(item);
                        }
                    });
                    recyclerView.setAdapter(adapterPeliculas);
                    adapterPeliculas.notifyDataSetChanged();
                }else {
                    Log.w(TAG,"onresponse"+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<results> call, Throwable t) {
                cargaPagina = true;
                wifi.setVisibility(View.VISIBLE);
                reintentar.setVisibility(View.VISIBLE);

                reintentar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        atras.setEnabled(false);
                        reintentar.setEnabled(false);
                        p1.setVisibility(View.VISIBLE);

                        Thread thread = new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                for (int i = 0; i <=100; i++) {
                                    try {
                                        sleep(500);
                                    }catch (InterruptedException e){
                                        e.printStackTrace();
                                    }
                                    p1.setProgress(i);
                                    i=i+10;
                                }
                            }
                        };
                        thread.start();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                Intent intent = new Intent(VistaPopular.this,VistaPopular.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 5000);


                    }
                });

                t.getMessage();
                Toast.makeText(VistaPopular.this, "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
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