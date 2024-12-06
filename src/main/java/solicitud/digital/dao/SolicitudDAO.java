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

import solicitud.digital.model.Solicitud;
import solicitud.digital.model.Usuario;

public class SolicitudDAO {

    
    
    // Método para actualizar el estado de la solicitud
    public boolean actualizarEstadoSolicitud(int idSolicitud, String nuevoEstado) {
        // Obtener el ID del estado "en proceso" (podemos usar el nombre directamente)
        int idNuevoEstado = obtenerIdEstadoPorNombre(nuevoEstado); // Obtener el ID de "en proceso"

        String sql = "UPDATE solicitud SET id_estado = ? WHERE id = ?";

        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idNuevoEstado);
            ps.setInt(2, idSolicitud);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Si la actualización es exitosa, devuelve true
        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado de la solicitud: " + e.getMessage());
            return false;
        }
    }
    
    // Método para obtener el ID del estado por nombre
    public int obtenerIdEstadoPorNombre(String nombreEstado) {
        String sql = "SELECT id FROM estado_solicitud WHERE nombre = ?";

        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nombreEstado); // Establecer el nombre del estado a buscar

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Devuelve el ID del estado
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el ID del estado: " + e.getMessage());
        }

        return -1; // Retorna -1 si no se encuentra el estado (esto es un error)
    }

    
        
    /**
     * Método para registrar una nueva solicitud en la base de datos.
     *
     * @param solicitud Objeto Solicitud que contiene los datos a registrar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    
    public boolean registrarSolicitud(Solicitud solicitud) {
    String sqlSolicitud = "INSERT INTO solicitud (descripcion, tipo, fecha_creacion, id_cliente, id_estado) "
                        + "VALUES (?, ?, NOW(), ?, ?)";
    String sqlAsignacion = "INSERT INTO asignacion_coordinador (id_solicitud, id_coordinador, fecha_asignacion) "
                         + "VALUES (?, ?, NOW())";

    try (Connection connection = new Conexion().getConnection()) {
        connection.setAutoCommit(false); // Inicia la transacción

        // Insertar la solicitud
        try (PreparedStatement psSolicitud = connection.prepareStatement(sqlSolicitud, PreparedStatement.RETURN_GENERATED_KEYS)) {
            psSolicitud.setString(1, solicitud.getDescripcion());
            psSolicitud.setString(2, solicitud.getTipo());
            psSolicitud.setInt(3, solicitud.getIdCliente());
            psSolicitud.setInt(4, solicitud.getIdEstado());
            psSolicitud.executeUpdate();

            // Obtener el ID de la solicitud recién creada
            try (ResultSet rs = psSolicitud.getGeneratedKeys()) {
                if (rs.next()) {
                    int idSolicitud = rs.getInt(1);

                    // Obtener el coordinador disponible
                    int idCoordinador = obtenerCoordinadorDisponible();

                    if (idCoordinador != -1) {
                        // Asignar la solicitud al coordinador
                        try (PreparedStatement psAsignacion = connection.prepareStatement(sqlAsignacion)) {
                            psAsignacion.setInt(1, idSolicitud);
                            psAsignacion.setInt(2, idCoordinador);
                            psAsignacion.executeUpdate();
                        }
                    } else {
                        throw new SQLException("No hay coordinadores disponibles.");
                    }
                } else {
                    throw new SQLException("Error al obtener el ID de la solicitud.");
                }
            }
        }

        connection.commit(); // Confirmar la transacción
        return true;

    } catch (SQLException e) {
        System.err.println("Error al registrar la solicitud con asignación: " + e.getMessage());
        return false;
    }
}

            public boolean editarSolicitud(Solicitud solicitud) {
            String sql = "UPDATE solicitud SET descripcion = ?, tipo = ?, id_estado = ?, fecha_actualizacion = NOW() WHERE id = ?";

            try (Connection connection = new Conexion().getConnection();
                 PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, solicitud.getDescripcion());
                ps.setString(2, solicitud.getTipo());
                ps.setInt(3, solicitud.getIdEstado());
                ps.setInt(4, solicitud.getId()); // ID de la solicitud a actualizar

                int rowsAffected = ps.executeUpdate();

                return rowsAffected > 0;
            } catch (SQLException e) {
                System.err.println("Error al editar la solicitud: " + e.getMessage());
                return false;
            }
        }

    
        public boolean eliminarSolicitud(int solicitudId) {
        String sql = "DELETE FROM solicitud WHERE id = ?";
        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // Establecer el ID de la solicitud que se va a eliminar
            ps.setInt(1, solicitudId);

            // Ejecutar la eliminación
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0; // Si se eliminó al menos una fila, la operación fue exitosa
        } catch (SQLException e) {
            System.err.println("Error al eliminar la solicitud: " + e.getMessage());
            return false; // Error al eliminar
        }
    }


    /**
     * Método para obtener todas las solicitudes activas (no finalizadas).
     *
     * @return Lista de solicitudes activas
     */
    public List<Solicitud> obtenerSolicitudesActivas() {
        List<Solicitud> solicitudes = new ArrayList<>();

        // Query para seleccionar solicitudes con estados diferentes a "finalizada"
        String sql = "SELECT s.id, s.descripcion, s.tipo, es.nombre AS estado, s.id_cliente " +
                     "FROM solicitud s " +
                     "JOIN estado_solicitud es ON s.id_estado = es.id " +
                     "WHERE es.nombre != 'finalizada'";

        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Iterar sobre el resultado y agregar solicitudes a la lista
            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setId(rs.getInt("id"));
                solicitud.setDescripcion(rs.getString("descripcion"));
                solicitud.setTipo(rs.getString("tipo"));
                solicitud.setEstado(rs.getString("estado")); // Nombre del estado textual
                solicitud.setIdCliente(rs.getInt("id_cliente"));

                solicitudes.add(solicitud);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener las solicitudes activas: " + e.getMessage());
        }

        return solicitudes;
    }
    
    
    public List<Solicitud> obtenerSolicitudesFinalizadas() {
    List<Solicitud> solicitudes = new ArrayList<>();

    // Query para seleccionar solicitudes con estado "finalizada"
    String sql = "SELECT s.id, s.descripcion, s.tipo, es.nombre AS estado, s.id_cliente " +
                 "FROM solicitud s " +
                 "JOIN estado_solicitud es ON s.id_estado = es.id " +
                 "WHERE es.nombre = 'finalizada'";

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        // Iterar sobre el resultado y agregar solicitudes a la lista
        while (rs.next()) {
            Solicitud solicitud = new Solicitud();
            solicitud.setId(rs.getInt("id"));
            solicitud.setDescripcion(rs.getString("descripcion"));
            solicitud.setTipo(rs.getString("tipo"));
            solicitud.setEstado(rs.getString("estado")); // Nombre del estado textual
            solicitud.setIdCliente(rs.getInt("id_cliente"));

            solicitudes.add(solicitud);
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener las solicitudes finalizadas: " + e.getMessage());
    }

    return solicitudes;
}

    
    
    public List<Solicitud> obtenerSolicitudesActivasPorCliente(int idCliente) {
    List<Solicitud> solicitudes = new ArrayList<>();

    // Query para seleccionar solicitudes activas del cliente específico
    String sql = "SELECT s.id, s.descripcion, s.tipo, es.nombre AS estado, s.id_cliente " +
                 "FROM solicitud s " +
                 "JOIN estado_solicitud es ON s.id_estado = es.id " +
                 "WHERE es.nombre != 'finalizada' AND s.id_cliente = ?";

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idCliente); // Asignar el idCliente a la consulta

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setId(rs.getInt("id"));
                solicitud.setDescripcion(rs.getString("descripcion"));
                solicitud.setTipo(rs.getString("tipo"));
                solicitud.setEstado(rs.getString("estado"));
                solicitud.setIdCliente(rs.getInt("id_cliente"));

                solicitudes.add(solicitud);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener las solicitudes activas para el cliente: " + e.getMessage());
    }

    return solicitudes;
}

    
    
    /**
 * Método para obtener una solicitud específica por su ID.
 *
 * @param solicitudId El ID de la solicitud a buscar.
 * @return Un objeto Solicitud si se encuentra, null en caso contrario.
 */
