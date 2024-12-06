/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.bean;

/**
 *
 * @author Luc-j
 */

import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;
import jakarta.servlet.http.HttpSession;

@Named("menuBean")
@ViewScoped
public class MenuBean implements Serializable {

    private String paginaActual = "registrarSolicitud.xhtml"; // Contenido dinámico del panel
    private String paginaActualMain = "menuMain.xhtml";       // Contenido dinámico completo
    private String nombreCliente; // Almacena el nombre del cliente

    public MenuBean() {
        cargarNombreCliente();
    }

    public String getPaginaActual() {
        return paginaActual;
    }

    public void cargarPagina(String pagina) {
        this.paginaActual = pagina + ".xhtml";
    }

    public String getPaginaActualMain() {
        return paginaActualMain;
    }

    public void cargarPaginaMain(String pagina) {
        this.paginaActualMain = pagina + ".xhtml";
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Carga el nombre del cliente desde la sesión.
     */
    private void cargarNombreCliente() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            nombreCliente = (String) session.getAttribute("nombreCliente");
        }
    }
}
