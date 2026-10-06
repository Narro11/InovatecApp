/*
 * PanelAlumno.java
 * Pestaña de Alumno. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de Alumno: campos, límites y relaciones con otras tablas. */
public class PanelAlumno extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelAlumno() {
        // El nombre de tabla debe coincidir con SQL Anywhere. El arreglo define el orden visual de los campos.
        // Campo.texto recibe: columna SQL, etiqueta visible, longitud máxima y obligatorio (true/false).
        // Campo.referencia recibe: columna SQL, etiqueta, tabla de referencia y clave de esa tabla.
        super(new Definicion("Alumno", new Campo[] {
            // Define N_Control con máximo de diez dígitos y activa su filtro numérico.
            numeroControl(),
            Campo.referencia("idInstituto", "Instituto", "Instituto", "idInstituto"),
            Campo.referencia("idCarreras", "Carrera", "Carreras", "idCarreras"),
            // Nombre: máximo 50 caracteres; obligatorio.
            Campo.texto("Nombre", "Nombre", 50, true),
            // Paterno: máximo 50 caracteres; opcional en este formulario.
            Campo.texto("Paterno", "Apellido paterno", 50, false),
            // Materno: máximo 50 caracteres; opcional en este formulario.
            Campo.texto("Materno", "Apellido materno", 50, false),
            // Telefono: máximo 20 caracteres; opcional en este formulario.
            Campo.texto("Telefono", "Teléfono", 20, false),
            // Correo: máximo 50 caracteres; opcional en este formulario.
            Campo.texto("Correo", "Correo", 50, false)
        // Tablas hijas que se consultan antes de eliminar: nombre de tabla y columna que apunta a esta clave.
        }, new String[][] {{"Registro", "N_Control"}, {"Staf", "N_Control"}}));
    }

    // Define N_Control con máximo de diez dígitos y activa su filtro numérico.
    private static Campo numeroControl() {
        Campo campo = Campo.texto("N_Control", "Número de control", 10, true);
        campo.numerico = true;
        return campo;
    }
}
