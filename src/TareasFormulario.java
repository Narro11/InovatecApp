/*
 * TareasFormulario.java
 * Ejecuta tareas de los formularios en segundo plano mediante SwingWorker.
 * Deshabilita controles mientras trabaja y presenta resultados o errores en el hilo de Swing.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import javax.swing.*;

/** Ejecuta consultas sin congelar la ventana y muestra avisos. */
final class TareasFormulario {
    // Muestra un cuadro de advertencia asociado con la pestaña correspondiente.
    static void mostrarAviso(Formulario f, String texto) {
        JOptionPane.showMessageDialog(f.panel, texto, "Revisar datos", JOptionPane.WARNING_MESSAGE);
    }

    // Recibe una tarea y una acción de finalización. T representa el tipo del resultado que devolverá la tarea.
    static <T> void ejecutarEnSegundoPlano(Formulario f, Callable<T> trabajo, Consumer<T> alTerminar) {
        // Evita iniciar otra tarea compartida mientras esta pestaña sigue trabajando.
        if (f.ocupado) return;
        f.ocupado = true; f.revision++; f.espera.stop(); TareasFormulario.habilitarFormulario(f, false);
        f.estado.setText("Consultando la base de datos...");
        new SwingWorker<T, Void>() {
            // Ejecuta el trabajo fuera del hilo de la interfaz; aquí se hacen las consultas que pueden tardar.
            protected T doInBackground() throws Exception { return trabajo.call(); }
            // Se ejecuta en el hilo de Swing al terminar la tarea y permite actualizar los componentes de la pantalla.
            protected void done() {
                // Reactiva la pestaña al finalizar; get() recupera el resultado o informa el error de la tarea.
                f.ocupado = false; TareasFormulario.habilitarFormulario(f, true);
                try { alTerminar.accept(get()); }
                catch (Exception ex) {
                    Throwable causa = ex.getCause() == null ? ex : ex.getCause();
                    f.estado.setText("No se completó la operación.");
                    TareasFormulario.mostrarAviso(f, causa.getMessage() == null ? causa.toString() : causa.getMessage());
                }
            }
        }.execute();
    }

    // Activa o desactiva campos, botones, combo de filtro y tabla durante una operación.
    static void habilitarFormulario(Formulario f, boolean habilitado) {
        for (JComponent c : f.entradas) c.setEnabled(habilitado);
        for (JButton b : f.botones) b.setEnabled(habilitado);
        f.filtroEstado.setEnabled(habilitado); f.tabla.setEnabled(habilitado);
    }
}
