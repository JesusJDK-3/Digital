/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class Asignacion {
    private int id;
    private int idSolicitud; // Relación con la tabla Solicitud
    private int idColaborador; // Relación con la tabla Usuario
    private String fechaAsignacion; // Fecha de asignación (ISO 8601)
}
