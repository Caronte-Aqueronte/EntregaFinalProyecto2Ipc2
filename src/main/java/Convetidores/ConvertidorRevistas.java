/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Convetidores;

import ModelosApi.Revista;
import java.util.ArrayList;

/**
 *
 * @author Luis Monterroso
 */
public class ConvertidorRevistas extends Convertidor<ArrayList<Revista>>{

    public ConvertidorRevistas(Class<ArrayList<Revista>> tipo) {
        super(tipo);
    }
    
}
