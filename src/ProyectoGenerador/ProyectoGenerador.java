package ProyectoGenerador;
import java.util.Scanner; //Importamos la libreria para leer datos por teclado
import java.util.Stack; //Importamos la libreria para hacer uso de pilas

public class ProyectoGenerador {
    
    public static void main(String[] args) {
//------------------------------------------------------------------------------Angel Eduardo
        Scanner teclado = new Scanner(System.in);
        System.out.println("INTRODUZCA UNA EXPRESIÃ“N ALGEBRAICA INFIJA: ");
        String exprInfi = CorregirExpresion(teclado.nextLine()); //Se lee y agregan separaciones para diferenciar los operandos y operadores de la expresion introducida
        String[] arrInf = exprInfi.split(" "); //Guardamos en un arreglo cada elemento que este separado por espacios de la expresion leida
        //Se declaran la pilas a usar
        Stack < String > E = new Stack < String > (); //Pila que nos servira de entrada
        Stack < String > T = new Stack < String > (); //Pila auxiliar temporal
        Stack < String > S = new Stack < String > (); //Pila para la salida a postfija      
        for (int i = arrInf.length - 1; i >= 0; i--) { //Recorremos el arreglo que contiene los datos de nuestra expresion de fin-inicio
            E.push(arrInf[i]); //Añadimos cada elemento del arreglo a la Pila de entrada (E)
        }
        //Algoritmo que pasa nuestra expresion de Infijo a Postfijo
        try { //Manejador de errores
            
            while (!E.isEmpty()) { //Mientras nuestra pila de entrada no este vacia realiza lo siguiente:
                switch (Jerarquia(E.peek())){ //Envia el dato de la cima de la pila consultada para obtener el grado de importancia si es un operador
                    case 1: // "("
                        T.push(E.pop());
                        break;
                    case 3: // "+" || "-"
                    case 4: // "*" || "/"
                        while(Jerarquia(T.peek()) >= Jerarquia(E.peek())) {
                            S.push(T.pop());
                        }
                        T.push(E.pop());
                        break;
                    case 2: // ")"
                        while(!T.peek().equals("(")) {
                            S.push(T.pop());
                        }
                        T.pop();
                        E.pop();
                        break;
                    default:
                        S.push(E.pop());
                }
            }
            String exprPost = S.toString().replaceAll("[\\]\\[,]",""); //Convertimos la pila de salida (S) en una cadena y le eliminamos los "[]" y ","
            String[] arrPost = exprPost.split(" "); //Pasamos a un arreglo cada elemento que este separado por espacios de la expresion postfija 
            E.clear(); //Vaciamos la pila de entrada para reutilizarla
            T.clear(); //Vaciamos la pila de los temporales para reutilizarla
//---------------------------------------------------------------------- -------Henry Froylan
            for (int i = arrPost.length - 1; i >= 0; i--) { //Recorremos el arreglo que contiene los datos de nuestra expresion postfija de fin-inicio
                E.push(arrPost[i]); //Añadimos cada elemento del arreglo postfijo a la Pila de entrada (E)
            }
            //Algoritmo que divide nuestra expresion y genera los temporales para el codigo intermedio
            String operadores = "+-*/";
            int cont=0; //Declaramos nuestro contador inicializado en 0 que llevara el registro de las T generadas
            while (!E.isEmpty()) { //Mientras la pila de entrada no este vacia realiza lo siguente:
                if (operadores.contains("" + E.peek())) { //Consulta el dato de la cima de la pila de entrada (E) y si se encuentra contenido en la cadena operadores, realiza lo siguiente:
                    cont++; //Incrementa el contador
                    T.push(Concatenar(E.pop(), T.pop(), T.pop()) + ""); //Extrae el operador de la pila E, extrae el ultimo y el anterior operando de la pila T y los envia a la funcion "Cocatenar"; el resultado que devuelve dicha funcion, es ingresada a la pila T
                    System.out.println("T"+cont+"= "+T.pop()); //Imprimimos la extraccion del ultimo elemento ingresado a la pila T, es decir, el ingresado en la instruccion anterior
                    T.push("T"+cont); //Ingresa el equivalente de la subexpresion a la pila T
                }else { //En caso contrario:
                    T.push(E.pop()); //Guarda los operandos de la pila E a la pila T
                }
            }
        }catch(Exception ex){
            System.out.println("ERROR -> La expresion algebraica es incorrecta");
            System.err.println(ex);
        }
    }
//------------------------------------------------------------------------------Miguel Vazquez
    //Metodo que corrige la expresion algebraica introducida para eliminar los espacios en blanco de exceso y reducirlos a uno solo
    private static String CorregirExpresion(String e) {
        e = e.replaceAll("\\s+", ""); //Reemplaza por vacio donde haya 1 o mas espacios en blanco
        e = "("+e+")"; //Agregamos parentesis externos para delimitar el inicio y fin de nuestra expresion algebraica 
        String operadores = "+-*/()";
        String ne = "";
        for (int i = 0; i < e.length(); i++) { //Recorremos el tamaño de la expresion algebraica
            if (operadores.contains(""+e.charAt(i))) { //Buscamos si hay coincidencias por cada letra de la expresion con la de la cadena operadores   
                ne+= " "+e.charAt(i)+" "; //Si es verdadero, agregamos un espacio antes y despues de cada operador dentro de la expresion
            }else
                ne += e.charAt(i); //Si es falso, solo agregamos el operando a nuestra nueva cadena
        }
        ne = ne.replaceAll("\\s+", " ").trim(); //Reducimos a un solo espacio en blanco donde hayan 2 o mas, y ademas eliminamos los espacios al inicio y final de la expresion
        return ne; //Retornamos nuestra expresion ya separada por espacios en blanco
    }
//------------------------------------------------------------------------------Jonathan Aldair
    //Devuelve el numero de orden de acuerdo a la jerarquia de los operadores en postfija
    private static int Jerarquia(String operador) {
        int orden=0; //Declaramos nuestra variable orden inicializada en 0
        if (operador.equals("(")) orden = 1; //Si el operador es "(", la variable "orden" toma el valor de 1
        if (operador.equals(")")) orden = 2; //Si el operador es ")", la variable "orden" toma el valor de 2
        if (operador.equals("+") || operador.equals("-")) orden = 3; //Si el operador es "+" o "-", la variable "orden" toma el valor de 3
        if (operador.equals("*") || operador.equals("/")) orden = 4; //Si el operador es "*" o "/", la variable "orden" toma el valor de 4
    return orden; //Retornamos el valor de la variable "orden"
    }
    //Metodo que se encarga de concatenar los valores de la expresiones de acuerdo a los operadores
    private static String Concatenar(String op, String v2, String v1) {
        if (op.equals("+")) return (v1+"+"+v2); //Si el operador es suma, retorna la concatenacion de la variable 1 y la variable 2 con el simbolo "+" al medio de los dos
        if (op.equals("-")) return (v1+"-"+v2); //Si el operador es resta, retorna la concatenacion de la variable 1 y la variable 2 con el simbolo "-" al medio de los dos
        if (op.equals("*")) return (v1+"*"+v2); //Si el operador es multiplicaciÃ³n, retorna la concatenacion de la variable 1 y la variable 2 con el simbolo "*" al medio de los dos
        if (op.equals("/")) return (v1+"/"+v2); //Si el operador es division, retorna la concatenacion de la variable 1 y la variable 2 con el simbolo "/" al medio de los dos
    return "";
    }
    
}

