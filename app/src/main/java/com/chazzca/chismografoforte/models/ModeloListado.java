package com.chazzca.chismografoforte.models;

import java.util.ArrayList;

public class ModeloListado {

    public int Id;
    public String Username;
    public ArrayList<ModeloPreguntasRespuesta> preguntas;

    public ModeloListado(int id, String username, ArrayList<ModeloPreguntasRespuesta> preguntas) {
        Id = id;
        Username = username;
        this.preguntas = preguntas;
    }
}
