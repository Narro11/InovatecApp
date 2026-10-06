/*
 * OrdenFoco.java
 * Define qué componente recibe el cursor con Tab y Shift+Tab.
 * Recorre una lista de controles y omite los que no pueden recibir el foco.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.awt.*;
import java.util.List;

/** Utilidad compartida: OrdenFoco. */
class OrdenFoco extends FocusTraversalPolicy {
        final List<Component> orden;
        // Constructor: conserva la lista de controles en su orden de captura.
        OrdenFoco(List<Component> orden) { this.orden = orden; }
        // Busca el próximo control utilizable: paso=1 avanza y paso=-1 retrocede, recorriendo la lista de forma circular.
        private Component siguiente(Component actual, int paso) {
            int indice = orden.indexOf(actual);
            for (int n = 1; n <= orden.size(); n++) {
                Component candidato = orden.get(Math.floorMod(indice + paso * n, orden.size()));
                if (candidato.isEnabled() && candidato.isVisible() && candidato.isFocusable())
                    return candidato;
            }
            return orden.get(0);
        }
        // Devuelve el siguiente control al presionar Tab.
        public Component getComponentAfter(Container c, Component a) { return siguiente(a, 1); }
        // Devuelve el control anterior al presionar Shift+Tab.
        public Component getComponentBefore(Container c, Component a) { return siguiente(a, -1); }
        // Devuelve el primer control del orden configurado.
        public Component getFirstComponent(Container c) { return orden.get(0); }
        // Devuelve el último control del orden configurado.
        public Component getLastComponent(Container c) { return orden.get(orden.size() - 1); }
        // Devuelve el control predeterminado para iniciar la captura.
        public Component getDefaultComponent(Container c) { return orden.get(0); }
    }
