package uo.ips.muebleria.logic.trabajador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;

public class TrabajadorConsultas {

	private final static String SQL_ADD_TRABAJADOR = "insert into Trabajador values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String SQL_ADD_TRANSPORTISTA = "insert into Transportista values (?)";
	private final static String SQL_ADD_VENDEDOR = "insert into Vendedor values (?)";
	private final static String SQL_ADD_PERSONALALMACEN = "insert into PersonalAlmacen values (?)";
	private final static String SQL_FINDALL_VENDEDORES = "select t.* from Trabajador t, Vendedor v where t.codigoTrabajador = v.codigoVendedor";

	public static void add(Trabajador t) {
		Connection c = null;
		PreparedStatement pst = null;

		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_ADD_TRABAJADOR);
			pst.setString(1, t.getCodigo());
			pst.setString(2, t.getDni());
			pst.setString(3, t.getNombre());
			pst.setString(4, t.getApellidos());
			pst.setString(5, t.getDepartamento());
			pst.setLong(6, t.getTelefono());
			pst.setString(7, t.getHoraInicioJornada());
			pst.setString(8, t.getHoraFinJornada());
			pst.setString(9, t.getUsuarioTrabajador());
			pst.setString(10, t.getContraseñaTrabajador());
			pst.execute();

			switch (t.getDepartamento()) {
			case "Transportista":
				PreparedStatement pst2 = c.prepareStatement(SQL_ADD_TRANSPORTISTA);
				pst2.setString(1, t.getCodigo());
				pst2.execute();
				pst2.close();
				break;
				
			case "Vendedor":
				PreparedStatement pst3 = c.prepareStatement(SQL_ADD_VENDEDOR);
				pst3.setString(1, t.getCodigo());
				pst3.execute();
				pst3.close();
				break;
				
			case "Personal de almacen":
				PreparedStatement pst4 = c.prepareStatement(SQL_ADD_PERSONALALMACEN);
				pst4.setString(1, t.getCodigo());
				pst4.execute();
				pst4.close();
				break;
			}

			pst.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static List<Vendedor> getVendedores() {
		List<Vendedor> vendedores = new ArrayList<Vendedor>();
		
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_FINDALL_VENDEDORES);
			rs = pst.executeQuery();

			while(rs.next()) {
				Vendedor v  = new Vendedor();
				v.setCodigo(rs.getString("codigoTrabajador"));
				v.setDni(rs.getString("nifTrabajador"));
				v.setNombre(rs.getString("nombreTrabajador"));
				v.setApellidos(rs.getString("apellidosTrabajador"));
				v.setDepartamento("Vendedor");
				v.setTelefono(rs.getLong("numeroTrabajador"));
				v.setHoraInicioJornada(rs.getString("horaInicioJornada"));
				v.setHoraFinJornada(rs.getString("horaFinJornada"));
				vendedores.add(v);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, pst, c);
		}
		
		return vendedores;
	}

}
