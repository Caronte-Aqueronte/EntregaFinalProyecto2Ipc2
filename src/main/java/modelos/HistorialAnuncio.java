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
public class HistorialAnuncio extends Anuncio{
    
    private String url;
    private String fechaDeDespliegue;
    
    public HistorialAnuncio(String url, String nombreAnuncio, String nombreAnunciante) {
        super(nombreAnuncio, nombreAnunciante);
        this.url = url;
    }

    public HistorialAnuncio(String url, String fechaDeDespliegue, String nombreAnuncio, String nombreAnunciante) {
        super(nombreAnuncio, nombreAnunciante);
        this.url = url;
        this.fechaDeDespliegue = fechaDeDespliegue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFechaDeDespliegue() {
        return fechaDeDespliegue;
    }

    public void setFechaDeDespliegue(String fechaDeDespliegue) {
        this.fechaDeDespliegue = fechaDeDespliegue;
    }
    
}
