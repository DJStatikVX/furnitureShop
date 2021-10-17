package uo.ips.muebleria.logic.transporte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.producto.Producto;
import uo.ips.muebleria.logic.producto.ProductoDelPedidoConsultas;
import uo.ips.muebleria.logic.venta.Venta;

public class GestionTransporte {
	
	public static final String SQL_CARGAR_TRANSPORTISTAS = 
			"SELECT Transportista.codigoTransportista, nifTrabajador, "
			+ "nombreTrabajador, apellidosTrabajador, departamentoTrabajador, "
			+ "numeroTrabajador, horaInicioJornada, "
			+ "horaFinJornada FROM Trabajador, Transportista WHERE "
			+ "Trabajador.codigoTrabajador = Transportista.codigoTransportista";
	
	public static final String SQL_INSERTAR_VENTA =
			"INSERT INTO Venta(codigoVenta, importeVenta, fechaVentaCreada, "
			+ "dniCliente, codigoTransportista, fechaEntregaDomicilio, horaEntregaDomicilio, estadoVenta, codigoVendedor) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	public static final String SQL_INSERTAR_VENTA_PRODUCTO =
			"INSERT INTO VentaProducto(codigoProducto, codigoVenta, tipoEntrega, "
			+ "montadoProducto, unidadesVenta) VALUES (?, ?, ?, ?, ?)";
	
	public static final String SQL_INSERTAR_PEDIDO =
			"INSERT INTO Pedido(codigoPedido, estadoPedido, tipoPedido, codigoProveedor, fechaPedido) VALUES (?, ?, ?, ?, ?)";
	
	public static final String SQL_INSERTAR_PRODUCTOS_PEDIDO =
			"INSERT INTO ContienePedido(codigoProducto, codigoPedido, "
			+ "unidadesPedido, precioProductoPedido) VALUES (?, ?, ?, ?)";
	
	private List<Transportista> listaTransportistas;
	private List<Producto> listaProductos;
	private String codigoVendedor;
	
	public GestionTransporte(String codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
		listaTransportistas = new ArrayList<Transportista>();
		listaProductos = new ArrayList<Producto>();
		inicializar();
	}
	
	private void inicializar() {
		cargarDatosTransportistas();
	}
	
	private void cargarDatosTransportistas() {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		String codigo;
		String dni;
		String nombre;
		String apellidos;
		int telefono;
		String horaInicioJornada;
		String horaFinJornada;
		
		try {
			con = Jdbc.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(SQL_CARGAR_TRANSPORTISTAS);
			
			while (rs.next()) {
				codigo = rs.getString("codigoTransportista");
				dni = rs.getString("nifTrabajador");
				nombre = rs.getString("nombreTrabajador");
				apellidos = rs.getString("apellidosTrabajador");
				telefono = rs.getInt("numeroTrabajador");
				horaInicioJornada = rs.getString("horaInicioJornada");
				horaFinJornada = rs.getString("horaFinJornada");
				
				listaTransportistas.add(new Transportista(codigo, dni, nombre, 
						apellidos, telefono, 
						horaInicioJornada, horaFinJornada));
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			Jdbc.close(rs, st, con);
		}
	}
	
	public List<Producto> cargarProductosVenta(Venta ventaSeleccionada) {
		return ventaSeleccionada.getProductos();
	}
	
	public void asignarTransporteAVenta(Venta venta, Transportista transportista,
			String fechaEntrega, String horaEntrega) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-YYYY");
		String fechaHoy = sdf.format(date);
		venta.setFechaVentaCreada(fechaHoy);
		
		venta.setCodigoTransportista(transportista.getCodigo());
		venta.setFechaEntregaDomicilio(fechaEntrega);
		venta.setHoraEntregaDomicilio(horaEntrega);
		venta.setEstadoVenta("Pendiente");
		
		insertarVenta(venta,codigoVendedor);
	}
	
	public void confirmarVentaSinTransporte(Venta venta) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-YYYY");
		String fechaHoy = sdf.format(date);
		venta.setFechaVentaCreada(fechaHoy);
		
		venta.setCodigoTransportista("");
		venta.setFechaEntregaDomicilio("");
		venta.setHoraEntregaDomicilio("");
		
		insertarVenta(venta,codigoVendedor);
	}
	
	private void insertarVenta(Venta venta, String codVendedor) {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_INSERTAR_VENTA);
			
			pst.setString(1, venta.getCodigoVenta());
			pst.setDouble(2, venta.getImporteVenta());
			pst.setString(3, venta.getFechaVentaCreada());
			pst.setString(4, venta.getDniCliente());
			pst.setString(5, venta.getCodigoTransportista());
			pst.setString(6, venta.getFechaEntregaDomicilio());
			pst.setString(7, venta.getHoraEntregaDomicilio());
			pst.setString(8, venta.getEstadoVenta());
			pst.setString(9, codVendedor);
			
			pst.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pst.close();
				Jdbc.close(con);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void insertarProductoVenta(Connection con, String codigoProducto, String codigoVenta,
			String tipoEntrega, boolean montadoProducto, int unidadesVenta) {
		
		int montar;
		PreparedStatement pst = null;
		
		if (montadoProducto)
			montar = 1;
		else
			montar = 0;
		
		try {
			pst = con.prepareStatement(SQL_INSERTAR_VENTA_PRODUCTO);
			
			pst.setString(1, codigoProducto);
			pst.setString(2, codigoVenta);
			pst.setString(3, tipoEntrega);
			pst.setInt(4, montar);
			pst.setInt(5, unidadesVenta);
			
			pst.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void crearPedido(Pedido p, List<Producto> productosAPedir) {		
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_INSERTAR_PEDIDO);
			
			pst.setString(1, p.getCodigoPedido());
			pst.setString(2, p.getEstadoPedido());
			pst.setString(3, "Automático");
			pst.setString(4, "001");
			pst.setString(5, p.getFechaPedido());
			
			pst.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return;
		} finally {
			try {
				pst.close();
				Jdbc.close(con);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		
		insertarProductosPedido(productosAPedir, p);
	}
	
	private void insertarProductosPedido(List<Producto> productosAPedir, Pedido pedido) {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_INSERTAR_PRODUCTOS_PEDIDO);
			
			for (Producto p : productosAPedir) {
				pst.setString(1, p.getCodigo());
				pst.setString(2, pedido.getCodigoPedido());
				pst.setInt(3, p.getUnidades());
				pst.setDouble(4, ProductoDelPedidoConsultas.getPrecioProveedor(p.getCodigo()));
				
				pst.executeUpdate();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				pst.close();
				Jdbc.close(con);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void actualizarTransporte(Venta venta, String fechaEntrega, String horaEntrega) {
		venta.setFechaEntregaDomicilio(fechaEntrega);
		venta.setHoraEntregaDomicilio(horaEntrega);
		actualizarEntregaVenta(venta);
	}
	
	private void actualizarEntregaVenta(Venta venta) {
		Connection con = null;
		PreparedStatement ps = null;
		String consulta = "update Venta set fechaEntregaDomicilio = ?, horaEntregaDomicilio = ? Where codigoVenta = ?";
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, venta.getFechaEntregaDomicilio());
			ps.setString(2, venta.getHoraEntregaDomicilio());
			ps.setString(3, venta.getCodigoVenta());
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		};
	}

	public List<Transportista> getTransportistas() {
		return listaTransportistas;
	}
	
	public List<Producto> getProductos() {
		return listaProductos;
	}

}
