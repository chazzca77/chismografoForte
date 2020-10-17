package com.chazzca.chismografoforte.models;



public class ModeloUsuario {

    public int Id;
    public String Username,Email;


    public ModeloUsuario(int id, String username, String email) {
        Id = id;
        Username = username;
        Email = email;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}