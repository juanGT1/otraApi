package com.example.myapi.vista;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.myapi.Lista_y_Adapters.AdapterFavorito;
import com.example.myapi.Lista_y_Adapters.ListaFavorito;
import com.example.myapi.R;
import com.example.myapi.model.BDfavorito;
import com.example.myapi.model.DatosApi;
import com.google.android.material.bottomappbar.BottomAppBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VistaFavoritos extends AppCompatActivity {
    private String title;
    List<ListaFavorito> favoritoList;
    List<DatosApi> datosApiList;

    ImageView inicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_favoritos);

        inicio = findViewById(R.id.iniciop);


        favorito();


        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VistaFavoritos.this,Vista.class);
                startActivity(intent);
                finish();
            }
        });





    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VistaFavoritos.this,Vista.class);
        startActivity(intent);
        finish();
    }

    public void favorito(){
        favoritoList = consulta();


        Collections.reverse(favoritoList);
        AdapterFavorito adapterFavorito = new AdapterFavorito(favoritoList, this, new AdapterFavorito.OnItemClickListener() {
            @Override
            public void onItemClick(ListaFavorito item) {
              moveToDescripcion(item);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclrefvorito);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterFavorito);




    }

    public ArrayList<ListaFavorito> consulta(){
        BDfavorito admin = new BDfavorito(this,"BDfavorito",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        ArrayList<ListaFavorito> peliFavoritad = new ArrayList<ListaFavorito>();
        try (Cursor cursor = bd.rawQuery("select * from favorito ",null)) {
            if (!cursor.moveToFirst()) {
                return new ArrayList<ListaFavorito>();
            }
            ListaFavorito peliFavorita;

            do {
                peliFavorita = new ListaFavorito();
                peliFavorita.setTitle(cursor.getString(0));
                peliFavorita.setPoster_path(cursor.getString(1));
                peliFavorita.setPopularity(cursor.getString(2));
                peliFavorita.setVote_average(cursor.getString(3));
                peliFavorita.setOverview(cursor.getString(4));
                peliFavoritad.add(peliFavorita);

            } while (cursor.moveToNext());
            bd.close();
            Log.w("peliculaas", String.valueOf(peliFavoritad.size()));
            return peliFavoritad;

        }
    }

    public void moveToDescripcion(ListaFavorito item) {
        Intent intent = new Intent(this,DescripciondeFavorito.class);
        intent.putExtra("datosFavorito", item);
        startActivity(intent);
        finish();
    }



}