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

import jakarta.faces.context.FacesContext;

import jakarta.servlet.http.HttpSession;

@Named("menuCoordinadorBean")
@ViewScoped
public class MenuCoordinadorBean implements Serializable {

    private String paginaActual = "asignarSolicitud.xhtml"; // Página inicial del panel dinámico
    private String paginaActualMain = "menuSidebarCoordinador.xhtml"; // Página principal del menú
    private String nombreCoordinador; // Almacena el nombre del coordinador

    public MenuCoordinadorBean() {
        cargarNombreCoordinador();
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

    public String getNombreCoordinador() {
        return nombreCoordinador;
    }

    /**
     * Carga el nombre del coordinador desde la sesión.
     */
    private void cargarNombreCoordinador() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        if (session != null) {
            nombreCoordinador = (String) session.getAttribute("nombreTrabajador");
        }
    }
}

