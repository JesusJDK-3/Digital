/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class EstadoSolicitud {
    private int id;
    private String nombre; // Ejemplo: activo, en proceso, pendiente en revisión, finalizada
}
