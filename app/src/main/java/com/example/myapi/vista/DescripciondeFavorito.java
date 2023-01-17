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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapi.Lista_y_Adapters.ListaFavorito;
import com.example.myapi.R;
import com.example.myapi.model.BDfavorito;
import com.example.myapi.model.DatosApi;

public class DescripciondeFavorito extends AppCompatActivity {
    TextView descripcion;
    ImageView img, favorito,corazon;
    String titulo;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcionde_favorito);
        descripcion = findViewById(R.id.tvdescription2);
        img = findViewById(R.id.imgDescription2);
        favorito = findViewById(R.id.imgfavorito2);
        corazon = findViewById(R.id.corazon2);

        ListaFavorito listaFavorito = (ListaFavorito) getIntent().getExtras().getSerializable("datosFavorito");
        descripcion.setText(listaFavorito.getOverview());
        titulo = listaFavorito.getTitle();
        //Log.w("Titulo",datosApi.getTitle());

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" +listaFavorito.getPoster_path())
                .override(900,900)
                .into(img);




        BDfavorito admin = new BDfavorito (this,"BDfavorito",null,1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues registra = new ContentValues();

                registra.put("title",listaFavorito.getTitle());
                registra.put("poster_path",listaFavorito.getPoster_path());
                registra.put("popularity",String.valueOf(listaFavorito.getPopularity()));
                registra.put("vote_average",String.valueOf(listaFavorito.getVote_average()));
                registra.put("overview",listaFavorito.getOverview());

                bd.insert("favorito",null,registra);
//                bd.close();
                favorito.setVisibility(View.GONE);
                corazon.setVisibility(View.VISIBLE);
                Toast.makeText(DescripciondeFavorito.this, "Se agrego a favoritos", Toast.LENGTH_SHORT).show();
            }
        });



        BDfavorito adminn = new BDfavorito (this,"BDfavorito",null,1);
        SQLiteDatabase database = adminn.getWritableDatabase();
        corazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                database.delete("favorito","title='"+titulo+"'",null);
//               database.close();
                favorito.setVisibility(View.VISIBLE);
                corazon.setVisibility(View.GONE);
                Toast.makeText(DescripciondeFavorito.this, "se elimino de favoritos", Toast.LENGTH_SHORT).show();
            }
        });


        if (!peliAgregada()){
            favorito.setVisibility(View.GONE);
            corazon.setVisibility(View.VISIBLE);

        }






    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,VistaFavoritos.class);
        startActivity(intent);
        finish();
    }

    public void retrocedee(View v){
        Intent intent = new Intent(this,VistaFavoritos.class);
        startActivity(intent);
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