/*
 * App.java
 * Punto de entrada del programa. Abre Login en el hilo de eventos de Swing.
 */
// Importa las clases de Java utilizadas en este archivo.
import javax.swing.SwingUtilities;

public class App {

    // Inicia la ejecución de esta clase.
    public static void main(String[] args) {
        // Swing debe crear y modificar sus ventanas desde su hilo de eventos.
        SwingUtilities.invokeLater(() -> {
            // Constructor: crea y acomoda los controles de acceso, conecta el botón y centra la ventana.
            new Login().setVisible(true);
        });
    }
}
