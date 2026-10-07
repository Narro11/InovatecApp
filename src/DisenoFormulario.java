/*
 * DisenoFormulario.java
 * Construye la interfaz a partir de la definición: campos, botones y cuadrícula.
 * También conecta eventos de selección, escritura del ID y cambios de combos.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;

/** Distribuye los campos, botones y tabla. Conecta sus eventos. */
final class DisenoFormulario {
    // Ensambla campos, botones, tabla y etiqueta de estado; configura el orden de Tab y la búsqueda automática.
    static void inicializar(Formulario f) {
        // Acumula los componentes en el orden en que debe avanzar el cursor.
        List<Component> orden = new ArrayList<>();
        JPanel campos = DisenoFormulario.crearCampos(f, orden);
        JPanel botones = DisenoFormulario.crearBotones(f);
        orden.addAll(f.botones);
        JPanel superior = new JPanel(new BorderLayout(0, 8));
        superior.add(campos, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);
        f.panel.add(superior, BorderLayout.NORTH);
        DisenoFormulario.crearTabla(f);
        f.panel.add(new JScrollPane(f.tabla), BorderLayout.CENTER);
        f.panel.add(f.estado, BorderLayout.SOUTH);
        orden.add(f.tabla);
        f.panel.setFocusTraversalPolicyProvider(true);
        // Asigna el recorrido de Tab según el orden de creación de los controles.
        f.panel.setFocusTraversalPolicy(new OrdenFoco(orden));
        DisenoFormulario.configurarBusquedaAutomatica(f);
    }

    // Recorre la definición y crea una caja de texto o un combo para cada atributo.
    static JPanel crearCampos(Formulario f, List<Component> orden) {
        JPanel panel = new JPanel(new GridBagLayout());
        int fila = 0;
        for (int i = 0; i < f.d.campos.length; i++) {
            Campo campo = f.d.campos[i];
            // En Instituto agrega un filtro de Estado antes del combo de Municipio.
            if (f.filtraMunicipios && i == 1) {
                fila = DisenoFormulario.agregarFila(panel, fila, "Estado (filtro) *", f.filtroEstado);
                orden.add(f.filtroEstado);
                f.filtroEstado.addActionListener(e -> {
                    if (!f.ajustando) DatosFormulario.filtrarMunicipios(f, "");
                });
            }
            // Los campos con tabla de referencia se muestran mediante combos en lugar de cajas.
            if (campo.tabla != null) f.entradas[i] = new JComboBox<Opcion>();
            else {
                JTextField texto = new JTextField(24);
                ((AbstractDocument) texto.getDocument()).setDocumentFilter(
                    // Constructor: guarda el máximo permitido y si debe aceptar únicamente dígitos.
                    // El filtro bloquea excesos de longitud y caracteres no numéricos cuando corresponde.
                    new Limite(campo.longitud, campo.numerico));
                f.entradas[i] = texto;
            }
            fila = DisenoFormulario.agregarFila(panel, fila, campo.etiqueta + (campo.obligatorio ? " *" : ""), f.entradas[i]);
            orden.add(f.entradas[i]);
            f.opciones.add(new ArrayList<>());
        }
        // Al cambiar de instituto, vuelve a filtrar las carreras disponibles.
        if (f.d.tabla.equals("Alumno")) DatosFormulario.obtenerCombo(f, 1).addActionListener(e -> {
            if (!f.ajustando) DatosFormulario.filtrarCarreras(f, "");
        });
        return panel;
    }

