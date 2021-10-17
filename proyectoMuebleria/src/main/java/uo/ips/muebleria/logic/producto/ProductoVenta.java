package uo.ips.muebleria.logic.producto;

public class ProductoVenta {

	private String nombre;
	private String transporte;
	private int montaje;
	private String fechaEntrega;
	private int unidades;
	private double precioProducto;


	public ProductoVenta(String nombre, String transporte, int montaje, String fechaEntrega, int unidades) {
		this.nombre = nombre;
		this.transporte = transporte;
		this.montaje = montaje;
		this.fechaEntrega = fechaEntrega;
		this.unidades = unidades;
	}
	
	public ProductoVenta(String nombre, String transporte, int montaje, String fechaEntrega, int unidades, double precioProducto) {
		this.nombre = nombre;
		this.transporte = transporte;
		this.montaje = montaje;
		this.fechaEntrega = fechaEntrega;
		this.unidades = unidades;
		this.precioProducto = precioProducto;
	}


	public String getNombre() {
		return nombre;
	}

	public int getUnidades() {
		return unidades;
	}


	public String getTransporte() {
		return transporte;
	}


	public int getMontaje() {
		return montaje;
	}


	public String getFechaEntrega() {
		return fechaEntrega;
	}

	public double getPrecioProducto() {
		return precioProducto;
	}



}
