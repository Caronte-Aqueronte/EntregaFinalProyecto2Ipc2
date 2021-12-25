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
public class TagUsuario extends Tag {

    private String nombreDeUsuario;

    /**
     * Constructor de la clase que inicializa el nombreDeUsuario y el nombreTag
     *
     * @param nombreDeUsuario
     * @param nombreTag
     */
    public TagUsuario(String nombreDeUsuario, String nombreTag) {
        super(nombreTag);
        this.nombreDeUsuario = nombreDeUsuario;
    }

    //getters y setters
    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

}
