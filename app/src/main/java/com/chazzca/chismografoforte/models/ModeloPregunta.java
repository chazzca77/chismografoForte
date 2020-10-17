package com.chazzca.chismografoforte.models;



public class ModeloPregunta {
    public int Id;
    public String Descripcion;

    public ModeloPregunta() {
        Id = 0;
        Descripcion = null;
    }

    public int getId() {
        return Id;
    }

    public void setNombrea(String Descripcion) {
        this.Descripcion = Descripcion;
    }
}