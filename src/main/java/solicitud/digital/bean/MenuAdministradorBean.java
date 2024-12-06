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

@Named("menuAdministradorBean")
@ViewScoped
public class MenuAdministradorBean implements Serializable {

    private String paginaActual = "crearUsuarios.xhtml"; // Página inicial del panel dinámico
    private String paginaActualMain = "menuSidebarAdministrador.xhtml"; // Página principal del menú
    private String nombreAdministrador; // Almacena el nombre del administrador

    public MenuAdministradorBean() {
        cargarNombreAdministrador();
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

    public String getNombreAdministrador() {
        return nombreAdministrador;
    }

    /**
     * Carga el nombre del administrador desde la sesión.
     */
    private void cargarNombreAdministrador() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            nombreAdministrador = (String) session.getAttribute("nombreTrabajador");
        }
    }
}
