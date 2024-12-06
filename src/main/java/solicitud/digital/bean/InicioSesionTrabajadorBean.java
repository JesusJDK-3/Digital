/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.bean;

/**
 *
 * @author Luc-j
 */

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import solicitud.digital.dao.InicioSesionTrabajadorDAO;
@Named("InicioSesionTrabajadorBean")
@RequestScoped
public class InicioSesionTrabajadorBean {

    private String correo;
    private String contrasena;
    private int idRol;

    // Getters y Setters
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    /**
     * Método para manejar el inicio de sesión de los trabajadores.
     *
     * @return Página a redirigir después del inicio de sesión.
     */
    public String iniciarSesion() {
        InicioSesionTrabajadorDAO dao = new InicioSesionTrabajadorDAO();
        int idTrabajador = dao.validarCredenciales(correo, contrasena, idRol);

        if (idTrabajador != -1) {
            // Obtener nombre del trabajador
            String nombreTrabajador = dao.obtenerNombreTrabajadorPorId(idTrabajador);

            // Guardar datos en la sesión
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            session.setAttribute("idTrabajador", idTrabajador);
            session.setAttribute("nombreTrabajador", nombreTrabajador);
            session.setAttribute("rolTrabajador", idRol);

            // Redirigir según el rol
            switch (idRol) {
                case 2:
                    return "MenuColaborador.xhtml?faces-redirect=true";
                case 3:
                    return "MenuCoordinador.xhtml?faces-redirect=true";
                case 4:
                    return "MenuAdministrador.xhtml?faces-redirect=true";
                default:
                    return null;
            }
        } else {
            // Mostrar un mensaje de error
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Credenciales incorrectas o rol inválido."));
            return null;
        }
    }
}
