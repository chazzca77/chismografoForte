package com.chazzca.chismografoforte;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.chazzca.chismografoforte.Interfaces.UrlInterface;
import com.chazzca.chismografoforte.models.ListaPreguntas;
import com.chazzca.chismografoforte.models.ModeloListado;
import com.chazzca.chismografoforte.models.ModeloPregunta;
import com.chazzca.chismografoforte.models.ModeloPreguntasRespuesta;
import com.chazzca.chismografoforte.utils.ListaAdapter;
import com.chazzca.chismografoforte.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChismografoActivity extends AppCompatActivity {

    private RecyclerView recyclerListadoChisme;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListaAdapter listAdapter;
    private Button btnEnviar;
    private ArrayList<ModeloListado> arrayListaUsuarios  = new ArrayList<>();
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chismografo);

        retrofit = Utils.retrofit;

        recyclerListadoChisme = findViewById(R.id.recyclerListadoChisme);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerListadoChisme.setLayoutManager(mLayoutManager);

        btnEnviar = findViewById(R.id.btnEnviarCorreo);

        btnEnviar.setOnClickListener(view -> {

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChismografoActivity.this);
            builder.setTitle("Próximamente");
            builder.setMessage("Disculpa la molestia D:")
                    .setNegativeButton("Aceptar", (dialog, id) -> {
                        dialog.dismiss();
                    });
            android.app.AlertDialog dialog = builder.create();
            dialog.show();

        });

        getListadoChisme();

    }

    private void getListadoChisme(){

        Call<JsonObject> serviceDownload = retrofit.create(UrlInterface.class).getListadoChisme();
        serviceDownload.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.e("e",response+"");
                if(response.code() >= 200 && response.code() <300) {
                    resultDatos(response.body());
                }else {
                    dialogReintentar("Comentarios", "No se pudo enviar petición,¿Desea volver a intentar?",
                            (dialog, which) ->
                                    getListadoChisme());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Utils.progresoDismiss();
                Log.e("PreReg",t+" -- "+call);
                dialogReintentar("Comentarios", "Revise su conexion a internet,¿Desea volver a intentar?",
                        (dialog, which) ->
                                getListadoChisme());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resultDatos(JsonObject objecto){

        try{
            JSONObject object = new JSONObject(objecto.toString());
            if(object.getInt("success")==1){

                JSONArray content = object.getJSONArray("content");
                arrayListaUsuarios.clear();

                for (int i=0;i<content.length();i++){
                    JSONObject preguntas = content.getJSONObject(i);

                    Gson gson = new Gson();
                    ModeloListado pregunta = gson.fromJson(String.valueOf(preguntas), ModeloListado.class);

                    if(pregunta.preguntas.size() == 6){
                        arrayListaUsuarios.add(pregunta);
                    }

                }

                listAdapter = new ListaAdapter(arrayListaUsuarios, getApplicationContext());
                recyclerListadoChisme.setAdapter(listAdapter);

            }else{
                Utils.alertDialog(this,"Aviso","Ocurrió un error por favor vuelve a intentar");
            }
        }catch (JSONException e){
            e.printStackTrace();
            Utils.alertDialog(this,"Aviso","Ocurrió un error en la respuesta");
        }
    }



    public void dialogReintentar(String titulo, String mensaje, DialogInterface.OnClickListener okListener) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChismografoActivity.this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje)
                .setPositiveButton("Reintentar", okListener)
                .setNegativeButton("Cancelar", (dialog, id) -> {

                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

}