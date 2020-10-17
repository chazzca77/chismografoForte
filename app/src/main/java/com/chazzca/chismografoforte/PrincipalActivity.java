package com.chazzca.chismografoforte;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.chazzca.chismografoforte.Interfaces.UrlInterface;
import com.chazzca.chismografoforte.models.ListaPreguntas;
import com.chazzca.chismografoforte.models.ModeloPregunta;
import com.chazzca.chismografoforte.models.ModeloUsuario;
import com.chazzca.chismografoforte.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PrincipalActivity extends AppCompatActivity {

    private Button btnContestar,btnEliminar,btnEditar,btnListado;
    private Retrofit retrofit;
    ArrayList<ModeloPregunta> lista  = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btnContestar = findViewById(R.id.btnContestar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnEditar = findViewById(R.id.btnEditar);
        btnListado = findViewById(R.id.btnListado);

        retrofit = Utils.retrofit;

        btnContestar.setOnClickListener(view -> {

            getPreguntasResp(Utils.usuarioGlobal.Id+"");

        });

        btnEliminar.setOnClickListener(view -> {
            deleteRespuestas(Utils.usuarioGlobal.Id+ "");
        });

        btnListado.setOnClickListener(view -> {
            Intent login=new Intent(PrincipalActivity.this, ChismografoActivity.class);
            //login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
            //finish();

        });

        getPreguntas();
    }

    //Peticion API

    private void deleteRespuestas(String  idUsuario){
        Call<JsonObject> serviceDownload = retrofit.create(UrlInterface.class).deleteRespuestas(idUsuario);
        serviceDownload.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.e("e",response+"");
                if(response.code() >= 200 && response.code() <300) {
                    try{
                        JSONObject object = new JSONObject(response.body().toString());
                        if(object.getInt("success")==1){
                            Utils.snackExito(btnContestar,"Tus respuestas eliminadas correctamente",getApplicationContext());
                        }else{
                            Utils.snackAlerta(btnContestar,"Error",getApplicationContext());
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }else{
                    Utils.snackError(btnContestar,"Error",getApplicationContext());

                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getPreguntasResp(String  idUsuario){
        Call<JsonObject> serviceDownload = retrofit.create(UrlInterface.class).getPreguntasResp(idUsuario);
        serviceDownload.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.e("e",response+"");
                if(response.code() >= 200 && response.code() <300) {
                    try{
                        JSONObject object = new JSONObject(response.body().toString());
                        if(object.getInt("success")==1){
                            Utils.snackError(btnContestar,"Ya contestaste tus preguntas",getApplicationContext());
                        }else{
                            Intent login=new Intent(PrincipalActivity.this, ContestarPreguntasActivity.class);
                            //login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(login);
                            //finish();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }else{
                    Utils.snackError(btnContestar,"Error",getApplicationContext());

                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void getPreguntas(){

        Call<JsonObject> serviceDownload = retrofit.create(UrlInterface.class).getPreguntas();
        serviceDownload.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.e("e",response+"");
                if(response.code() >= 200 && response.code() <300) {
                    resultDatos(response.body());
                }else {
                    dialogReintentar("Comentarios", "No se pudo enviar tu registro,¿Desea volver a intentar?",
                            (dialog, which) ->
                                    getPreguntas());
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Utils.progresoDismiss();
                Log.e("PreReg",t+" -- "+call);
                dialogReintentar("Comentarios", "Revise su conexion a internet,¿Desea volver a intentar?",
                        (dialog, which) ->
                                getPreguntas());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resultDatos(JsonObject objecto){

        try{
            JSONObject object = new JSONObject(objecto.toString());
            if(object.getInt("success")==1){

                JSONArray content = object.getJSONArray("content");
                lista.clear();

                for (int i=0;i<content.length();i++){
                    JSONObject preguntas = content.getJSONObject(i);

                    Gson gson = new Gson();
                    ModeloPregunta pregunta = gson.fromJson(String.valueOf(preguntas), ModeloPregunta.class);

                    lista.add(pregunta);
                    Utils.listaGlobal = lista;
                }

            }else{
                Utils.alertDialog(this,"Aviso","Ocurrió un error por favor vuelve a intentar");
            }
        }catch (JSONException e){
            e.printStackTrace();
            Utils.alertDialog(this,"Aviso","Ocurrió un error en la respuesta");
        }
    }



    public void dialogReintentar(String titulo, String mensaje, DialogInterface.OnClickListener okListener) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PrincipalActivity.this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje)
                .setPositiveButton("Reintentar", okListener)
                .setNegativeButton("Cancelar", (dialog, id) -> {

                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}