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
public class AnuncioTexto extends Anuncio {

    private String texto;

    /**
     * Crea un anuncio de texto para insertar en la db
     *
     * @param texto
     * @param nombreAnuncio
     * @param nombreAnunciante
     */
    public AnuncioTexto(String texto, String nombreAnuncio, String nombreAnunciante) {
        super(nombreAnuncio, "Texto", 100, "Activo", nombreAnunciante);
        this.texto = texto;
    }

    /**
     * Crea un anuncio desde cero
     *
     * @param texto
     * @param nombreAnuncio
     * @param tipoAnuncio
     * @param pago
     * @param estado
     * @param nombreAnunciante
     */
    public AnuncioTexto(String texto, String nombreAnuncio, String tipoAnuncio, double pago, String estado, String nombreAnunciante) {
        super(nombreAnuncio, tipoAnuncio, pago, estado, nombreAnunciante);
        this.texto = texto;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

}
