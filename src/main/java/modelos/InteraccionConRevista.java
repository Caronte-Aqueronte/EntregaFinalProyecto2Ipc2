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
public class InteraccionConRevista {

    private boolean estadoLikes;
    private boolean estadoComentarios;
    private boolean estadoSuscripciones;

    public InteraccionConRevista(boolean estadoLikes, boolean estadoComentarios, boolean estadoSuscripciones) {
        this.estadoLikes = estadoLikes;
        this.estadoComentarios = estadoComentarios;
        this.estadoSuscripciones = estadoSuscripciones;
    }

    public boolean isEstadoLikes() {
        return estadoLikes;
    }

    public void setEstadoLikes(boolean estadoLikes) {
        this.estadoLikes = estadoLikes;
    }

    public boolean isEstadoComentarios() {
        return estadoComentarios;
    }

    public void setEstadoComentarios(boolean estadoComentarios) {
        this.estadoComentarios = estadoComentarios;
    }

    public boolean isEstadoSuscripciones() {
        return estadoSuscripciones;
    }

    public void setEstadoSuscripciones(boolean estadoSuscripciones) {
        this.estadoSuscripciones = estadoSuscripciones;
    }

}
