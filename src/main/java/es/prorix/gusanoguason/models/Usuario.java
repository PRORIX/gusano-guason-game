package es.prorix.gusanoguason.models;
import java.util.Objects;

/**
 * Clase que representa un usuario en el sistema.
 * Contiene información básica del usuario como su nombre, correo electrónico y contraseña.
 * Esta clase es parte del modelo de la aplicación y se utiliza para gestionar la información del usuario.
 * @author prorix
 * @version 1.0.0
 */
public class Usuario {
    
    private String nombre;
    private String email;
    private String contrasena;
    private int record;

    /**
     * Constructor vacio
     */
    public Usuario(){

    }

    /**
     * Constructor solo con el correo electronico
     * @param email Correo electronico del usuario
     */
    public Usuario(String email) {
        this.email = email;
    }

    /**
     * Constructor con todos los parametros
     * @param nombre Nombre del usuario
     * @param email Correo electronico del usuario
     * @param contrasena Contraseña del usuario
     * @param record Record del usuario
     */
    public Usuario(String nombre, String email, String contrasena, int record) {
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
    }

    /**
     * Getters y setters
     */

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return this.contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getRecord() {
        return this.record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    /**
     * Método toString que devuelve una representación en cadena del objeto Usuario.
     * @return Cadena con la información del usuario.
     */

    @Override
    public String toString() {
        return getNombre() + "," + getEmail() + "," + getContrasena() + "," + getRecord();
    }

    /**
     * Método equals que compara dos objetos Usuario.
     * @param obj Objeto a comparar.
     * @return true si son iguales, false en caso contrario.
     */

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Usuario)) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        return Objects.equals(email, usuario.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
    
}