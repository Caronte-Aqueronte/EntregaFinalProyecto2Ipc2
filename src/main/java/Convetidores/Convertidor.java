/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Convetidores;

import com.google.gson.Gson;

/**
 *
 * @author Luis Monterroso
 * @param <Objeto>
 */
public abstract class Convertidor<Objeto> {

    private Gson json;
    private Class<Objeto> tipo;

    public Convertidor(Class<Objeto> tipo) {
        this.json = new Gson();
        this.tipo = tipo;
    }

    public Objeto deJsonAClase(String cadenaJson) {
        return json.fromJson(cadenaJson, tipo);
    }

    public String deObjetoAJson(Objeto objeto) {
        return json.toJson(objeto, tipo);
    }
}
