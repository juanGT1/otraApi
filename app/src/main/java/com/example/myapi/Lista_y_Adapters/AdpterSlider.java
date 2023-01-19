package com.example.myapi.Lista_y_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapi.R;
import com.example.myapi.model.DatosApi;
import com.example.myapi.model.results;

import java.util.List;

import retrofit2.Callback;

public class AdpterSlider extends  RecyclerView.Adapter<AdpterSlider.ViewHolder> {
    private List<DatosApi> datosApiList;
    LayoutInflater inflater;
    Context context;


    public AdpterSlider(List<DatosApi> datosApiList, Context context) {
        this.datosApiList = datosApiList;
        this.inflater = LayoutInflater.from( context);
        this.context =  context;
    }


    @NonNull
    @Override
    public AdpterSlider.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.itemslider, null);
        return new AdpterSlider.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdpterSlider.ViewHolder holder, int position) {
        DatosApi imagen = datosApiList.get(position);
        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500" + imagen.getPoster_path())
                .override(300,300)
                .into(holder.caratula);
    }

    @Override
    public int getItemCount() {return datosApiList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView caratula;
        TextView descripcion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            caratula = itemView.findViewById(R.id.imgSlider);
            descripcion = itemView.findViewById(R.id.tvdescription);
        }


    }
}
