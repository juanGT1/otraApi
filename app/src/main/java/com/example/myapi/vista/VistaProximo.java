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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VistaProximo extends AppCompatActivity {
    public List<DatosApi> lista = new ArrayList<>();
    public RecyclerView recyclerView;
    public AdapterPeliculas adapterPeliculas;
    //private DatosApi item;
    private int page = 1;
    private boolean cargaPagina;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_proximo);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerproximo);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);



                if (dy <0) {
                    int pastVertical = layoutManager.findLastVisibleItemPosition();//
                    Log.w(TAG,"pasv "+ pastVertical + " pag "+ page);
                    if (cargaPagina &&  pastVertical==5){
                        if (page==1){

                        } else  {
                            cargaPagina = false;
                            page-=1;
                            optenDatos(page);
                        }
                    }
                }


                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();//Devuelve el número actual de vistas secundarias adjuntas al RecyclerView principal. Esto no incluye las vistas secundarias que se separaron o descartaron temporalmente.
                    int totalItemCount = layoutManager.getItemCount(); //Devuelve la cantidad de elementos en el adaptador vinculado al RecyclerView principal. Tenga en cuenta que este número no es necesariamente igual a StategetItemCount().
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();//Devuelve la posición del adaptador de la primera vista visible. Esta posición no incluye los cambios de adaptador que se enviaron después del último pase de diseño.
                    Log.w(TAG, "lllegamos al final"+"/visibleItemCount "+visibleItemCount+
                            "/totalItemCount " +totalItemCount+"/pastVisibleItems "+pastVisibleItems
                    +" dy "+dy+" dx "+" page "+page);
                    if (cargaPagina) {
                        if (  pastVisibleItems==14) {

                            cargaPagina = false;
                            page += 1;
                            optenDatos(page);
                        }

                    }

                }
            }
        });
        cargaPagina = true;

        optenDatos(page);


    }

    public void optenDatos(int page) {
        Call<results> call = LlamaApi.getllamaApi().create(ApiPeliculas.class)
                .leerpopular("511174b10ded86a2c00770c3b55b5b88", page, "es-US");
        call.enqueue(new Callback<results>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<results> call, Response<results> response) {
                cargaPagina = true;
                results info = response.body();
                lista = Arrays.asList(info.getResults());

                adapterPeliculas = new AdapterPeliculas(lista, getApplicationContext(), new AdapterPeliculas.OnItemClickListener() {
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
                Toast.makeText(VistaProximo.this, "ERROR DE CONEXIÓN", Toast.LENGTH_SHORT).show();
                Log.w("Prueba", " " + t.getMessage());
            }
        });


    }


    public void moveToDescripcion(DatosApi item) {
        Intent intent = new Intent(this, VistaDescripcion.class);
        intent.putExtra("datosApi", item);
        startActivity(intent);
    }


}