package com.example.myapi.Lista_y_Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.util.List;

public class AdapterFavorito extends  RecyclerView.Adapter<AdapterFavorito.ViewHolder>{

    private List<ListaFavorito> favoritoList;
    List<DatosApi> datosApiList;
    LayoutInflater inflater;
    Context context;

    final AdapterFavorito.OnItemClickListener listener;

    public  interface OnItemClickListener{
        void onItemClick(ListaFavorito item);
    }


    public AdapterFavorito(List<ListaFavorito> favoritoList, Context context,AdapterFavorito.OnItemClickListener listener) {
        this.favoritoList = favoritoList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterFavorito.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_favorito, null);
        return new AdapterFavorito.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFavorito.ViewHolder holder, int position) {
        ListaFavorito imagen = favoritoList.get(position);
        holder.titulo.setText(favoritoList.get(position).getTitle());
        holder.vistas.setText(favoritoList.get(position).getPopularity());
        holder.calificacion.setRating(Float.parseFloat(favoritoList.get(position).getVote_average()));
        holder.bindata(favoritoList.get(position));
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + imagen.getPoster_path())
                .circleCrop()
                .override(200,200)
                .into(holder.portada);
    }

    @Override
    public int getItemCount() {
        return favoritoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView portada;
        TextView titulo,  vistas;
        RatingBar calificacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            portada = itemView.findViewById(R.id.imagenpeliculaf);
            titulo = itemView.findViewById(R.id.titulof);
            calificacion = itemView.findViewById(R.id.calificacionf);
            vistas = itemView.findViewById(R.id.vistasf);
        }

     public void bindata( ListaFavorito item){
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   listener.onItemClick( item);
               }
           });
     }

    }
}
