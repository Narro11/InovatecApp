/*
 * PanelPatrocinador.java
 * Pestaña para gestionar los patrocinadores del evento.
 */

public class PanelPatrocinador extends Formulario {
    public PanelPatrocinador() {
        super(new Definicion("Patrocinador", new Campo[] {
            Campo.texto("idEmpresa", "ID Empresa", 30, true),
            Campo.referencia("Edicion_idEdicion", "Edición", "Edicion", "idEdicion"),
            Campo.referencia("Municipio_IdMunicipio", "Municipio", "Municipio", "IdMunicipio"),
            Campo.texto("Nombre", "Nombre Patrocinador", 30, false),
            Campo.texto("Aportacion", "Aportación", 100, false)
        }, new String[0][0]));
    }
}
