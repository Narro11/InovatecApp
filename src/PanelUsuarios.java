/*
 * PanelUsuarios.java
 * Pestaña exclusiva para el Administrador. 
 * Permite registrar nuevos usuarios y asignarles su rol.
 */

public class PanelUsuarios extends Formulario {
    public PanelUsuarios() {
        super(new Definicion("Usuarios", new Campo[] {
            Campo.texto("idUsuario", "Usuario (ID)", 20, true),
            Campo.texto("nombre", "Nombre", 30, true),
            Campo.texto("paterno", "Apellido Paterno", 30, true),
            Campo.texto("materno", "Apellido Materno", 30, true),
            Campo.texto("contrasena", "Contraseña", 255, true),
            
            // MODIFICACIÓN: Agregamos "Asesor" a las instrucciones de la etiqueta
            Campo.texto("Rol", "Rol (Estudiante / Admin / Juez / Asesor)", 20, true)
        }, new String[0][0]));
    }
}