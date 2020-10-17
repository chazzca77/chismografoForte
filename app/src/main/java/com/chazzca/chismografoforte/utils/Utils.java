package com.chazzca.chismografoforte.utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chazzca.chismografoforte.LoginActivity;
import com.chazzca.chismografoforte.R;
import com.chazzca.chismografoforte.models.ModeloPregunta;
import com.chazzca.chismografoforte.models.ModeloUsuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Utils {

    public static final String urlBase = "http://10.0.2.2/chismografo/php/";
    public static final String urlWebServices = urlBase + "";

    public static final Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    public static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100,TimeUnit.SECONDS).build();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Utils.urlWebServices)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    public static  ArrayList<ModeloPregunta> listaGlobal = new ArrayList<>();
    public static ModeloUsuario usuarioGlobal;

    public static ProgressDialog progreso;


    public static void toastLargo(Context context, String texto) {
        Toast.makeText(context, texto, Toast.LENGTH_LONG).show();
    }

    public static void volverInicio(Context context){
        Intent login=new Intent(context, LoginActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(login);

    }

    public static void progressDialogGen(Context context , String texto, Boolean indeterminate, Boolean cancelable){
        progreso = new ProgressDialog(context);
        progreso.setIndeterminate(indeterminate);
        progreso.setCancelable(cancelable);
        progreso.setMessage(texto);
        progreso.show();
    }

    public static void progresoDismiss(){
        progreso.dismiss();
    }

    public static boolean isConnected(Context context) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ConnectivityManager cm = (ConnectivityManager)context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("http://www.google.com/");
                HttpURLConnection urlc = (HttpURLConnection)url.openConnection();
                urlc.setRequestProperty("User-Agent", "test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(3000); // mTimeout is in seconds
                urlc.connect();
                if (urlc.getResponseCode() == 200 ) {
                    return true;
                } else {
                    return false;
                }
            } catch (IOException e) {
                Log.i("warning", "Error checking internet connection", e);
                return false;
            }
        }
        return false;
    }



    public static void alertDialog(Context context, String titulo, String texto){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setTitle(titulo);
        alert.setMessage(texto);
        alert.setPositiveButton("Ok", (dialog, which) -> {
            // getActivity().finish();
        });
        alert.show();
    }

    public static void alertDialogListener(Context context, String titulo, String texto, DialogInterface.OnClickListener okListener, DialogInterface.OnCancelListener cancelListener){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setTitle(titulo);
        alert.setMessage(texto);
        alert.setPositiveButton("Ok", okListener);
        alert.setOnCancelListener(cancelListener);
        alert.show();
    }

    public static void alertDialogRecompensa(Context context, String titulo, String texto, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener noListener){
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
        alert.setTitle(titulo);
        alert.setMessage(texto);
        alert.setPositiveButton("Ok", okListener);
        alert.setNegativeButton("Esta vez no",noListener);
        alert.show();
    }

    public static String ucFirst(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }


    public static void snackExito(View view, String texto, Context context){
        Snackbar snackbar = Snackbar.make(view, texto, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.verde));
        snackbar.show();
    }

    public static void snackAlerta(View view, String texto, Context context){
        Snackbar snackbar = Snackbar.make(view, texto, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.anaranjado));
        snackbar.show();
    }

    public static void snackError(View view, String texto, Context context){
        Snackbar snackbar = Snackbar.make(view, texto, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(context.getResources().getColor(R.color.rojo));
        snackbar.show();
    }



}
