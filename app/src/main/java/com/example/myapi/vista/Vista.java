package com.example.myapi.vista;

import static android.content.ContentValues.TAG;
import static com.example.myapi.constantes.Constantes.BASE_URL;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapi.Lista_y_Adapters.AdpterSlider;
import com.example.myapi.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.myapi.Lista_y_Adapters.AdapterPeliculas;
import com.example.myapi.interfas.ApiPeliculas;
import com.example.myapi.interfas.Direccionpresenter;
import com.example.myapi.interfas.Direccionvista;
import com.example.myapi.model.DatosApi;
import com.example.myapi.model.LlamaApi;
import com.example.myapi.model.results;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Vista extends AppCompatActivity {

    public List<DatosApi> listacompleta = new ArrayList<>();
    public List<DatosApi> slider = new ArrayList<>();
    public RecyclerView recyclerView, recyclerViewSlider;
    public List<DatosApi> datosApiList = new ArrayList<>();
    public AdapterPeliculas adapterPeliculas;
    public AdpterSlider adpterSlider;
    public Button popular, proximo, mastop;
    public ImageView imageView,vistaFavorito,wifi;
    private int page;
    private boolean cargaPagina;

    Button reintentar;
    ProgressBar p1;
    Context ctx = this;



    Direccionpresenter p;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista);
        p1 =(ProgressBar) findViewById(R.id.progressBarVista);
        reintentar = (Button) findViewById(R.id.reintentarVista);
        wifi = (ImageView) findViewById(R.id.wifiVista);

        imageView = findViewById(R.id.imageView);
        mastop = findViewById(R.id.mastop);
        vistaFavorito = findViewById(R.id.favorito);
        proximo = findViewById(R.id.proximo);
        popular = findViewById(R.id.popular);



        recyclerViewSlider = (RecyclerView) findViewById(R.id.recyclerSlider);
        recyclerViewSlider.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL, false));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerpeliculas);
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
                            obtenDatos(page);
                            Log.w("page", " " + page);
                        }
                    }
                }



                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (cargaPagina) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            Log.i(TAG, "lllegamos al final");

                            cargaPagina = false;
                            page += 1;
                            obtenDatos(page);
                            Log.w("page", " " + page);


                        }

                    }

                }
            }
        });
        cargaPagina = true;
        page = 1;
        obtenDatos(page);


        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popular.setEnabled(false);
                Intent intent = new Intent(Vista.this, VistaPopular.class);
                startActivity(intent);
            }
        });


        proximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proximo.setEnabled(false);
                Intent intent = new Intent(Vista.this, VistaProximo.class);
                startActivity(intent);

            }
        });

        mastop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mastop.setEnabled(false);
                Intent intent = new Intent(Vista.this, VisataTop.class);
                startActivity(intent);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setEnabled(false);
                Intent intent = new Intent(Vista.this, VistaBusqueda.class);
                startActivity(intent);
                finish();
            }
        });
        vistaFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vistaFavorito.setEnabled(false);
                Intent intent = new Intent(Vista.this, VistaFavoritos.class);
                startActivity(intent);
                finish();
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reintentar.setEnabled(true);
        mastop.setEnabled(true);
        proximo.setEnabled(true);
        popular.setEnabled(true);
    }


    public void obtenDatos(int page) {
        Call<results> call = LlamaApi.getllamaApi().create(ApiPeliculas.class).leerPeliculas("511174b10ded86a2c00770c3b55b5b88", page, "es-US");
        call.enqueue(new Callback<results>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<results> call, Response<results> response) {
                recyclerView.setVisibility(View.VISIBLE);

                if (response.isSuccessful()) {
                    cargaPagina = true;
                    results info = response.body();
                    listacompleta = Arrays.asList(info.getResults());
                    datosApiList.clear();
                    datosApiList.addAll(listacompleta);
                    adapterPeliculas = new AdapterPeliculas(datosApiList,ctx, new AdapterPeliculas.OnItemClickListener() {
                        @Override
                        public void onItemClick(DatosApi item) {
                            moveToDescripcion(item);
                        }
                    });
                    recyclerView.setAdapter(adapterPeliculas);
                    adapterPeliculas.notifyDataSetChanged();
                }
                results sliderr = response.body();
                slider = Arrays.asList(sliderr.getResults());
                adpterSlider = new AdpterSlider(slider, ctx);
                recyclerViewSlider.setAdapter(adpterSlider);


            }

            @Override
            public void onFailure(Call<results> call, Throwable t) {
                cargaPagina = true;
                wifi.setVisibility(View.VISIBLE);
                reintentar.setVisibility(View.VISIBLE);

                reintentar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        proximo.setEnabled(false);
                        mastop.setEnabled(false);
                        popular.setEnabled(false);
                        reintentar.setEnabled(false);
                        imageView.setEnabled(false);
                        vistaFavorito.setEnabled(false);

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
                                Intent intent = new Intent(Vista.this,Vista.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 5000);


                    }

                });


                t.getMessage();
                Toast.makeText(Vista.this, "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
                Log.w("Prueba", " " + t.getMessage());

            }


        });

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void moveToDescripcion(DatosApi item) {

        Intent intent = new Intent(this, VistaDescripcion.class);
        intent.putExtra("datosApi", item);
        startActivity(intent);
//        finish();


    }


}