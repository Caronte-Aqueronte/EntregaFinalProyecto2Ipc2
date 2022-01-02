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

    private String textoAnuncio;

    /**
     * Crea un anuncio de texto para insertar en la db
     *
     * @param texto
     * @param nombreAnuncio
     * @param nombreAnunciante
     */
    public AnuncioTexto(String texto, String nombreAnuncio, String nombreAnunciante) {
        super(nombreAnuncio, "Texto", 100, "Activo", nombreAnunciante);
        this.textoAnuncio = texto;
    }

    /**
     * Crea un anuncio desde cero
     *
     * @param textoAnuncio
     * @param nombreAnuncio
     * @param tipoAnuncio
     * @param pago
     * @param estado
     * @param nombreAnunciante
     */
    public AnuncioTexto(String textoAnuncio, String nombreAnuncio, String tipoAnuncio, double pago, String estado, String nombreAnunciante) {
        super(nombreAnuncio, tipoAnuncio, pago, estado, nombreAnunciante);
        this.textoAnuncio = textoAnuncio;
    }

    public String getTextoAnuncio() {
        return textoAnuncio;
    }

    public void setTextoAnuncio(String textoAnuncio) {
        this.textoAnuncio = textoAnuncio;
    }

   

}
