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
public class TagRevista extends Tag {

    private String nombreRevista;
    private String usuarioCreador;

    /**
     * Constructor de la clase TagRevista que inicia los atributos de la clase
     * con los parametros establecidos
     *
     * @param nombreRevista
     * @param usuarioCreador
     * @param nombreTag
     */
    public TagRevista(String nombreRevista, String usuarioCreador, String nombreTag) {
        super(nombreTag);
        this.nombreRevista = nombreRevista;
        this.usuarioCreador = usuarioCreador;
    }
    //Getters y setters
    public String getNombreRevista() {
        return nombreRevista;
    }

    public void setNombreRevista(String nombreRevista) {
        this.nombreRevista = nombreRevista;
    }

    public String getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(String usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

}
