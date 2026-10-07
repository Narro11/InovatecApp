/*
 * PanelJuez.java
 * Pestaña para asignar jueces (internos o externos) a una edición.
 */

public class PanelJuez extends Formulario {
    public PanelJuez() {
        super(crearDefinicion());
    }

    private static Definicion crearDefinicion() {
        // 1. Creamos los campos problemáticos por separado
        Campo curpExterna = Campo.referencia("PersonaExterna_CURP", "CURP (Juez Externo)", "PersonaExterna", "CURP");
        curpExterna.obligatorio = false; // Le quitamos el candado de obligatorio

        Campo numEmpleado = Campo.referencia("Empleado_NumEmpleado", "Núm. Empleado (Juez Interno)", "Empleado", "NumEmpleado");
        numEmpleado.obligatorio = false; // Le quitamos el candado de obligatorio

        // 2. Armamos la definición con los campos modificados
        return new Definicion("Juez", new Campo[] {
            Campo.texto("idJuez", "ID Juez", 10, true),
            Campo.referencia("Edicion_idEdicion", "Edición", "Edicion", "idEdicion"), // Este se queda obligatorio
            curpExterna,
            numEmpleado
        }, new String[0][0]);
    }
}
