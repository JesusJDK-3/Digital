/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import solicitud.digital.dao.SolicitudDAO;
import solicitud.digital.model.Solicitud;
import solicitud.digital.service.EmailService;


@Named("solicitudBean")
@ViewScoped
public class SolicitudBean implements Serializable {

    private List<Solicitud> solicitudesActivas;
    
    private List<Solicitud> solicitudesFinalizadas;
    private final SolicitudDAO solicitudDAO = new SolicitudDAO();
    private Solicitud solicitud = new Solicitud();

    @Inject
    private MenuBean menuBean; // Inyección del MenuBean para navegación dinámica
    
    @Inject
    private EmailService emailService; // Inyección del servicio de correo
    
    
    
    // Constructor o inicializador
    public SolicitudBean() {
        cargarSolicitudesActivas();
    }

    // Método para cargar las solicitudes activas del cliente en sesión
    private void cargarSolicitudesActivas() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        int idCliente = (int) session.getAttribute("idCliente"); // Obtener el idCliente de la sesión

        // Cargar solo las solicitudes activas del cliente actual
        solicitudesActivas = solicitudDAO.obtenerSolicitudesActivasPorCliente(idCliente);
    }

    
    // Método para registrar una nueva solicitud
    public String registrarSolicitud() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
    int idCliente = (int) session.getAttribute("idCliente");

    solicitud.setIdCliente(idCliente);
    // Obtener el ID del estado "activo"
    int idEstadoActivo = solicitudDAO.obtenerIdEstadoPorNombre("activo");
    solicitud.setIdEstado(idEstadoActivo); 

    boolean exito = solicitudDAO.registrarSolicitud(solicitud);

    if (exito) {
        limpiarFormulario();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("MenuCliente.xhtml");
        } catch (IOException e) {
        }
    }
    return null;
}
    
     // Método para editar una solicitud
   /* public String editarSolicitud(Solicitud solicitud) {
        boolean exito = solicitudDAO.editarSolicitud(solicitud);
        if (exito) {
            // Obtener el correo del usuario desde la solicitud usando el DAO
            String correoUsuario = solicitudDAO.obtenerCorreoPorSolicitudId(solicitud.getId());

            // Verificar si el correo no es null o vacío
            if (correoUsuario != null && !correoUsuario.isEmpty()) {
                // Enviar correo de notificación de edición
                emailService.enviarCorreoNotificacionEdicion(correoUsuario, solicitud.getId());
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "La solicitud fue editada correctamente", "exito"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Hubo un problema al editar la solicitud. Intenta nuevamente.", "Error"));
        }
        return null;
    }*/




    // Método para editar una solicitud
    public String editarSolicitud(Solicitud solicitud) {
        boolean exito = solicitudDAO.editarSolicitud(solicitud);
        if (exito) {
            // Obtener el correo del usuario desde la solicitud usando el DAO
            String correoUsuario = solicitudDAO.obtenerCorreoPorSolicitudId(solicitud.getId());

            // Verificar si el correo no es null o vacío
            if (correoUsuario != null && !correoUsuario.isEmpty()) {
                // Enviar correo de notificación de edición
                emailService.enviarCorreoNotificacionEdicion(correoUsuario, solicitud.getId());
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "La solicitud fue editada correctamente", "exito"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Hubo un problema al editar la solicitud. Intenta nuevamente.", "Error"));
        }
        return null;
    }


public String eliminarSolicitud(int solicitudId) {
    // Primero, obtener el correo del usuario asociado a la solicitud
    String correoUsuario = solicitudDAO.obtenerCorreoPorSolicitudId(solicitudId);

    // Verificar si se encontró el correo antes de proceder
    if (correoUsuario == null || correoUsuario.isEmpty()) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                "No se encontró un correo asociado a la solicitud. No se enviará notificación.", "Advertencia"));
        return null; // Termina aquí si no hay correo
    }

    // Luego, eliminar la solicitud
    boolean exito = solicitudDAO.eliminarSolicitud(solicitudId);

    if (exito) {
        // Recargar las solicitudes activas después de eliminar
        cargarSolicitudesActivas();

        // Enviar correo de notificación de eliminación
        emailService.enviarCorreoNotificacionEliminacion(correoUsuario, solicitudId);

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "La solicitud fue eliminada correctamente", "Éxito"));
    } else {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "Hubo un problema al eliminar la solicitud. Intenta nuevamente.", "Error"));
    }
    return null;
}



    // Método para ver el detalle de una solicitud
    public String verDetalle(int solicitudId) {
        Solicitud solicitudSeleccionada = solicitudDAO.obtenerSolicitudPorId(solicitudId);
        if (solicitudSeleccionada != null) {
            this.solicitud = solicitudSeleccionada;
            menuBean.cargarPaginaMain("DetalleSolicitud");
            FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("mainContentPanel");
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "La solicitud seleccionada no existe o no se pudo cargar.", "Advertencia"));
        }
        return null;
    }

    // Método para limpiar el formulario de solicitud
    private void limpiarFormulario() {
        solicitud = new Solicitud();
    }

    // Getters y Setters
    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public List<Solicitud> getSolicitudesActivas() {
        return solicitudesActivas;
    }

    public void setSolicitudesActivas(List<Solicitud> solicitudesActivas) {
        this.solicitudesActivas = solicitudesActivas;
    }
    

    public void cargarSolicitudesFinalizadas() {
        solicitudesFinalizadas = solicitudDAO.obtenerSolicitudesFinalizadas();
    }

    public List<Solicitud> getSolicitudesFinalizadas() {
        return solicitudesFinalizadas;
    }

}
