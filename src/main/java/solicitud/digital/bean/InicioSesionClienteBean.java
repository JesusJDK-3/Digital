/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import solicitud.digital.dao.InicioSesionClienteDAO;

@Named("InicioSesionClienteBean")
@RequestScoped
public class InicioSesionClienteBean {

    private String correo; 
    private String contrasena;

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

   
    public String iniciarSesion() {
        InicioSesionClienteDAO dao = new InicioSesionClienteDAO();
        int idCliente = dao.obtenerIdClientePorCredenciales(correo, contrasena); // Usar correo en lugar de nombre

        if (idCliente != -1) {
            // Guardar el ID del cliente en la sesión
            FacesContext facesContext = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            session.setAttribute("idCliente", idCliente);
            session.setAttribute("nombreCliente", dao.obtenerNombreClientePorId(idCliente));
            
            
            return "MenuCliente.xhtml?faces-redirect=true";
        } else {
            // Mostrar un mensaje de error
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Error",
                    "Credenciales incorrectas. Por favor, intente nuevamente."
                ));

            return null; 
        }
    }
}
