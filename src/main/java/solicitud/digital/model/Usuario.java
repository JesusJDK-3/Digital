/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class Usuario {
    private int id;
    private String nombre;
    private String apellido;
    private String correo;
    private String dni;
    private String contrasena;
    private int idRol; // Relación con la tabla Rol
    private String especializacion; // Solo para colaboradores
    private boolean estado; // Activo o inactivo
    private String createdAt; // Fecha de creación (ISO 8601: yyyy-MM-dd'T'HH:mm:ss)
}
