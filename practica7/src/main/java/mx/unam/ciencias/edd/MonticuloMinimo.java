package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return (indice < arbol.length) && (arbol[indice] != null);
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
	    if(indice >= arbol.length)
		throw new NoSuchElementException();
	    return arbol[indice++];
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            // Aquí va su código.
	    elemento = elemento;
	    indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            // Aquí va su código.
	    return indice;
	}

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            // Aquí va su código.
	    indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            // Aquí va su código.
	    return elemento.compareTo(adaptador.elemento);
	}
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100); /* 100 es arbitrario. */
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        // Aquí va su código.
	arbol = nuevoArreglo(n);
        elementos = n;
        int i = 0;
        for(T e : iterable){
            arbol[i] = e;
            e.setIndice(i++);
        }
        int j = (elementos/2)-1;
        while(j >= -1){
            acomodaAbajo(j);
	    j--;
        }
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if(elemento == null)
            throw new IllegalArgumentException();
        if(elementos == arbol.length){
            T[] n = nuevoArreglo(elementos*2);
            for(int i = 0; i < elementos; i++){
                n[i] = arbol[i];
                n[i].setIndice(i);
            }
            arbol = n;
        }
        arbol[elementos] = elemento;
        arbol[elementos].setIndice(elementos);
        acomodaArriba(elementos);
        elementos++;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        // Aquí va su código.
        if(esVacia())
            throw new IllegalStateException();
        intercambia(0, elementos-1);
        elementos--;
        arbol[elementos].setIndice(-1);
        acomodaAbajo(0);
        return arbol[elementos];
    }

    private void intercambia(int a, int b){
        T n = arbol[a];
        arbol[a] = arbol[b];
        arbol[a].setIndice(a);
        arbol[b] = n;
        arbol[b].setIndice(b);
    }
    
    private void acomodaAbajo(int i){
        int izq = i*2+1;
        int der = i*2+2;
        if(i < 0 || i >= elementos-1)
            return;
	if(izq >= elementos)
            return;
	if(izq == elementos-1){
            if(arbol[i].compareTo(arbol[izq]) >= 0)
                intercambia(i, izq);
            return;
        }
        int m = menor(izq, der);
        if(arbol[i].compareTo(arbol[m]) >= 0){
            intercambia(i, m);
            acomodaAbajo(m);
        }
    }

    private void acomodaArriba(int i){
        if(i == 0)
            return;
        int n = (i-1)/2;
        if(arbol[i].compareTo(arbol[n]) < 0){
            intercambia(n, i);
            acomodaArriba(n);
        }
    }
    
    private int menor(int i, int d){
        return (arbol[i].compareTo(arbol[d]) <= 0) ? i : d;
    }
    
    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	int i = elemento.getIndice();
        if (i < 0 || i >= elementos)
            return;
        intercambia(i, elementos-1);
        elementos--;
        reordena(arbol[i]);
        arbol[elementos].setIndice(-1);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        if(elemento.getIndice() >= elementos || elemento.getIndice() < 0)
            return false;
        return elemento == arbol[elemento.getIndice()];
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
	return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
	for(int i = 0; i < elementos; i++)
            arbol[i] = null;
        elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        // Aquí va su código.
	int e = elemento.getIndice();
        int p = (e-1)/2;
        if(arbol[e].compareTo(arbol[p]) < 0)
            acomodaArriba(e);
        else
            acomodaAbajo(e);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        // Aquí va su código.
	return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        // Aquí va su código.
	if(i < 0 || i >= getElementos())
            throw new NoSuchElementException();
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        // Aquí va su código.
	String s = "";
        for (int i = 0; i < elementos; i++)
            s += String.format("%s, ", get(i).toString());
        return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        // Aquí va su código.
	if(elementos != monticulo.elementos)
            return false;
        for(int i = 0; i < elementos; i++)
            if(!(arbol[i].compareTo(monticulo.arbol[i]) == 0))
                return false;
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        // Aquí va su código.
	Lista<Adaptador<T>> l1 = new Lista<>();
        for(T e : coleccion){
            Adaptador<T> i = new Adaptador<T>(e);
            l1.agrega(i);
        }
        Lista<T> l2 = new Lista<>();
        MonticuloMinimo<Adaptador<T>> m = new MonticuloMinimo<Adaptador<T>>(l1);
        while(!m.esVacia()){
            T e = m.elimina().elemento;
            l2.agrega(e);
        }
        return l2;
    }
}
