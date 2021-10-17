package uo.ips.muebleria.logic.venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;

public class VentaConsultas {
	private final static String SQL_FINDBY_VENDEDOR = "select * from Venta where codigoVendedor = ?";
	
	public static List<Venta> getVentasPorVendedor(String codigoVendedor) {
		List<Venta> resultado = new ArrayList<Venta>();
		
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_FINDBY_VENDEDOR);
			pst.setString(1, codigoVendedor);
			rs = pst.executeQuery();

			while(rs.next()) {
				Venta v  = new Venta();
				v.setCodigoVenta(rs.getString("codigoVendedor"));
				v.setImporteVenta(rs.getDouble("importeVenta"));
				v.setFechaVentaCreada(rs.getString("fechaVentaCreada"));
				v.setDniCliente(rs.getString("dniCliente"));
				v.setCodigoTransportista(rs.getString("codigoTransportista"));
				v.setFechaEntregaDomicilio(rs.getString("fechaEntregaDomicilio"));
				v.setHoraEntregaDomicilio(rs.getString("horaEntregaDomicilio"));
				v.setEstadoVenta(rs.getString("estadoVenta"));
				v.setCodigoVendedor(rs.getString("codigoVendedor"));
				resultado.add(v);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, c);
		}
		
		return resultado;
	}

}
