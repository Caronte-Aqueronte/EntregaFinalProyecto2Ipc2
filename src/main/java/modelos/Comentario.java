/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

/**
 *
 * @author Luis Monterroso
 */
public class Comentario extends Revista{
    private String contenidoComentario;

    public Comentario(String contenidoComentario, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.contenidoComentario = contenidoComentario;
    }

    public String getContenidoComentario() {
        return contenidoComentario;
    }

    public void setContenidoComentario(String contenidoComentario) {
        this.contenidoComentario = contenidoComentario;
    }
    
}
