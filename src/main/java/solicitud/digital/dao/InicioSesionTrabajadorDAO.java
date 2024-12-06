/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.dao;

/**
 *
 * @author Luc-j
 */


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import solicitud.digital.Conexion.Conexion;

public class InicioSesionTrabajadorDAO {

    /**
     * Método para validar las credenciales del trabajador.
     * 
     * @param correo El correo del trabajador.
     * @param contrasena La contraseña del trabajador.
     * @param idRol El ID del rol del trabajador.
     * @return El ID del trabajador si las credenciales son válidas, -1 en caso contrario.
     */
    public int validarCredenciales(String correo, String contrasena, int idRol) {
        String sql = "SELECT id FROM usuario WHERE correo = ? AND contrasena = ? AND id_rol = ?";
        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ps.setInt(3, idRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error al validar las credenciales: " + e.getMessage());
        }
        return -1; // Credenciales incorrectas
    }
    
    public String obtenerNombreTrabajadorPorId(int idTrabajador) {
    String sql = "SELECT nombre FROM usuario WHERE id = ?";
    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idTrabajador);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("nombre");
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener el nombre del trabajador: " + e.getMessage());
    }
    return null;
}

}
