/*
 * PanelRegistro.java
 * Pestaña de Registro. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Registro: campos, límites y relaciones con otras tablas. */
public class PanelRegistro extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelRegistro() {
        // El nombre de tabla debe coincidir con SQL Anywhere.
        super(new Definicion("Registro", new Campo[] {
            // idRegistro: máximo 20 caracteres; obligatorio.
            Campo.texto("idRegistro", "ID registro", 20, true),
            
            // Llave foránea a la tabla Edicion
            Campo.referencia("Edicion_idEdicion", "Edición", "Edicion", "idEdicion"),
            
            // Llave foránea a la tabla Equipo
            Campo.referencia("idEquipo", "Equipo", "Equipo", "idEquipo"),
            
            // Llave foránea a la tabla Alumno (Número de control)
            Campo.referencia("N_Control", "Núm. Control Alumno", "Alumno", "N_Control"),
            
            // Rol del alumno en el equipo: máximo 30 caracteres (ej. Líder, Desarrollador, Expositor).
            Campo.texto("Rol", "Rol en el equipo", 30, false)
            
        // No tiene tablas hijas que dependan directamente de esta.
        }, new String[0][0]));
    }
}
