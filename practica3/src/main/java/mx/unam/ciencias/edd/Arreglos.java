package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
	quickSort(arreglo, 0, arreglo.length-1, comparador);
    }

    private static <T> void quickSort(T[] a, int ini, int fin, Comparator<T> c){
	if(fin <= ini)
	    return;
	int i = ini+1, j= fin;
	while(i < j){
	    if(c.compare(a[i], a[ini]) > 0 && c.compare(a[j], a[ini]) <= 0)
		intercambia(a, i++, j--);
	    else if(c.compare(a[i], a[ini]) <= 0)
		i++;
	    else
		j--;
	}
	if(c.compare(a[i], a[ini]) > 0)
	    i--;
	intercambia(a, ini, i);
	quickSort(a, ini, i-1, c);
	quickSort(a, i+1, fin, c);
    }

    private static <T> void intercambia(T[] a, int x, int y){
	if(x == y)
	    return;
	T t = a[y];
	a[y] = a[x];
	a[x] = t;
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        // Aquí va su código.
	for(int i = 0; i < arreglo.length; i++){
	    int min = i;
	    for(int j = i+1; j < arreglo.length; j++)
		if(comparador.compare(arreglo[j], arreglo[min]) < 0)
		    min = j;
	    intercambia(arreglo, i, min);
	}
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        // Aquí va su código.
	return busqueda(arreglo, elemento, 0, arreglo.length-1, comparador);
    }

    private static <T> int busqueda(T[] a, T e, int i, int f, Comparator<T> c){
	if(i > f)
	    return -1;
	int m = (i+f)/2;
	if(c.compare(a[m], e) == 0)
	    return m;
	if(c.compare(a[m], e) > 0)
	    return busqueda(a, e, i, m-1, c);
	return busqueda(a, e, m+1, f, c);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
