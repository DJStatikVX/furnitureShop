package uo.ips.muebleria.logic.grafico;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;

public class GraficoData {

	public static final String MES = "mes";
	public static final String AÑO = "año";

	private List<VentaGrafico> ventas;
	private List<PedidoGrafico> pedidos;
	private List<VendedorGrafico> vendedores;

	public GraficoData() {
		ventas = cargarVentas();
		pedidos = cargarPedidos();
		cargarVendedores();
	}

	public List<VentaGrafico> getVentas() {
		return ventas;
	}

	public List<PedidoGrafico> getPedidos() {
		return pedidos;
	}

	public List<VendedorGrafico> getVendedores() {
		return vendedores;
	}

	private List<VentaGrafico> cargarVentas() {
		List<VentaGrafico> v = new ArrayList<>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String codigo, fecha;
		double importe;
		try {
			con = Jdbc.getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select * from venta");
			while (rs.next()) {
				codigo = rs.getString("codigoVendedor");
				fecha = rs.getString("fechaVentaCreada");
				importe = rs.getDouble("importeVentaMasMontaje");
				v.add(new VentaGrafico(codigo, fecha, importe));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, st, con);
		}
		return v;
	}

	private List<PedidoGrafico> cargarPedidos() {
		List<PedidoGrafico> v = new ArrayList<>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String fecha;
		double importe;
		try {
			con = Jdbc.getConnection();
			st = con.createStatement();
			rs = st.executeQuery(
					"select sum(unidadesPedido*precioProductoPedido) total, fechaPedido from pedido natural join contienePedido group by fechaPedido, codigoPedido");
			while (rs.next()) {
				fecha = rs.getString("fechaPedido");
				importe = rs.getDouble("total");
				v.add(new PedidoGrafico(fecha, importe));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, st, con);
		}
		return v;
	}

	private void cargarVendedores() {
		vendedores = new ArrayList<>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String codigo, nombre, apellido;
		VendedorGrafico v;
		try {
			con = Jdbc.getConnection();
			st = con.createStatement();
			rs = st.executeQuery("select * from vendedor,trabajador where vendedor.codigoVendedor = trabajador.codigoTrabajador");
			while (rs.next()) {
				codigo = rs.getString("codigoTrabajador");
				nombre = rs.getString("nombreTrabajador");
				apellido = rs.getString("apellidosTrabajador");
				v = new VendedorGrafico(codigo, nombre, apellido);
				for (VentaGrafico venta : ventas) {
					if (venta.getCodigoVendedor().equals(codigo))
						v.getVentas().add(venta);
				}
				vendedores.add(v);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Jdbc.close(rs, st, con);
		}
	}

	public List<VentaGrafico> getVentasDespues(LocalDate date) {
		List<VentaGrafico> result = new ArrayList<>();
		for (VentaGrafico v : ventas) {
			if (v.getFecha().isAfter(date))
				result.add(v);
		}
		return result;
	}

	public List<PedidoGrafico> getPedidosDespues(LocalDate date) {
		List<PedidoGrafico> result = new ArrayList<>();
		for (PedidoGrafico v : pedidos) {
			if (v.getFecha().isAfter(date))
				result.add(v);
		}
		return result;
	}

	public double getGeneradoDespuesDe(List<VentaGrafico> list, LocalDate o) {
		double total = 0;
		for (VentaGrafico v : list) {
			if (v.getFecha().isAfter(o))
				total += v.getPrecio();
		}
		return total;
	}

	public double getGeneradoVentasDespuesDe(LocalDate o) {
		return getGeneradoDespuesDe(ventas, o);
	}

	public double getGastadoPedidosDespuesDe(LocalDate o) {
		double total = 0;
		for (PedidoGrafico v : pedidos) {
			if (v.getFecha().isAfter(o))
				total += v.getPrecio();
		}
		return total;
	}

	public class VendedorGrafico {

		private List<VentaGrafico> ventas;
		private String codigo;
		private String nombre;
		private String apellido;

		public VendedorGrafico(String codigo, String nombre, String apellido) {
			this.ventas = new ArrayList<>();
			this.codigo = codigo;
			this.nombre = nombre;
			this.apellido = apellido;
		}

		public List<VentaGrafico> getVentas() {
			return ventas;
		}

		public String getCodigo() {
			return codigo;
		}

		public String getNombre() {
			return nombre;
		}

		public String getApellido() {
			return apellido;
		}

		@Override
		public String toString() {
			return "VendedorGrafico [codigo=" + codigo + ", nombre=" + nombre + ", apellido=" + apellido + "]";
		}
	}

	public class VentaGrafico {

		private String codigoVendedor;
		private LocalDate fecha;
		private double precio;

		public VentaGrafico(String codigoVendedor, String fecha, double precio) {
			super();
			this.codigoVendedor = codigoVendedor;
			String[] f = fecha.split("-");
			this.fecha = LocalDate.of(Integer.parseInt(f[2]), Integer.parseInt(f[1]), Integer.parseInt(f[0]));
			this.precio = precio;
		}

		public String getCodigoVendedor() {
			return codigoVendedor;
		}

		public LocalDate getFecha() {
			return fecha;
		}

		public double getPrecio() {
			return precio;
		}

		@Override
		public String toString() {
			return "VentaGrafico [codigoVendedor=" + codigoVendedor + ", fecha=" + fecha + ", precio=" + precio + "]";
		}

	}

	public class PedidoGrafico {

		private LocalDate fecha;
		private double precio;

		public PedidoGrafico(String fecha, double precio) {
			super();
			String[] f = fecha.split("-");
			this.fecha = LocalDate.of(Integer.parseInt(f[2]), Integer.parseInt(f[1]), Integer.parseInt(f[0]));
			this.precio = precio;
		}

		public LocalDate getFecha() {
			return fecha;
		}

		public double getPrecio() {
			return precio;
		}

		@Override
		public String toString() {
			return "PedidoGrafico [fecha=" + fecha + ", precio=" + precio + "]";
		}

	}

}
