/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import gfutria.Expresiones;

/**
 *
 * @author nicor
 */
public class Compilador {

    private List<String> lista;
    private Queue<String> sentenciasIf;
    private Queue<String> sentenciasWhile;
    private Queue<String> sentenciasFor;
    private Stack<String> endFor;
    private String arr[][];
    private Stack<String> secuenciaIfWhileFor;
    private Stack<Integer> pila;
    private int numL;

//    private Stack<Double> pilaNumeros;
//    private Stack<Boolean> pilaBoolean;
//    private Queue<String> opAritmetico;
//    private Queue<String> opLogico;
    /**
     * En el constructor se inicializa los atributos normalmente, pero en las
     * posiciones iniciales del arreglo de retorno se asignan los nombres
     * correspondientes a los datos para facilitar el entendimiento al usuario.
     */
    public Compilador() {
        LeerTxt();
        sentenciasIf = new LinkedList();
        sentenciasWhile = new LinkedList();
        sentenciasFor = new LinkedList();
        endFor = new Stack();
        arr = new String[lista.size() + 1][3];
        secuenciaIfWhileFor = new Stack();
        pila = new Stack();
        numL = 1;
        arr[0][0] = "i";
        arr[0][1] = "Expresión";
        arr[0][2] = "Jump";

//        pilaNumeros = new Stack();
//        pilaBoolean = new Stack();
//        opAritmetico = new LinkedList();
//        opLogico = new LinkedList();
    }

    /**
     * Este metodo lee un archivo, almacena el contenido del archivo por linea y
     * lo guarda en un arrayList llamado lineas por ultimo,de la anterior se
     * copian los datos a la lista.
     */
    public void LeerTxt() {
        //"C:\Users\57300\Documents\JULIAN\U\V\TIC\ArchivosComp-Inter\Entrada1.in"
        //String nombreArchivo = "C:\\Users\\tomas\\OneDrive\\Documentos\\Tomás Vera\\Universidad\\Programación\\5to Semestre\\Entrada.txt";
        String nombreArchivo = "C:\\Users\\57300\\Documents\\JULIAN\\U\\V\\TIC\\ArchivosComp-Inter\\DirectionChange.in";
        List<String> lineas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.length() > 0) {
                    lineas.add(LimpiarTxt(linea));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        lista = lineas;
    }

    /**
     * Este metodo quita los espacios y diferencia los tipos de datos primitivos
     * a el otro texto en las lineas para facilitar el proceso de compilación.
     *
     * @param linea
     * @return String retorna la cadena sin espacios y con los tipos de datos
     * primitivos separados con un espacio.
     */
    private String LimpiarTxt(String linea) {
        return linea.trim().replaceAll(" ", "").replaceAll("int", "int ")
                .replaceAll("String", "String ").replaceAll("double", "double ")
                .replaceAll("boolean", "boolean ").replaceAll("long", "long ")
                .replaceAll("char", "char ").replaceAll("print ", "print").replaceAll("\\“","\"").replaceAll("\\”", "\"");
    }

    /**
     * Este metodo crea un nuevo archivo txt en donde se almacenara el arreglo
     * nuevo con el codigo recibido ya compilado.
     *
     * @throws IOException
     */
    public void GenerarTxt() throws IOException {
        try (FileWriter fichero = new FileWriter("C:\\Users\\57300\\Documents\\JULIAN\\U\\V\\TIC\\ArchivosComp-Inter\\DirectionChangeSalida.out")) {
            String salida = "";
            for (int i = 0; i < arr.length; i++) {
                for (int j = 0; j < arr[i].length; j++) {
                    if (arr[i][j] != null && !arr[i][j].equals("")) {
                        salida += "[" + arr[i][j] + "]";

                        if (j == arr[i].length - 1) {
                            salida += "\n";
                        } else {
                            salida += "  ";
                        }
                    }
                }
            }
            salida += "[" + numL + "]" + "  [end]" + "  [end]";
            fichero.write(salida);
        }
    }

    /**
     * Este metodo nos ayuda a separar las sentencias que esten dentro de un
     * if,while o for.
     */
    public void Separador() {
        for (String s : lista) {
            //System.out.println(lista);
            if (s.charAt(0) == 'i' && s.charAt(1) == 'f') {
                s = s.replaceAll("if\\(", "");
                s = s.substring(0, s.length() - 1);
                sentenciasIf.add(s);
            } else if (s.charAt(0) == 'e' && s.charAt(1) == 'l' && s.charAt(2) == 's' && s.charAt(3) == 'e') {
                sentenciasIf.add("jump");
            } else if (s.charAt(0) == 'w' && s.charAt(1) == 'h' && s.charAt(2) == 'i' && s.charAt(3) == 'l' && s.charAt(4) == 'e') {
                s = s.replaceAll("while\\(", "");
                s = s.substring(0, s.length() - 1);
                sentenciasWhile.add(s);
            } else if (s.charAt(0) == 'f' && s.charAt(1) == 'o' && s.charAt(2) == 'r') {
                s = s.replaceAll("for\\(", "").replaceAll("\\)", "");
                sentenciasFor.add(s);
            }
        }
    }

