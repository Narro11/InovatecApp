/*
 * PanelRegistroAsesor.java
 * Pestaña de RegistroAsesor. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de RegistroAsesor: campos, límites y relaciones con otras tablas. */
public class PanelRegistroAsesor extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelRegistroAsesor() {
        // El nombre de tabla debe coincidir exactamente con SQL Anywhere.
        super(new Definicion("RegistroAsesor", new Campo[] {
            // Llave foránea que apunta al empleado (asesor)
            Campo.referencia("NumEmpleado", "Núm. Empleado Asesor", "Empleado", "NumEmpleado"),
            
            // Llave foránea que apunta al equipo participante
            Campo.referencia("idEquipo", "Equipo", "Equipo", "idEquipo")
            
        // No tiene tablas hijas que dependan directamente de esta relación.
        }, new String[0][0]));
    }
}
