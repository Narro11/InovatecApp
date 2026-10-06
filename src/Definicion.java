/*
 * Definicion.java
 * Describe una tabla completa: nombre SQL, campos y tablas hijas.
 * El primer Campo del arreglo representa la clave primaria usada para buscar, actualizar y eliminar.
 */

/** Utilidad compartida: Definicion. */
class Definicion {
        final String tabla;
        final Campo[] campos;
        // Relaciones usadas para impedir el DELETE si existen registros dependientes.
        final String[][] hijos;
        // Constructor: registra la tabla, su arreglo de campos y las relaciones que se revisarán antes de eliminar.
        Definicion(String tabla, Campo[] campos, String[][] hijos) {
            this.tabla = tabla; this.campos = campos; this.hijos = hijos;
        }
        // Devuelve los nombres de columnas separados por comas, en el mismo orden que los controles.
        String columnas() {
            String[] nombres = new String[campos.length];
            for (int i = 0; i < campos.length; i++) nombres[i] = campos[i].columna;
            return String.join(", ", nombres);
        }
    }