    /**
     * Este metodo tiene un algoritmo para asignar la linea, sentencia y jump
     * por cada linea ingresada en el codigo ignorando las llaves abiertas y
     * tomando en cuenta las cerradas.
     *
     */
    public void AlgoritmoPrincipal(){
        String temp[] = lista.toArray(String[]::new);
        for (int i = 0; i < temp.length + 1; i++) {
            if (i != 0) {
                if (temp[i - 1].charAt(temp[i - 1].length() - 1) == ';') {
                    arr[numL][0] = numL + "";
                    arr[numL][1] = temp[i - 1].replaceAll(";", "");
                    arr[numL][2] = "-";
                    numL++;

                } else if (temp[i - 1].charAt(0) == 'i' && temp[i - 1].charAt(1) == 'f') {
                    secuenciaIfWhileFor.push("if");
                    arr[numL][0] = numL + "";
                    arr[numL][1] = sentenciasIf.remove();
                    arr[numL][2] = "";
                    pila.push(numL);
                    numL++;

                } else if (temp[i - 1].charAt(0) == 'e' && temp[i - 1].charAt(1) == 'l'
                        && temp[i - 1].charAt(2) == 's' && temp[i - 1].charAt(3) == 'e') {

                    secuenciaIfWhileFor.push("else");
                    arr[numL][0] = numL + "";
                    arr[numL][1] = sentenciasIf.remove();
                    arr[numL][2] = "";
                    pila.push(numL);
                    numL++;

                } else if (temp[i - 1].charAt(0) == 'w' && temp[i - 1].charAt(1) == 'h'
                        && temp[i - 1].charAt(2) == 'i' && temp[i - 1].charAt(3) == 'l'
                        && temp[i - 1].charAt(4) == 'e') {
                    secuenciaIfWhileFor.push("while");
                    arr[numL][0] = numL + "";
                    arr[numL][1] = sentenciasWhile.remove();
                    arr[numL][2] = "";
                    pila.push(numL);
                    numL++;

                } else if (temp[i - 1].charAt(0) == 'f' && temp[i - 1].charAt(1) == 'o'
                        && temp[i - 1].charAt(2) == 'r') {
                    secuenciaIfWhileFor.push("for");
                    String arrSentFor[] = sentenciasFor.remove().split(";");
                    endFor.push(arrSentFor[2]);

                    arr[numL][0] = (numL) + "";
                    arr[numL][1] = arrSentFor[0];
                    arr[numL][2] = "-";
                    numL++;
                    arr[numL][0] = (numL) + "";
                    arr[numL][1] = arrSentFor[1];
                    arr[numL][2] = " ";
                    pila.push(numL);
                    numL++;

                } else if (temp[i - 1].equals("}")) {
                    if (!secuenciaIfWhileFor.isEmpty()) {
                        if (i < temp.length && temp[i].equals("else")) {
                            arr[pila.pop()][2] = (numL + 1) + "";
                        } else if (secuenciaIfWhileFor.peek().equals("if")) {
                            arr[pila.pop()][2] = numL + "";
                        } else if (secuenciaIfWhileFor.peek().equals("else")) {
                            arr[pila.pop()][2] = numL + "";
                        } else if (secuenciaIfWhileFor.peek().equals("while")) {
                            int auxW = pila.pop();
                            arr[numL][0] = (numL) + "";
                            arr[numL][1] = "jump";
                            arr[numL][2] = auxW + "";
                            arr[auxW][2] = (numL + 1) + "";
                            numL++;
                        } else if (secuenciaIfWhileFor.peek().equals("for")) {
                            int auxF = pila.pop();
                            arr[numL][0] = (numL) + "";
                            arr[numL][1] = endFor.pop();
                            arr[numL][2] = auxF + "";
                            arr[auxF][2] = ++numL + "";
                        }
                    }
                    secuenciaIfWhileFor.pop();
                } else if (temp[i - 1].equals("{")) {
                }else{
                    System.err.println("Error de sintaxis");
                }
            }
        }
    }

    public void conversionesInfaPos() {
        String aux = "";
        for (int i = 0; i < numL; i++) {
            if (arr[i][1] != null) {
                if ((!(arr[i][1].contains("int ") || arr[i][1].contains("double ") || arr[i][1].contains("boolean ") || arr[i][1].contains("String ")
                        || arr[i][1].contains("char ") || arr[i][1].contains("long ") || arr[i][1].contains("read") || arr[i][1].contains("print"))) && esInfaPos(arr[i][1])) {
                    aux = arr[i][1];
                    arr[i][1] = concatenarString(aux);
                }
            }
        }

    }

    private String concatenarString(String a) {
        String salida = "";
        ArrayList<String> b = Expresiones.infAPosf(a);
        for (int i = 0; i < b.size(); i++) {
            salida += b.get(i) + " ";
        }
        salida = salida.substring(0, salida.length() - 1);
        return salida;
    }

    public void compilar() throws IOException{
        Separador();
        AlgoritmoPrincipal();
        conversionesInfaPos();
        GenerarTxt();
        
    }    
    private boolean esInfaPos(String a) {
        return a.contains("+") || a.contains("-") || a.contains("*") || a.contains("/") || a.contains("%") || a.contains("<")
                || a.contains("<=") || a.contains(">") || a.contains(">=") || a.contains("==") || a.contains("!=") || a.contains("&&") || a.contains("||")
                || a.contains("!") || a.contains("=");
    }
}
