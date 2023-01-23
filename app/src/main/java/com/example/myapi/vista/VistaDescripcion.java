package com.example.myapi.vista;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapi.R;
import com.example.myapi.model.BDfavorito;
import com.example.myapi.model.DatosApi;

public class VistaDescripcion extends AppCompatActivity {
    TextView descripcion, tituloo, vistas;
    RatingBar calificacion;
    ImageView img, favorito,corazon;
    String titulo;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_descripcion);
        descripcion = findViewById(R.id.tvdescription);
        tituloo = findViewById(R.id.tituloo);
        calificacion = findViewById(R.id.califi);
        vistas = findViewById(R.id.visto);
        img = findViewById(R.id.imgDescription);
        favorito = findViewById(R.id.imgfavorito);
        corazon = findViewById(R.id.corazon);

        DatosApi datosApi = (DatosApi) getIntent().getExtras().getSerializable("datosApi");
        descripcion.setText(datosApi.getOverview());
        tituloo.setText(datosApi.getTitle());
        calificacion.setRating((float) datosApi.getVote_average()/2);
        vistas.setText(String.valueOf(datosApi.getPopularity()));
        titulo = datosApi.getTitle();
        //Log.w("Titulo",datosApi.getTitle());

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" +datosApi.getPoster_path())
                .override(900,900)
                .into(img);




        BDfavorito admin = new BDfavorito (this,"BDfavorito",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues registra = new ContentValues();

                registra.put("title",datosApi.getTitle());
                registra.put("poster_path",datosApi.getPoster_path());
                registra.put("popularity",String.valueOf(datosApi.getPopularity()));
                registra.put("vote_average",String.valueOf(datosApi.getVote_average()));
                registra.put("overview",datosApi.getOverview());

                bd.insert("favorito",null,registra);
//                bd.close();
                favorito.setVisibility(View.GONE);
                corazon.setVisibility(View.VISIBLE);
                Toast.makeText(VistaDescripcion.this, "Se agrego a favoritos", Toast.LENGTH_SHORT).show();
            }
        });



        BDfavorito adminn = new BDfavorito (this,"BDfavorito",null,1);
        SQLiteDatabase database = adminn.getWritableDatabase();
        corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.delete("favorito","title='"+titulo+"'",null);
//                database.close();
                favorito.setVisibility(View.VISIBLE);
                corazon.setVisibility(View.GONE);
                Toast.makeText(VistaDescripcion.this, "se elimino de favoritos", Toast.LENGTH_SHORT).show();
            }
        });

        if (!peliAgregada()){
            favorito.setVisibility(View.GONE);
            corazon.setVisibility(View.VISIBLE);

        }


//        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(this,Vista.class);
//        startActivity(intent);
        finish();
    }

    public void retrocede(View v){
//        Intent intent = new Intent(this,Vista.class);
//        startActivity(intent);
        finish();
    }
    public boolean peliAgregada(){
        BDfavorito adminn = new BDfavorito (this,"BDfavorito",null,1);
        SQLiteDatabase database = adminn.getWritableDatabase();
        Cursor cursor = database.rawQuery("select title from favorito where title='"+titulo+"'",null);
        if (cursor.moveToFirst()){
            cursor.close();
            database.close();
            return false;
        }
        return true;
    }




}