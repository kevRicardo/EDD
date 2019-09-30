package mx.unam.ciencias.edd.proyecto1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.text.Collator;

import mx.unam.ciencias.edd.Lista;

/**
 * <p>Proyecto 1: Manejador de archivos.</p>
 *
 * <p>Un ordenador lexicográfico,
 * en una versión más sencilla del programa <code>sort</code> en Unix.</p>
 *
 * <p>Funciona con uno o más archivos de texto o la entrada estándar 
 * y que imprima su salida en la salida estándar.</p>
 *
 * @author Kevin Ricardo Villegas Salvador
 */
public class Archivo{

    /**
     * <p>Clase para poder manejar las lineas del parrafo, 
     * así como realizar las comparaciones entre ellas.</p>
     */
    private class Linea implements Comparable<Linea>{

	String linea;
	
	/**
	 * Constructor de la clase.
	 *
	 * @param linea La linea con que trabajar. 
	 */
	public Linea(String linea){
	    this.linea = linea;
	}
	
	@Override
	public String toString(){
	    return linea;
	}
	
	@Override
	public int compareTo(Linea l){
	    Collator c = Collator.getInstance();
	    c.setStrength(Collator.PRIMARY);
	    return c.compare(linea.replaceAll("\\P{L}+", ""),
			     l.toString().replaceAll("\\P{L}+", ""));
	}
    }
    
    /*Las lineas del parrafo*/
    private static Lista<String> archivo = new Lista<>();
    /*Nombre de los archivo salida*/
    private static String archivoSalida = "";;
    /*Bandera para la reversa del archivo*/
    private static boolean reversa = false;
    /*Bandera para guardar el archivo*/
    private static boolean guarda = false;

    /**
     * <p>Constructor de la clase que recibe un arreglo,
     * los argumentos pasados por consola.</p>
     *
     * @param args Los argumentos pasados por consola.    
     */
    public Archivo(String[] args){
	if(args.length == 0)
	    return;
	argumentos(args);
	for(String s: archivo){
	    Lista<Linea> l = ordena(lectura(s), reversa);
	    if(guarda)
		escritura(l);
	    else
		for(Linea k: l)
		    System.out.println(k.toString());		
	}
    }

    /**
     * <p>Realiza la lectura del archivo.
     * Guarda en una lista, cada renglón del archivo.</p>
     *
     * @param archivo El archivo a leer.
     * @return La lista con las líneas del archivo.
     */
    public Lista<Linea> lectura(String archivo){
	Lista<Linea> l = new Lista<>();
	String linea;
	try{
	    BufferedReader b = new BufferedReader(new FileReader(archivo));
	    while((linea = b.readLine()) != null)
		l.agrega(new Linea(linea));
	    b.close();
	}catch(FileNotFoundException f){
	    System.out.println("Archivo no encontrado."
			       +"\nVerifica la ruta del archivo.");
	    System.exit(-1);
	}catch(IOException e){
	    System.out.println("Error en la entrada de datos.");
	    System.exit(-1);
	}
	return l;
    }

    /**
     * <p>Realiza la escritura en un archivo <code>.txt</code>.
     * Éste método se ejecuta sólo en caso de que el usuario lo deseé.</p>
     *
     * @param lista La lista con el archivo ya procesado.
     */
    public void escritura(Lista<Linea> lista){
	try{
	    File f = new File("./salida");
	    f.mkdirs();
	    FileWriter fw = new FileWriter(f.getPath()+"/"+archivoSalida+".txt");
	    BufferedWriter bw = new BufferedWriter(fw);
	    PrintWriter pw = new PrintWriter(bw);
	    for(Linea l : ordena(lista, reversa))
		pw.append(l.toString()+"\n");
	    pw.close();
	    bw.close();
	}catch(IOException e){
	    System.out.println("Error en la salida de datos.");
	}
    }

    /**
     * <p>Ordena la lista.
     * Regresa la reversa de la lista si así lo quiere el usuario.</p>
     * 
     * @param l La lista a ordenar.
     * @param reversa El booleano de si regresaremos la reversa o no de la lista.
     * @return La lista ya procesada.    
     */
    public Lista<Linea> ordena(Lista<Linea> l, boolean reversa){
	return reversa ? Lista.mergeSort(l).reversa() : Lista.mergeSort(l);
    }

    /*Procesa los argumentos*/
    private static void argumentos(String[] args){
	for(int i = 0; i < args.length; i++)
	    if(args[i].equals("-r"))
		reversa = true;
	    else
		if(args[i].equals("-o")){
		    guarda = true;
		    if(i+1 < args.length){
			archivoSalida = args[i+1];
			i += 1;
		    }
		    else{
			System.out.println("No hay archivo de salida, se le dará un nombre por defecto.");
			archivoSalida = "ArchivoSalida";
		    }
		}else
		    archivo.agrega(args[i]);
    }
}
