/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelosApi;

import java.io.InputStream;

/**
 *
 * @author Luis Monterroso
 */
public class Revista {

    private String nombreRevista;
    private String descripcion;
    private String categoria;
    private InputStream contenido;
    private InputStream miniatura;
    private double costoDeSuscripcion;
    private String estadoSuscripcion;
    private String estadoComentarios;
    private String estadoLikes;
    private String fechaDePublicacion;
    private String usuarioCreador;

    /**
     * Constructor de la clase Revista que inicializa todos los atributos de la
     * clase
     *
     * @param nombreRevista
     * @param descripcion
     * @param categoria
     * @param contenido
     * @param miniatura
     * @param costoDeSuscripcion
     * @param estadoSuscripcion
     * @param estadoComentarios
     * @param estadoLikes
     * @param fechaDePublicacion
     * @param usuarioCreador
     */
    public Revista(String nombreRevista, String descripcion, String categoria, InputStream contenido, InputStream miniatura, double costoDeSuscripcion, String estadoSuscripcion, String estadoComentarios, String estadoLikes, String fechaDePublicacion, String usuarioCreador) {
        this.nombreRevista = nombreRevista;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.contenido = contenido;
        this.miniatura = miniatura;
        this.costoDeSuscripcion = costoDeSuscripcion;
        this.estadoSuscripcion = estadoSuscripcion;
        this.estadoComentarios = estadoComentarios;
        this.estadoLikes = estadoLikes;
        this.fechaDePublicacion = fechaDePublicacion;
        this.usuarioCreador = usuarioCreador;
    }
    
    /**
     * COnstructor para representar una revista como una card
     * @param nombreRevista
     * @param descripcion
     * @param categoria 
     */
    public Revista(String nombreRevista, String descripcion, String categoria) {
        this.nombreRevista = nombreRevista;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    //Getters y Setters de la clase
    public String getNombreRevista() {
        return nombreRevista;
    }

    public void setNombreRevista(String nombreRevista) {
        this.nombreRevista = nombreRevista;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public InputStream getContenido() {
        return contenido;
    }

    public void setContenido(InputStream contenido) {
        this.contenido = contenido;
    }

    public double getCostoDeSuscripcion() {
        return costoDeSuscripcion;
    }

    public void setCostoDeSuscripcion(double costoDeSuscripcion) {
        this.costoDeSuscripcion = costoDeSuscripcion;
    }

    public String getEstadoSuscripcion() {
        return estadoSuscripcion;
    }

    public void setEstadoSuscripcion(String estadoSuscripcion) {
        this.estadoSuscripcion = estadoSuscripcion;
    }

    public String getEstadoComentarios() {
        return estadoComentarios;
    }

    public void setEstadoComentarios(String estadoComentarios) {
        this.estadoComentarios = estadoComentarios;
    }

    public String getEstadoLikes() {
        return estadoLikes;
    }

    public void setEstadoLikes(String estadoLikes) {
        this.estadoLikes = estadoLikes;
    }

    public String getFechaDePublicacion() {
        return fechaDePublicacion;
    }

    public void setFechaDePublicacion(String fechaDePublicacion) {
        this.fechaDePublicacion = fechaDePublicacion;
    }

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public InputStream getMiniatura() {
        return miniatura;
    }

    public void setMiniatura(InputStream miniatura) {
        this.miniatura = miniatura;
    }
    
}

