/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import solicitud.digital.Conexion.Conexion;

public class InicioSesionClienteDAO {

    /**
     * Método para obtener el ID del cliente a partir de sus credenciales.
     *
     * @param correo El correo del cliente.
     * @param contrasena La contraseña del cliente.
     * @return El ID del cliente si las credenciales son correctas, -1 en caso contrario.
     */
    public int obtenerIdClientePorCredenciales(String correo, String contrasena) {
        String sql = "SELECT id FROM usuario WHERE correo = ? AND contrasena = ?";
        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error al validar las credenciales: " + e.getMessage());
        }
        return -1; // Credenciales incorrectas
    }

    /**
     * Método para obtener el nombre del cliente a partir de su ID.
     *
     * @param idCliente El ID del cliente.
     * @return El nombre del cliente, o null si no se encuentra.
     */
    public String obtenerNombreClientePorId(int idCliente) {
        String sql = "SELECT nombre FROM usuario WHERE id = ?";
        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nombre");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el nombre del cliente: " + e.getMessage());
        }
        return null;
    }
}
