package com.chazzca.chismografoforte.Interfaces;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UrlInterface {
    ////////PHP

    @POST("getPreguntas.php")
    Call<JsonObject> getPreguntas();


    @FormUrlEncoded
    @POST("setRegistro.php")
    Call<JsonObject> setRegistro(@Field("nombre") String nombre,
                                   @Field("pass") String pass,
                                   @Field("email") String email,
                                   @Field("usuario") String usuario);

    @POST("getListadoChisme.php")
    Call<JsonObject> getListadoChisme();

    @FormUrlEncoded
    @POST("login.php")
    Call<JsonObject> login(@Field("usuario") String usuario,
                                         @Field("password") String password    );

    @FormUrlEncoded
    @POST("setRespuesta.php")
    Call<JsonObject> setRespuesta(@Field("idPregunta") String idPregunta,
                                       @Field("idUsuario") String idUsuario,
                                       @Field("respuesta") String respuesta);
    @FormUrlEncoded
    @POST("deleteRespuestas.php")
    Call<JsonObject> deleteRespuestas(@Field("idUsuario") String idUsuario);

    @FormUrlEncoded
    @POST("getPreguntasResp.php")
    Call<JsonObject> getPreguntasResp(@Field("idUsuario") String idUsuario);


    @FormUrlEncoded
    @POST("editRespuesta.php")
    Call<JsonObject> editRespuesta(@Field("respuesta") String respuesta,
                                  @Field("idUsuario") String idUsuario,
                                  @Field("idPregunta") String idPregunta);

}
