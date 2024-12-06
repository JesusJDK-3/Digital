/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package solicitud.digital.service;

/**
 *
 * @author Luc-j
 */
import jakarta.annotation.Resource;
import jakarta.mail.Session;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Properties;
import solicitud.digital.dao.NotificacionDAO;
import solicitud.digital.model.Notificacion;


public class EmailService implements Serializable{

    @Resource(lookup = "mail/digital123") 
    private Session mailSession;

    // Método para enviar correo de registro 
    public void enviarCorreoRegistro(String destinatario, String nombreUsuario, int idUsuario) {
    try {
        //  STARTTLS
        Properties propiedades = mailSession.getProperties();
        propiedades.put("mail.smtp.starttls.enable", "true"); // Habilita STARTTLS
        propiedades.put("mail.smtp.auth", "true");

        
        MimeMessage mensaje = new MimeMessage(mailSession);
        mensaje.setSubject("Bienvenido a Digital123");
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setFrom(new InternetAddress("soporte@digital123.com")); 

        // Contenido del correo
        String contenido = "Hola " + nombreUsuario + ",\n\n"
                + "¡Gracias por registrarte en Digital123! Estamos encantados de tenerte con nosotros.\n\n"
                + "Saludos,\nEquipo Digital123";
        mensaje.setText(contenido);

        
        Transport.send(mensaje);

        System.out.println("Correo enviado exitosamente a " + destinatario);

        
        Notificacion notificacion = new Notificacion();
        notificacion.setMensaje("Bienvenido a Digital123, " + nombreUsuario + "! Tu registro fue exitoso.");
        notificacion.setIdUsuario(idUsuario); 
        notificacion.setLeido(false); 

        
        NotificacionDAO notificacionDAO = new NotificacionDAO();
        notificacionDAO.guardarNotificacion(notificacion);

        System.out.println("Notificación guardada exitosamente en la base de datos.");
    } catch (MessagingException e) {
        System.err.println("Error al enviar correo: " + e.getMessage());
    }
}


    public void enviarCorreoNotificacionEdicion(String destinatario, int solicitudId) {
    try {
        
        Properties propiedades = mailSession.getProperties();
        propiedades.put("mail.smtp.starttls.enable", "true"); 
        propiedades.put("mail.smtp.auth", "true"); 

        
        MimeMessage mensaje = new MimeMessage(mailSession);
        mensaje.setSubject("Notificación de Digital123 - Solicitud Editada");
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setFrom(new InternetAddress("soporte@digital123.com")); 

        
        String mensajeNotificacion = "La solicitud con ID " + solicitudId + " ha sido editada.";
        mensaje.setText(mensajeNotificacion);

        
        Transport.send(mensaje);

        System.out.println("Correo de notificación enviado exitosamente a " + destinatario);
    } catch (MessagingException e) {
        
        System.err.println("Error al enviar correo de notificación de edición: " + e.getMessage());
    }
}

    
    public void enviarCorreoNotificacionEliminacion(String destinatario, int solicitudId) {
    try {
        
        Properties propiedades = mailSession.getProperties();
        propiedades.put("mail.smtp.starttls.enable", "true"); 
        propiedades.put("mail.smtp.auth", "true"); 

        
        MimeMessage mensaje = new MimeMessage(mailSession);
        mensaje.setSubject("Notificación de Digital123 - Solicitud Eliminada");
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setFrom(new InternetAddress("soporte@digital123.com")); 

        
        String mensajeNotificacion = "La solicitud con ID " + solicitudId + " ha sido eliminada.";
        mensaje.setText(mensajeNotificacion);

        
        Transport.send(mensaje);

        System.out.println("Correo de notificación enviado exitosamente a " + destinatario);
    } catch (MessagingException e) {
       
        System.err.println("Error al enviar correo de notificación de eliminación: " + e.getMessage());
    }
}

}
