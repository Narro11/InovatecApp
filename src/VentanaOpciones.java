/*
 * VentanaOpciones.java
 * Ventana que se abre después del login. Organiza las pestañas y carga los datos de la pestaña seleccionada.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.awt.*;
import javax.swing.*;

/** Ventana principal: solamente organiza las pestañas. */
public class VentanaOpciones extends JFrame {
    private final JTabbedPane pestanas = new JTabbedPane();

    // Constructor: configura la ventana, crea las pestañas y programa la carga inicial.
    // MODIFICACIÓN: Ahora recibe el String rolUsuario
    public VentanaOpciones(String rolUsuario) {
        super("Inovatec - Administración de registros (" + rolUsuario + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 760);
        setMinimumSize(new Dimension(880, 640));
        setLocationRelativeTo(null);
        JLabel titulo = new JLabel("INOVATEC  |  Administración de registros");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        add(titulo, BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
        
        // Pasamos el rol al método que crea las pestañas
        crearPestanas(rolUsuario);
        
        pestanas.addChangeListener(e -> cargarPestana());
        SwingUtilities.invokeLater(this::cargarPestana);
    }

    // Agrega cada panel con el nombre que se mostrará en su pestaña.
    // MODIFICACIÓN: Evalúa el rol para decidir qué mostrar
    private void crearPestanas(String rolUsuario) {
        // Pestañas que pueden ver TODOS (Estudiantes y Administradores)
        pestanas.addTab("Alumnos", new PanelAlumno());
        pestanas.addTab("Proyectos", new PanelProyecto());
        pestanas.addTab("Equipos", new PanelEquipo());
        pestanas.addTab("Inscripciones", new PanelRegistro());
        //pestanas.addTab("Asesores", new PanelRegistroAsesor());

        // Pestañas que SOLO ve el Administrador
        if ("Administrador".equals(rolUsuario)) {
            pestanas.addTab("Estados", new PanelEstado());
            pestanas.addTab("Municipios", new PanelMunicipio());
            pestanas.addTab("Institutos", new PanelInstituto());
            pestanas.addTab("Campus", new PanelCampus());
            pestanas.addTab("Carreras", new PanelCarreras());
            pestanas.addTab("Personas externas", new PanelPersonaExterna());
        }
    }

    // Obtiene el formulario visible y solicita actualizar sus datos.
    private void cargarPestana() {
        Formulario formulario = (Formulario) pestanas.getSelectedComponent();
        if (formulario != null) formulario.cargarRegistros();
    }
}