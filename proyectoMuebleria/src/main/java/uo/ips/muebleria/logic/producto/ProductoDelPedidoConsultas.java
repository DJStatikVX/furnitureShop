package uo.ips.muebleria.logic.producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.transporte.Pedido;

public class ProductoDelPedidoConsultas {
	
	private final static String SQL_SELECCIONAR_PRODUCTOS_PROVEEDOR = "SELECT p.codigoProducto, p.nombreProducto, p.categoriaProducto, pp.precioProductoProveedor from Producto p, ProveedorProducto pp where p.codigoProducto = pp.codigoProducto and pp.codigoProveedor = ?";
	private final static String SQL_AÑADIR_PEDIDO = "insert into pedido values(?, ?, ?, ?, ?)";
	private final static String SQL_AÑADIR_CONTIENEPEDIDO = "insert into contienepedido values(?, ?, ?, ?)";
	private final static String SQL_GET_PRECIO_PROVEEDOR = "select precioProductoProveedor from ProveedorProducto where codigoProducto = ?";
	
	private final static String codigoProveedor = "001";
	
	public static List<Producto> getProductosDeProveedor() {
		List<Producto> productos = new ArrayList<Producto>();
		Producto producto;
		
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_SELECCIONAR_PRODUCTOS_PROVEEDOR);
			pst.setString(1, codigoProveedor);
			rs = pst.executeQuery();
			
			while (rs.next()) {
				producto = new Producto(
						rs.getString("codigoProducto"),
						rs.getString("nombreProducto"), 
						rs.getString("categoriaProducto"), 
						Double.parseDouble(rs.getString("precioProductoProveedor"))
						);
				productos.add(producto);
			}
			
			rs.close();
			pst.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return productos;
	}
	
	public static void añadirPedido(Pedido pedido) {
		Connection c = null;
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		
		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_AÑADIR_PEDIDO);
			pst.setString(1, pedido.getCodigoPedido());
			pst.setString(2, pedido.getEstadoPedido());
			pst.setString(3, pedido.getTipoPedido());
			pst.setString(4, codigoProveedor);
			pst.setString(5, pedido.getFechaPedido());

			pst.execute();

			for (Producto p : pedido.getProductos()) {
				pst2 = c.prepareStatement(SQL_AÑADIR_CONTIENEPEDIDO);
				pst2.setString(1, p.getCodigo());
				pst2.setString(2, pedido.getCodigoPedido());
				pst2.setInt(3, p.getUnidades());
				pst2.setDouble(4, p.getPrecio() / p.getUnidades());
				pst2.execute();
			}
			
			
			pst2.close();
			pst.close();
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static double getPrecioProveedor(String codigoProducto) {
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		double precio = 0.0;
		
		try {
			c = Jdbc.getConnection();
			pst = c.prepareStatement(SQL_GET_PRECIO_PROVEEDOR);
			pst.setString(1, codigoProducto);
			
			rs = pst.executeQuery();
			
			rs.next();
			
			precio = rs.getDouble("PRECIOPRODUCTOPROVEEDOR");
			
			rs.close();
			pst.close();
			c.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return precio;
	}
	
}
