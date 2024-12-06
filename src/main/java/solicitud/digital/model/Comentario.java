/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class Comentario {
    private int id;
    private int idSolicitud; // Relación con la tabla Solicitud
    private int idUsuario; // Usuario que hizo el comentario
    private String mensaje;
    private String fecha; // Fecha y hora del comentario (ISO 8601)
}

