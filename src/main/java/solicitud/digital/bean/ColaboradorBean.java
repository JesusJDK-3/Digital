/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.bean;

/**
 *
 * @author Luc-j
 */
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import solicitud.digital.dao.SolicitudDAO;
import solicitud.digital.model.Solicitud;

@Named
@ViewScoped
public class ColaboradorBean implements Serializable {
    private List<Solicitud> solicitudesActivas;
    private int idColaborador; // ID del colaborador, obtenido desde la sesión.

    @PostConstruct
public void init() {
    idColaborador = obtenerIdColaboradorDesdeSesion(); // Obtener el ID desde la sesión
    if (idColaborador > 0) {
        SolicitudDAO solicitudDAO = new SolicitudDAO(); // Crear instancia
        solicitudesActivas = solicitudDAO.obtenerSolicitudesActivasPorColaborador(idColaborador);
    } else {
        solicitudesActivas = new ArrayList<>(); // Evitar errores si no hay sesión válida
    }
}


    // Método para ver detalle de una solicitud
    public String verDetalle(int idSolicitud) {
        // Redirigir a la vista de detalles pasando el ID de la solicitud
        return "detalleSolicitud.xhtml?idSolicitud=" + idSolicitud + "&faces-redirect=true";
    }

    // Getter para solicitudes activas
    public List<Solicitud> getSolicitudesActivas() {
        return solicitudesActivas;
    }

    // Método para obtener el ID del colaborador desde la sesión
    private int obtenerIdColaboradorDesdeSesion() {
        // Simulación: Obtener el ID desde la sesión
        // Cambia este código según tu implementación
        return (int) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("idTrabajador");
    }
}
