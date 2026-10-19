/*
 * PanelEmpleado.java
 * Pestaña exclusiva para el Administrador. 
 * Registra a los maestros/empleados del instituto (que luego serán Asesores o Jueces).
 */

public class PanelEmpleado extends Formulario {
    
    public PanelEmpleado() {
        super(crearDefinicion());
    }

    private static Definicion crearDefinicion() {
        // Creamos el campo del ID y lo forzamos a ser solo números
        Campo numEmp = Campo.texto("NumEmpleado", "Núm. Empleado", 10, true);
        numEmp.numerico = true;

        return new Definicion("Empleado", new Campo[] {
            numEmp,
            Campo.referencia("idInstituto", "Instituto", "Instituto", "idInstituto"),
            Campo.texto("Nombre", "Nombre", 30, true),
            Campo.texto("Paterno", "Apellido Paterno", 30, true),
            Campo.texto("Materno", "Apellido Materno", 30, false),
            Campo.texto("Telefono", "Teléfono", 20, false),
            Campo.texto("Correo", "Correo Electrónico", 50, false),
            Campo.texto("RFC", "RFC", 20, false)
        }, new String[0][0]);
    }
}
