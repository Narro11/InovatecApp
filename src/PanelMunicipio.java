/*
 * PanelMunicipio.java
 * Pestaña de Municipio. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Municipio: campos, límites y relaciones con otras tablas. */
public class PanelMunicipio extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelMunicipio() {
        // El nombre de tabla debe coincidir con SQL Anywhere. El arreglo define el orden visual de los campos.
        // Campo.texto recibe: columna SQL, etiqueta visible, longitud máxima y obligatorio (true/false).
        // Campo.referencia recibe: columna SQL, etiqueta, tabla de referencia y clave de esa tabla.
        super(new Definicion("Municipio", new Campo[] {
            // IdMunicipio: máximo 10 caracteres; obligatorio.
            Campo.texto("IdMunicipio", "ID municipio", 10, true),
            Campo.referencia("IdEstado", "Estado", "Estado", "IdEstado"),
            // Nombre: máximo 30 caracteres; obligatorio.
            Campo.texto("Nombre", "Nombre", 30, true)
        // Tablas hijas que se consultan antes de eliminar: nombre de tabla y columna que apunta a esta clave.
        }, new String[][] {{"Instituto", "Municipio_IdMunicipio"},
                           {"Patrocinador", "Municipio_IdMunicipio"}}));
    }
}
