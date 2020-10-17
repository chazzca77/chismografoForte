package com.chazzca.chismografoforte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chazzca.chismografoforte.Interfaces.UrlInterface;
import com.chazzca.chismografoforte.models.ModeloPregunta;
import com.chazzca.chismografoforte.utils.Utils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.chazzca.chismografoforte.utils.Utils.volverInicio;

public class ContestarPreguntasActivity extends AppCompatActivity {

    private TextView txtPregunta;
    private EditText edtRespuesta;
    private Button btnSiguiente;
    private int contador=0;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contestar_preguntas);

        txtPregunta = findViewById(R.id.txtPregunta);
        edtRespuesta = findViewById(R.id.edtRespuesta);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        retrofit = Utils.retrofit;

        txtPregunta.setText(Utils.listaGlobal.get(0).Id + " " + Utils.listaGlobal.get(0).Descripcion );


        btnSiguiente.setOnClickListener(view -> {
            if(contador == Utils.listaGlobal.size()){
                finish();
            }else{
                setRespuesta(Utils.listaGlobal.get(contador).Id+"",Utils.usuarioGlobal.Id+"",edtRespuesta.getText().toString());
            }


        });

    }

    //Peticion API
    private void setRespuesta(String idPregunta,String idUsuario,String respuesta){

        Call<JsonObject> serviceDownload = retrofit.create(UrlInterface.class).setRespuesta(idPregunta,idUsuario,respuesta);
        serviceDownload.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Log.e("e",response+"");
                if(response.code() >= 200 && response.code() <300) {
                    resultDatos(response.body());
                }else {
                    dialogReintentar("Comentarios", "No se pudo enviar tu registro,¿Desea volver a intentar?",
                            (dialog, which) ->
                                    setRespuesta(idPregunta,idUsuario,respuesta));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("PreReg",t+" -- "+call);
                dialogReintentar("Comentarios", "Revise su conexion a internet,¿Desea volver a intentar?",
                        (dialog, which) ->
                                setRespuesta(idPregunta,idUsuario,respuesta));
            }
        });
    }

    public void resultDatos(JsonObject objecto){

        try{
            JSONObject object = new JSONObject(objecto.toString());
            if(object.getInt("success")==1){
                contador++;
                if(contador >= Utils.listaGlobal.size()){
                    finish();
                }else{
                    txtPregunta.setText(Utils.listaGlobal.get(contador).Id + " " + Utils.listaGlobal.get(contador).Descripcion );
                    edtRespuesta.setText("");
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ContestarPreguntasActivity.this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje)
                .setPositiveButton("Reintentar", okListener)
                .setNegativeButton("Cancelar", (dialog, id) -> {

                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }


}