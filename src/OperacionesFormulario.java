/*
 * OperacionesFormulario.java
 * Coordina las operaciones CRUD: crear (INSERT), consultar (SELECT), actualizar (UPDATE) y eliminar (DELETE).
 * Usa Conexion para acceder a SQL Anywhere y actualiza la interfaz al terminar.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;

/** Coordina buscar, guardar, actualizar, eliminar y refrescar. */
final class OperacionesFormulario {
    // Busca el ID en segundo plano y aplica el resultado solamente si el usuario no cambió de registro.
    static void buscarIdAutomaticamente(Formulario f) {
        if (f.ocupado || f.ajustando || DatosFormulario.obtenerValor(f, 0).isEmpty() || f.idCargado != null) return;
        final String id = DatosFormulario.obtenerValor(f, 0);
        // Guarda la versión de esta búsqueda para detectar si quedó desactualizada.
        final long marca = f.revision;
        new SwingWorker<String[], Void>() {
            // Ejecuta el trabajo fuera del hilo de la interfaz; aquí se hacen las consultas que pueden tardar.
            protected String[] doInBackground() throws Exception {
                try (Connection c = Conexion.conectar()) { return ConsultasBD.consultarPorId(f, c, id); }
            }
            // Se ejecuta en el hilo de Swing al terminar la tarea y permite actualizar los componentes de la pantalla.
            protected void done() {
                // Ignora una respuesta atrasada si el usuario cambió el ID o inició otra operación.
                if (marca != f.revision || f.ocupado || !id.equals(DatosFormulario.obtenerValor(f, 0))) return;
                try {
                    String[] datos = get();
                    if (datos != null) DatosFormulario.mostrarRegistro(f, datos);
                    else f.estado.setText("ID disponible para un nuevo registro.");
                } catch (Exception ex) {
                    f.estado.setText("No se pudo buscar el ID. Usa Buscar para consultar el detalle.");
                }
            }
        }.execute();
    }

    // Valida el ID, consulta la base y llena el formulario; avisa si el registro no existe.
    static void buscarRegistro(Formulario f) {
        String[] datos = DatosFormulario.validarFormulario(f, true);
        if (datos == null) return;
        TareasFormulario.ejecutarEnSegundoPlano(f, () -> {
            try (Connection c = Conexion.conectar()) { return ConsultasBD.consultarPorId(f, c, datos[0]); }
        }, encontrados -> {
            if (encontrados == null) TareasFormulario.mostrarAviso(f, "No existe un registro con ese ID.");
            else DatosFormulario.mostrarRegistro(f, encontrados);
        });
    }

    // Inserta o actualiza según el argumento actualizar. Comprueba la existencia del ID y usa una transacción.
    static void guardarRegistro(Formulario f, String[] datos, boolean actualizar) {
        if (datos == null) return;
        if (actualizar && (f.idCargado == null || !f.idCargado.equals(datos[0]))) {
            TareasFormulario.mostrarAviso(f, "Busca o selecciona el registro antes de actualizar."); return;
        }
        TareasFormulario.ejecutarEnSegundoPlano(f, () -> {
            try (Connection c = Conexion.conectar()) {
                // Agrupa la modificación en una transacción: commit confirma y rollback revierte si hay error.
                c.setAutoCommit(false);
                try {
                    // Consulta primero si existe la clave para distinguir inserción de actualización.
                    boolean existe = ConsultasBD.consultarPorId(f, c, datos[0]) != null;
                    // Evita insertar un ID repetido y pide utilizar Actualizar.
                    if (!actualizar && existe) throw new SQLException("El ID ya existe. Usa Buscar y después Actualizar.");
                    // Impide actualizar un registro que ya fue eliminado.
                    if (actualizar && !existe) throw new SQLException("El registro ya no existe. Refresca la lista.");
                    String sql;
                    if (actualizar) {
                        List<String> asignaciones = new ArrayList<>();
                        for (int i = 1; i < f.d.campos.length; i++) asignaciones.add(f.d.campos[i].columna + " = ?");
                        sql = "UPDATE " + f.d.tabla + " SET " + String.join(", ", asignaciones)
                            + " WHERE " + f.d.campos[0].columna + " = ?";
                    } else {
                        // Crea un signo ? por cada valor a insertar; los datos se asignan por separado.
                        String[] marcas = new String[datos.length]; Arrays.fill(marcas, "?");
                        sql = "INSERT INTO " + f.d.tabla + " (" + f.d.columnas() + ") VALUES ("
                            + String.join(", ", marcas) + ")";
                    }
                    try (PreparedStatement ps = c.prepareStatement(sql)) {
                        // JDBC numera los parámetros desde 1, mientras los arreglos de Java empiezan en 0.
                        int pos = 1;
                        for (int i = actualizar ? 1 : 0; i < datos.length; i++)
                            ConsultasBD.asignarParametro(ps, pos++, f.d.campos[i], datos[i]);
                        // En UPDATE la clave primaria se coloca al final, en la condición WHERE.
                        if (actualizar) ConsultasBD.asignarParametro(ps, pos, f.d.campos[0], datos[0]);
                        // Comprueba que la operación afectó exactamente una fila.
                        if (ps.executeUpdate() != 1) throw new SQLException("No se modificó exactamente un registro.");
                    }
                    // Confirma la modificación cuando todas las comprobaciones terminaron correctamente.
                    c.commit();
                // Si ocurre un error SQL, revierte los cambios pendientes y comunica la causa.
                } catch (SQLException ex) { c.rollback(); throw ex; }
            }
            return actualizar ? "Registro actualizado." : "Registro guardado.";
        }, mensaje -> { JOptionPane.showMessageDialog(f.panel, mensaje); OperacionesFormulario.cargarRegistros(f); });
    }

