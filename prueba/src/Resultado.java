import java.util.List;

public class Resultado {
    private final String nombre;
    private final List<Vertice> visitados;
    private final List<Vertice> camino;
    private final boolean encontrado;
    public Resultado(String nombre, List<Vertice> visitados, List<Vertice> camino, boolean encontrado) {
        this.nombre = nombre;
        this.visitados = visitados;
        this.camino = camino;
        this.encontrado = encontrado;
    }
    public String getNombre() {
        return nombre;
    }
    public List<Vertice> getVisitados() {
        return visitados;
    }
    public List<Vertice> getCamino() {
        return camino;
    }
    public boolean isEncontrado() {
        return encontrado;
    }

    public int getLongitud() {
        return camino.size() - 1; //solo tiene sentido si hay solucion
    }
}