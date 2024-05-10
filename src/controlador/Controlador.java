/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.io.IOException;
import modelo.Compilador;

/**
 *
 * @author Usuario
 */
public class Controlador {
    private Compilador compilador;
           
    public Controlador(){
        compilador = new Compilador();
    }
    
    public Compilador getCompilador(){
        return compilador;
    }
    
    public void compilar() throws IOException{
        compilador.compilar();
    }
            
}
