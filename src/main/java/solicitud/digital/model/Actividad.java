/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class Actividad {
    private int id;
    private int idSolicitud; // Relación con la tabla Solicitud
    private int idUsuario; // Usuario que realizó la actividad
    private int idTipoActividad; // Relación con la tabla TipoActividad
    private String descripcion;
    private String fecha; // Fecha y hora de la actividad (ISO 8601)
}
