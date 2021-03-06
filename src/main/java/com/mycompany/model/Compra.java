package com.mycompany.model;

public class Compra {
    private String usuario;
    private String nomPelicula;
    private int fila;
    private int asiento;
    private String horaSesion;
    private String sala;
    private double precio;
    
    public Compra(){
        
    }

    public Compra(String usuario,String nomPelicula, int fila, int asiento, String horaSesion, String sala) {
        this.usuario=usuario;
        this.nomPelicula = nomPelicula;
        this.fila = fila;
        this.asiento = asiento;
        this.horaSesion = horaSesion;
        this.sala = sala;
        this.precio = 5.00;
    }
    
    public String getNomPelicula() {
        return nomPelicula;
    }

    public void setNomPelicula(String nomPelicula) {
        this.nomPelicula = nomPelicula;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getAsiento() {
        return asiento;
    }

    public void setAsiento(int asiento) {
        this.asiento = asiento;
    }

    public String getHoraSesion() {
        return horaSesion;
    }

    public void setHoraSesion(String horaSesion) {
        this.horaSesion = horaSesion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }
    
}
