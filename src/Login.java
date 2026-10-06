/*
 * Login.java
 * Ventana de acceso. Captura usuario y contraseña, consulta Usuarios y abre VentanaOpciones si encuentra coincidencia.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Login extends JFrame {

    private final JTextField txtUsuario = new JTextField(20);
    private final JPasswordField txtContrasena = new JPasswordField(20);
    private final JButton btnIngresar = new JButton("INGRESAR");

    // Constructor: crea y acomoda los controles de acceso, conecta el botón y centra la ventana.
    public Login() {
        setTitle("Inovatec - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Configura posiciones y espacios de la distribución por filas y columnas.
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 5, 10, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("INOVATEC", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 26));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(titulo, c);

        c.gridwidth = 1;
        c.gridy = 1;
        panel.add(new JLabel("Usuario:"), c);

        c.gridx = 1;
        panel.add(txtUsuario, c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Contraseña:"), c);

        c.gridx = 1;
        panel.add(txtContrasena, c);

        c.gridy = 3;
        panel.add(btnIngresar, c);

        // Al hacer clic se ejecuta iniciarSesion().
        btnIngresar.addActionListener(e -> iniciarSesion());
        // Permite activar el botón de ingreso con la tecla Enter.
        getRootPane().setDefaultButton(btnIngresar);

        add(panel);
        pack();
        setLocationRelativeTo(null);
    }

    // Valida los campos y busca una coincidencia de usuario y contraseña en Usuarios.
   private void iniciarSesion() {
        // Lee los datos capturados. trim() elimina espacios externos del usuario.
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        // Evita consultar cuando faltan datos y dirige el cursor al campo pendiente.
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Escribe tu usuario y contraseña.",
                "Campos incompletos",
                JOptionPane.WARNING_MESSAGE
            );

            // Evita consultar cuando faltan datos y dirige el cursor al campo pendiente.
            if (usuario.isEmpty()) {
                txtUsuario.requestFocusInWindow();
            } else {
                txtContrasena.requestFocusInWindow();
            }
            return;
        }

        // MODIFICACIÓN 1: Agregamos "Rol" a la consulta SQL
        String sql =
            "SELECT nombre, Rol FROM Usuarios "
            + "WHERE idUsuario = ? AND contrasena = ?";

        try (
            Connection conexion = Conexion.conectar();
            // Prepara la consulta parametrizada. try con recursos cerrará la conexión y la sentencia.
            PreparedStatement ps = conexion.prepareStatement(sql)
        ) {
            // Los parámetros ocupan los signos ? en orden: primero usuario y después contraseña.
            ps.setString(1, usuario);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                // Si la consulta devolvió una fila, se encontró una coincidencia de acceso.
                if (rs.next()) {
                    
                    // MODIFICACIÓN 2: Extraemos el rol de la base de datos
                    String rolUsuario = rs.getString("Rol");
                    // Precaución por si el campo está vacío en la base de datos
                    if (rolUsuario == null) {
                        rolUsuario = "Estudiante"; 
                    }

                    // MODIFICACIÓN 3: Le pasamos el rol a la VentanaOpciones
                    new VentanaOpciones(rolUsuario).setVisible(true);
                    
                    // Cierra únicamente esta ventana de login después de abrir la principal.
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Usuario o contraseña incorrectos.",
                        "Acceso denegado",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error al consultar la base de datos:\n" + e.getMessage(),
                "Error de conexión",
                JOptionPane.ERROR_MESSAGE
            );
        // Este bloque se ejecuta al terminar el intento, tanto si tuvo éxito como si ocurrió un error.
        } finally {
            txtContrasena.setText("");
        }
    }
}
