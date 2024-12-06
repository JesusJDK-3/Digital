/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package solicitud.digital.bean;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.sql.SQLException;
import lombok.Getter;
import lombok.Setter;
import solicitud.digital.model.Usuario;
import solicitud.digital.dao.UsuarioDAO;
import solicitud.digital.service.EmailService;

@Named
@RequestScoped
public class CrearCuentaClienteBean {

    @Getter @Setter
    private Usuario usuario = new Usuario(); 

    @Inject
    private UsuarioDAO usuarioDAO; 

    @Inject
    private EmailService emailService; 

    public String registrar() {
        try {
            
            usuario.setIdRol(1); 
            usuario.setEstado(true); 

            
            usuarioDAO.registrar(usuario);

            int idUsuario = usuarioDAO.obtenerIdUsuarioPorCorreo(usuario.getCorreo());
            // Enviar correo de bienvenida
            emailService.enviarCorreoRegistro(
                usuario.getCorreo(), 
                usuario.getNombre(),
                idUsuario
            );

            
            FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, 
                                                           "Registro exitoso", 
                                                           "El usuario ha sido registrado exitosamente."));

            
            return null;
        } catch (SQLException e) {
            // Manejo de excepciones
            System.err.println("Error al registrar al cliente: " + e.getMessage());
            FacesContext.getCurrentInstance()
                        .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                                           "Error", 
                                                           "No se pudo registrar el usuario. Intente nuevamente."));
            return null; 
        }
    }
}
