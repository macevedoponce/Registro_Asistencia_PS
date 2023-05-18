package com.acevedo.registroasistencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.registroasistencia.Adapters.ParticipanteAdapter;
import com.acevedo.registroasistencia.Clases.Grupo;
import com.acevedo.registroasistencia.Clases.Participante;
import com.acevedo.registroasistencia.Util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListParticipantesActivity extends AppCompatActivity {

    RecyclerView rvParticipantes;
    RequestQueue requestQueue;
    List<Participante> participanteList;
    TextView tvGrupoSeleccionado;

    JsonObjectRequest jsonObjectRequest;

    Date date = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String fechaActual = dateFormat.format(date);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_participantes);
        rvParticipantes = findViewById(R.id.rvParticipantes);
        tvGrupoSeleccionado = findViewById(R.id.tvGrupoSeleccionado);
        rvParticipantes.setHasFixedSize(true);
        rvParticipantes.setLayoutManager(new LinearLayoutManager(this));
        participanteList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        cargarParticipantes();
    }

    private void cargarParticipantes() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando datos...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        int grupo_id = getIntent().getIntExtra("grupo_id",0);

        if(grupo_id == 1) tvGrupoSeleccionado.setText("GRUPO 5");
        if(grupo_id == 2) tvGrupoSeleccionado.setText("GRUPO 7");
        if(grupo_id == 3) tvGrupoSeleccionado.setText("GRUPO 9");


        String url = Util.RUTA_PARTICIPANTES+"?grupo_id="+grupo_id+"&fecha_actual="+fechaActual;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("participantes");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String dni = jsonObject.getString("dni");
                        String nombre = jsonObject.getString("nombre");
                        String estado = jsonObject.getString("estado");
                        Participante participante = new Participante(id, dni, nombre,estado);
                        participanteList.add(participante);
                    }
                    ParticipanteAdapter adapter = new ParticipanteAdapter(ListParticipantesActivity.this, participanteList);


                    rvParticipantes.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListParticipantesActivity.this, "No se encontro participantes", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void registrarAsistencia(int id_participante, String estado ){

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando Cambios ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = Util.RUTA_REGISTRAR_ASISTENCIA;
        String participante_id = id_participante +"";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Mostrar mensaje de api
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar el error de la solicitud (si es necesario)
                        Toast.makeText(getApplicationContext(), "Error al enviar los datos", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("participante_id", participante_id);
                params.put("fecha_registro", fechaActual);
                params.put("estado", estado);
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley
        requestQueue.add(request);
    }
}