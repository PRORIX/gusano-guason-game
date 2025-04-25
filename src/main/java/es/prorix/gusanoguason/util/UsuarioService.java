package es.prorix.gusanoguason.util;

import es.prorix.gusanoguason.models.Usuario;

/**
 * Clase que obtiene y da el usuario que esta usando la aplicacion en este momento
 * @author prorix
 * @version 1.0.1
 */
public class UsuarioService {
    public static Usuario usuarioActual;

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