    // Crea los seis botones y asocia cada uno con el método correspondiente del formulario.
    static JPanel crearBotones(Formulario f) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        DisenoFormulario.agregarBoton(f, panel, "Nuevo / Limpiar", f::limpiar);
        DisenoFormulario.agregarBoton(f, panel, "Buscar", f::buscar);
        DisenoFormulario.agregarBoton(f, panel, "Guardar", f::guardar);
        DisenoFormulario.agregarBoton(f, panel, "Actualizar", f::actualizar);
        DisenoFormulario.agregarBoton(f, panel, "Eliminar", f::eliminar);
        DisenoFormulario.agregarBoton(f, panel, "Refrescar", f::cargarRegistros);
        return panel;
    }

    // Reemplaza los manejadores anteriores de un botón por una acción nueva. Es una utilidad disponible para personalizarlo.
    static void conectarBoton(JButton boton, Runnable accion) {
        for (java.awt.event.ActionListener anterior : boton.getActionListeners())
            boton.removeActionListener(anterior);
        boton.addActionListener(e -> accion.run());
    }

    // Crea la cuadrícula y sus encabezados. Impide editar celdas directamente y escucha la selección de filas.
    static void crearTabla(Formulario f) {
        String[] encabezados = Arrays.stream(f.d.campos).map(c -> c.etiqueta).toArray(String[]::new);
        f.modelo = new DefaultTableModel(encabezados, 0) {
            // Devuelve false para que los cambios se realicen con el formulario y no escribiendo sobre la cuadrícula.
            public boolean isCellEditable(int fila, int columna) { return false; }
        };
        f.tabla = new JTable(f.modelo);
        // Permite ordenar la cuadrícula al pulsar un encabezado.
        f.tabla.setAutoCreateRowSorter(true);
        f.tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        f.tabla.setRowHeight(25);
        f.tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) DisenoFormulario.seleccionarFila(f);
        });
    }

    // Lee la fila seleccionada y muestra sus valores en los controles del formulario.
    static void seleccionarFila(Formulario f) {
        if (f.ajustando || f.ocupado || f.tabla.getSelectedRow() < 0) return;
        // Traduce la fila visible a su posición real en el modelo, incluso si se ordenó la tabla.
        int fila = f.tabla.convertRowIndexToModel(f.tabla.getSelectedRow());
        String[] datos = new String[f.d.campos.length];
        for (int i = 0; i < datos.length; i++) datos[i] = (String) f.modelo.getValueAt(fila, i);
        f.revision++;
        DatosFormulario.mostrarRegistro(f, datos);
    }

    // Observa los cambios del ID y programa una búsqueda tras 650 milisegundos sin nuevas modificaciones.
    static void configurarBusquedaAutomatica(Formulario f) {
        // Cada cambio del ID reinicia esta espera; así no se consulta por cada tecla inmediatamente.
        f.espera = new javax.swing.Timer(650, e -> OperacionesFormulario.buscarIdAutomaticamente(f));
        // El temporizador se ejecuta una sola vez por cada reinicio.
        f.espera.setRepeats(false);
        
        // VALIDACIÓN DE SEGURIDAD: Solo aplicar si el primer componente es un JTextField
        if (f.entradas.length > 0 && f.entradas[0] instanceof javax.swing.JTextField) {
            javax.swing.JTextField id = (javax.swing.JTextField) f.entradas[0];
            
            id.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                // Invalida búsquedas anteriores y reinicia el temporizador al modificar el ID manualmente.
                private void cambio() {
                    if (f.ajustando) return;
                    f.revision++;
                    f.idCargado = null;
                    f.espera.restart();
                }
                // Notifica que se agregaron caracteres al ID.
                public void insertUpdate(javax.swing.event.DocumentEvent e) { cambio(); }
                // Notifica que se quitaron caracteres del ID.
                public void removeUpdate(javax.swing.event.DocumentEvent e) { cambio(); }
                // Notifica cambios de atributos del documento; en texto simple normalmente no se utiliza.
                public void changedUpdate(javax.swing.event.DocumentEvent e) { cambio(); }
            });
            id.addActionListener(e -> OperacionesFormulario.buscarRegistro(f));
        }
    }

    // Coloca una etiqueta y su control en una fila del GridBagLayout; devuelve el índice de la siguiente fila.
    static int agregarFila(JPanel p, int fila, String titulo, JComponent entrada) {
        // Configura posiciones y espacios de la distribución por filas y columnas.
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 5, 3, 10);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0; c.gridy = fila;
        p.add(new JLabel(titulo), c);
        c.gridx = 1; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        p.add(entrada, c);
        return fila + 1;
    }

    // Crea el botón, conecta su acción y lo guarda en la lista para poder habilitarlo o deshabilitarlo.
    static void agregarBoton(Formulario f, JPanel p, String titulo, Runnable accion) {
        JButton b = new JButton(titulo);
        b.addActionListener(e -> accion.run());
        f.botones.add(b); p.add(b);
    }
}
