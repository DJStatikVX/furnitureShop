package uo.ips.muebleria.logic.presupuesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultListModel;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.producto.Producto;

public class PresupuestosConsultas {

	public final static String SQL_SELECCIONARTODOSLOSPRESUPUESTOS = "SELECT codigoPresupuesto, importePresupuesto, fechaPresupuesto, estadoPresupuesto, dniCliente, codigoVendedor from presupuesto where tipoPresupuesto!='MODELO'";
	
	public final static String SQL_SELECCIONARTODOSLOSPRESUPUESTOS_MODELO = "SELECT * from presupuesto where tipoPresupuesto='MODELO'";
	
	public final static String SQL_SELECCIONAR_PRODUCTOS = "SELECT Producto.codigoProducto, Producto.categoriaProducto, Producto.nombreProducto, Producto.precioProducto, ContieneProducto.unidadesPresupuesto FROM presupuesto, producto, contieneProducto "
			+ "WHERE producto.codigoProducto = contieneProducto.codigoProducto and contieneProducto.codigoPresupuesto = Presupuesto.codigoPresupuesto and Presupuesto.codigoPresupuesto = ?";
	
	public final static String SQL_UPDATE_PRESUPUESTO = "UPDATE presupuesto SET importePresupuesto=?, fechaPresupuesto=?, estadoPresupuesto=?, dniCliente=? WHERE codigoPresupuesto=?";
	
	public final static String SQL_UPDATE_PRESUPUESTO_MODELO = "UPDATE presupuesto SET nombrePresupuesto=?, tipoPresupuesto=? WHERE codigoPresupuesto=?";
	
	public final static String SQL_MARCAR_PRESUPUESTO_TRAMITADO = "UPDATE Presupuesto SET estadoPresupuesto = 'Tramitado' WHERE codigoPresupuesto = ?";
	
	public final static String SQL_INSERTAR_PRESUPUESTO = "insert into Presupuesto(codigoPresupuesto,importePresupuesto,fechaPresupuesto,estadoPresupuesto,dniCliente,codigoVendedor,tipoPresupuesto) values (?,?,?,?,?,?,?)";
	
	public final static String SQL_FIND_BY_CODE = "SELECT codigoPresupuesto, importePresupuesto, fechaPresupuesto, estadoPresupuesto, dniCliente, codigoVendedor from presupuesto where codigoPresupuesto = ?";
	
	public final static String SQL_INSERT_PRESUPUESTO_PRODUCTO = "insert into ContieneProducto(codigoProducto,codigoPresupuesto,unidadesPresupuesto,precioProductoPresupuesto) values \r\n"
			+ "	(?,?,?,?)";
	public final static String SQL_GET_RELACION_PRESUPUESTO_PRODUCTO_POR_PRODUCTO = "select * from ContieneProducto where codigoProducto = ?";
	public final static String SQL_UPDATE_RELACION_PRESUPUESTO_PRODUCTO = "update ContieneProducto SET unidadesPresupuesto = ?, precioProductoPresupuesto = ? WHERE codigoPresupuesto = ? and codigoProducto = ?";
	
	public static DefaultListModel<Presupuesto> getPresupuestosNoAceptadosConCliente() {
		DefaultListModel<Presupuesto> presupuestos = new DefaultListModel<Presupuesto>();
		List<Presupuesto> presupuestosCaducados = new ArrayList<Presupuesto>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_SELECCIONARTODOSLOSPRESUPUESTOS);
			rs = ps.executeQuery();

