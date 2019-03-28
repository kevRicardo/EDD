package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
	    super(elemento);
	    color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            // Aquí va su código.
	    return (color == Color.ROJO ? "R" : "N") + "{" + elemento.toString() + "}";
	}

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.
	    return (color == vertice.color && super.equals(objeto));
	}
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
	VerticeRojinegro r = verticeRojinegro(vertice);
	return r.color;
    }

    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> v){
	VerticeRojinegro n = (VerticeRojinegro)v;
	return n;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	super.agrega(elemento);
	VerticeRojinegro v = verticeRojinegro(getUltimoVerticeAgregado());
	v.color = Color.ROJO;
	rebalancea(v);
    }

    private void rebalancea(VerticeRojinegro v){
	VerticeRojinegro p, t, a, u;
	if(!v.hayPadre()){
            v.color = Color.NEGRO;
            return;
	}
        p = verticeRojinegro(v.padre);
	if(!esRojo(p)) return;
        a = verticeRojinegro(p.padre);
        if(esHijoIzquierdo(p))
            t = verticeRojinegro(a.derecho);
        else
            t = verticeRojinegro(a.izquierdo);
        if(t != null && esRojo(t)){
            t.color = Color.NEGRO;
            p.color = Color.NEGRO;
            a.color = Color.ROJO;
            rebalancea(a);
            return;
        }
        if(esHijoIzquierdo(v) != esHijoIzquierdo(p)){
            if(esHijoIzquierdo(p))
                super.giraIzquierda(p);
            else
                super.giraDerecha(p);
            u = p;
            p = v;
            v = u;
        }
        p.color = Color.NEGRO;
        a.color = Color.ROJO;
        if(esHijoIzquierdo(v))
            super.giraDerecha(a);
        else
            super.giraIzquierda(a);
    }

    private boolean esHijoIzquierdo(Vertice v){
	return !v.hayPadre() ? false : v.padre.izquierdo == v;
    }
    
    private boolean esHijoDerecho(Vertice v){
	return !v.hayPadre() ? false : v.padre.derecho == v;
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	VerticeRojinegro u, h;
        VerticeRojinegro v = verticeRojinegro(super.busca(elemento));
        if(v == null) return;
        elementos--;
        if(v.hayIzquierdo() && v.hayDerecho())
            v = verticeRojinegro(intercambiaEliminable(v));
        if(!v.hayIzquierdo() && !v.hayDerecho()){
            u = verticeRojinegro(nuevoVertice(null));
            u.color = Color.NEGRO;
            u.padre = v;
            v.izquierdo = u;
        }
        h = v.hayIzquierdo() ? verticeRojinegro(v.izquierdo) : verticeRojinegro(v.derecho);
        eliminaVertice(v);
        if (esRojo(h)){
            h.color = Color.NEGRO;
            return;
        }
        if (esNegro(h) && esNegro(v))
            rebalanceaElimina(h);
        eliminaVertice(h);
    }

    private boolean esRojo(VerticeRojinegro v){
	return v != null && v.color == Color.ROJO;
    }

    private boolean esNegro(VerticeRojinegro v){
	return v == null || v.color == Color.NEGRO;
    }

    private VerticeRojinegro hermano(VerticeRojinegro v){
	return esHijoIzquierdo(v) ? verticeRojinegro(v.padre.derecho) : verticeRojinegro(v.padre.izquierdo);
    }

    private void rebalanceaElimina(VerticeRojinegro v){
	VerticeRojinegro h, p, sIzq, sDer;
        if(!v.hayPadre()) return;
        p = verticeRojinegro(v.padre);
        h = hermano(v);
        if(esRojo(h)){
            p.color = Color.ROJO;
	    h.color = Color.NEGRO;
            if(esHijoIzquierdo(v)){
                super.giraIzquierda(p);
                h = verticeRojinegro(v.padre.derecho);
            }else{
                super.giraDerecha(p);
                h = verticeRojinegro(v.padre.izquierdo);
            }
        }
        sIzq = verticeRojinegro(h.izquierdo);
        sDer = verticeRojinegro(h.derecho);
	if(esNegro(p) && esNegro(h) && esNegro(sIzq) && esNegro(sDer)){
            h.color = Color.ROJO;
            rebalanceaElimina(p);
	    return;
        }
        if(esNegro(h) && esNegro(sIzq) && esNegro(sDer) && esRojo(p)){
            h.color = Color.ROJO;
            p.color = Color.NEGRO;
            return;
        }
        if((esHijoIzquierdo(v) && esRojo(sIzq) && esNegro(sDer)) ||
	   (esHijoDerecho(v) && esNegro(sIzq) && esRojo(sDer))){
            h.color = Color.ROJO;
            if(esRojo(sIzq))
                sIzq.color = Color.NEGRO;
            else
                sDer.color = Color.NEGRO;
            if(esHijoIzquierdo(v)){
                super.giraDerecha(h);
                h = verticeRojinegro(v.padre.derecho);
	    }else{
                super.giraIzquierda(h);
                h = verticeRojinegro(v.padre.izquierdo);
            }
        }
        sIzq = verticeRojinegro(h.izquierdo);
        sDer = verticeRojinegro(h.derecho);
        h.color = p.color;
        p.color = Color.NEGRO;
        if(esHijoIzquierdo(v)){
            sDer.color = Color.NEGRO;
            super.giraIzquierda(p);
        }else{
            sIzq.color = Color.NEGRO;
            super.giraDerecha(p);
        }
    }
    
    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
