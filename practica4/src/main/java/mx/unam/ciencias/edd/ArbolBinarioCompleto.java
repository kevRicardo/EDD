package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            // Aquí va su código.
	    this.cola = new Cola<>();
	    if(!esVacia())
		cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
	    if(cola.esVacia())
		throw new java.util.NoSuchElementException();
	    Vertice v = cola.saca();
	    if(v.hayIzquierdo())
		cola.mete(v.izquierdo);
	    if(v.hayDerecho())
		cola.mete(v.derecho);
	    return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if(elemento == null)
	    throw new IllegalArgumentException();
	Vertice t = nuevoVertice(elemento);
	elementos++;
        if(esVacia())
            raiz = t;
        else{
            Cola<Vertice> c = new Cola<>();
            c.mete(raiz);
            while(!c.esVacia()){
                Vertice v = c.saca();
                if(v.izquierdo == null){
                    v.izquierdo = t;
                    t.padre = v;
                    return;
                }
                if(v.derecho == null){
                    v.derecho = t;
                    t.padre = v;
                    return;
                }
                c.mete(v.izquierdo);
                c.mete(v.derecho);
            }
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	Vertice v = vertice(busca(elemento));
        if(esVacia() || elemento == null || v == null)
            return;
        elementos--;
        if(elementos == 0){
            raiz = null;
            return;
        }
        Vertice u = vertice(ultimoVertice());
        if(u.padre.izquierdo == u){
            intercambia(v, u);
            u.padre.izquierdo = null;
            return;
        }
        if(u.padre.derecho == u){
            intercambia(v, u);
            u.padre.derecho = null;
            return;
        }
    }

    private Vertice ultimoVertice(){
        Cola<Vertice> c = new Cola<>();
        Vertice v = raiz;
        c.mete(raiz);
        while(!c.esVacia()){
            v = c.saca();
            if(v.izquierdo != null)
                c.mete(v.izquierdo);
            if(v.derecho != null)
                c.mete(v.derecho);
        }
        return v;
    }
    
    private void intercambia(Vertice a, Vertice b){
        T t = a.elemento;
        a.elemento = b.elemento;
        b.elemento = t;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
	return super.altura();
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
	if(esVacia())
            return;
        Cola<Vertice> c = new Cola<>();
        c.mete(raiz);
        while(!c.esVacia()){
            Vertice v = c.saca();
            accion.actua(v);
            if(v.izquierdo != null)
                c.mete(v.izquierdo);
            if(v.derecho != null)
                c.mete(v.derecho);
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
