package com.chazzca.chismografoforte.models;


import java.util.List;

public class ListaPreguntas {
    public List<ModeloPregunta> listaPreguntas;

    public ListaPreguntas(List<ModeloPregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    public List<ModeloPregunta> getListaPreguntas() {
        return listaPreguntas;
    }

    public void setListaPreguntas(List<ModeloPregunta> listaPreguntas) {
        this.listaPreguntas = listaPreguntas;
    }

    @Override
    public String toString() {
        return "ListaPreguntas{" +
                "listaPreguntas=" + listaPreguntas +
                '}';
    }
}