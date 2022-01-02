/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import java.io.InputStream;

/**
 *
 * @author Luis Monterroso
 */
public class AnuncioImagen extends Anuncio {

    private String textoAnuncio;
    private InputStream imagen;

    /**
     * Crea un AnuncioImagen para insertar en la bd
     *
     * @param texto
     * @param imagen
     * @param nombreAnuncio
     * @param nombreAnunciante
     */
    public AnuncioImagen(String textoAnuncio, InputStream imagen, String nombreAnuncio, String nombreAnunciante) {
        super(nombreAnuncio, "Imagen", 200, "Activo", nombreAnunciante);
        this.textoAnuncio = textoAnuncio;
        this.imagen = imagen;
    }


    
    public String getTexto() {
        return textoAnuncio;
    }

    public void setTexto(String texto) {
        this.textoAnuncio = texto;
    }

    public InputStream getImagen() {
        return imagen;
    }

    public void setImagen(InputStream imagen) {
        this.imagen = imagen;
    }

}
