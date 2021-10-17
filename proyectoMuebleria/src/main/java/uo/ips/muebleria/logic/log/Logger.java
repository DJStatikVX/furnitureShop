package uo.ips.muebleria.logic.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	private boolean debug = false;
	

	public void saveLog(String usuario, String ventana, String accion){
		try {
			
				Date date = new Date();
				DateFormat fecha = new SimpleDateFormat("dd-MM-yyyy");
				
				File fichero = new File("Logs/log - " + usuario + " - " + fecha.format(date) + ".txt");	
				
		        if(!fichero.exists()) {
		        	fichero.createNewFile();
			        BufferedWriter Fescribe=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichero,true)));
					Fescribe.write(registraAccion(usuario, ventana, accion) + "\n");
					Fescribe.close();
					
		        }else if(accion.equals("Cerrar sesion")) {
		        	File ficheroCerrado = new File("Logs/log - " + usuario + " - " + fecha.format(date) + " - sesion "+ randomInt() + ".txt");
		        	fichero.renameTo(ficheroCerrado);
		        	BufferedWriter Fescribe=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ficheroCerrado,true)));
					Fescribe.write(registraAccion(usuario, ventana, accion) + "\n");
		        	Fescribe.close();
		        	
		        }else {
		        	BufferedWriter Fescribe=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fichero,true)));
					Fescribe.write(registraAccion(usuario, ventana, accion) + "\n");
					Fescribe.close();
		        }
		       
			}

		catch (FileNotFoundException fnfe) {
		      System.out.println("El archivo no se ha podido guardar");
		    }
		catch (IOException ioe) {
		      throw new RuntimeException("Error de entrada/salida");
		
		}
	  }
	
	private String registraAccion(String usuario, String ventana, String accion) {
		Date date = new Date();
		DateFormat fecha = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		String log = "[" + fecha.format(date) + "] "+  usuario + " " + ventana + " - " + accion;
		if(debug)
			System.out.println(log);
		return log;
		
	}
	private int randomInt() {
		int i = (int) (Math.random()*10000);
		return i;
	}
}
