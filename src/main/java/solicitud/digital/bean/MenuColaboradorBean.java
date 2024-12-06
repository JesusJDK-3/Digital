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

@Named("menuColaboradorBean")
@ViewScoped
public class MenuColaboradorBean implements Serializable {

    private String paginaActual = "solicitudesActivas.xhtml"; // Página inicial del panel dinámico
    private String paginaActualMain = "menuSidebarColaborador.xhtml"; // Página principal del menú
    private String nombreColaborador; // Almacena el nombre del colaborador

    public MenuColaboradorBean() {
        cargarNombreColaborador();
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

    public String getNombreColaborador() {
        return nombreColaborador;
    }

    /**
     * Carga el nombre del colaborador desde la sesión.
     */
    private void cargarNombreColaborador() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            nombreColaborador = (String) session.getAttribute("nombreTrabajador");
        }
    }
}
