package com.chazzca.chismografoforte.models;

public class ModeloPreguntasRespuesta {

    public int IdPregunta;
    public String pregunta;
    public String respuesta;

    public ModeloPreguntasRespuesta() {
        IdPregunta = 0;
        pregunta = null;
        respuesta = null;
    }

    public int getIdPregunta() {
        return IdPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        IdPregunta = idPregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
