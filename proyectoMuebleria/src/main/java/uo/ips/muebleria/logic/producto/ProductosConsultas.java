package uo.ips.muebleria.logic.producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;

public class ProductosConsultas {

	private final static String SQL_GET_PRODUCTOS_CATALOGO = "select * from producto order by categoriaProducto";
	private final static String SQL_GET_TIPOS = "select distinct categoriaProducto from producto";
	private final static String SQL_ACTUALIZAR_PRECIO = "update Producto set precioProducto = ? where codigoProducto=?";
	private static final String SQL_GET_PRODUCTOS_MODELO = "select * from producto natural join contieneProducto where codigoPresupuesto = ?";

	public static List<Producto> getProductosCatalogo() {
		List<Producto> productos = new ArrayList<>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String codigo, nombre, tipo;
		double precio;
		try {
			con = Jdbc.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(SQL_GET_PRODUCTOS_CATALOGO);
			while (rs.next()) {
				codigo = rs.getString("codigoProducto");
				nombre = rs.getString("nombreProducto");
				tipo = rs.getString("categoriaProducto");
				precio = rs.getDouble("precioProducto");
				productos.add(new Producto(codigo, nombre, tipo, precio));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, st, con);
		}
		return productos;
	}

	public static List<String> getTipoProductos() {
		List<String> tipos = new ArrayList<>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String tipo;
		try {
			con = Jdbc.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(SQL_GET_TIPOS);
			while (rs.next()) {
				tipo = rs.getString("categoriaProducto");
				tipos.add(tipo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, st, con);
		}
		return tipos;
	}
	
	public static void actualizarPrecio(String codigo, double precio) {
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_ACTUALIZAR_PRECIO);
			pst.setDouble(1, precio);
			pst.setString(2, codigo);
			
			pst.execute();

			pst.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
	}
	

	public static List<Producto> getProductosModelo(String cod) {
		List<Producto> productos = new ArrayList<>();
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String codigo, nombre, tipo;
		double precio;
		int unidades;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_GET_PRODUCTOS_MODELO);
			pst.setString(1, cod);
			rs = pst.executeQuery();
			while (rs.next()) {
				codigo = rs.getString("codigoProducto");
				nombre = rs.getString("nombreProducto");
				tipo = rs.getString("categoriaProducto");
				precio = rs.getDouble("precioProducto");
				unidades = rs.getInt("unidadesPresupuesto");
				productos.add(new Producto(codigo, nombre, tipo, unidades, precio));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, con);
		}
		return productos;
	}
}
