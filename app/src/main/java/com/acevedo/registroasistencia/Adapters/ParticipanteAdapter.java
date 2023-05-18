package com.acevedo.registroasistencia.Adapters;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.registroasistencia.Clases.Participante;
import com.acevedo.registroasistencia.ListParticipantesActivity;
import com.acevedo.registroasistencia.R;
import com.acevedo.registroasistencia.Util.Util;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParticipanteAdapter extends RecyclerView.Adapter<ParticipanteAdapter.ParticipanteHolder> implements View.OnClickListener {
    Context context;
    List<Participante> participanteList;
    View.OnClickListener listener;


    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    String fechaActual = dateFormat.format(date);
    //verificar si hay datos guardados

    public ParticipanteAdapter(Context context, List<Participante> participanteList) {
        this.context = context;
        this.participanteList = participanteList;
    }


    @NonNull
    @NotNull
    @Override
    public ParticipanteHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.item_participante, parent,false);
        mView.setOnClickListener(this);
        return new ParticipanteHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ParticipanteHolder holder, int position) {
        Participante participante = participanteList.get(position);
        String dni = participante.getDni();
        String nombre = participante.getNombre();
        String estado = participante.getEstado();
        int id_participante = participante.getId();

        holder.setDni(dni);
        holder.setNombre(nombre);
        holder.setEstado(estado);

        //holder.setParticipantesGuardados(id_participante);
        holder.rgAsistencia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // Mostrar la posición del RecyclerView
                String estado;
                if (i == R.id.rbTemprano) {
                    estado = "Temprano";
                } else if (i == R.id.rbTarde) {
                    estado = "Tarde";
                } else {
                    estado = "Falta";
                }

                //ejecutar funcion registrarAsistencia que esta en ListParticipantesActivity
                ((ListParticipantesActivity) radioGroup.getContext()).registrarAsistencia(id_participante,estado);

            }
        });
    }

    @Override
    public int getItemCount() {
        return participanteList.size();
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

    public class ParticipanteHolder extends RecyclerView.ViewHolder {

        TextView tvDni, tvNombre;
        RadioGroup rgAsistencia;
        RadioButton rbTemprano, rbTarde, rbFalta;
        View view;


        public ParticipanteHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            view = itemView;
            tvDni = view.findViewById(R.id.tvDniParticipante);
            tvNombre = view.findViewById(R.id.tvNombreParticipante);
            rgAsistencia = view.findViewById(R.id.rgAsistencia);
            rbTemprano = view.findViewById(R.id.rbTemprano);
            rbTarde = view.findViewById(R.id.rbTarde);
            rbFalta = view.findViewById(R.id.rbFalta);

        }


        public void setDni(String dni) {
            tvDni.setText(dni);
        }

        public void setNombre(String nombre) {
            tvNombre.setText(nombre);
        }

        public void setEstado(String estado) {
            int radioButtonIndex;

            switch (estado){
                case "Temprano":
                    radioButtonIndex = R.id.rbTemprano;
                    break;
                case "Tarde":
                    radioButtonIndex = R.id.rbTarde;
                    break;
                case "Falta":
                    radioButtonIndex = R.id.rbFalta;
                    break;
                default:
                    radioButtonIndex = -1;
                    break;
            }

            // Seleccionar el radiobutton correspondiente al estado
            if (radioButtonIndex != -1) {
                RadioButton radioButton = itemView.findViewById(radioButtonIndex);
                radioButton.setChecked(true);
            }

        }
    }
}
