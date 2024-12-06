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
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import solicitud.digital.dao.SolicitudDAO;
import solicitud.digital.dao.UsuarioDAO;
import solicitud.digital.model.Solicitud;
import solicitud.digital.model.Usuario;

@Named("asignarSolicitudBean")
@SessionScoped
public class AsignarSolicitudBean implements Serializable {

    private List<Solicitud> solicitudes; // Lista de solicitudes activas echo
    private List<Usuario> colaboradores; // Lista de colaboradores disponibles echo
    private int idSolicitud; // ID de la solicitud a asignar  echo
    private List<Solicitud> solicitudesFiltradas; // Lista de solicitudes filtradas echo

    private final SolicitudDAO solicitudDAO = new SolicitudDAO(); 
    private final UsuarioDAO usuarioDAO = new UsuarioDAO(); 

    @PostConstruct
public void init() {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

    if (session != null && session.getAttribute("idTrabajador") != null) {
        int idCoordinador = (int) session.getAttribute("idTrabajador");

        // El mismo sistema asigna la solictud "Asignar Solicitudes"
        solicitudes = solicitudDAO.obtenerSolicitudesActivasPorCoordinador(idCoordinador);

        // Cargar solicitudes filtradas (en proceso o pendientes de revisión) para "Seguimiento de Solicitudes"
        solicitudesFiltradas = solicitudDAO.obtenerSolicitudesFiltradas();
    } else {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se encontró el ID del coordinador en la sesión."));
        
        // Inicializar listas vacías si no hay sesión
        solicitudes = new ArrayList<>();
        solicitudesFiltradas = new ArrayList<>();
    }

    colaboradores = new ArrayList<>(); 
}



    public AsignarSolicitudBean() {
        cargarSolicitudes(); 
        
    }

    /**
     * Cargo las solicitudes activas asignadas al coordinador actual
     */
    private void cargarSolicitudes() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);

        if (session != null && session.getAttribute("idTrabajador") != null) {
            int idCoordinador = (int) session.getAttribute("idTrabajador"); // Obtener el ID del coordinador desde la sesión
            solicitudes = solicitudDAO.obtenerSolicitudesActivasPorCoordinador(idCoordinador);
        } else {
            solicitudes = new ArrayList<>(); // Si no hay sesión, inicializar con lista vacía
        }
    }

    /**
     * Prepara la asignación de un colaborador para una solicitud específica.
     *
     * @param idSolicitud El ID de la solicitud a asignar.
     * @return Redirección a la página de asignación.
     */
    public String prepararAsignacion(int idSolicitud) {
        this.idSolicitud = idSolicitud;
        Solicitud solicitud = solicitudDAO.obtenerSolicitudPorId(idSolicitud);

        if (solicitud != null) {
            String especializacion = determinarEspecializacion(solicitud);
            System.out.println("Especialización requerida: " + especializacion);

            colaboradores = usuarioDAO.obtenerColaboradoresPorEspecializacion(especializacion);
            System.out.println("Colaboradores encontrados: " + colaboradores.size());

            for (Usuario colaborador : colaboradores) {
                System.out.println("Colaborador: " + colaborador.getNombre() + " - " + colaborador.getEspecializacion());
            }
        } else {
            System.out.println("Solicitud no encontrada: " + idSolicitud);
        }

        return "AsignarColaborador.xhtml?id=" + idSolicitud + "&faces-redirect=true";
    }

    /**
     * Asigna un colaborador a una solicitud específica.
     *
     * @param idColaborador El ID del colaborador a asignar.
     * @return Redirección a la página principal después de la asignación.
     */
    public String asignarColaborador(int idColaborador) {
        // Verificar si la solicitud ya tiene un colaborador asignado
        if (solicitudDAO.solicitudYaAsignada(idSolicitud)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "Esta solicitud ya tiene un colaborador asignado."));
            return null; // No hacer nada si ya está asignada
        }

        // Si no está asignada, realizar la asignación
        if (solicitudDAO.asignarColaborador(idSolicitud, idColaborador)) {
            // Cambiar el estado de la solicitud a "en proceso"
            boolean estadoCambiado = solicitudDAO.actualizarEstadoSolicitud(idSolicitud, "en proceso");

            if (estadoCambiado) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Colaborador asignado y estado actualizado correctamente."));
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Colaborador asignado, pero no se pudo actualizar el estado."));
            }

            return "vistaPrincipal.xhtml?faces-redirect=true"; // Redirige después de la asignación
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo asignar el colaborador."));
            return null; // Si ocurre un error
        }
    }

    /**
     * Determina la especialización requerida según el tipo de solicitud.
     *
     * @param solicitud La solicitud para la cual se requiere un colaborador.
     * @return La especialización requerida.
     */
    private String determinarEspecializacion(Solicitud solicitud) {
        // Lógica para determinar la especialización según el tipo de solicitud
        return switch (solicitud.getTipo().toLowerCase()) {
            case "error" -> "programador";
            case "capacitacion" -> "analista";
            case "requerimiento" -> "diseñador";
            default -> null;
        };
    }

    // Getters y Setters
    public List<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(List<Solicitud> solicitudes) {
        this.solicitudes = solicitudes;
    }

    public List<Usuario> getColaboradores() {
        System.out.println("Colaboradores en getColaboradores(): " + (colaboradores != null ? colaboradores.size() : 0));
        return colaboradores;
    }

    public void setColaboradores(List<Usuario> colaboradores) {
        this.colaboradores = colaboradores;
    }

    public List<Solicitud> getSolicitudesFiltradas() {
        return solicitudesFiltradas;
    }

    public void setSolicitudesFiltradas(List<Solicitud> solicitudesFiltradas) {
        this.solicitudesFiltradas = solicitudesFiltradas;
    }
}
