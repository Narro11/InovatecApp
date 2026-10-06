/*
 * PanelCampus.java
 * Pestaña de Campus. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Campus: campos, límites y relaciones con otras tablas. */
public class PanelCampus extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelCampus() {
        // El nombre de tabla debe coincidir con SQL Anywhere. El arreglo define el orden visual de los campos.
        // Campo.texto recibe: columna SQL, etiqueta visible, longitud máxima y obligatorio (true/false).
        // Campo.referencia recibe: columna SQL, etiqueta, tabla de referencia y clave de esa tabla.
        super(new Definicion("Campus", new Campo[] {
            // idCampus: máximo 10 caracteres; obligatorio.
            Campo.texto("idCampus", "ID campus", 10, true),
            Campo.referencia("idInstituto", "Instituto", "Instituto", "idInstituto"),
            // Nombre: máximo 50 caracteres; obligatorio.
            Campo.texto("Nombre", "Nombre", 50, true)
        // No se han configurado tablas hijas para revisar desde este panel.
        }, new String[0][0]));
    }
}
