/*
 * PanelProyecto.java
 * Pestaña de Proyecto. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Proyecto: campos, límites y relaciones con otras tablas. */
public class PanelProyecto extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelProyecto() {
        // El nombre de tabla debe coincidir con SQL Anywhere. El arreglo define el orden visual de los campos.
        super(new Definicion("Proyecto", new Campo[] {
            // idProyecto: máximo 10 caracteres; obligatorio.
            Campo.texto("idProyecto", "ID proyecto", 10, true),
            
            // Llaves foráneas: El framework buscará automáticamente el ID y Nombre de estas tablas.
            Campo.referencia("Edicion_idEdicion", "Edición", "Edicion", "idEdicion"),
            Campo.referencia("idCategoriaProyecto", "Categoría", "CategoriaProyecto", "idCategoriaProyecto"),
            
            // Nombre: máximo 100 caracteres permitidos por tablas2.sql; obligatorio.
            Campo.texto("Nombre", "Nombre del proyecto", 100, true),
            
            // Descripcion: máximo 30 caracteres; opcional.
            Campo.texto("Descripcion", "Descripción", 30, false)
            
        // Tablas hijas que se consultan antes de eliminar para proteger la integridad referencial.
        }, new String[][] {{"Equipo", "idProyecto"}}));
    }
}

