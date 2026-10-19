/*
 * App.java
 * Punto de entrada del programa. Abre Login en el hilo de eventos de Swing.
 */
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;

public class App {

    public static void main(String[] args) {
        
        // 1. REPARAR EL ESCALADO: Ignora el 0.8 de tu computadora y lo fuerza a 1.25 (125%)
        // Si lo sientes muy grande, cámbialo a "1.0". Si lo quieres más grande, prueba "1.5".
        System.setProperty("sun.java2d.uiScale", "1.25");

        try {
            // 2. Aplicar el diseño moderno de Windows
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // 3. AUMENTAR LA LETRA GLOBAL: Cambia todo a tamaño 14 para mayor legibilidad
            Font fuenteGlobal = new Font("Segoe UI", Font.PLAIN, 14);
            java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof javax.swing.plaf.FontUIResource) {
                    UIManager.put(key, new javax.swing.plaf.FontUIResource(fuenteGlobal));
                }
            }
            
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar el diseño del sistema.");
        }

        // Arranca la aplicación
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}