/*
 * Conexion.java
 * Abre conexiones JDBC con SQL Anywhere. Se utiliza desde Login y desde las operaciones de los formularios.
 * La cuenta usada aquí pertenece al servidor de base de datos; Login consulta los usuarios de la aplicación.
 */
// Importa las clases de Java utilizadas en este archivo.
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Carga el controlador JDBC y devuelve una conexión abierta. Si falla, informa mediante SQLException.
    public static Connection conectar() throws SQLException {
        try {
            // Busca la clase del driver; el archivo sajdbc4.jar debe estar disponible al ejecutar.
            Class.forName("sap.jdbc4.sqlanywhere.IDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro sajdbc4.jar.", e);
        }

        // Abre la conexión usando servidor, base de datos y credenciales configurados.
        return DriverManager.getConnection(
            "jdbc:sqlanywhere:ServerName=Proyecto_Inovatec;"
            + "DatabaseName=Proyecto_Inovatec",
            "gael",
            "gael"
        );
    }

    // Inicia la ejecución de esta clase.
    public static void main(String[] args) {
        try (Connection conexion = conectar()) {
            System.out.println("Conexion exitosa.");
        } catch (SQLException | LinkageError e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