public Solicitud obtenerSolicitudPorId(int solicitudId) {
    Solicitud solicitud = null;

    // Consulta SQL para obtener una solicitud específica con su estado textual
    String sql = "SELECT s.id, s.descripcion, s.tipo, s.fecha_creacion, s.id_cliente, s.id_estado, es.nombre AS estado " +
                 "FROM solicitud s " +
                 "JOIN estado_solicitud es ON s.id_estado = es.id " +
                 "WHERE s.id = ?";

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, solicitudId); // Asignar el ID de la solicitud al parámetro

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                solicitud = new Solicitud();
                solicitud.setId(rs.getInt("id"));
                solicitud.setDescripcion(rs.getString("descripcion"));
                solicitud.setTipo(rs.getString("tipo"));
                solicitud.setFechaCreacion(rs.getString("fecha_creacion")); 
                solicitud.setIdCliente(rs.getInt("id_cliente"));
                solicitud.setIdEstado(rs.getInt("id_estado")); // Estado en formato numérico
                solicitud.setEstado(rs.getString("estado")); // Estado en formato textual
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener la solicitud por ID: " + e.getMessage());
    }

    return solicitud; 
}

// Método para obtener el correo del usuario por el ID de la solicitud
    public String obtenerCorreoPorSolicitudId(int solicitudId) {
        String sql = "SELECT u.correo FROM usuario u " +
                     "JOIN solicitud s ON u.id = s.id_cliente " +
                     "WHERE s.id = ?"; // Relacionamos la solicitud con el usuario
        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, solicitudId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("correo");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el correo del usuario para la solicitud: " + e.getMessage());
        }
        return null;
    }
    
    
   public List<Solicitud> obtenerSolicitudesActivasPorCoordinador(int idCoordinador) {
    List<Solicitud> solicitudes = new ArrayList<>();

    String sql = """
        SELECT s.id, s.descripcion, s.tipo, s.fecha_creacion, es.nombre AS estado,
               u.nombre AS nombreCliente
        FROM solicitud s
        JOIN estado_solicitud es ON s.id_estado = es.id
        JOIN usuario u ON s.id_cliente = u.id
        LEFT JOIN asignacion_coordinador ac ON s.id = ac.id_solicitud
        WHERE s.id_estado = 1 -- Estado activo
          AND ac.id_coordinador = ?
    """;

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idCoordinador);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setId(rs.getInt("id"));
                solicitud.setDescripcion(rs.getString("descripcion"));
                solicitud.setTipo(rs.getString("tipo"));
                solicitud.setFechaCreacion(rs.getString("fecha_creacion"));
                solicitud.setEstado(rs.getString("estado")); // Estado textual
                solicitud.setNombreCliente(rs.getString("nombreCliente")); // Nombre del cliente

                solicitudes.add(solicitud);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener solicitudes activas del coordinador: " + e.getMessage());
    }

    return solicitudes;
}

    public int obtenerCoordinadorDisponible() {
    String sql = """
        SELECT u.id
        FROM usuario u
        LEFT JOIN (
            SELECT a.id_colaborador AS id_coordinador, COUNT(*) AS solicitudesAsignadas
            FROM asignacion a
            JOIN solicitud s ON a.id_solicitud = s.id
            WHERE s.id_estado = 1 -- Estado activo
            GROUP BY a.id_colaborador
        ) AS t ON u.id = t.id_coordinador
        WHERE u.id_rol = 3 -- Rol de coordinador
          AND u.estado = true -- Coordinador activo
        ORDER BY COALESCE(t.solicitudesAsignadas, 0) ASC
        LIMIT 1
    """;

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt("id"); 
        }

    } catch (SQLException e) {
        System.err.println("Error al obtener un coordinador disponible: " + e.getMessage());
    }

    return -1; 
}

