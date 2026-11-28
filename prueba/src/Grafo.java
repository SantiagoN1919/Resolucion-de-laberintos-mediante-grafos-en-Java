import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Grafo {
    private static final int[] DF = {-1, 0, 1, 0};
    private static final int[] DC = {0, 1, 0, -1};

    private final Laberinto laberinto;
    private final Vertices vertices = new Vertices();
    private final Hashtable<Integer, LinkedList<Arcos>> adyacencia = new Hashtable<>();
    private final int entrada;
    private final int salida;

    private Resultado resultadoBFS;

    public Grafo(Laberinto lab) {
        this.laberinto = lab;
        int filas = lab.getFilas();
        int cols = lab.getColumnas();

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                if (lab.estransitable(f, c)) {
                    int id = f * cols + c;
                    vertices.agregarVertice(new Vertice(id, f, c));
                    adyacencia.put(id, new LinkedList<>());
                }
            }
        }

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                if (!lab.estransitable(f, c)) continue;
                int id = f * cols + c;
                for (int d = 0; d < 4; d++) {
                    int nf = f + DF[d];
                    int nc = c + DC[d];
                    if (nf >= 0 && nf < filas && nc >= 0 && nc < cols && lab.estransitable(nf, nc)) {
                        adyacencia.get(id).add(new Arcos(id, nf * cols + nc, 1));
                    }
                }
            }
        }

        this.entrada = lab.getFilaEntrada() * cols + lab.getColEntrada();
        this.salida = lab.getFilaSalida() * cols + lab.getColSalida();
    }

    public Laberinto getLaberinto() {
        return laberinto; 
    }
    public Vertices getVertices() { 
        return vertices; 
    }
    public int getEntrada() { 
        return entrada; 
    }
    public int getSalida() { 
        return salida; 
    }
    public Resultado getResultadoBFS() { 
        return resultadoBFS; 
    }

    /** BFS con cola. Devuelve los vertices en el orden en que se retiran de la cola. */
    public List<Vertice> BFS() {
        Hashtable<Integer, Integer> predecesor = new Hashtable<>();
        Set<Integer> descubiertos = new HashSet<>();
        Queue<Integer> cola = new LinkedList<>();
        List<Vertice> visitados = new ArrayList<>();
        boolean encontrado = false;

        cola.add(entrada);
        descubiertos.add(entrada);

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            visitados.add(vertices.obtener(actual));

            if (actual == salida) {
                encontrado = true;
                break;
            }
            for (Arcos a : adyacencia.get(actual)) {
                int vecino = a.getDestino();
                if (descubiertos.add(vecino)) {
                    predecesor.put(vecino, actual);
                    cola.add(vecino);
                }
            }
        }

        List<Vertice> camino = encontrado ? reconstruirCamino(predecesor) : new ArrayList<>();
        resultadoBFS = new Resultado("BFS", visitados, camino, encontrado);
        return visitados;
    }

    private List<Vertice> reconstruirCamino(Hashtable<Integer, Integer> predecesor) {
        List<Vertice> camino = new ArrayList<>();
        Integer actual = salida;
        while (actual != null) {
            camino.add(vertices.obtener(actual));
            actual = (actual == entrada) ? null : predecesor.get(actual);
        }
        Collections.reverse(camino);
        return camino;
    }
}