/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class Notificacion {
    private int id;
    private String mensaje;
    private String fecha; // Fecha de creación (ISO 8601)
    private int idUsuario; // Relación con la tabla Usuario
    private boolean leido; // Si la notificación ha sido leída
}

