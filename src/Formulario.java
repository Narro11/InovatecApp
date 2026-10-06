/*
 * Formulario.java
 * Clase base de las pestañas. Agrupa sus controles y ofrece las acciones comunes.
 * Cada Panel hereda de esta clase y entrega su Definicion al constructor.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Formulario base compartido por las pestañas. */
public class Formulario extends JPanel {
        // La pestaña es este mismo JPanel; panel permite que las utilidades lo utilicen como contenedor.
        final JPanel panel = this;
        // Describe la tabla y sus campos. Las operaciones consultan d para construir el SQL.
        final Definicion d;
        // Guarda las cajas y combos en el mismo orden que d.campos.
        final JComponent[] entradas;
        final List<JButton> botones = new ArrayList<>();
        // El modelo almacena las filas; JTable se encarga de mostrarlas.
        DefaultTableModel modelo;
        JTable tabla;
        final JLabel estado = new JLabel(" ");
        final JComboBox<Opcion> filtroEstado = new JComboBox<>();
        final boolean filtraMunicipios;
        List<List<Opcion>> opciones = new ArrayList<>();
        javax.swing.Timer espera;
        // ajustando evita eventos durante cambios por código; ocupado indica una consulta en curso.
        boolean ajustando, ocupado;
        // Contador que permite descartar búsquedas automáticas antiguas.
        long revision;
        // Clave del registro mostrado actualmente, o null cuando se captura uno nuevo.
        String idCargado;

        // Constructor: prepara el panel y sus datos, y encarga la creación visual a DisenoFormulario.
        Formulario(Definicion definicion) {
            super(new BorderLayout(10, 10));
            d = definicion;
            entradas = new JComponent[d.campos.length];
            filtraMunicipios = d.tabla.equals("Instituto");
            panel.setBorder(BorderFactory.createEmptyBorder(16, 20, 12, 20));
            DisenoFormulario.inicializar(this);
        }
        // Recarga los registros y las opciones de los combos desde la base de datos.
        public void cargarRegistros() { OperacionesFormulario.cargarRegistros(this); }
    // Vacía los controles y vuelve a permitir la captura de un ID nuevo.
    public void limpiar() { DatosFormulario.limpiarFormulario(this); }
    // Solicita la búsqueda del registro cuyo ID está escrito en el formulario.
    public void buscar() { OperacionesFormulario.buscarRegistro(this); }
    // Valida los datos y solicita insertar un registro nuevo; false indica INSERT.
    public void guardar() { OperacionesFormulario.guardarRegistro(this, validar(), false); }
    // Valida los datos y solicita modificar el registro cargado; true indica UPDATE.
    public void actualizar() { OperacionesFormulario.guardarRegistro(this, validar(), true); }
    // Solicita eliminar por ID, con confirmación y revisión de registros hijos.
    public void eliminar() { OperacionesFormulario.eliminarRegistro(this); }
    // Devuelve los valores revisados o null si hay algún error; false pide validar todos los campos.
    protected String[] validar() { return DatosFormulario.validarFormulario(this, false); }
}

