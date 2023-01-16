package com.example.myapi.vista;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapi.Lista_y_Adapters.AdapterPeliculas;
import com.example.myapi.R;
import com.example.myapi.interfas.ApiPeliculas;
import com.example.myapi.interfas.Direccionpresenter;
import com.example.myapi.interfas.Direccionvista;
import com.example.myapi.model.DatosApi;
import com.example.myapi.model.LlamaApi;
import com.example.myapi.model.results;
import com.example.myapi.presenter.Presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VistaBusqueda extends AppCompatActivity implements Direccionvista {
    private TextView texfiltro;
    private EditText texfiltro1;
    private List<DatosApi> lista = new ArrayList<>();
    private List<DatosApi> listacompleta = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterPeliculas adapterPeliculas;
    private Direccionpresenter presenter;
    public ImageView atras;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_busqueda);
        texfiltro = findViewById(R.id.filtro);
        texfiltro1 = findViewById(R.id.filtro);
        atras = findViewById(R.id.atras);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        presenter = new Presenter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        texfiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String g = texfiltro.getText().toString();
                if (!g.isEmpty()) {
                    presenter.leerpelis(g);
                    Log.w("Prueba", "1" + g);
                    Toast.makeText(VistaBusqueda.this, "Buscando", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VistaBusqueda.this, "ingrese el nombre de una pelicula", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void moveToDescripcion(DatosApi item) {
        Intent intent = new Intent(this, VistaDescripcion.class);
        intent.putExtra("datosApi", item);
        startActivity(intent);
    }


    @Override
    public void ResultBusqueda(Call<results> call) {
        call.enqueue(new Callback<results>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<results> call, Response<results> response) {
                results info = response.body();
                if (info != null) {
                    listacompleta = Arrays.asList(info.getResults());
                    lista.clear();
                    lista.addAll(listacompleta);
                    adapterPeliculas = new AdapterPeliculas(lista, getApplicationContext(), new AdapterPeliculas.OnItemClickListener() {
                        @Override
                        public void onItemClick(DatosApi item) {
                            moveToDescripcion(item);
                        }
                    });
                    recyclerView.setAdapter(adapterPeliculas);
                    adapterPeliculas.notifyDataSetChanged();
                }

            }


            @Override
            public void onFailure(Call<results> call, Throwable t) {
                t.getMessage();
                Toast.makeText(VistaBusqueda.this, "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
                Log.w("Prueba", " " + t.getMessage());

            }
        });


        texfiltro.setOnKeyListener(new View.OnKeyListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                lista.clear();
                lista.addAll(listacompleta.stream()
                        .filter(x -> x.getTitle().toLowerCase().contains(texfiltro.getText()
                                .toString().toLowerCase()))
                        .collect(Collectors.toList())
                );
                adapterPeliculas.notifyDataSetChanged();
                return false;
            }
        });





    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VistaBusqueda.this,Vista.class);
        startActivity(intent);
        finish();
    }

    public void atras(View v){
        Intent intent = new Intent(VistaBusqueda.this,Vista.class);
            startActivity(intent);
            finish();
    }


    @Override
    public void error(String err) {
        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }


}