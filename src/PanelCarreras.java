/*
 * PanelCarreras.java
 * Pestaña de Carreras. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Carreras: campos, límites y relaciones con otras tablas. */
public class PanelCarreras extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelCarreras() {
        // El nombre de tabla debe coincidir con SQL Anywhere. El arreglo define el orden visual de los campos.
        // Campo.texto recibe: columna SQL, etiqueta visible, longitud máxima y obligatorio (true/false).
        // Campo.referencia recibe: columna SQL, etiqueta, tabla de referencia y clave de esa tabla.
        super(new Definicion("Carreras", new Campo[] {
            // idCarreras: máximo 10 caracteres; obligatorio.
            Campo.texto("idCarreras", "ID carrera", 10, true),
            Campo.referencia("idInstituto", "Instituto", "Instituto", "idInstituto"),
            // Nombre: máximo 50 caracteres; obligatorio.
            Campo.texto("Nombre", "Nombre", 50, true)
        // Tablas hijas que se consultan antes de eliminar: nombre de tabla y columna que apunta a esta clave.
        }, new String[][] {{"Alumno", "idCarreras"}}));
    }
}
