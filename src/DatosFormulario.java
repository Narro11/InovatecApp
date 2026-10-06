/*
 * DatosFormulario.java
 * Lee y valida los controles, muestra registros y limpia la pantalla.
 * También filtra municipios por estado y carreras por instituto; no abre conexiones JDBC.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/** Lee, valida, limpia y muestra los datos de cada formulario. */
final class DatosFormulario {
    // Convierte el control indicado por i a JComboBox. Se usa solamente para campos definidos como referencias.
    static JComboBox<Opcion> obtenerCombo(Formulario f, int i) { return (JComboBox<Opcion>) f.entradas[i]; }

    // Obtiene texto sin espacios al inicio o al final, o el ID de la opción seleccionada en un combo.
    static String obtenerValor(Formulario f, int i) {
        if (f.entradas[i] instanceof JTextField) return ((JTextField) f.entradas[i]).getText().trim();
        Opcion o = (Opcion) DatosFormulario.obtenerCombo(f, i).getSelectedItem();
        return o == null ? "" : o.id;
    }

    // Sustituye las opciones del combo y selecciona el ID solicitado si está presente.
    static void llenarCombo(JComboBox<Opcion> combo, List<Opcion> lista, String id) {
        combo.removeAllItems();
        // Agrega una opción inicial vacía para que el usuario seleccione un registro.
        combo.addItem(new Opcion("", "", ""));
        for (Opcion o : lista) {
            combo.addItem(o);
            if (o.id.equals(id)) combo.setSelectedItem(o);
        }
    }

    // Conserva solamente los municipios cuyo padre coincide con el estado elegido.
    static void filtrarMunicipios(Formulario f, String id) {
        Opcion padre = (Opcion) f.filtroEstado.getSelectedItem();
        String estadoId = padre == null ? "" : padre.id;
        List<Opcion> filtradas = new ArrayList<>();
        for (Opcion o : f.opciones.get(1)) if (o.padre.equals(estadoId)) filtradas.add(o);
        DatosFormulario.llenarCombo(DatosFormulario.obtenerCombo(f, 1), filtradas, id);
    }

    // Conserva solamente las carreras del instituto seleccionado en el formulario de Alumno.
    static void filtrarCarreras(Formulario f, String id) {
        List<Opcion> filtradas = new ArrayList<>();
        for (Opcion o : f.opciones.get(2)) if (o.padre.equals(DatosFormulario.obtenerValor(f, 1))) filtradas.add(o);
        DatosFormulario.llenarCombo(DatosFormulario.obtenerCombo(f, 2), filtradas, id);
    }

    // Detiene búsquedas pendientes, vacía campos y selección, desbloquea el ID y solicita el foco inicial.
    static void limpiarFormulario(Formulario f) {
        // Cancela el temporizador y cambia la revisión para descartar resultados antiguos.
        f.espera.stop(); f.revision++; f.ajustando = true; f.idCargado = null;
        for (JComponent c : f.entradas) {
            if (c instanceof JTextField) ((JTextField) c).setText("");
            else if (((JComboBox<?>) c).getItemCount() > 0) ((JComboBox<?>) c).setSelectedIndex(0);
        }
        ((JTextField) f.entradas[0]).setEditable(true);
        if (f.filtraMunicipios) {
            if (f.filtroEstado.getItemCount() > 0) f.filtroEstado.setSelectedIndex(0);
            DatosFormulario.filtrarMunicipios(f, "");
        }
        if (f.d.tabla.equals("Alumno")) DatosFormulario.filtrarCarreras(f, "");
        f.tabla.clearSelection(); f.ajustando = false;
        f.estado.setText("Nuevo registro. Los campos con * son obligatorios.");
        f.entradas[0].requestFocusInWindow();
    }

    // Coloca una fila recuperada en las cajas y combos; conserva y bloquea su ID para actualizar ese registro.
    static void mostrarRegistro(Formulario f, String[] datos) {
        // Evita que llenar los campos por código provoque búsquedas automáticas adicionales.
        f.espera.stop(); f.ajustando = true;
        if (f.filtraMunicipios) {
            String padre = "";
            for (Opcion o : f.opciones.get(1)) if (o.id.equals(datos[1])) padre = o.padre;
            for (int n = 0; n < f.filtroEstado.getItemCount(); n++)
                if (f.filtroEstado.getItemAt(n).id.equals(padre)) f.filtroEstado.setSelectedIndex(n);
            DatosFormulario.filtrarMunicipios(f, datos[1]);
        }
        for (int i = 0; i < datos.length; i++) {
            if (f.entradas[i] instanceof JTextField) ((JTextField) f.entradas[i]).setText(datos[i]);
            else {
                if (f.d.tabla.equals("Alumno") && i == 2) DatosFormulario.filtrarCarreras(f, datos[i]);
                DatosFormulario.obtenerCombo(f, i).setSelectedIndex(0);
                for (int n = 1; n < DatosFormulario.obtenerCombo(f, i).getItemCount(); n++)
                    if (DatosFormulario.obtenerCombo(f, i).getItemAt(n).id.equals(datos[i])) DatosFormulario.obtenerCombo(f, i).setSelectedIndex(n);
            }
        }
        // Recuerda la clave de la fila que está cargada para comprobar futuras actualizaciones.
        f.idCargado = datos[0];
        // Bloquea la clave primaria del registro cargado; Nuevo/Limpiar permite escribir otra.
        ((JTextField) f.entradas[0]).setEditable(false);
        f.ajustando = false;
        f.estado.setText("Registro cargado. Puedes actualizarlo o eliminarlo. Usa Nuevo para otro ID.");
    }

    // Comprueba campos obligatorios, dígitos y longitudes. Con soloId=true revisa únicamente la clave primaria.
    // No comprueba formatos específicos de correo, CURP o teléfono.
    static String[] validarFormulario(Formulario f, boolean soloId) {
        String[] datos = new String[f.d.campos.length];
        for (int i = 0; i < datos.length; i++) {
            datos[i] = DatosFormulario.obtenerValor(f, i);
            // Buscar y eliminar requieren el ID, sin exigir completar el resto del formulario.
            if (soloId && i > 0) continue;
            Campo c = f.d.campos[i];
            if ((c.obligatorio && datos[i].isEmpty()) ||
                (c.numerico && !datos[i].matches("[0-9]{1," + c.longitud + "}")) ||
                (c.tabla == null && datos[i].length() > c.longitud)) {
                TareasFormulario.mostrarAviso(f, "Revisa el campo: " + c.etiqueta + ".");
                f.entradas[i].requestFocusInWindow();
                return null;
            }
        }
        return datos;
    }
}
