import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Laberinto {
    private final char[][] celdas; // '#' ya convertido a ' ', E y S convertidos a ' '
    private final int filas;
    private final int columnas;
    private final int filaEntrada;
    private final int colEntrada;
    private final int filaSalida;
    private final int colSalida;

    private Laberinto(char[][] celdas, int fE, int cE, int fS, int cS) {
        this.celdas = celdas;
        this.filas = celdas.length;
        this.columnas = celdas[0].length;
        this.filaEntrada = fE;
        this.colEntrada = cE;
        this.filaSalida = fS;
        this.colSalida = cS;
    }

    public int getFilas() {
        return filas;
    }
    public int getColumnas() {
        return columnas;
    }
    public char getCelda(int f, int c) { //f de filas y c de columnas
        return celdas[f][c];
    }
    public boolean estransitable(int f, int c) {
        return celdas[f][c] == ' ';
    }
    public int getFilaEntrada() {
        return filaEntrada;
    }
    public int getColEntrada() {
        return colEntrada;
    }
    public int getFilaSalida() {
        return filaSalida;
    }
    public int getColSalida() {
        return colSalida;
    }

     public static Laberinto cargar(String ruta) throws LaberintoException {
        Path path;
        try {
            path = Paths.get(ruta.trim().replace("\"", ""));
        } catch (InvalidPathException e) {
            throw new LaberintoException("La ruta no es valida: " + ruta);
        }
        if (!Files.exists(path)) {
            throw new LaberintoException("El archivo no existe: " + path);
        }
        if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
            throw new LaberintoException("No se puede leer el archivo: " + path);
        }

        List<String> lineas;
        try {
            lineas = Files.readAllLines(path, StandardCharsets.UTF_8); // NO se hace trim
        } catch (IOException e) {
            throw new LaberintoException("Error al leer el archivo: " + e.getMessage());
        }

        if (lineas.isEmpty()) {
            throw new LaberintoException("El archivo esta vacio.");
        }
        int ancho = lineas.get(0).length();
        if (ancho == 0) {
            throw new LaberintoException("El renglon 1 tiene longitud cero.");
        }

        int filas = lineas.size();
        char[][] celdas = new char[filas][ancho];
        int nE = 0, nS = 0;
        int fE = -1, cE = -1, fS = -1, cS = -1;

        for (int f = 0; f < filas; f++) {
            String linea = lineas.get(f);
            if (linea.length() != ancho) {
                throw new LaberintoException("El renglon " + (f + 1) + " mide " + linea.length()
                        + " columnas y se esperaban " + ancho + ".");
            }
            for (int c = 0; c < ancho; c++) {
                char ch = linea.charAt(c);
                switch (ch) {
                    case '+': case '-': case '|': case ' ':
                        celdas[f][c] = ch;
                        break;
                    case '#':
                        celdas[f][c] = ' ';
                        break;
                    case 'E':
                        nE++; fE = f; cE = c;
                        celdas[f][c] = ' ';
                        break;
                    case 'S':
                        nS++; fS = f; cS = c;
                        celdas[f][c] = ' ';
                        break;
                    default:
                        throw new LaberintoException("Caracter invalido '" + ch + "' en renglon "
                                + (f + 1) + ", columna " + (c + 1) + ".");
                }
            }
        }

        if (nE > 0 || nS > 0) {
            if (nE != 1 || nS != 1) {
                throw new LaberintoException("Con marcas explicitas debe haber exactamente una E y una S"
                        + " (hay " + nE + " E y " + nS + " S).");
            }
        } else {
            List<int[]> borde = new ArrayList<>();
            for (int f = 0; f < filas; f++) {
                for (int c = 0; c < ancho; c++) {
                    boolean enBorde = f == 0 || f == filas - 1 || c == 0 || c == ancho - 1;
                    if (enBorde && celdas[f][c] == ' ') {
                        borde.add(new int[]{f, c});
                    }
                }
            }
            if (borde.size() != 2) {
                throw new LaberintoException("Sin marcas E/S debe haber exactamente 2 posiciones transitables"
                        + " en el borde (se encontraron " + borde.size() + ").");
            }
            fE = borde.get(0)[0]; cE = borde.get(0)[1];
            fS = borde.get(1)[0]; cS = borde.get(1)[1];
        }

        return new Laberinto(celdas, fE, cE, fS, cS);
    }
}