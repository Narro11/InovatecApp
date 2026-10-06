/*
 * ConsultasBD.java
 * Rutinas JDBC reutilizables: buscar una fila, convertir resultados y cargar combos.
 * Recibe una conexión ya abierta; quien la abrió es responsable de cerrarla.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Consultas JDBC y parámetros SQL compartidos. */
final class ConsultasBD {
    // Ejecuta SELECT por clave primaria. Devuelve una fila como arreglo de textos, o null si no existe.
    static String[] consultarPorId(Formulario f, Connection c, String id) throws SQLException {
        String sql = "SELECT " + f.d.columnas() + " FROM " + f.d.tabla
            + " WHERE " + f.d.campos[0].columna + " = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ConsultasBD.asignarParametro(ps, 1, f.d.campos[0], id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? ConsultasBD.leerFila(f, rs) : null; }
        }
    }

    // Convierte la fila actual del ResultSet en un arreglo con el orden de campos del formulario.
    static String[] leerFila(Formulario f, ResultSet rs) throws SQLException {
        String[] fila = new String[f.d.campos.length];
        for (int i = 0; i < fila.length; i++) {
            fila[i] = rs.getString(i + 1);
            // Representa un NULL de la base como texto vacío en la interfaz.
            if (fila[i] == null) fila[i] = "";
        }
        return fila;
    }

    // Asigna un valor a un signo ? de la consulta, utilizando NULL, NUMERIC o VARCHAR según el campo.
    static void asignarParametro(PreparedStatement ps, int pos, Campo campo, String valor) throws SQLException {
        if (valor.isEmpty()) ps.setNull(pos, campo.numerico ? Types.NUMERIC : Types.VARCHAR);
        else if (campo.numerico) ps.setBigDecimal(pos, new BigDecimal(valor));
        else ps.setString(pos, valor);
    }

    // Consulta ID y Nombre para llenar un combo; opcionalmente recupera la clave del padre.
    static List<Opcion> leerOpciones(Connection c, String tabla, String pk, String padre) throws SQLException {
        List<Opcion> lista = new ArrayList<>();
        String sql = "SELECT " + pk + ", Nombre" + (padre == null ? "" : ", " + padre)
            + " FROM " + tabla + " ORDER BY Nombre, " + pk;
        try (PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Opcion(rs.getString(1),
                rs.getString(2) == null ? "" : rs.getString(2), padre == null ? "" : rs.getString(3)));
        }
        return lista;
    }
}
