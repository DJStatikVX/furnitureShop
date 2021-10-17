package uo.ips.muebleria.logic.cliente;

public class Cliente {
	
	private String dni;
	private String nombre;
	private String apellidos;
	private String direccion;
	private long telefono;
	private String correo;
	
	public Cliente(String dni, String nombre, String apellidos, String direccion, long telefono, String correo) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.telefono = telefono;
		this.correo = correo;
	}
	
	public Cliente(String nombre, String apellidos, long telefono, String correo) {
		this.nombre = nombre;
		this.apellidos = apellidos;;
		this.telefono = telefono;
		this.correo = correo;
	}
	
	public Cliente() {
	}

	public String getDni() {
		return dni;
	}
	public String getNombre() {
		return nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public String getDireccion() {
		return direccion;
	}
	public long getTelefono() {
		return telefono;
	}
	public String getCorreo() {
		return correo;
	}
	
	public String toString() {
		String cadena = "";
		
		cadena+= dni + ", " + nombre + " " + apellidos;
		
		return cadena;
	}

}
