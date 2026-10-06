/*
 * Campo.java
 * Describe un atributo de una tabla para construir su control visual y sus validaciones.
 * No almacena lo que escribió el usuario: almacena cómo debe ser ese campo.
 */

/** Utilidad compartida: Campo. */
class Campo {
        // columna es el nombre SQL; etiqueta es el texto que se muestra al usuario.
        final String columna, etiqueta;
        // Número máximo de caracteres admitidos en este campo.
        final int longitud;
        // true exige un valor; false permite dejarlo vacío.
        final boolean obligatorio;
        // Activa el filtro de dígitos y el envío del dato como NUMERIC a JDBC.
        boolean numerico;
        // Para combos: tabla consultada y nombre de su clave primaria.
        String tabla, pk;
        // Constructor: guarda la columna SQL, etiqueta, longitud máxima y obligatoriedad.
        Campo(String columna, String etiqueta, int longitud, boolean obligatorio) {
            this.columna = columna; this.etiqueta = etiqueta;
            this.longitud = longitud; this.obligatorio = obligatorio;
        }
    
    // Crea la descripción de un campo que se representará con una caja de texto.
    static Campo texto(String columna, String etiqueta, int longitud, boolean obligatorio) {
        return new Campo(columna, etiqueta, longitud, obligatorio);
    }
    // Crea un campo obligatorio de tipo combo, asociado con la clave de otra tabla.
    static Campo referencia(String columna, String etiqueta, String tabla, String pk) {
        Campo c = new Campo(columna, etiqueta, 10, true);
        c.tabla = tabla;
        c.pk = pk;
        return c;
    }
}
