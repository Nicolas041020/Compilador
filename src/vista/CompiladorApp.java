/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import controlador.Controlador;
import java.io.IOException;
import modelo.Compilador;

/**
 *
 * @author nicor
 */
public class CompiladorApp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException{
        Controlador ctrl = new Controlador();
        ctrl.compilar();
//        logica.Separador();
//        logica.AlgoritmoPrincipal();
//        logica.conversionesInfaPos();
//        logica.GenerarTxt();
    }
}
