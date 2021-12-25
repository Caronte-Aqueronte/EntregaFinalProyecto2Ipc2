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
public class Tag {

    private String nombreTag;

    /**
     * Constructor de la clase Tag
     *
     * @param nombreTag
     */
    public Tag(String nombreTag) {
        this.nombreTag = nombreTag;
    }

    //getters y setters
    public String getNombreTag() {
        return nombreTag;
    }

    public void setNombreTag(String nombreTag) {
        this.nombreTag = nombreTag;
    }

}
