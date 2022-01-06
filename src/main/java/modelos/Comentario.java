/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author Luis Monterroso
 */
public class Comentario extends Revista {

    private String contenidoComentario;
    private LocalDate fechaDeComentario;
    private String nombreUsuarioComentador;

    public Comentario(String contenidoComentario, LocalDate fechaDeComentario, String nombreUsuarioComentador, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.contenidoComentario = contenidoComentario;
        this.fechaDeComentario = fechaDeComentario;
        this.nombreUsuarioComentador = nombreUsuarioComentador;
    }

    

    public String getContenidoComentario() {
        return contenidoComentario;
    }

    public void setContenidoComentario(String contenidoComentario) {
        this.contenidoComentario = contenidoComentario;
    }

    public LocalDate getFechaDeComentario() {
        return fechaDeComentario;
    }

    public void setFechaDeComentario(LocalDate fechaDeComentario) {
        this.fechaDeComentario = fechaDeComentario;
    }

    public String getNombreUsuarioComentador() {
        return nombreUsuarioComentador;
    }

    public void setNombreUsuarioComentador(String nombreUsuarioComentador) {
        this.nombreUsuarioComentador = nombreUsuarioComentador;
    }

}
