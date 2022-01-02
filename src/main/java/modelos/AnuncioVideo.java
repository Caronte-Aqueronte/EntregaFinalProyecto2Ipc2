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
public class AnuncioVideo extends Anuncio{
    String link;
    String textoAnuncio;

    public AnuncioVideo(String link, String textoAnuncio, String nombreAnuncio, String nombreAnunciante) {
        super(nombreAnuncio, "Video", 300, "Activo", nombreAnunciante);
        this.link = link;
        this.textoAnuncio = textoAnuncio;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTextoAnuncio() {
        return textoAnuncio;
    }

    public void setTextoAnuncio(String textoAnuncio) {
        this.textoAnuncio = textoAnuncio;
    }
    
    
}