    // Confirma la eliminación, verifica que el registro exista y que no tenga hijos antes del DELETE.
    static void eliminarRegistro(Formulario f) {
        String[] datos = DatosFormulario.validarFormulario(f, true);
        if (datos == null) return;
        if (JOptionPane.showConfirmDialog(f.panel, "¿Eliminar el registro " + datos[0] + " de " + f.d.tabla + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        TareasFormulario.ejecutarEnSegundoPlano(f, () -> {
            try (Connection c = Conexion.conectar()) {
                // Agrupa la modificación en una transacción: commit confirma y rollback revierte si hay error.
                c.setAutoCommit(false);
                try {
                    if (ConsultasBD.consultarPorId(f, c, datos[0]) == null) throw new SQLException("El registro no existe.");
                    // Cada pareja contiene tabla hija y columna que referencia el ID del registro actual.
                    for (String[] hijo : f.d.hijos) {
                        try (PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM " + hijo[0]
                                + " WHERE " + hijo[1] + " = ?")) {
                            ConsultasBD.asignarParametro(ps, 1, f.d.campos[0], datos[0]);
                            try (ResultSet rs = ps.executeQuery()) {
                                rs.next();
                                // Si COUNT(*) encontró hijos, se detiene la eliminación con un mensaje.
                                if (rs.getLong(1) > 0) throw new SQLException(
                                    "No se puede eliminar: tiene registros relacionados en " + hijo[0] + ".");
                            }
                        }
                    }
                    // Las FK de la base respaldan la comprobación ante operaciones simultáneas.
                    try (PreparedStatement ps = c.prepareStatement("DELETE FROM " + f.d.tabla
                            + " WHERE " + f.d.campos[0].columna + " = ?")) {
                        ConsultasBD.asignarParametro(ps, 1, f.d.campos[0], datos[0]);
                        // Comprueba que la operación afectó exactamente una fila.
                        if (ps.executeUpdate() != 1) throw new SQLException("No se pudo eliminar el registro.");
                    }
                    // Confirma la modificación cuando todas las comprobaciones terminaron correctamente.
                    c.commit();
                // Si ocurre un error SQL, revierte los cambios pendientes y comunica la causa.
                } catch (SQLException ex) { c.rollback(); throw ex; }
            }
            return "Registro eliminado.";
        }, mensaje -> { JOptionPane.showMessageDialog(f.panel, mensaje); OperacionesFormulario.cargarRegistros(f); });
    }

    // Recarga los registros y las opciones de los combos desde la base de datos.
    static void cargarRegistros(Formulario f) {
        TareasFormulario.ejecutarEnSegundoPlano(f, () -> {
            Carga carga = new Carga();
            try (Connection c = Conexion.conectar()) {
                if (f.filtraMunicipios) carga.estados.addAll(ConsultasBD.leerOpciones(c, "Estado", "IdEstado", null));
                for (Campo campo : f.d.campos) {
                    String padre = "Municipio".equals(campo.tabla) ? "IdEstado"
                        : "Carreras".equals(campo.tabla) ? "idInstituto" : null;
                    carga.opciones.add(campo.tabla == null ? new ArrayList<>()
                        : ConsultasBD.leerOpciones(c, campo.tabla, campo.pk, padre));
                }
                try (PreparedStatement ps = c.prepareStatement("SELECT " + f.d.columnas() + " FROM "
                        + f.d.tabla + " ORDER BY " + f.d.campos[0].columna);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) carga.filas.add(ConsultasBD.leerFila(f, rs));
                }
            }
            return carga;
        }, carga -> {
            f.ajustando = true; f.opciones = carga.opciones;
            if (f.filtraMunicipios) DatosFormulario.llenarCombo(f.filtroEstado, carga.estados, "");
            for (int i = 0; i < f.entradas.length; i++)
                if (f.d.campos[i].tabla != null) DatosFormulario.llenarCombo(DatosFormulario.obtenerCombo(f, i), f.opciones.get(i), "");
            // Vacía las filas visibles antes de colocar los resultados recién consultados.
            f.modelo.setRowCount(0);
            // Agrega cada registro recuperado como una fila nueva de la cuadrícula.
            for (String[] fila : carga.filas) f.modelo.addRow(fila);
            f.ajustando = false; DatosFormulario.limpiarFormulario(f);
            f.estado.setText(carga.filas.size() + " registros. * Obligatorio. Escribe el ID para buscar automáticamente.");
        });
    }
}
