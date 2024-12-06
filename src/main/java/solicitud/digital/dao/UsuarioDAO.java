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
import java.util.ArrayList;
import java.util.List;
import solicitud.digital.Conexion.Conexion;
import solicitud.digital.model.Usuario;

public class UsuarioDAO {

    public void registrar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, apellido, correo, dni, contrasena, id_rol, estado, created_at) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getDni());
            ps.setString(5, usuario.getContrasena());
            ps.setInt(6, usuario.getIdRol());
            ps.setBoolean(7, usuario.isEstado());
            ps.executeUpdate();
        }
    }
    
    public int obtenerIdUsuarioPorCorreo(String correo) throws SQLException {
    String sql = "SELECT id FROM usuario WHERE correo = ?";
    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, correo);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
    }
    throw new SQLException("No se encontró un usuario con el correo especificado.");
}
    
    
         // Método para obtener un usuario por su ID
    public Usuario obtenerUsuarioPorId(int idUsuario) {
    String sql = "SELECT id, nombre, apellido, correo, dni, contrasena FROM usuario WHERE id = ?";
    Usuario usuario = null;

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idUsuario);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setApellido(rs.getString("apellido"));
            usuario.setCorreo(rs.getString("correo"));
            usuario.setDni(rs.getString("dni"));
            usuario.setContrasena(rs.getString("contrasena"));
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener los datos del usuario: " + e.getMessage());
    }

    return usuario;
}


    // Método para actualizar los datos personales del usuario
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuario SET nombre = ?, apellido = ?, correo = ?, dni = ?, contrasena = ? WHERE id = ?";
        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getDni());
            ps.setString(5, usuario.getContrasena());
            ps.setInt(6, usuario.getId());
            ps.executeUpdate();
            return true; // Actualización exitosa
        } catch (SQLException e) {
            System.err.println("Error al actualizar los datos del usuario: " + e.getMessage());
            return false; // Error al actualizar
        }
    }
    
   public List<Usuario> obtenerColaboradoresPorEspecializacion(String especializacion) {
    List<Usuario> colaboradores = new ArrayList<>();
    String sql = """
        SELECT id, nombre, especializacion 
        FROM usuario 
        WHERE id_rol = 2 -- Solo colaboradores
          AND estado = true -- Activos
          AND LOWER(especializacion) = LOWER(?)
    """;

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {

        ps.setString(1, especializacion);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario colaborador = new Usuario();
                colaborador.setId(rs.getInt("id"));
                colaborador.setNombre(rs.getString("nombre"));
                colaborador.setEspecializacion(rs.getString("especializacion"));
                colaboradores.add(colaborador);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener colaboradores por especialización: " + e.getMessage());
    }

    return colaboradores;
}



}

