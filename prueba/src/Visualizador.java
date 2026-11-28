import java.util.List;
import java.util.Scanner;

public class Visualizador {
    private final Laberinto lab;
    private final Scanner sc;
    private final long pausaMs; // <= 0 : avanzar con Enter

    public Visualizador(Laberinto lab, Scanner sc, long pausaMs) {
        this.lab = lab;
        this.sc = sc;
        this.pausaMs = pausaMs;
    }

    public void despliegaLaberinto(List<Vertice> ordenVisita, Resultado res) {
        boolean saltarAlFinal = false;

        for (int i = 0; i < ordenVisita.size(); i++) {
            dibujar(res.getNombre(), ordenVisita, i, null,
                    "Paso " + (i + 1) + "/" + ordenVisita.size());
            if (saltarAlFinal) continue;

            if (pausaMs > 0) {
                try {
                    Thread.sleep(pausaMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } else {
                System.out.print("Enter = siguiente, f = saltar al final: ");
                String s = sc.nextLine().trim();
                if (s.equalsIgnoreCase("f")) saltarAlFinal = true;
            }
        }

        String mensaje;
        if (res.isEncontrado()) {
            mensaje = "Solucion encontrada. Vertices visitados: " + ordenVisita.size()
                    + " ... Longitud del camino: " + res.getLongitud() + " movimientos.";
        } else {
            mensaje = "NO existe camino entre la entrada y la salida. Vertices visitados: "+ ordenVisita.size();
        }
        dibujar(res.getNombre(), ordenVisita, ordenVisita.size() - 1,
                res.isEncontrado() ? res.getCamino() : null, mensaje);
    }

    private void dibujar(String titulo, List<Vertice> orden, int hasta, List<Vertice> camino, String mensaje) {
        int filas = lab.getFilas();
        int cols = lab.getColumnas();
        char[][] m = new char[filas][cols];
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                m[f][c] = lab.getCelda(f, c);
            }
        }

        for (int i = 0; i <= hasta && i < orden.size(); i++) {
            Vertice v = orden.get(i);
            m[v.getFila()][v.getColumna()] = '.';
        }
        if (camino != null) {
            for (Vertice v : camino) {
                m[v.getFila()][v.getColumna()] = '*';
            }
        }
        m[lab.getFilaEntrada()][lab.getColEntrada()] = 'E';
        m[lab.getFilaSalida()][lab.getColSalida()] = 'S';

        boolean enAnimacion = camino == null && mensaje.startsWith("Paso");
        if (enAnimacion && hasta >= 0) {
            Vertice actual = orden.get(hasta);
            m[actual.getFila()][actual.getColumna()] = '@';
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("=== " + titulo + " ===");
        StringBuilder sb = new StringBuilder();
        for (int f = 0; f < filas; f++) {
            sb.append(m[f]).append('\n');
        }
        System.out.print(sb);
        System.out.println("Leyenda: + - | pared | E entrada | S salida | . visitado | @ actual | * camino final");
        System.out.println(mensaje);
    }
}