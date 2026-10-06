/*
 * PanelEstado.java
 * Pestaña de Estado. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Estado: campos, límites y relaciones con otras tablas. */
public class PanelEstado extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelEstado() {
        // El nombre de tabla debe coincidir con SQL Anywhere. El arreglo define el orden visual de los campos.
        // Campo.texto recibe: columna SQL, etiqueta visible, longitud máxima y obligatorio (true/false).
        // Campo.referencia recibe: columna SQL, etiqueta, tabla de referencia y clave de esa tabla.
        super(new Definicion("Estado", new Campo[] {
            // IdEstado: máximo 5 caracteres; obligatorio.
            Campo.texto("IdEstado", "ID estado", 5, true),
            // Nombre: máximo 50 caracteres; obligatorio.
            Campo.texto("Nombre", "Nombre", 50, true)
        // Tablas hijas que se consultan antes de eliminar: nombre de tabla y columna que apunta a esta clave.
        }, new String[][] {{"Municipio", "IdEstado"}}));
    }
}