			int i = 0;
			while (rs.next()) {
				String codigoPresupuesto = rs.getString("codigoPresupuesto");
				double importe = rs.getDouble("importePresupuesto");
				String fechaPresupuesto = rs.getString("fechaPresupuesto");
				String estado = rs.getString("estadoPresupuesto");
				String dniCliente = rs.getString("dniCliente");
				String codigoVendedor = rs.getString("codigoVendedor");

				if (!comprobarCaducidad(fechaPresupuesto)) {
					estado = "Caducado";
					Presupuesto p = new Presupuesto(codigoPresupuesto, importe, fechaPresupuesto, estado, dniCliente, codigoVendedor);
					presupuestosCaducados.add(p);
				} else {
					if (dniCliente != null && estado.equals("Pendiente")) {
						Presupuesto p = new Presupuesto(codigoPresupuesto, importe, fechaPresupuesto, estado,
								dniCliente, codigoVendedor);
						presupuestos.add(i, p);
						i++;
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		actualizarPresupuestos(presupuestosCaducados);
		return presupuestos;
	}

	private static void actualizarPresupuestos(List<Presupuesto> presupuestosCaducados) {
		for (Presupuesto p : presupuestosCaducados) {
			update(p);
		}
	}

	private static boolean comprobarCaducidad(String fecha) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date fechaInicial;
			fechaInicial = dateFormat.parse(fecha);
			Date fechaFinal = new Date();

			int dias = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 86400000);
			if (dias < 15) {
				return true;
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static DefaultListModel<Presupuesto> getPresupuestosSinCliente() {
		DefaultListModel<Presupuesto> presupuestos = new DefaultListModel<Presupuesto>();
		List<Presupuesto> presupuestosCaducados = new ArrayList<Presupuesto>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_SELECCIONARTODOSLOSPRESUPUESTOS);
			rs = ps.executeQuery();

			int i = 0;
			while (rs.next()) {
				String codigoPresupuesto = rs.getString("codigoPresupuesto");
				double importe = rs.getDouble("importePresupuesto");
				String fechaPresupuesto = rs.getString("fechaPresupuesto");
				String estado = rs.getString("estadoPresupuesto");
				String dniCliente = rs.getString("dniCliente");
				String codigoVendedor = rs.getString("codigoVendedor");

				if (!comprobarCaducidad(fechaPresupuesto)) {
					estado = "Caducado";
					Presupuesto p = new Presupuesto(codigoPresupuesto, importe, fechaPresupuesto, estado, dniCliente,codigoVendedor);
					presupuestosCaducados.add(p);
				} else {
					if (dniCliente == null) {
						Presupuesto p = new Presupuesto(codigoPresupuesto, importe, fechaPresupuesto, estado,
								dniCliente,codigoVendedor);
						presupuestos.add(i, p);
						i++;
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		actualizarPresupuestos(presupuestosCaducados);
		return presupuestos;
	}

	public static void update(Presupuesto p) {
		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_UPDATE_PRESUPUESTO);

			ps.setDouble(1, p.getImportePresupuesto());
			ps.setString(2, p.getFechaPresupuesto());
			ps.setString(3, p.getEstado());
			ps.setString(4, p.getDniCliente());
			ps.setString(5, p.getCodigoPresupuesto());

			ps.execute();

			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void marcarComoTramitado(Presupuesto p) {
		Connection c = null;
		PreparedStatement pst = null;

		try {
			try {
				c = Jdbc.getConnection();
				pst = c.prepareStatement(SQL_MARCAR_PRESUPUESTO_TRAMITADO);
				pst.setString(1, p.getCodigoPresupuesto());

				pst.executeUpdate();
				
			} finally {
				pst.close();
				c.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Producto> getProductos(String codigoPresupuesto) {
		List<Producto> productos = new ArrayList<Producto>();
		Producto p;

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_SELECCIONAR_PRODUCTOS);

			ps.setString(1, codigoPresupuesto);

			rs = ps.executeQuery();

			while (rs.next()) {
				String codigoProducto = rs.getString("codigoProducto");
				String categoriaProducto = rs.getString("categoriaProducto");
				String nombreProducto = rs.getString("nombreProducto");
				double precioProducto = rs.getDouble("precioProducto");
				int unidadesPresupuesto = rs.getInt("unidadesPresupuesto");

				p = new Producto(codigoProducto, nombreProducto, categoriaProducto, unidadesPresupuesto,
						precioProducto);

				productos.add(p);
			}

			rs.close();
			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productos;
	}

	public static Presupuesto insertarPresupuesto(String codigo, double total, String codVendedor) {
		Connection con = null;
		PreparedStatement pst = null;
		Calendar f = Calendar.getInstance();
		String fecha = f.get(Calendar.DAY_OF_MONTH) + "-" + (f.get(Calendar.MONTH) + 1) + "-" + f.get(Calendar.YEAR);
		Presupuesto p = null;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_INSERTAR_PRESUPUESTO);
			pst.setString(1, codigo);
			pst.setDouble(2, total);
			pst.setString(3, fecha);
			pst.setString(4, "Pendiente");
			pst.setString(5, null);
			pst.setString(6, codVendedor);
			pst.setString(7, "NO_MODELO");
			pst.executeUpdate();
			p = new Presupuesto(codigo, total, fecha, "Pendiente", null, codVendedor);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(pst);
			Jdbc.close(con);
		}
		return p;
	}

	public static void relacionarPresupuestoProducto(String codigo, List<Producto> productosPresupuesto) {
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_INSERT_PRESUPUESTO_PRODUCTO);

			for (Producto p : productosPresupuesto) {
				pst.setString(1, p.getCodigo());
				pst.setString(2, codigo);
				pst.setInt(3, p.getUnidades());
				pst.setDouble(4, p.getPrecio());
				pst.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(pst);
			Jdbc.close(con);
		}
	}

	public static Presupuesto findByCode(String code) {
		Presupuesto presupuesto = new Presupuesto();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_FIND_BY_CODE);
			ps.setString(1, code);
			rs = ps.executeQuery();

			while (rs.next()) {
				String codigoPresupuesto = rs.getString("codigoPresupuesto");
				double importe = rs.getDouble("importePresupuesto");
				String fechaPresupuesto = rs.getString("fechaPresupuesto");
				String estado = rs.getString("estadoPresupuesto");
				String dniCliente = rs.getString("dniCliente");
				String codigoVendedor = rs.getString("codigoVendedor");
				
				presupuesto = new Presupuesto(codigoPresupuesto, importe, fechaPresupuesto, estado, dniCliente,codigoVendedor);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return presupuesto;
	}

	public static List<ContieneProducto> getContieneProductos(String codigoProducto) {
		List<ContieneProducto> productos = new ArrayList<ContieneProducto>();
		ContieneProducto p;

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_GET_RELACION_PRESUPUESTO_PRODUCTO_POR_PRODUCTO);

			ps.setString(1, codigoProducto);

			rs = ps.executeQuery();

			while (rs.next()) {
				String codigoPresupuesto = rs.getString("codigoPresupuesto");
				double precioProducto = rs.getDouble("precioProductoPresupuesto");
				int unidadesPresupuesto = rs.getInt("unidadesPresupuesto");

				p = new ContieneProducto(codigoProducto, codigoPresupuesto, unidadesPresupuesto,
						precioProducto);

				productos.add(p);
			}

			rs.close();
			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productos;
	}
	
	public static void updateContieneProducto(ContieneProducto p) {
		Connection c = null;
		PreparedStatement ps = null;
		
		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_UPDATE_RELACION_PRESUPUESTO_PRODUCTO);
			
			ps.setInt(1, p.getUnidadesProducto());
			ps.setDouble(2, p.getPrecioProducto());
			ps.setString(3, p.getCodigoPresupuesto());
			ps.setString(4, p.getCodigoProducto());
			
			ps.execute();
			
			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void marcarComoModelo(String codigoPresupuesto) {
		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_UPDATE_PRESUPUESTO_MODELO);

			ps.setString(1, "Modelo");
			ps.setString(2, "MODELO");
			ps.setString(3, codigoPresupuesto);

			ps.execute();

			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void marcarComoModelo(String codigoPresupuesto, String nombre) {
		Connection c = null;
		PreparedStatement ps = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_UPDATE_PRESUPUESTO_MODELO);

			ps.setString(1, nombre);
			ps.setString(2, "MODELO");
			ps.setString(3, codigoPresupuesto);

			ps.execute();

			ps.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Presupuesto> getPresupuestosModelo() {
		List<Presupuesto> presupuestos = new ArrayList<Presupuesto>();

		Connection c = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			ps = c.prepareStatement(SQL_SELECCIONARTODOSLOSPRESUPUESTOS_MODELO);
			rs = ps.executeQuery();

			while (rs.next()) {
				String codigoPresupuesto = rs.getString("codigoPresupuesto");
				double importe = rs.getDouble("importePresupuesto");
				String fechaPresupuesto = rs.getString("fechaPresupuesto");
				String estado = rs.getString("estadoPresupuesto");
				String dniCliente = rs.getString("dniCliente");
				String codigoVendedor = rs.getString("codigoVendedor");
				String nombre = rs.getString("nombrePresupuesto");
				
				presupuestos.add(new Presupuesto(codigoPresupuesto, importe, fechaPresupuesto, estado, dniCliente, codigoVendedor, nombre));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return presupuestos;
	}

}
