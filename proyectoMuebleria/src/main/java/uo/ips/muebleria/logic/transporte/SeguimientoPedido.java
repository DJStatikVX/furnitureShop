package uo.ips.muebleria.logic.transporte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.producto.Producto;

public class SeguimientoPedido {

	private List<Pedido> seguimientoPedidos;
	private List<Producto> productosPedido;

	public SeguimientoPedido() {
		seguimientoPedidos = new ArrayList<Pedido>();
		productosPedido = new ArrayList<Producto>();
		cargarPedidos();
	}

	public void cargarPedidos() {
		seguimientoPedidos.removeAll(seguimientoPedidos);
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String consulta = "select codigoPedido, tipoPedido, estadoPedido From Pedido";
		String codigo, estado, tipo;
		try {
			con = Jdbc.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(consulta);
			while(rs.next()) {
				codigo =  rs.getString("codigoPedido");
				tipo = rs.getString("tipoPedido");
				estado = rs.getString("estadoPedido"); 
				seguimientoPedidos.add(new Pedido(codigo, tipo, estado));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, st, con);
		}
	}
	
	public void cargarProductosPedido(String codigoPedido) {
		productosPedido.removeAll(productosPedido);
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String consulta = "Select p.nombreProducto, cp.unidadesPedido "
				+ "From Producto p, ContienePedido cp "
				+ "Where cp.codigoPedido = ? and cp.codigoProducto = p.codigoProducto";
		String nombre;
		int unidades;
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, codigoPedido);
			rs = ps.executeQuery();
			while(rs.next()) {
				nombre =  rs.getString("nombreProducto");
				unidades = rs.getInt("unidadesPedido"); 
				productosPedido.add(new Producto(nombre, unidades));
			}
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		};
	}
	
	public void confirmarRecepcionPedido(String codigoPedido){
		
		Connection con = null;
		PreparedStatement ps = null;
		String consulta = "update Pedido set estadoPedido = 'recibido' Where codigoPedido = ?";
		try {
			con = Jdbc.getConnection();
			ps = con.prepareStatement(consulta);
			ps.setString(1, codigoPedido);
			ps.executeUpdate();
			ps.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		};
	}
	


	public List<Pedido> getSegumientoPedidos() {
		return seguimientoPedidos;


	}

	public List<Producto> getProductosPedido(){
		return productosPedido;
	}

}
