/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.model;

import lombok.Data;

@Data
public class Solicitud {
    private int id; // ID de la solicitud
    private String descripcion; // Descripción de la solicitud
    private String tipo; // Ejemplo: error, capacitación, requerimiento
    private String fechaCreacion; // Fecha de creación (ISO 8601)
    private int idCliente; // Relación con la tabla Usuario (cliente)
    private int idEstado; // Relación con la tabla EstadoSolicitud

    // Campo adicional para manejar el estado en formato textual
    private String estado;

    // Campos adicionales para visualización
    private String nombreCliente; // Nombre del cliente asociado a la solicitud
    private String colaboradorAsignado; // Nombre del colaborador asignado (si corresponde)

    // Agregado: Colaborador asociado a la solicitud
    private Usuario colaborador;

    /**
     * Método para establecer el nombre del estado y ajustar el idEstado correspondiente.
     * Si el nombre del estado no es reconocido, idEstado se establece en 0.
     *
     * @param estado Nombre textual del estado (Ej: "activo", "en proceso")
     */
    public void setEstado(String estado) {
        this.estado = estado;

        // Asignar el idEstado basado en el nombre del estado
        switch (estado.toLowerCase()) {
            case "activo":
                this.idEstado = 1;
                break;
            case "en proceso":
                this.idEstado = 2;
                break;
            case "pendiente en revisión":
                this.idEstado = 3;
                break;
            case "finalizada":
                this.idEstado = 4;
                break;
            default:
                this.idEstado = 0; // Valor por defecto si no se reconoce el estado
                break;
        }
    }

    /**
     * Método para obtener el nombre del estado a partir de idEstado.
     * @return Nombre del estado
     */
    public String getEstado() {
        return estado;
    }
}
