package uo.ips.muebleria.logic.transporte;

import uo.ips.muebleria.logic.trabajador.Trabajador;

/**
 * Clase Transportista para transportar la información
 * de los transportistas a asignar a ventas en la base de datos
 * @author Samuel Rodríguez Ares (UO271612)
 */
public class Transportista extends Trabajador {
	
	/**
	 * Constructor del objeto de tipo Transportista
	 * @param dni DNI o código identificativo del transportista, de tipo String
	 * @param nombre nombre del transportista, de tipo String
	 * @param telefono número de teléfono del transportista, de tipo int
	 * @param horaInicioJornada hora de inicio de la jornada, de tipo String
	 * @param horaFinJornada hora de fin de la jornada, de tipo String
	 */
	public Transportista(String codigo, String dni, String nombre, String apellidos, 
			int telefono, String horaInicioJornada, String horaFinJornada) {
		
		this.codigo = codigo;
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.telefono = telefono;
		this.horaInicioJornada = horaInicioJornada;
		this.horaFinJornada = horaFinJornada;
	}
	
	
	/**
	 * Devuelve una cadena de caracteres en formato "nombre - telefono"
	 * @return String con información del transportista en formato (nombre - telefono)
	 */
	@Override
	public String toString() {
		return String.format("%s %s - %d", nombre, apellidos, telefono);
	}

}