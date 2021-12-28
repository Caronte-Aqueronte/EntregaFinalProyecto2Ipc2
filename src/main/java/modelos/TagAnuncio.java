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
public class TagAnuncio extends Tag{
    private String nombreAnuncio;
    private String nombreAunciante;

    public TagAnuncio(String nombreAnuncio, String nombreAunciante, String nombreTag) {
        super(nombreTag);
        this.nombreAnuncio = nombreAnuncio;
        this.nombreAunciante = nombreAunciante;
    }

    public String getNombreAnuncio() {
        return nombreAnuncio;
    }

    public void setNombreAnuncio(String nombreAnuncio) {
        this.nombreAnuncio = nombreAnuncio;
    }

    public String getNombreAunciante() {
        return nombreAunciante;
    }

    public void setNombreAunciante(String nombreAunciante) {
        this.nombreAunciante = nombreAunciante;
    }
    
}
