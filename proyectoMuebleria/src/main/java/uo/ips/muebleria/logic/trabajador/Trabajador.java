package uo.ips.muebleria.logic.trabajador;

import java.util.UUID;

public class Trabajador {

	protected String codigo;
	protected String dni;
	protected String nombre;
	protected String apellidos;
	protected String departamento;
	protected long telefono;
	protected String horaInicioJornada;
	protected String horaFinJornada;
	protected String usuarioTrabajador;
	protected String contraseñaTrabajador;

	public Trabajador() {
		this.codigo = UUID.randomUUID().toString();
	}

	public Trabajador(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	/**
	 * Devuelve el DNI del trabajador
	 * @return DNI del trabajador, de tipo String
	 */
	public String getDni() {
		return dni;
	}

	/**
	 * Devuelve el nombre del trabajador
	 * @return Nombre del trabajador, de tipo String
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Devuelve los apellidos del trabajador
	 * @return Apellidos del trabajador, de tipo String
	 */
	public String getApellidos() {
		return apellidos;
	}

	public String getDepartamento() {
		return departamento;
	}

	/**
	 * Devuelve el número de teléfono del trabajador
	 * @return Número de teléfono del trabajador, de tipo int
	 */
	public long getTelefono() {
		return telefono;
	}

	/**
	 * Devuelve la hora de inicio de la jornada del trabajador en forma de cadena
	 * @return Hora de inicio de la jornada del trabajador, de tipo String
	 */
	public String getHoraInicioJornada() {
		return horaInicioJornada;
	}

	/**
	 * Devuelve la hora de fin de la jornada del trabajador en forma de cadena
	 * @return Hora de fin de la jornada del trabajador, de tipo String
	 */
	public String getHoraFinJornada() {
		return horaFinJornada;
	}

	/**
	 * Devuelve el nombre de usuario del trabajador en forma de cadena
	 * @return Hora de fin de la jornada del trabajador, de tipo String
	 */
	public String getUsuarioTrabajador() {
		return usuarioTrabajador;
	}

	/**
	 * Devuelve la contraseña del trabajador en forma de cadena
	 * 
	 * @return Contraseña del trabajador, de tipo String
	 */
	public String getContraseñaTrabajador() {
		return contraseñaTrabajador;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDni(String nif) {
		this.dni = nif;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public void setTelefono(long phoneNumber) {
		this.telefono = phoneNumber;
	}

	public void setHoraInicioJornada(String horaInicioJornada) {
		this.horaInicioJornada = horaInicioJornada;
	}

	public void setHoraFinJornada(String horaFinalJornada) {
		this.horaFinJornada = horaFinalJornada;
	}
	public void setUsuarioTrabajador(String usuarioTrabajador) {
		this.usuarioTrabajador = usuarioTrabajador;
	}
	public void setContraseñaTrabajador(String contraseñaTrabajador) {
		this.contraseñaTrabajador = contraseñaTrabajador;
	}
}