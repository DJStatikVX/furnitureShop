package uo.ips.muebleria.logic.cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;

import uo.ips.muebleria.jdbc.Jdbc;


public class ClienteConsultas {
	
	public final static String SQL_SELECCIONARTODOSLOSCLIENTES = "SELECT dniCliente, nombreCliente, apellidosCliente, direccionCliente, telefonoCliente, correoCliente from Cliente";
	public final static String SQL_FIND_BY_DNI = "SELECT dniCliente, nombreCliente, apellidosCliente, direccionCliente, telefonoCliente, correoCliente from Cliente where dniCliente = ?";
	public final static String SQL_AÑADIRCLIENTE = "INSERT INTO CLIENTE(dniCliente,nombreCliente,apellidosCliente,direccionCliente, telefonoCliente, correoCliente) VALUES(?, ?, ?, ?, ?, ?)";
	
	public static DefaultListModel<Cliente> getListaClientes() {
		DefaultListModel<Cliente> clientes = new DefaultListModel<Cliente>();
		Cliente cliente;
		
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_SELECCIONARTODOSLOSCLIENTES);
			rs = pst.executeQuery();
			
			int i=0;
			while (rs.next()) {
				cliente = new Cliente(
						rs.getString("dniCliente"),
						rs.getString("nombreCliente"), 
						rs.getString("apellidosCliente"), 
						rs.getString("direccionCliente"),
						Integer.parseInt(rs.getString("telefonoCliente")),
								rs.getString("correoCliente"));
				clientes.add(i, cliente);
				i++;
			}
			
			rs.close();
			pst.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return clientes;
	}
	
	public static void añadirCliente(Cliente cliente) {
		Connection c = null;
		PreparedStatement pst = null;
		
		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_AÑADIRCLIENTE);
			pst.setString(1, cliente.getDni());
			pst.setString(2, cliente.getNombre());
			pst.setString(3, cliente.getApellidos());
			pst.setString(4, cliente.getDireccion());
			pst.setLong(5, cliente.getTelefono());
			pst.setString(6, cliente.getCorreo());
			pst.execute();
			
			pst.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Cliente findByDni(String dni) {
		Cliente cliente = null;
		
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_FIND_BY_DNI);
			pst.setString(1, dni);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				cliente = new Cliente(
						rs.getString("dniCliente"),
						rs.getString("nombreCliente"), 
						rs.getString("apellidosCliente"), 
						rs.getString("direccionCliente"),
						Integer.parseInt(rs.getString("telefonoCliente")),
						rs.getString("correoCliente"));
			}
			
			rs.close();
			pst.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cliente;
	}
	
}
