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
        
        // --- CONTENEDOR SUPERIOR (Título + Botón) ---
        JPanel panelNorte = new JPanel(new BorderLayout());
        
        // 1. Título original (Lado Izquierdo)
        JLabel titulo = new JLabel("INOVATEC  |  Administración de registros");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        panelNorte.add(titulo, BorderLayout.WEST);
        
        // 2. Botón de Cerrar Sesión (Lado Derecho)
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 20)); // Márgenes para alinear con el título
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        
        // Acción del botón
        btnCerrarSesion.addActionListener(e -> {
            this.dispose(); // Cierra esta ventana
            // Vuelve a abrir el Login
            SwingUtilities.invokeLater(() -> new Login().setVisible(true));
        });
        
        panelBoton.add(btnCerrarSesion);
        panelNorte.add(panelBoton, BorderLayout.EAST);
        
        // Agregamos el contenedor al NORTE de la ventana principal
        add(panelNorte, BorderLayout.NORTH);
        add(pestanas, BorderLayout.CENTER);
        
        // Pasamos el rol al método que crea las pestañas
        crearPestanas(rolUsuario);
        
        pestanas.addChangeListener(e -> cargarPestana());
        SwingUtilities.invokeLater(this::cargarPestana);
    }

   private void crearPestanas(String rolUsuario) {
        
        // =========================================================
        // 1. CATEGORÍA: INSCRIPCIONES (Visible para todos)
        // =========================================================
        javax.swing.JTabbedPane tabsInscripciones = new javax.swing.JTabbedPane();
        tabsInscripciones.addTab("Alumnos", new PanelAlumno());
        tabsInscripciones.addTab("Proyectos", new PanelProyecto());
        tabsInscripciones.addTab("Equipos", new PanelEquipo());
        tabsInscripciones.addTab("Inscripciones", new PanelRegistro());
        
        // ---> NUEVO: Activa la recarga automática al moverte entre estas pestañas
        tabsInscripciones.addChangeListener(e -> cargarPestana());
        
        // Agregamos esta categoría al panel principal
        pestanas.addTab(" Inscripciones", tabsInscripciones);

        // =========================================================
        // CATEGORÍA EXCLUSIVA PARA EL ASESOR (Y el Administrador)
        // =========================================================
        if ("Asesor".equals(rolUsuario) || "Administrador".equals(rolUsuario)) {
            javax.swing.JTabbedPane tabsAsesor = new javax.swing.JTabbedPane();
            tabsAsesor.addTab("Mis Equipos", new PanelRegistroAsesor());
            
            tabsAsesor.addChangeListener(e -> cargarPestana());
            pestanas.addTab("Portal Asesor", tabsAsesor);
        }

        // =========================================================
        // CATEGORÍAS EXCLUSIVAS PARA ADMINISTRADOR
        // =========================================================
        if ("Administrador".equals(rolUsuario)) {
            
            // 2. CATEGORÍA: EVENTO
            javax.swing.JTabbedPane tabsEvento = new javax.swing.JTabbedPane();
            tabsEvento.addTab("Ediciones", new PanelEdicion());
            tabsEvento.addTab("Categorías de proyecto", new PanelCategoriaProyecto());
            tabsEvento.addTab("Patrocinadores", new PanelPatrocinador());
            tabsEvento.addTab("Jueces", new PanelJuez());
            tabsEvento.addTab("Staff", new PanelStaf());
            // Si tienes el panel de Personas Externas, agrégalo aquí:
            // tabsEvento.addTab("Personas externas", new PanelPersonaExterna()); 
            
            // 3. CATEGORÍA: CATÁLOGOS BASE
            javax.swing.JTabbedPane tabsCatalogos = new javax.swing.JTabbedPane();
            tabsCatalogos.addTab("Estados", new PanelEstado());
            tabsCatalogos.addTab("Municipios", new PanelMunicipio());
            tabsCatalogos.addTab("Institutos", new PanelInstituto());
            tabsCatalogos.addTab("Campus", new PanelCampus());
            tabsCatalogos.addTab("Carreras", new PanelCarreras());
            tabsCatalogos.addTab("Empleados", new PanelEmpleado());
            
            // 4. CATEGORÍA: SISTEMA Y REPORTES
            javax.swing.JTabbedPane tabsSistema = new javax.swing.JTabbedPane();
            tabsSistema.addTab("Usuarios", new PanelUsuarios());
            tabsSistema.addTab("Reporte General", new PanelReportes());

            // ---> NUEVO: Activa la recarga automática para los grupos del Administrador
            tabsEvento.addChangeListener(e -> cargarPestana());
            tabsCatalogos.addChangeListener(e -> cargarPestana());
            tabsSistema.addChangeListener(e -> cargarPestana());

            // Agregamos las categorías al panel principal
            pestanas.addTab(" Gestión del Evento", tabsEvento);
            pestanas.addTab(" Catálogos", tabsCatalogos);
            pestanas.addTab(" Sistema y Reportes", tabsSistema);
        }
    }

    // Obtiene el formulario visible y solicita actualizar sus datos.
    private void cargarPestana() {
        java.awt.Component seleccionado = pestanas.getSelectedComponent();
        
        // Si el componente seleccionado es una Categoría (un grupo de sub-pestañas)
        if (seleccionado instanceof javax.swing.JTabbedPane) {
            javax.swing.JTabbedPane subGrupo = (javax.swing.JTabbedPane) seleccionado;
            seleccionado = subGrupo.getSelectedComponent();
        }
        
        // Validamos de forma segura que sea un Formulario y cargamos sus datos
        if (seleccionado instanceof Formulario) {
            Formulario formulario = (Formulario) seleccionado;
            formulario.cargarRegistros();
        }
    }
}