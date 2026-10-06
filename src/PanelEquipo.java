/*
 * PanelEquipo.java
 * Pestaña de Equipo. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Equipo: campos, límites y relaciones con otras tablas. */
public class PanelEquipo extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelEquipo() {
        // El nombre de tabla debe coincidir con SQL Anywhere.
        super(new Definicion("Equipo", new Campo[] {
            // idEquipo: numérico de hasta 10 dígitos; obligatorio.
            idEquipoCampo(),
            
            // Relación con la tabla Proyecto
            Campo.referencia("idProyecto", "Proyecto", "Proyecto", "idProyecto"),
            
            // Nombre del equipo: máximo 50 caracteres; obligatorio.
            Campo.texto("Nombre", "Nombre del equipo", 50, true)
            
        // Tablas hijas que se consultan antes de eliminar para proteger la integridad referencial.
        }, new String[][] {{"Registro", "idEquipo"}, {"RegistroAsesor", "idEquipo"}}));
    }

    // Configura el campo idEquipo como numérico
    private static Campo idEquipoCampo() {
        Campo campo = Campo.texto("idEquipo", "ID equipo", 10, true);
        campo.numerico = true;
        return campo;
    }
}
