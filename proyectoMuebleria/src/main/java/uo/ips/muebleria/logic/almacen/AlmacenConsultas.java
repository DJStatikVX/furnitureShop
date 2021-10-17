package uo.ips.muebleria.logic.almacen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;

public class AlmacenConsultas {
	
	private final static String SQL_GET_STOCK = "select stock from AlmacenProducto where codigoProducto = ?";
	private final static String SQL_UPDATE_STOCK = "update AlmacenProducto set stock = ? where codigoProducto = ?";
	private final static String SQL_GET_UNIDADES_VENDIDAS = "select sum(unidadesVenta) total from VentaProducto where codigoProducto = ?";
	private final static String SQL_GET_UNIDADES_PENDIENTES = "select sum(unidadesPedido) total from ContienePedido natural join Pedido "
			+ "where codigoProducto = ? and lower(estadoPedido)='solicitado' and lower(tipoPedido)='manual'";
	private final static String SQL_GET_CODIGO_PEDIDO = "select * from contienePedido where codigoPedido = ?";
	
	public static int getStock(String codigo) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_GET_STOCK);
			pst.setString(1, codigo);
			rs = pst.executeQuery();
			rs.next();
			int stock = rs.getInt("stock");
			return stock;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, con);
		}
		return 0;	
	}
	
	public static void updateStock(String codigo, int newStock) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_UPDATE_STOCK);
			pst.setInt(1, newStock);
			pst.setString(2, codigo);
			pst.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, con);
		}
	}
	
	public static int getUnidadesVendidas(String codigo) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_GET_UNIDADES_VENDIDAS);
			pst.setString(1, codigo);
			rs = pst.executeQuery();
			rs.next();
			int stock = rs.getInt("total");
			return stock;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, con);
		}
		return 0;	
	}
	
	public static int getUnidadesPendientes(String codigo) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_GET_UNIDADES_PENDIENTES);
			pst.setString(1, codigo);
			rs = pst.executeQuery();
			rs.next();
			int stock = rs.getInt("total");
			return stock;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, con);
		}
		return 0;	
	}

	public static List<String> getCodigoProductoDePedido(String codigoPedido) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		List<String> list = new ArrayList<>();
		try {
			con = Jdbc.getConnection();
			pst = con.prepareStatement(SQL_GET_CODIGO_PEDIDO);
			pst.setString(1, codigoPedido);
			rs = pst.executeQuery();
			while(rs.next()) {
				list.add(rs.getString("codigoProducto")+"@"+rs.getString("unidadesPedido"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, con);
		}
		return list;
	}
}
