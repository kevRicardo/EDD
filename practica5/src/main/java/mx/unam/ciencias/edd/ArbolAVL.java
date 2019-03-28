package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            // Aquí va su código.
	    super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
	    return altura;
	}

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            // Aquí va su código.
	    return elemento + " " + altura + "/" + balance(this);
	}

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            // Aquí va su código.
	    return (altura == vertice.altura && super.equals(objeto));
        }
    }

    /* Convierte el vértice a VerticeAVL */
    private VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
	return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	super.agrega(elemento);
	VerticeAVL v = verticeAVL(ultimoAgregado);
	rebalancea(verticeAVL(v.padre));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	VerticeAVL v = verticeAVL(super.busca(elemento));
	if(v == null)
	    return;
        if(v.hayIzquierdo() && v.hayDerecho())
	    v = verticeAVL(intercambiaEliminable(v));
        if(!v.hayIzquierdo() && !v.hayDerecho())
	    eliminaHoja(v);
        else
	    subirHijo(v);
	rebalancea(verticeAVL(v.padre));
    }

    private void rebalancea(VerticeAVL v){
	VerticeAVL d, i;
	if(v == null)
	    return;
	calculaAltura(v);
	if(balance(v) == -2){
	    if(balance(verticeAVL(v.derecho)) == 1){
		d = verticeAVL(v.derecho);
		super.giraDerecha(d);
		calculaAltura(d);
		calculaAltura(verticeAVL(d.padre));
	    }
	    super.giraIzquierda(v);
	    calculaAltura(v);
	}else if(balance(v) == 2){
	    if(balance(verticeAVL(v.izquierdo)) == -1){
		i = verticeAVL(v.izquierdo);
		super.giraIzquierda(i);
		calculaAltura(i);
		calculaAltura(verticeAVL(i.padre));
	    }
	    super.giraDerecha(v);
	    calculaAltura(v);
	}
	rebalancea(verticeAVL(v.padre));
    }

    private void calculaAltura(VerticeAVL v){
	if(v == null)
	    return;
	v.altura = Math.max(getAltura(v.izquierdo), getAltura(v.derecho)) + 1;
    }

    private int balance(VerticeAVL v){
	return v == null ? 0 : getAltura(verticeAVL(v.izquierdo)) - getAltura(verticeAVL(v.derecho));
    }

    private int getAltura(VerticeArbolBinario<T> v){
	return v == null ? -1 : verticeAVL(v).altura;
    }

    private void subirHijo(Vertice v){
	if(!v.hayIzquierdo())
	    eliminaSinIzquierdo(v);
        else
	    eliminaSinDerecho(v);
    }

    private boolean esHijoIzquierdo(Vertice v){
	return !v.hayPadre() ? false : v.padre.izquierdo == v;
    }

    private void eliminaSinDerecho(Vertice v){
        if(raiz == v){
            raiz = raiz.izquierdo;
            v.izquierdo.padre = null;
        }else{
            v.izquierdo.padre = v.padre;
            if(esHijoIzquierdo(v))
                v.padre.izquierdo = v.izquierdo;
            else
                v.padre.derecho = v.izquierdo;
        }
        elementos--;
    }

    private void eliminaSinIzquierdo(Vertice v){
        if(raiz == v){
            raiz = raiz.derecho;
            v.derecho.padre = null;
        }else{
            v.derecho.padre = v.padre;
            if(esHijoIzquierdo(v))
                v.padre.izquierdo = v.derecho;
            else
                v.padre.derecho = v.derecho;
        }
        elementos--;
    }

    private void eliminaHoja(Vertice v){
        if(raiz == v){
            raiz = null;
            ultimoAgregado = null;
        }else if(esHijoIzquierdo(v))
            v.padre.izquierdo = null;
        else
            v.padre.derecho = null;
        elementos--;
    }
    
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
