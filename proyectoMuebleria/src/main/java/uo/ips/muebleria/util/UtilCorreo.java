package uo.ips.muebleria.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import uo.ips.muebleria.logic.cliente.ClienteConsultas;
import uo.ips.muebleria.logic.producto.Producto;
import uo.ips.muebleria.logic.venta.HistorialVentas;
import uo.ips.muebleria.logic.venta.Venta;

public class UtilCorreo {
	
    public static void enviarCorreoEnEntrega(Venta venta) {
        // Dirección de correo electrónico del destinatario
        String destinatario = ClienteConsultas.findByDni(venta.getDniCliente()).getCorreo();

        // Dirección de correo electrónico del remitente
        String remitente = "ipsmuebleria2020@gmail.com";

        // El correo electrónico se envía a través de localhost
        String host = "smtp.gmail.com";

        // Configuración del servidor de correo
        Properties propiedades = System.getProperties();
        propiedades.setProperty("mail.smtp.host", host);
        propiedades.put("mail.smtp.port", "465");
        propiedades.put("mail.smtp.ssl.enable", "true");
        propiedades.put("mail.smtp.auth", "true");

        // Se obtiene la sesión y se autentica
        Session sesion = Session.getInstance(propiedades, new javax.mail.Authenticator() {
        	protected PasswordAuthentication getPasswordAuthentication() {
        		return new PasswordAuthentication(
        				"ipsmuebleria2020@gmail.com", "JMuebleria2020");
        	}
        });
        
        try {
            // Creación de un objeto MimeMessage
            MimeMessage mensaje = new MimeMessage(sesion);

            // Se establece remitente y destinatario
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            
            // Se establece el asunto del correo
            mensaje.setSubject("[Mueblería] Su compra " + venta.getCodigoVenta() + " se encuentra en entrega");

            // Cuerpo del correo electrónico
            StringBuilder cuerpo = new StringBuilder(String.format("¡Hola, %s!\n\n",
                ClienteConsultas.findByDni(venta.getDniCliente()).getNombre()));

            cuerpo.append("Desde la mueblería le informamos de que su compra " + venta.getCodigoVenta() + " se encuentra en entrega.\n");
            cuerpo.append("\nLe recordamos los detalles de su compra:\n\n");

            // Lista de productos
            List<Producto> productos = HistorialVentas.getProductosVenta(venta.getCodigoVenta());
            
            for (Producto p : productos) {
                cuerpo.append("- " + p + "\n");
            }

            if (venta.getImporteVentaMasMontaje() != venta.getImporteVenta()) {
            	cuerpo.append("\nImporte total (sin montaje): " + venta.getImporteVenta() + "€");
            	cuerpo.append("\nImporte total (con montaje): " + venta.getImporteVentaMasMontaje() + "€");
            } else {
            	cuerpo.append("\nImporte total: " + venta.getImporteVenta() + "€");
            }
            
            if (venta.getFechaEntregaDomicilio() != null && venta.getFechaEntregaDomicilio().length() != 0) {
            	cuerpo.append("\n\nFecha estimada de entrega: " + venta.getFechaEntregaDomicilio());
            }
            
            cuerpo.append("\n\nReciba un cordial saludo,\n\nMueblería");

            mensaje.setText(cuerpo.toString());

            // Se envía el mensaje
            Transport.send(mensaje);
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }

}