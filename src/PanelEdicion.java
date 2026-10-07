/*
 * PanelEdicion.java
 * Pestaña exclusiva para el Administrador. Gestiona las ediciones del evento Inovatec.
 */

public class PanelEdicion extends Formulario {
    public PanelEdicion() {
        // El nombre debe coincidir exactamente con la tabla en SQL Anywhere
        super(new Definicion("Edicion", new Campo[] {
            // idEdicion: VARCHAR(20), obligatorio
            Campo.texto("idEdicion", "ID Edición", 20, true),
            
            // Llave foránea que apunta a la tabla Instituto
            Campo.referencia("Instituto_idInstituto", "Instituto Sede", "Instituto", "idInstituto"),
            
            // Nombre de la edición: VARCHAR(30)
            Campo.texto("Nombre", "Nombre de la Edición", 30, false),
            
            // Descripcion: VARCHAR(50)
            Campo.texto("Descripcion", "Descripción", 50, false),
            
            // Fechas: Se manejan como texto con formato YYYY-MM-DD para SQL Anywhere
            Campo.texto("FechaInicio", "Fecha Inicio (AAAA-MM-DD)", 10, false),
            Campo.texto("FechaFin", "Fecha Fin (AAAA-MM-DD)", 10, false)
            
        // Si hay tablas que se actualizan en cascada al cambiar la edición, se pondrían aquí. 
        // Por ahora lo dejamos vacío.
        }, new String[0][0]));
    }
}
