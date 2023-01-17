package com.example.myapi.Lista_y_Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapi.R;
import com.example.myapi.model.DatosApi;
import com.example.myapi.vista.VistaFavoritos;

import java.util.List;

public class AdapterPeliculas extends RecyclerView.Adapter<AdapterPeliculas.ViewHolder>  {
    List<DatosApi> datosApiList;
    LayoutInflater inflater;
    Context context;
    final AdapterPeliculas.OnItemClickListener listener;

    public  interface OnItemClickListener{
        void onItemClick(DatosApi item);
    }


    public AdapterPeliculas(List<DatosApi> datosApiList, Context context, AdapterPeliculas.OnItemClickListener listener) {
        this.datosApiList = datosApiList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterPeliculas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.itempeliculas, null);
        return new AdapterPeliculas.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatosApi imagen = datosApiList.get(position);
        holder.titulo.setText(datosApiList.get(position).getTitle());
        holder.vistas.setText(String.valueOf(datosApiList.get(position).getPopularity()));
        holder.calificacion.setRating((float ) datosApiList.get(position).getVote_average());
        holder.bindata(datosApiList.get(position));
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + imagen.getPoster_path())
                .circleCrop()
                .override(200,200)
                .into(holder.portada);




    }



    @Override
    public int getItemCount() {
        return datosApiList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView portada,ojo;
        TextView titulo, vistas;
        RatingBar calificacion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portada = itemView.findViewById(R.id.imagenpelicula);
            titulo = itemView.findViewById(R.id.titulo);
            calificacion = itemView.findViewById(R.id.calificacion);
            vistas = itemView.findViewById(R.id.vistas);
            ojo = itemView.findViewById(R.id.ojo);

        }

        void bindata ( final DatosApi item){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }



}
//.circleCrop()