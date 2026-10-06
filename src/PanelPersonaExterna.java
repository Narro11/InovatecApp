/*
 * PanelPersonaExterna.java
 * Pestaña de PersonaExterna. Define sus campos y relaciones; hereda el funcionamiento común de Formulario.
 */

/** Pestaña de PersonaExterna: campos, límites y relaciones con otras tablas. */
public class PanelPersonaExterna extends Formulario {
    // Constructor: entrega a Formulario los campos y las relaciones de esta pestaña.
    public PanelPersonaExterna() {
        // El nombre de tabla debe coincidir con SQL Anywhere. El arreglo define el orden visual de los campos.
        // Campo.texto recibe: columna SQL, etiqueta visible, longitud máxima y obligatorio (true/false).
        // Campo.referencia recibe: columna SQL, etiqueta, tabla de referencia y clave de esa tabla.
        super(new Definicion("PersonaExterna", new Campo[] {
            // CURP: máximo 20 caracteres; obligatorio.
            Campo.texto("CURP", "CURP", 20, true),
            // Nombre: máximo 30 caracteres; obligatorio.
            Campo.texto("Nombre", "Nombre", 30, true),
            // Paterno: máximo 30 caracteres; opcional en este formulario.
            Campo.texto("Paterno", "Apellido paterno", 30, false),
            // Materno: máximo 30 caracteres; opcional en este formulario.
            Campo.texto("Materno", "Apellido materno", 30, false),
            // Numero: máximo 20 caracteres; opcional en este formulario.
            Campo.texto("Numero", "Número / teléfono", 20, false),
            // Correo: máximo 50 caracteres; opcional en este formulario.
            Campo.texto("Correo", "Correo", 50, false)
        // Tablas hijas que se consultan antes de eliminar: nombre de tabla y columna que apunta a esta clave.
        }, new String[][] {{"Juez", "PersonaExterna_CURP"}}));
    }

    // Cada botón llama a uno de estos métodos. Aquí puedes personalizar tu parte.
    // Valida los datos y solicita insertar un registro nuevo; false indica INSERT.
    // @Override redefine un método heredado; super llama a la implementación común de Formulario.
    @Override public void guardar() { super.guardar(); }
    // Solicita la búsqueda del registro cuyo ID está escrito en el formulario.
    // @Override redefine un método heredado; super llama a la implementación común de Formulario.
    @Override public void buscar() { super.buscar(); }
    // Valida los datos y solicita modificar el registro cargado; true indica UPDATE.
    // @Override redefine un método heredado; super llama a la implementación común de Formulario.
    @Override public void actualizar() { super.actualizar(); }
    // Solicita eliminar por ID, con confirmación y revisión de registros hijos.
    // @Override redefine un método heredado; super llama a la implementación común de Formulario.
    @Override public void eliminar() { super.eliminar(); }
    // Vacía los controles y vuelve a permitir la captura de un ID nuevo.
    // @Override redefine un método heredado; super llama a la implementación común de Formulario.
    @Override public void limpiar() { super.limpiar(); }
    // Devuelve los valores revisados o null si hay algún error; false pide validar todos los campos.
    // @Override redefine un método heredado; super llama a la implementación común de Formulario.
    @Override protected String[] validar() { return super.validar(); }
}
