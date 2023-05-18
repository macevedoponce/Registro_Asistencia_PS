package com.acevedo.registroasistencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.acevedo.registroasistencia.Adapters.GrupoAdapter;
import com.acevedo.registroasistencia.Clases.Grupo;
import com.acevedo.registroasistencia.Util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView rvGrupos;
    RequestQueue requestQueue;
    List<Grupo> grupoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvGrupos = findViewById(R.id.rvGrupos);
        rvGrupos.setHasFixedSize(true);
        rvGrupos.setLayoutManager(new LinearLayoutManager(this));
        grupoList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        cargarGrupos();
    }

    private void cargarGrupos() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando datos...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = Util.RUTA_GRUPOS;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("grupos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String nombre = jsonObject.getString("nombre");
                        Grupo grupo = new Grupo(id, nombre);
                        grupoList.add(grupo);
                    }
                    GrupoAdapter adapter = new GrupoAdapter(MainActivity.this, grupoList);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int grupo_id = grupoList.get(rvGrupos.getChildAdapterPosition(view)).getId();
                            //Toast.makeText(MainActivity.this, id +"", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ListParticipantesActivity.class);
                            intent.putExtra("grupo_id",grupo_id);
                            startActivity(intent);
                        }
                    });

                    rvGrupos.setAdapter(adapter);

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No se encontro grupos", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}