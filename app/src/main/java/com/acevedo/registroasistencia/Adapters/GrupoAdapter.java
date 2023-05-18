package com.acevedo.registroasistencia.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.registroasistencia.Clases.Grupo;
import com.acevedo.registroasistencia.R;

import java.util.List;

public class GrupoAdapter extends RecyclerView.Adapter<GrupoAdapter.GrupoHolder> implements View.OnClickListener {

    Context context;
    List<Grupo> grupoList;
    View.OnClickListener listener;

    public GrupoAdapter(Context context, List<Grupo> grupoList){
        this.context = context;
        this.grupoList = grupoList;
    }

    @NonNull
    @Override
    public GrupoAdapter.GrupoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.item_grupo, parent,false);
        mView.setOnClickListener(this);
        return new GrupoHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull GrupoAdapter.GrupoHolder holder, int position) {
        Grupo grupo = grupoList.get(position);
        String nombre = grupo.getNombre();

        holder.setNombre(nombre);
    }

    @Override
    public int getItemCount() {
        return grupoList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;

    }


    @Override
    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public static class GrupoHolder extends RecyclerView.ViewHolder {

        TextView tvNombreGrupo;
        View view;
        public GrupoHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvNombreGrupo = view.findViewById(R.id.tvNombreGrupo);
        }

        public void setNombre(String nombre) {
            tvNombreGrupo.setText(nombre);
        }
    }
}
