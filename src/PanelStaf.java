/*
 * PanelStaf.java
 * Pestaña para registrar al personal de apoyo (Staff) del evento.
 */

public class PanelStaf extends Formulario {
    public PanelStaf() {
        super(new Definicion("Staf", new Campo[] {
            Campo.texto("idStaf", "ID Staff", 10, true),
            Campo.referencia("Edicion_idEdicion", "Edición", "Edicion", "idEdicion"),
            Campo.referencia("N_Control", "Núm. Control (Alumno de apoyo)", "Alumno", "N_Control"),
            Campo.referencia("Empleado_NumEmpleado", "Núm. Empleado (Maestro a cargo)", "Empleado", "NumEmpleado")
        }, new String[0][0]));
    }
}
