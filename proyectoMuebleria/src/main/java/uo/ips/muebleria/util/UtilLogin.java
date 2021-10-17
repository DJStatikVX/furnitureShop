package uo.ips.muebleria.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.trabajador.Trabajador;

public class UtilLogin {
	
	private static final String SQL_RECUPERAR_USUARIOS =
			"SELECT USUARIOTRABAJADOR FROM TRABAJADOR";
	
	private static final String SQL_RECUPERAR_CONTRASEÑA =
			"SELECT CONTRASENATRABAJADOR FROM TRABAJADOR "
			+ "WHERE USUARIOTRABAJADOR = ?";
	
	private static final String SQL_RECUPERAR_DATOS_TRABAJADOR =
			"SELECT CODIGOTRABAJADOR, DEPARTAMENTOTRABAJADOR FROM TRABAJADOR " +
					"WHERE USUARIOTRABAJADOR = ?";
	
	public static List<String> recuperarUsuarios() {
		Connection c = null;
		Statement st = null;
		ResultSet rs = null;
		
		List<String> usuarios = new ArrayList<>();
		
		try {
			try {
				c = Jdbc.getConnection();
				st = c.createStatement();
				rs = st.executeQuery(SQL_RECUPERAR_USUARIOS);
				
				while (rs.next()) {
					usuarios.add(rs.getString("USUARIOTRABAJADOR"));
				}
				
			} finally {
				rs.close();
				st.close();
				c.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return usuarios;
	}
	
	public static String encriptarContraseña(String contraseña) {
		String algoritmo = "SHA-256";
		StringBuffer hexadecimal = null;
		
		try {
			MessageDigest digest = MessageDigest.getInstance(algoritmo);
			byte[] hash = digest.digest(contraseña.getBytes(StandardCharsets.UTF_8));
			hexadecimal = new StringBuffer();
			
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexadecimal.append("0");
				hexadecimal.append(hex);
			}
			
		} catch (NoSuchAlgorithmException e) {
			System.err.println("No se encontró el algoritmo " + algoritmo);
		}
		
		String encriptada = null;
		
		if (hexadecimal != null) {
			encriptada = hexadecimal.toString();
		}
		
		return encriptada;
	}
	
	public static String recuperarContraseña(String usuario) {
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		String contraseña = null;
		
		try {
			try {
				c = Jdbc.getConnection();
				pst = c.prepareStatement(SQL_RECUPERAR_CONTRASEÑA);
				pst.setString(1, usuario);
				rs = pst.executeQuery();
				
				rs.next();
				
				contraseña = rs.getString("CONTRASENATRABAJADOR");
				
			} finally {
				rs.close();
				pst.close();
				c.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return contraseña;
	}
	
	public static Trabajador recuperarDatosTrabajador(String usuario) {
		Connection c = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		Trabajador t = new Trabajador();
		
		try {
			try {
				c = Jdbc.getConnection();
				pst = c.prepareStatement(SQL_RECUPERAR_DATOS_TRABAJADOR);
				pst.setString(1, usuario);
				rs = pst.executeQuery();
				
				rs.next();
				
				t.setCodigo(rs.getString("CODIGOTRABAJADOR"));
				t.setDepartamento(rs.getString("DEPARTAMENTOTRABAJADOR"));
				
			} finally {
				rs.close();
				pst.close();
				c.close();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		t.setUsuarioTrabajador(usuario);
		
		return t;
	}

}