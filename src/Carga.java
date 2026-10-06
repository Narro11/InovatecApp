/*
 * Carga.java
 * Contenedor temporal del resultado de una consulta: filas y opciones de los combos.
 * Permite recuperar datos en segundo plano y después mostrarlos en el hilo de Swing.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.util.ArrayList;
import java.util.List;

/** Utilidad compartida: Carga. */
class Carga {
        // Una lista de opciones por cada campo; los campos de texto tienen una lista vacía.
        final List<List<Opcion>> opciones = new ArrayList<>();
        // Opciones del combo de estados utilizado como filtro de municipios.
        final List<Opcion> estados = new ArrayList<>();
        // Cada arreglo representa un registro recuperado de la tabla.
        final List<String[]> filas = new ArrayList<>();
    }
