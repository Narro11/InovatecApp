/*
 * PanelReportes.java
 * Vista de solo lectura para el Administrador. 
 * Muestra un reporte consolidado usando INNER JOIN de varias tablas.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PanelReportes extends JPanel {
    private DefaultTableModel modelo;
    private JTable tablaReporte;

    public PanelReportes() {
        // Configuramos el diseño del panel
        setLayout(new BorderLayout());

        // --- PANEL SUPERIOR: Título y Botón ---
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblTitulo = new JLabel("Reporte General: Equipos, Proyectos y Alumnos Inscritos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnRefrescar = new JButton("Cargar / Refrescar Datos");
        panelNorte.add(lblTitulo);
        panelNorte.add(btnRefrescar);
        add(panelNorte, BorderLayout.NORTH);

        // --- CENTRO: Tabla de datos ---
        // Definimos las columnas que verá el administrador
        modelo = new DefaultTableModel(new String[]{"Equipo", "Proyecto", "Núm. Control", "Alumno", "Rol"}, 0);
        tablaReporte = new JTable(modelo);
        tablaReporte.setEnabled(false); // Es solo lectura, no se edita aquí
        
        add(new JScrollPane(tablaReporte), BorderLayout.CENTER);

        // --- ACCIÓN DEL BOTÓN ---
        btnRefrescar.addActionListener(e -> cargarReporte());
    }

    private void cargarReporte() {
        modelo.setRowCount(0); // Limpia la tabla antes de cargar datos nuevos
        
        // Consulta SQL con INNER JOIN combinando 4 tablas
        String sql = "SELECT e.Nombre AS Equipo, p.Nombre AS Proyecto, a.N_Control, a.Nombre, a.Paterno, r.Rol " +
                     "FROM Registro r " +
                     "INNER JOIN Equipo e ON r.idEquipo = e.idEquipo " +
                     "INNER JOIN Proyecto p ON e.idProyecto = p.idProyecto " +
                     "INNER JOIN Alumno a ON r.N_Control = a.N_Control " +
                     "ORDER BY e.Nombre";

        // Usamos tu clase Conexion.java para la conexión a Sybase
        try (Connection con = Conexion.conectar();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Recorremos los resultados y los agregamos a la tabla de la interfaz
            while (rs.next()) {
                String equipo = rs.getString("Equipo");
                String proyecto = rs.getString("Proyecto");
                String nControl = rs.getString("N_Control");
                // Juntamos el nombre y apellido del alumno en una sola celda
                String alumno = rs.getString("Nombre") + " " + rs.getString("Paterno");
                String rol = rs.getString("Rol");
                
                modelo.addRow(new Object[]{equipo, proyecto, nControl, alumno, rol});
            }
            
            JOptionPane.showMessageDialog(this, "Reporte actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar reporte: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}