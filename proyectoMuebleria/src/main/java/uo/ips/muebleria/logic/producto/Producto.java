package uo.ips.muebleria.logic.producto;

public class Producto {

	private String codigo;
	private String nombre;
	private String tipo;

	private double precio;

	private int unidades;


	public Producto(String codigo, String nombre, String tipo, int unidades, double precio) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.tipo = tipo;
		this.unidades = unidades;
		this.precio = precio;
	}
	
	public Producto(String codigo, String nombre, String tipo, double precio) {
		super();
		this.codigo = codigo;
		this.nombre = nombre;
		this.tipo = tipo;
		this.precio = precio;
		this.unidades = 1;
	}



	public Producto(String nombre) {
		super();
		this.nombre = nombre;
		this.unidades = 1;
	}

	public Producto(String nombre, int unidades) {
		this.nombre = nombre;
		this.unidades = unidades;
	}



	public String getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	public double getPrecio() {
		return precio;
	}

	@Override
	public String toString() {
		return unidades != 1 ? (nombre + " (" + unidades + " uds., " + precio 
				+ " €/ud)") 
				: (nombre + " (" + unidades + " ud., " + precio + " €/ud)");
	}

	public Producto copy() {
		return new Producto(codigo, nombre, tipo, unidades, precio);
	}
}
