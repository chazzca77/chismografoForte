package com.chazzca.chismografoforte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chazzca.chismografoforte.Interfaces.UrlInterface;
import com.chazzca.chismografoforte.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

import static com.chazzca.chismografoforte.utils.Utils.volverInicio;

public class RegistroActivity extends AppCompatActivity {

    private EditText edtNombre,edtCorreo,edtPass,edtUsuario;
    private Button btnRegistro;
    protected Retrofit retrofit;


    private void inicializar(){
        btnRegistro = findViewById(R.id.btnRegistro);
        edtNombre = findViewById(R.id.edtNombre);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtPass = findViewById(R.id.edtPass);
        edtUsuario = findViewById(R.id.edtUsuario);

        ////RETROFIT
        retrofit = Utils.retrofit;


        btnRegistro.setOnClickListener(view -> {
            if(edtNombre.getText().toString().isEmpty()){
                Utils.snackAlerta(edtNombre,"No debes dejar capos vacios",this);
            }else if(edtCorreo.getText().toString().isEmpty()){
                Utils.snackAlerta(edtNombre,"No debes dejar capos vacios",this);
            }else if(edtUsuario.getText().toString().isEmpty()){
                Utils.snackAlerta(edtNombre,"No debes dejar capos vacios",this);
            }else if(edtPass.getText().toString().isEmpty()){
                Utils.snackAlerta(edtNombre,"No debes dejar capos vacios",this);
            }else{
                if(Utils.isConnected(getApplicationContext())){
                    Utils.progressDialogGen(this,"Registrando",true,true);

                    setRegistro(edtNombre.getText().toString(),edtPass.getText().toString(),edtCorreo.getText().toString(),edtUsuario.getText().toString());

                }else{
                    Utils.snackAlerta(edtCorreo,"Verifique su conexión a Internet",getApplicationContext());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        inicializar();
    }

    //Peticion API
    private void setRegistro(String nombre,String pass,String email,String usuario){

        Call<JsonObject> serviceDownload = retrofit.create(UrlInterface.class).setRegistro(nombre,pass,email,usuario);
        serviceDownload.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                Utils.progresoDismiss();
                Log.e("e",response+"");
                if(response.code() >= 200 && response.code() <300) {
                    resultDatos(response.body());
                }else {
                    dialogReintentar("Comentarios", "No se pudo enviar tu registro,¿Desea volver a intentar?",
                            (dialog, which) ->
                                    setRegistro(nombre,pass,email,usuario));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Utils.progresoDismiss();
                Log.e("PreReg",t+" -- "+call);
                dialogReintentar("Comentarios", "Revise su conexion a internet,¿Desea volver a intentar?",
                        (dialog, which) ->
                                setRegistro(nombre,pass,email,usuario));
            }
        });
    }

    public void resultDatos(JsonObject objecto){

        try{
            JSONObject object = new JSONObject(objecto.toString());
            if(object.getInt("success")==1){
                Utils.progresoDismiss();
                Utils.snackExito(btnRegistro,"Registro Exitoso",this);
                volverInicio(this);
                finish();
            }else{
                Utils.alertDialog(this,"Aviso","Ocurrió un error por favor vuelve a intentar");
            }
        }catch (JSONException e){
            e.printStackTrace();
            Utils.alertDialog(this,"Aviso","Ocurrió un error en la respuesta");
        }
    }



    public void dialogReintentar(String titulo, String mensaje, DialogInterface.OnClickListener okListener) {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegistroActivity.this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje)
                .setPositiveButton("Reintentar", okListener)
                .setNegativeButton("Cancelar", (dialog, id) -> {

                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}