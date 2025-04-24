package es.prorix.gusanoguason.util;

import es.prorix.gusanoguason.models.Usuario;

public class UsuarioService {
    public static Usuario usuarioActual;

    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }
}
