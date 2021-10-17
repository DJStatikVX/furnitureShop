package uo.ips.muebleria.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uo.ips.muebleria.util.exception.ApplicationException;

/**
 * Utilidades varias con metodos generales de serializacion, conversion a csv y conversion de fechas
 */
public class Util {
	private Util() {
	    throw new IllegalStateException("Utility class");
	}
	
	/** 
	 * Convierte fecha repesentada como un string iso a fecha java (para conversion de entradas de tipo fecha)
	 */
	public static Date isoStringToDate(String isoDateString) {
		try {
		return new SimpleDateFormat("dd-MM-yyyy").parse(isoDateString);
		} catch (ParseException e) {
			throw new ApplicationException("Formato ISO incorrecto para fecha: "+isoDateString);
		}
	}
	
	/** 
	 * Convierte fecha java a un string formato iso (para display o uso en sql) 
	 */
	public static String dateToIsoString(Date javaDate) {
		Format formatter = new SimpleDateFormat("dd-MM-yyyy");
		return formatter.format(javaDate);
	}
	
}