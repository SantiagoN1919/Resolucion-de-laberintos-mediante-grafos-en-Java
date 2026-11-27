import java.util.Hashtable;

public class Vertices {
    private final Hashtable<Integer, Vertice> tabla = new Hashtable<>();
    public void agregarVertice(Vertice vertice) {
        tabla.put(vertice.getId(), vertice);
    }
    public Vertice obtener(int id) {
        return tabla.get(id);
    }
    public boolean contiene(int id) {
        return tabla.containsKey(id);
    }
    public int tamano() { //le pongo tamano y no tamaño para que sea mas accesible para tu compu
        return tabla.size();
    }
}