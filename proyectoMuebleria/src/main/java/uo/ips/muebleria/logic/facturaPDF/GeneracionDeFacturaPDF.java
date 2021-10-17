package uo.ips.muebleria.logic.facturaPDF;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.cliente.Cliente;
import uo.ips.muebleria.logic.producto.ProductoVenta;
import uo.ips.muebleria.logic.venta.Venta;

public class GeneracionDeFacturaPDF {

	private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.NORMAL);
	private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);


	private Cliente clienteVenta;
	private Venta venta;
	private List<ProductoVenta> productosVenta;

	private static final String imagenLogo = "img/logoMuebleria.PNG";
	
	public void generaFacturaPDF(String codigoVenta, String dniCliente) {
		Date date = new Date();
		DateFormat fecha = new SimpleDateFormat("dd-MM-yyyy");
		GeneracionDeFacturaPDF facturaPDF = new GeneracionDeFacturaPDF();
		
		facturaPDF.createPDF(new File("facturasPDF/Factura de venta " + codigoVenta + " - " + fecha.format(date) + ".pdf"), codigoVenta, dniCliente, fecha.format(date));

	}

	private void createPDF(File pdfNewFile, String codigoVenta, String dniCliente, String fecha) {
		recuperarDatosCliente(dniCliente);
		recuperarProductosVenta(codigoVenta);
		recuperarDatosVenta(codigoVenta);
		try {
			Document document = new Document();
			try {
				PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));
			} catch (FileNotFoundException fileNotFoundException) {
				System.out.println("No such file was found to generate the PDF "
						+ "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
			}
			document.open();

			// AQUÍ COMPLETAREMOS NUESTRO CÓDIGO PARA GENERAR EL PDF
			document.addTitle("Factura de una venta");
			document.addSubject("Factura de venta");
			document.addKeywords("Factura, Montaje");
			document.addAuthor("MuebleriaUniovi");
			document.addCreator("MuebleriaUniovi");

			// First page
			// Primera página 
			Chunk chunk = new Chunk("\n\n\n\n", chapterFont);
			// Let's create de first Chapter (Creemos el primer capítulo)
			Chapter chapter = new Chapter(new Paragraph(chunk), 1);
			chapter.setNumberDepth(0);
			chapter.add(new Paragraph("Facturar a:                                                                                      "
					+ "Fecha: " + fecha + "\nNombre: " + clienteVenta.getNombre() + "                            "
					+ "Factura: Venta-" + codigoVenta + "\nApellidos: " + clienteVenta.getApellidos() + "\nE-mail: " + clienteVenta.getCorreo() + "\nTeléfono: " + clienteVenta.getTelefono() + "\n\nProductos de la venta:\n", paragraphFont));
			chapter.add(new Paragraph("\n\n"));
			// We add an image (Añadimos una imagen)
			Image image;
			try {
				image = Image.getInstance(imagenLogo);  
				image.setAbsolutePosition(250, 700);
				chapter.add(image);
			} catch (BadElementException ex) {
				System.out.println("Image BadElementException" +  ex);
			} catch (IOException ex) {
				System.out.println("Image IOException " +  ex);
			}
			document.add(chapter);
			
			//Tabla de productos de la venta
			
			PdfPTable table = new PdfPTable(4);                
            
            // addCell() agrega una celda a la tabla, el cambio de fila
            // ocurre automaticamente al llenar la fila
			table.addCell("Descripción");
            table.addCell("Unidades");
            table.addCell("Montaje");
            table.addCell("Precio Unidad");
			for(ProductoVenta pV : productosVenta) {
				table.addCell(pV.getNombre());
				table.addCell(String.valueOf(pV.getUnidades()));
				if(pV.getMontaje()==0) {
					table.addCell("No");
				}else {
					table.addCell("Si");
				}
				table.addCell(String.valueOf(pV.getPrecioProducto() + " €"));
				
			}
             
            // Si desea crear una celda de mas de una columna
            // Cree un objecto Cell y cambie su propiedad span
			 DecimalFormat df = new DecimalFormat("#.00");
			double precioMontaje = 0.0;
			double precioTotal = 0.0;
			if(!(venta.getImporteVentaMasMontaje()== 0.0)) {
				precioMontaje = (venta.getImporteVentaMasMontaje() - venta.getImporteVenta());
				precioTotal = venta.getImporteVentaMasMontaje();
			}else {
				precioMontaje = 0.0;
				precioTotal = venta.getImporteVenta();
			}
             
            PdfPCell subtotal = new PdfPCell(new Paragraph("                                                                               "
            		+ "Subtotal: " + df.format(venta.getImporteVenta()) + " €"));
            PdfPCell montaje = new PdfPCell(new Paragraph("                                                                      "
            		+ "          Montaje: "+ precioMontaje + " €"));
            PdfPCell total = new PdfPCell(new Paragraph("                                                                            "
            		+ "        Total: " + df.format(precioTotal) + " €"));
             
            // Indicamos cuantas columnas ocupa la celda
            subtotal.setColspan(4);
            montaje.setColspan(4);
            total.setColspan(4);
            
            table.addCell(subtotal);
            table.addCell(montaje);
            table.addCell(total);
             
            // Agregamos la tabla al documento            
            document.add(table);
			
			document.close();
			//System.out.println("Your PDF file has been generated!(¡Se ha generado tu hoja PDF!)");
		} catch (DocumentException documentException) {
			System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
		}
	}
	
	private void recuperarDatosCliente(String dniCliente) {
		clienteVenta = new Cliente();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String consulta = "select nombreCliente, apellidosCliente, correoCliente, telefonoCliente From Cliente Where dniCliente = ?";
		String nombre, apellidos, correo;
		Long telefono;
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, dniCliente);
			rs = ps.executeQuery();
			nombre = rs.getString("nombreCliente");
			apellidos = rs.getString("apellidosCliente");
			correo = rs.getString("correoCliente");
			telefono = rs.getLong("telefonoCliente");
			clienteVenta = new Cliente(nombre, apellidos, telefono, correo);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, ps, con);
		}
	}
	
	private void recuperarDatosVenta(String codigoVenta) {
		venta = new Venta();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String consulta = "select importeVenta, importeVentaMasMontaje From Venta Where codigoVenta = ?";
		Double importeVenta, importeVentaMasMontaje;
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, codigoVenta);
			rs = ps.executeQuery();
			importeVenta = rs.getDouble("importeVenta");
			importeVentaMasMontaje = rs.getDouble("importeVentaMasMontaje");
			venta = new Venta(importeVenta, importeVentaMasMontaje);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, ps, con);
		}
	}
	
	private void recuperarProductosVenta(String codigoVenta){
		productosVenta = new ArrayList<ProductoVenta>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String consulta = "Select p.nombreProducto, vp.unidadesVenta, vp.montadoProducto, vp.tipoEntrega, v.fechaEntregaDomicilio, p.precioProducto "
				+ "From Producto p, VentaProducto vp, Venta v "
				+ "Where vp.codigoVenta = ? and vp.codigoProducto = p.codigoProducto and vp.codigoVenta = v.codigoVenta";
		String nombre, transporte, fechaEntrega;
		int montaje, unidades;
		double precioProducto;
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, codigoVenta);
			rs = ps.executeQuery();
			while(rs.next()) {
				nombre =  rs.getString("nombreProducto");
				transporte = rs.getString("tipoEntrega");
				montaje = rs.getInt("montadoProducto");
				fechaEntrega = rs.getString("fechaEntregaDomicilio");
				unidades = rs.getInt("unidadesVenta"); 
				precioProducto = rs.getDouble("precioProducto");
				productosVenta.add(new ProductoVenta(nombre, transporte, montaje, fechaEntrega, unidades, precioProducto));
			}
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
