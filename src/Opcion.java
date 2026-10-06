/*
 * Opcion.java
 * Elemento de un combo. Conserva el ID real, el nombre visible y el ID del padre para filtrar.
 */

/** Utilidad compartida: Opcion. */
class Opcion {
        // ID almacenado en BD, nombre visible y clave del padre para los combos dependientes.
        final String id, nombre, padre;
        // Constructor: conserva el ID de la opción, su descripción y su relación con el padre.
        Opcion(String id, String nombre, String padre) {
            this.id = id; this.nombre = nombre; this.padre = padre;
        }
        // Determina el texto visible en el combo. Un ID vacío representa la opción Seleccione.
        public String toString() {
            return id.isEmpty() ? "Seleccione..." : id + " - " + nombre;
        }
    }
