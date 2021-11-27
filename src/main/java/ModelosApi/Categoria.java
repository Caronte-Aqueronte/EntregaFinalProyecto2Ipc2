/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModelosApi;

/**
 * Clase que representa el concepto de una categoria dentro del sistema
 *
 * @author Luis Monterroso
 */
public class Categoria {

    //Atributos que representan a una categoria
    String nombreCategoria;

    /**
     * Constructor de la clase Categoria que inicializa los atributos de la
     * clase
     *
     * @param nombreCategoria
     */
    public Categoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

}