public List<Solicitud> obtenerSolicitudesPorCoordinador(int idCoordinador) {
    List<Solicitud> solicitudes = new ArrayList<>();

    // Modificada la consulta SQL para obtener el nombre del cliente desde la tabla usuario
    String sql = """
        SELECT s.id, s.descripcion, s.tipo, s.fecha_creacion, es.nombre AS estado, s.id_cliente, u.nombre AS nombre_cliente
        FROM solicitud s
        JOIN estado_solicitud es ON s.id_estado = es.id
        JOIN asignacion_coordinador ac ON s.id = ac.id_solicitud
        JOIN usuario u ON s.id_cliente = u.id  -- JOIN con la tabla usuario para obtener el nombre del cliente
        WHERE ac.id_coordinador = ?
    """;

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setInt(1, idCoordinador); 

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setId(rs.getInt("id"));
                solicitud.setDescripcion(rs.getString("descripcion"));
                solicitud.setTipo(rs.getString("tipo"));
                solicitud.setFechaCreacion(rs.getString("fecha_creacion"));
                solicitud.setEstado(rs.getString("estado")); // Estado textual
                solicitud.setIdCliente(rs.getInt("id_cliente"));
                solicitud.setNombreCliente(rs.getString("nombre_cliente")); 

                solicitudes.add(solicitud);
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener las solicitudes por coordinador: " + e.getMessage());
    }

    return solicitudes;
}



