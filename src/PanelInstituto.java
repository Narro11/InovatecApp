/*
 * PanelInstituto.java
 * Pestaña de Instituto. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Instituto: campos, límites y relaciones con otras tablas. */
public class PanelInstituto extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelInstituto() {
        // El nombre de tabla debe coincidir con SQL Anywhere. El arreglo define el orden visual de los campos.
        // Campo.texto recibe: columna SQL, etiqueta visible, longitud máxima y obligatorio (true/false).
        // Campo.referencia recibe: columna SQL, etiqueta, tabla de referencia y clave de esa tabla.
        super(new Definicion("Instituto", new Campo[] {
            // idInstituto: máximo 10 caracteres; obligatorio.
            Campo.texto("idInstituto", "ID instituto", 10, true),
            Campo.referencia("Municipio_IdMunicipio", "Municipio", "Municipio", "IdMunicipio"),
            // Nombre: máximo 30 caracteres; obligatorio.
            Campo.texto("Nombre", "Nombre", 30, true)
        // Tablas hijas que se consultan antes de eliminar: nombre de tabla y columna que apunta a esta clave.
        }, new String[][] {{"Campus", "idInstituto"}, {"Carreras", "idInstituto"},
            {"Empleado", "idInstituto"}, {"Edicion", "Instituto_idInstituto"},
            {"Alumno", "idInstituto"}}));
    }
}
