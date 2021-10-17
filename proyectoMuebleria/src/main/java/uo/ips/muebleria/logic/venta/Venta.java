package uo.ips.muebleria.logic.venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.producto.Producto;
public class Venta {

	private String codigoVenta;
	private double importeVenta;
	private String fechaVentaCreada;
	private String dniCliente;
	private String codigoTransportista;
	private String fechaEntregaDomicilio;
	private String horaEntregaDomicilio;
	private String estadoVenta;
	private double importeVentaMasMontaje;
	private String codigoVendedor;
	private List<Producto> productos;
	
	public Venta() {
		productos = new ArrayList<Producto>();
	}

	public Venta(String codigo, String fecha, double importe) {
		this.codigoVenta = codigo;
		this.fechaVentaCreada = fecha;
		this.importeVenta = importe;
	}
	
	public Venta(Double importeVenta, Double importeVentaMasMontaje) {
		this.importeVenta = importeVenta;
		this.importeVentaMasMontaje = importeVentaMasMontaje;
	}
	
	public Venta(String codigo, String fechaVentaCreada, double importe, double importeVentaMasMontaje, String fechaEntrega, String estado, String dniCliente, String codigoTransportista) {
		this.codigoVenta = codigo;
		this.fechaVentaCreada = fechaVentaCreada;
		this.importeVenta = importe;
		this.importeVentaMasMontaje = importeVentaMasMontaje;
		this.fechaEntregaDomicilio = fechaEntrega;
		this.estadoVenta = estado;
		this.dniCliente = dniCliente;
		this.codigoTransportista = codigoTransportista;
	}

	public Venta(String codigo, String fecha, double importe, String estado) {
		this.codigoVenta = codigo;
		this.fechaVentaCreada = fecha;
		this.importeVenta = importe;
		this.estadoVenta = estado;
	}

	public String getCodigoVenta() {
		return codigoVenta;
	}

	public void setCodigoVenta(String codigoVenta) {
		this.codigoVenta = codigoVenta;
	}

	public double getImporteVenta() {
		return importeVenta;
	}

	public void setImporteVenta(double importeVenta) {
		this.importeVenta = importeVenta;
	}

	public String getFechaVentaCreada() {
		return fechaVentaCreada;
	}

	public void setFechaVentaCreada(String fechaVentaCreada) {
		this.fechaVentaCreada = fechaVentaCreada;
	}

	public String getDniCliente() {
		return dniCliente;
	}

	public void setDniCliente(String dniCliente) {
		this.dniCliente = dniCliente;
	}

	public String getCodigoTransportista() {
		return codigoTransportista;
	}

	public void setCodigoTransportista(String codigoTransportista) {
		this.codigoTransportista = codigoTransportista;
	}

	public String getFechaEntregaDomicilio() {
		return fechaEntregaDomicilio;
	}

	public void setFechaEntregaDomicilio(String fechaEntregaDomicilio) {
		this.fechaEntregaDomicilio = fechaEntregaDomicilio;
	}

	public String getHoraEntregaDomicilio() {
		return horaEntregaDomicilio;
	}

	public void setHoraEntregaDomicilio(String horaEntregaDomicilio) {
		this.horaEntregaDomicilio = horaEntregaDomicilio;
	}
	
	public String getEstadoVenta() {
		return estadoVenta;
	}
	
	public void setEstadoVenta(String estadoVenta) {
		this.estadoVenta = estadoVenta;
	}
	
	public double getImporteVentaMasMontaje() {
		return importeVentaMasMontaje;
	}
	
	public void setImporteVentaMasMontaje(double importeVentaMasMontaje) {
		this.importeVentaMasMontaje = importeVentaMasMontaje;
	}
	
	public void añadirProductos(List<Producto> productosAñadir) {
		for (Producto p: productosAñadir) {
			productos.add(p);
		}
	}
	
	public List<Producto> getProductos() {
		return productos;
	}
	
	public String getCodigoVendedor() {
		return codigoVendedor;
	}

	public void setCodigoVendedor(String codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}

	public void marcarComo(String codigoVenta, String estado) {
		Connection con = null;
		PreparedStatement ps = null;
		String consulta = "update Venta set estadoVenta = ? Where codigoVenta = ?";
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, estado);
			ps.setString(2, codigoVenta);
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		};
	}
}
