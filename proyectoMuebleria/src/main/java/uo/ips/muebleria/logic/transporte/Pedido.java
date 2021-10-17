package uo.ips.muebleria.logic.transporte;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import uo.ips.muebleria.logic.producto.Producto;

public class Pedido {
	
	private String codigoPedido;
	private String estadoPedido;
	private String tipoPedido;
	private String fechaPedido;
	
	private List<Producto> productos = new ArrayList<Producto>();
	
	public Pedido() {
		this.codigoPedido = UUID.randomUUID().toString();
		setEstadoPedido("solicitado");
		tipoPedido = "sampleText";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-YYYY");
		String fechaHoy = sdf.format(date);
		this.fechaPedido = fechaHoy;
	}
	public Pedido(String codigoPedido, String tipo, String estadoPedido) {
		super();
		this.codigoPedido = codigoPedido;
		this.tipoPedido = tipo;
		this.estadoPedido = estadoPedido;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-YYYY");
		String fechaHoy = sdf.format(date);
		this.fechaPedido = fechaHoy;
	}

	public String getCodigoPedido() {
		return codigoPedido;
	}
	
	public String getEstadoPedido() {
		return estadoPedido;
	}
	
	public void setEstadoPedido(String estadoPedido) {
		this.estadoPedido = estadoPedido;
	}
	
	public List<Producto> getProductos() {
		return productos;
	}
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	
	public String getTipoPedido() {
		return tipoPedido;
	}
	public void setTipoPedido(String tipoPedido) {
		this.tipoPedido = tipoPedido;
	}
	
	public String getFechaPedido() {
		return fechaPedido;
	}
	
	@Override
	public String toString() {
		return "Pedido " + codigoPedido + " (" + estadoPedido + ")";
	}
}