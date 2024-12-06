/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class HistorialSolicitud {
    private int id;
    private int idSolicitud; // Relación con la tabla Solicitud
    private String fechaInicio; // Fecha de inicio (ISO 8601)
    private String fechaFinal; // Fecha de finalización (ISO 8601)
    private int estadoFinal; // Relación con la tabla EstadoSolicitud
}
