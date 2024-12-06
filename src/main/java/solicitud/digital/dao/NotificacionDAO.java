/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.dao;

/**
 *
 * @author Luc-j
 */


import solicitud.digital.model.Notificacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import solicitud.digital.Conexion.Conexion;

public class NotificacionDAO {

    public void guardarNotificacion(Notificacion notificacion) {
        String sql = "INSERT INTO notificacion (mensaje, fecha, id_usuario, leido) VALUES (?, NOW(), ?, ?)";

        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, notificacion.getMensaje());
            ps.setInt(2, notificacion.getIdUsuario());
            ps.setBoolean(3, notificacion.isLeido());

            ps.executeUpdate();
            System.out.println("Notificación guardada exitosamente en la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al guardar la notificación: " + e.getMessage());
        }
    }
}
