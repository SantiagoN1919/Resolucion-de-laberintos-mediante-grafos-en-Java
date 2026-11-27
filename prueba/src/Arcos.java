public class Arcos {
    private final int origen;
    private final int destino;
    private final int costo;

    public Arcos(int origen, int destino, int costo) {
        this.origen = origen;
        this.destino = destino;
        this.costo = costo;
    }

    public int getOrigen() {
        return origen;
    }

    public int getDestino() {
        return destino;
    }

    public int getCosto() {
        return costo;
    }
}