public boolean asignarColaborador(int idSolicitud, int idColaborador) {
    String sql = "INSERT INTO asignacion (id_solicitud, id_colaborador, fecha_asignacion) VALUES (?, ?, NOW())";

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {

        ps.setInt(1, idSolicitud);
        ps.setInt(2, idColaborador);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0; // Devuelve true si se realizó la inserción correctamente

    } catch (SQLException e) {
        System.err.println("Error al asignar colaborador: " + e.getMessage());
        return false;
    }
}

public boolean solicitudYaAsignada(int idSolicitud) {
    String sql = "SELECT COUNT(*) FROM asignacion WHERE id_solicitud = ?";

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql)) {
        
        ps.setInt(1, idSolicitud);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Si el conteo es mayor que 0, significa que ya está asignada
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al verificar la asignación: " + e.getMessage());
    }

    return false; // Si no se encuentra asignación
}


public List<Solicitud> obtenerSolicitudesAsignadas(int idCoordinador) {
        List<Solicitud> solicitudes = new ArrayList<>();
        String sql = "SELECT s.id, s.descripcion, s.tipo, es.nombre AS estado " +
                     "FROM solicitud s " +
                     "JOIN asignacion a ON s.id = a.id_solicitud " +
                     "JOIN estado_solicitud es ON s.id_estado = es.id " +
                     "WHERE a.id_coordinador = ?";  // Filtrar por el coordinador

        try (Connection connection = new Conexion().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, idCoordinador);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Solicitud solicitud = new Solicitud();
                    solicitud.setId(rs.getInt("id"));
                    solicitud.setDescripcion(rs.getString("descripcion"));
                    solicitud.setTipo(rs.getString("tipo"));
                    solicitud.setEstado(rs.getString("estado")); // Estado textual
                    solicitudes.add(solicitud);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las solicitudes asignadas: " + e.getMessage());
        }

        return solicitudes;
    }

public List<Solicitud> obtenerSolicitudesFiltradas() {
    List<Solicitud> solicitudes = new ArrayList<>();
    String sql = "SELECT s.id AS id_solicitud, s.tipo, es.nombre AS estado, " +
                 "u.nombre || ' ' || u.apellido AS colaborador " +
                 "FROM solicitud s " +
                 "JOIN asignacion a ON s.id = a.id_solicitud " +
                 "JOIN usuario u ON a.id_colaborador = u.id " +
                 "JOIN estado_solicitud es ON s.id_estado = es.id " +
                 "WHERE (es.nombre = 'en proceso' OR es.nombre = 'pendiente en revisión')";

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Solicitud solicitud = new Solicitud();
            solicitud.setId(rs.getInt("id_solicitud"));
            solicitud.setTipo(rs.getString("tipo"));
            solicitud.setEstado(rs.getString("estado"));

            // Crear y asignar el colaborador
            Usuario colaborador = new Usuario();
            colaborador.setNombre(rs.getString("colaborador"));
            solicitud.setColaborador(colaborador);

            solicitudes.add(solicitud); // Agregar la solicitud a la lista
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener las solicitudes filtradas: " + e.getMessage());
    }

    return solicitudes;
}

public List<Solicitud> obtenerSolicitudesActivasPorColaborador(int idColaborador) {
    List<Solicitud> solicitudes = new ArrayList<>();

    String query = """
        SELECT s.id, s.tipo, s.descripcion, e.nombre AS estado
        FROM solicitud s
        INNER JOIN asignacion a ON s.id = a.id_solicitud
        INNER JOIN estado_solicitud e ON s.id_estado = e.id
        WHERE a.id_colaborador = ? 
          AND (e.nombre = 'en proceso' OR e.nombre = 'pendiente en revisión')
    """;

    try (Connection connection = new Conexion().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, idColaborador);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Solicitud solicitud = new Solicitud();
                solicitud.setId(rs.getInt("id"));
                solicitud.setTipo(rs.getString("tipo"));
                solicitud.setDescripcion(rs.getString("descripcion"));
                solicitud.setEstado(rs.getString("estado")); // Nombre del estado textual
                solicitudes.add(solicitud);
            }
        }
    } catch (Exception e) {
        System.err.println("Error al obtener solicitudes activas por colaborador: " + e.getMessage());
    }

    return solicitudes;
}



}

