/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.bean;

/**
 *
 * @author Luc-j
 */

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import jakarta.servlet.http.HttpSession;
import solicitud.digital.dao.UsuarioDAO;
import solicitud.digital.model.Usuario;

@Named("usuarioBean")
@ViewScoped
public class UsuarioBean implements Serializable {

    private Usuario usuario = new Usuario();

    // Inicializar el usuario cargando los datos desde la sesión
    public void cargarDatosUsuario() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        // Obtener el id del usuario desde la sesión
        int idUsuario = (int) session.getAttribute("idCliente");

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuario = usuarioDAO.obtenerUsuarioPorId(idUsuario);
    }

    // Método para guardar los datos actualizados del usuario
    public String guardarDatos() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        boolean exito = usuarioDAO.actualizarUsuario(usuario);

        if (exito) {
            // Mostrar mensaje de éxito
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                                 "Éxito", 
                                 "Tus datos han sido actualizados correctamente."));
            return null; // Permanecer en la misma página
        } else {
            // Mostrar mensaje de error
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                 "Error", 
                                 "Hubo un problema al actualizar tus datos. Intenta nuevamente."));
            return null; // Permanecer en la misma página
        }
    }

    // Getters y Setters
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
