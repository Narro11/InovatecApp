/*
 * PanelCategoriaProyecto.java
 * Pestaña exclusiva para el Administrador. Gestiona las categorías de los proyectos.
 */

public class PanelCategoriaProyecto extends Formulario {
    public PanelCategoriaProyecto() {
        // Se enlaza exactamente con la tabla CategoriaProyecto de tu base de datos
        super(new Definicion("CategoriaProyecto", new Campo[] {
            // idCategoriaProyecto: VARCHAR(10), obligatorio
            Campo.texto("idCategoriaProyecto", "ID Categoría", 10, true),
            
            // Nombre: VARCHAR(30), obligatorio
            Campo.texto("Nombre", "Nombre de la Categoría", 30, true),
            
            // Descripcion: VARCHAR(30), lo dejamos opcional (false)
            Campo.texto("Descripcion", "Descripción", 30, false)
        }, new String[0][0]));
    }
}
