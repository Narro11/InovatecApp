/*
 * Limite.java
 * Filtro aplicado al documento de una caja de texto. Comprueba la longitud antes de aceptar escritura o pegado.
 * Cuando numerico es true, solamente admite dígitos; no permite signos ni puntos decimales.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.awt.*;
import javax.swing.text.*;

/** Utilidad compartida: Limite. */
class Limite extends DocumentFilter {
        final int maximo;
        final boolean numerico;
        // Constructor: guarda el máximo permitido y si debe aceptar únicamente dígitos.
        Limite(int maximo, boolean numerico) {
            this.maximo = maximo; this.numerico = numerico;
        }
        // Reutiliza la validación de replace para revisar una inserción de texto.
        public void insertString(FilterBypass fb, int pos, String s, AttributeSet a)
                throws BadLocationException {
            // Calcula cómo quedaría el texto después del cambio y lo acepta solamente si cumple las restricciones.
            replace(fb, pos, 0, s, a);
        }
        // Calcula cómo quedaría el texto después del cambio y lo acepta solamente si cumple las restricciones.
        public void replace(FilterBypass fb, int pos, int longitud, String s, AttributeSet a)
                throws BadLocationException {
            if (s == null) s = "";
            String anterior = fb.getDocument().getText(0, fb.getDocument().getLength());
            // Reconstruye el texto resultante incluyendo escritura, pegado o sustitución de una selección.
            String nuevo = anterior.substring(0, pos) + s + anterior.substring(pos + longitud);
            if (nuevo.length() <= maximo && (!numerico || nuevo.matches("[0-9]*")))
                fb.replace(pos, longitud, s, a);
            // Rechaza el cambio y emite un sonido cuando excede el límite o contiene caracteres no permitidos.
            else Toolkit.getDefaultToolkit().beep();
        }
    }
