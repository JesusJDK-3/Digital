/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class Configuracion {
    private int id;
    private int maxSolicitudesCoordinador;
    private int maxSolicitudesColaborador;
    private String fechaActualizacion; // Fecha de última actualización (ISO 8601)
}
