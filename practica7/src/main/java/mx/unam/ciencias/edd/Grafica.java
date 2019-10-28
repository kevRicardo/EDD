package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            // Aquí va su código.
	    iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
	    return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
	    return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.
	    this.elemento = elemento;
	    color = Color.NINGUNO;
	    vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
	    return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
	    return vecinos.getLongitud();
	}

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            // Aquí va su código.
	    return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
	    return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        // Aquí va su código.
	vertices = new Lista<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
	return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
	return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
	if(elemento == null || contiene(elemento))
	    throw new IllegalArgumentException();
	Vertice v = new Vertice(elemento);
	vertices.agrega(v);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Aquí va su código.
	if(!contiene(a) || !contiene(b))
	    throw new NoSuchElementException();
	if(a == b || sonVecinos(a,b))
	    throw new IllegalArgumentException();
	Vertice v = busca(a), e = busca(b);
	v.vecinos.agrega(e);
	e.vecinos.agrega(v);
	aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.
	if(!contiene(a) || !contiene(b))
	    throw new NoSuchElementException();
	Vertice v = busca(a), e = busca(b);
	if(!sonVecinos(a,b))
	    throw new IllegalArgumentException();
	v.vecinos.elimina(e);
	e.vecinos.elimina(v);
	aristas-=1;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
	return busca(elemento) != null;
    }

    private Vertice busca(T elemento){
	for(Vertice v: vertices)
	    if(v.elemento.equals(elemento))
		return v;
	return null;
    }
    
    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
	Vertice v = busca(elemento);
	if(v == null)
	    throw new NoSuchElementException();
	for(Vertice e : v.vecinos)
	    desconecta(v.elemento, e.elemento);
	vertices.elimina(v);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
	if(!contiene(a) || !contiene(b))
	    throw new NoSuchElementException();
	Vertice v = busca(a), e = busca(b);
	for(Vertice r: v.vecinos)
	    if(r == e)
		return true;
	return false;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
	if(!contiene(elemento))
	    throw new NoSuchElementException();
	return busca(elemento);
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        // Aquí va su código.
	if(!(vertice instanceof Grafica.Vertice))
	    throw new IllegalArgumentException();
	Vertice v = (Vertice)vertice;
	v.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
	// Aquí va su código.
        Vertice v = vertices.getPrimero();
        for(Vertice n : vertices)
            n.color = Color.ROJO;
        Cola<Vertice> c = new Cola<>();
        v.color = Color.ROJO;
        c.mete(v);
        while(!c.esVacia()){
            Vertice m = c.saca();
            for(Vertice x : m.vecinos)
                if(x.color == Color.ROJO){
                    x.color = Color.NEGRO;
                    c.mete(x);
                }
        }
        for(Vertice n : vertices)
            if(n.color == Color.ROJO)
                return false;
        return true;
    }
    
    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
	for(Vertice v : vertices)
	    accion.actua(v);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
	if(!contiene(elemento))
	    throw new NoSuchElementException();
	Cola<Vertice> c = new Cola<>();
	recorre(elemento, accion, c);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
	if(!contiene(elemento))
	    throw new NoSuchElementException();
	Pila<Vertice> c = new Pila<>();
	recorre(elemento, accion, c);
    }

    private void recorre(T elemento,
			 AccionVerticeGrafica<T> accion,
			 MeteSaca<Vertice> ms) {
	Vertice v = busca(elemento), e;
	for(Vertice n : vertices)
	    n.color = Color.ROJO;
	ms.mete(v);
	v.color = Color.NEGRO;
	while(!ms.esVacia()) {
	    e = ms.saca();
	    accion.actua(e);
	    for (Vertice j: e.vecinos)
		if(j.color == Color.ROJO) {
		    ms.mete(j);
		    j.color = Color.NEGRO;
		}
	}
	for(Vertice x : vertices)
	    x.color = Color.NINGUNO;
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
	return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
	vertices.limpia();
	aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
        String s = "{";
        Lista<Vertice> l = new Lista<>();
	for(Vertice v : vertices)
	    s += v.elemento.toString() + ", ";
	s += "}, {";
        for (Vertice k : vertices){
            for (Vertice j : vertices)
                if (sonVecinos(k.elemento, j.elemento) && !l.contiene(j))
                    s += "(" + k.elemento.toString() + ", " +
			j.elemento.toString()+"), ";
            l.agrega(k);
        }
        return s + "}";
    }
    
    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        // Aquí va su código.
        if ((vertices.getLongitud() != grafica.vertices.getLongitud()) || (aristas != grafica.aristas) || (equalVertice(vertices, grafica.vertices)))
            return false;
        for (Vertice v : vertices)
            for (Vertice e : vertices)
                if(v.elemento != e.elemento && sonVecinos(v.elemento, e.elemento) && !grafica.sonVecinos(v.elemento, e.elemento))
                    return false;
        return true;
    }
    
    private boolean equalVertice(Lista<Vertice> x, Lista<Vertice> y){
        for (Vertice v : x)
            if(!y.contiene(v))
                return false;
        return true;
    }
    
    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
