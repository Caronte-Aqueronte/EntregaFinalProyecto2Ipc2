/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelosReportes;

import java.time.LocalDate;
import modelos.Revista;

/**
 *
 * @author Luis Monterroso
 */
public class LikeDeRevista extends Revista {

    private LocalDate fechaLike;
    private int numeroLikes;

    /**
     * Construye un like de revista donde adjunta la fecha del like y las veces
     * que se a dado like a esa revista
     *
     * @param fechaLike
     * @param numeroLikes
     * @param nombreRevista
     * @param usuarioCreador
     */
    public LikeDeRevista(LocalDate fechaLike, int numeroLikes, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.fechaLike = fechaLike;
        this.numeroLikes = numeroLikes;
    }

    /**
     * COnstruye un like simple donde solo adjuntara la revista y la fecha del
     * like
     *
     * @param fechaLike
     * @param nombreRevista
     * @param usuarioCreador
     */
    public LikeDeRevista(LocalDate fechaLike, String nombreRevista, String usuarioCreador) {
        super(nombreRevista, usuarioCreador);
        this.fechaLike = fechaLike;
    }

    public LocalDate getFechaLike() {
        return fechaLike;
    }

    public void setFechaLike(LocalDate fechaLike) {
        this.fechaLike = fechaLike;
    }

    public int getNumeroLikes() {
        return numeroLikes;
    }

    public void setNumeroLikes(int numeroLikes) {
        this.numeroLikes = numeroLikes;
    }

}
