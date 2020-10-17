package com.chazzca.chismografoforte;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chazzca.chismografoforte.Interfaces.UrlInterface;
import com.chazzca.chismografoforte.models.ModeloUsuario;
import com.chazzca.chismografoforte.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.chazzca.chismografoforte.utils.Utils.volverInicio;

public class LoginActivity extends AppCompatActivity {

    protected Retrofit retrofit;
    EditText edtUsuario,edtPass;
    private Button btnInicio,btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUsuario = findViewById(R.id.edtUsuario);
        edtPass = findViewById(R.id.edtPass);
        btnInicio = findViewById(R.id.btnInicio);
        btnRegistro = findViewById(R.id.btnRegistro);

        ////RETROFIT
        retrofit = Utils.retrofit;


        btnInicio.setOnClickListener(view -> {
            if(edtUsuario.getText().toString().isEmpty()){
                Utils.snackAlerta(edtUsuario,"No debes dejar capos vacios",this);
            }else if(edtPass.getText().toString().isEmpty()){
                Utils.snackAlerta(edtUsuario,"No debes dejar capos vacios",this);
            }else{
                if(Utils.isConnected(getApplicationContext())){
                    Utils.progressDialogGen(this,"Iniciando",true,true);
                    login(edtUsuario.getText().toString(),edtPass.getText().toString());
                }else{
                    Utils.snackAlerta(edtPass,"Verifique su conexión a Internet",getApplicationContext());
                }
            }
        });


        btnRegistro.setOnClickListener(view -> {
            Intent login=new Intent(this, RegistroActivity.class);
            //login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(login);
        });

    }

    //Peticion API
    private void login(String usuario,String pass){

        Call<JsonObject> serviceDownload = retrofit.create(UrlInterface.class).login(usuario,pass);
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
                                    login(usuario,pass));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Utils.progresoDismiss();
                Log.e("PreReg",t+" -- "+call);
                dialogReintentar("Comentarios", "Revise su conexion a internet,¿Desea volver a intentar?",
                        (dialog, which) ->
                                login(usuario,pass));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void resultDatos(JsonObject objecto){

        try{
            JSONObject object = new JSONObject(objecto.toString());
            if(object.getInt("success")==1){
                Utils.progresoDismiss();

                JSONArray content = object.getJSONArray("content");
                JSONObject content0 = content.getJSONObject(0);

                Gson gson = new Gson();
                Utils.usuarioGlobal = gson.fromJson(String.valueOf(content0), ModeloUsuario.class);
                Log.e("TEST",Utils.usuarioGlobal.getUsername());


                Intent login=new Intent(this, PrincipalActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(login);
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje)
                .setPositiveButton("Reintentar", okListener)
                .setNegativeButton("Cancelar", (dialog, id) -> {

                });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }
}