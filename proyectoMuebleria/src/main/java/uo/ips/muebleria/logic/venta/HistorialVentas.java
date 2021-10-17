package uo.ips.muebleria.logic.venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.producto.Producto;
import uo.ips.muebleria.logic.producto.ProductoVenta;
import uo.ips.muebleria.util.Util;

public class HistorialVentas {

	private List<Venta> historialVentas;
	private List<Venta> historialVentasFiltrado;
	private List<ProductoVenta> productosVenta;

	public HistorialVentas() {
		historialVentas = new ArrayList<Venta>();
		historialVentasFiltrado = new ArrayList<Venta>();
		productosVenta = new ArrayList<ProductoVenta>();
		cargarVentas();
	}

	public void añadirPrecioMontaje(String codigoVenta) {
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		ResultSet rs = null;
		
		String consulta1 = "Select montadoProducto from VentaProducto Where codigoVenta = ?";
		String consulta2 = "Update Venta set importeVentaMasMontaje = importeVenta + ? Where codigoVenta = ?";
		
		double importeMontaje = 0.0;
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta1);
			ps2 = con.prepareStatement(consulta2);
			
			ps.setString(1, codigoVenta);
			rs = ps.executeQuery();
			while(rs.next()) {
				if(rs.getInt("montadoProducto") == 1) {
					importeMontaje = importeMontaje + 5;
				}
			}
			
			ps2.setDouble(1, importeMontaje);
			ps2.setString(2, codigoVenta);
			ps2.executeUpdate();
			
			rs.close();
			ps.close();
			ps2.close();
			con.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 


	}

	private void cargarVentas() {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String consulta = "select codigoVenta, fechaVentaCreada, importeVenta,importeVentaMasMontaje, fechaentregaDomicilio, estadoVenta, dniCliente, codigoTransportista From Venta";
		String codigo,fecha,estado,fechaEntrega,dniCliente, codigoTransportista;
		double importe, importeVentaMasMontaje;
		try {
			con = Jdbc.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(consulta);
			while(rs.next()) {
				codigo =  rs.getString("codigoVenta");
				fecha = rs.getString("fechaVentaCreada");
				importe = rs.getDouble("importeVenta"); 
				importeVentaMasMontaje = rs.getDouble("importeVentaMasMontaje");
				fechaEntrega = rs.getString("fechaentregaDomicilio");
				estado = rs.getString("estadoVenta");
				dniCliente = rs.getString("dniCliente");
				codigoTransportista = rs.getString("codigoTransportista");
				historialVentas.add(new Venta(codigo, fecha, importe, importeVentaMasMontaje, fechaEntrega, estado, dniCliente, codigoTransportista));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, st, con);
		}
	}


	public List<Venta> getHistorialVentas() {
		return historialVentas;


	}

	public List<Venta> getHistorialVentasFiltrado(){
		return historialVentasFiltrado;
	}

	public void filtrarFecha(List<Venta> list, Date fechaIni, Date fechaFin){

		List<Venta> l = new ArrayList<Venta>();
		for(Venta v: list) {
			Date fechaVenta = Util.isoStringToDate(v.getFechaVentaCreada());
			if((fechaVenta.equals(fechaIni) || fechaVenta.after(fechaIni)) &&
					(fechaVenta.equals(fechaFin) || fechaVenta.before(fechaFin))) {

				l.add(v);
			}

		}
		historialVentasFiltrado = l;
	}
	
	public Venta getVentaBBDD(String codVenta) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String consulta = "select * From Venta Where codigoVenta = ?";
		String codigo,fecha,fechaEntrega, estado, dniCliente, codigoTransportista;
		double importe, importeVentaMasMontaje;
		Venta venta = null;
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, codVenta);
			rs = ps.executeQuery();
			
			codigo =  rs.getString("codigoVenta");
			fecha = rs.getString("fechaVentaCreada");
			importe = rs.getDouble("importeVenta"); 
			importeVentaMasMontaje = rs.getDouble("importeVentaMasMontaje");
			fechaEntrega = rs.getString("fechaEntregaDomicilio");
			estado = rs.getString("estadoVenta");
			dniCliente = rs.getString("dniCliente");
			codigoTransportista = rs.getString("codigoTransportista");
			venta = new Venta(codigo, fecha, importe, importeVentaMasMontaje, fechaEntrega, estado, dniCliente, codigoTransportista);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, ps, con);
		}
		return venta;
	}

	public void cargarProductosVenta(String codigoVenta) {
		productosVenta.removeAll(productosVenta);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String consulta = "Select p.nombreProducto, vp.unidadesVenta, vp.montadoProducto, vp.tipoEntrega, v.fechaEntregaDomicilio "
				+ "From Producto p, VentaProducto vp, Venta v "
				+ "Where vp.codigoVenta = ? and vp.codigoProducto = p.codigoProducto and vp.codigoVenta = v.codigoVenta";
		String nombre, transporte, fechaEntrega;
		int montaje, unidades;
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
				productosVenta.add(new ProductoVenta(nombre, transporte, montaje, fechaEntrega, unidades));
			}
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Producto> getProductosVenta(String codigoVenta){
		List<Producto> productos = new ArrayList<>();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String codigo, nombre, tipo;
		double precio;
		Producto p;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement("select * from producto natural join ventaProducto where codigoVenta = ?");
			pst.setString(1, codigoVenta);
			rs = pst.executeQuery();
			while (rs.next()) {
				codigo = rs.getString("codigoProducto");
				nombre = rs.getString("nombreProducto");
				tipo = rs.getString("categoriaProducto");
				precio = rs.getDouble("precioProducto");
				p = new Producto(codigo, nombre, tipo, precio);
				p.setUnidades(rs.getInt("UnidadesVenta"));
				productos.add(p);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, con);
		}		
		return productos;
	}
		
	public List<ProductoVenta> getProductosVenta() {
		return productosVenta;
	}

	public double getImporteMasMontaje(String codVenta) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String consulta = "select importeVentaMasMontaje From Venta Where codigoVenta = ?";
		double importeVentaMasMontaje = 0.0;

		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, codVenta);
			rs = ps.executeQuery();
			
			importeVentaMasMontaje = rs.getDouble("importeVentaMasMontaje");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, ps, con);
		}
		return importeVentaMasMontaje;
	}
}
