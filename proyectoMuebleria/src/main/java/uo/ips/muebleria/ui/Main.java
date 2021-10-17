package uo.ips.muebleria.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;

import uo.ips.muebleria.util.db.Database;
import java.awt.Toolkit;

/**
 * Punto de entrada principal que incluye botones para la ejecucion de las
 * pantallas de las aplicaciones de ejemplo y acciones de inicializacion de la
 * base de datos. No sigue MVC pues es solamente temporal para que durante el
 * desarrollo se tenga posibilidad de realizar acciones de inicializacion
 */
public class Main {

	private JFrame frmMuebleria;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() { // NOSONAR codigo autogenerado
			public void run() {
				try {
					Properties props = new Properties();
					props.put("logoString", "Muebleria IPS");
					AluminiumLookAndFeel.setCurrentTheme(props);
					UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
					Main window = new Main();
					window.frmMuebleria.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace(); // NOSONAR codigo autogenerado
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMuebleria = new JFrame();
		frmMuebleria.setIconImage(Toolkit.getDefaultToolkit().getImage(
				Main.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		frmMuebleria.setTitle("Muebleria");
		frmMuebleria.setBounds(0, 0, 300, 185);
		frmMuebleria.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMuebleria.setLocationRelativeTo(null);

		JButton btnEjecutarAplicacion = new JButton("Ejecutar Aplicación");
		btnEjecutarAplicacion.setFocusPainted(false);
		btnEjecutarAplicacion.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnEjecutarAplicacion.addActionListener(new ActionListener() { // NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				iniciarApp();
			}
		});
		frmMuebleria.getContentPane().setLayout(new GridLayout(0, 1, 10, 10));
		frmMuebleria.getContentPane().add(btnEjecutarAplicacion);

		JButton btnInicializarBaseDeDatos = new JButton("Inicializar Base de Datos en Blanco");
		btnInicializarBaseDeDatos.setFocusPainted(false);
		btnInicializarBaseDeDatos.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnInicializarBaseDeDatos.addActionListener(new ActionListener() { // NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				iniciarBDVacia();
			}
		});
		frmMuebleria.getContentPane().add(btnInicializarBaseDeDatos);

		JButton btnCargarDatosIniciales = new JButton("Cargar Datos Iniciales para Pruebas");
		btnCargarDatosIniciales.setFocusPainted(false);
		btnCargarDatosIniciales.setFont(new Font("Times New Roman", Font.BOLD, 16));
		btnCargarDatosIniciales.addActionListener(new ActionListener() { // NOSONAR codigo autogenerado
			public void actionPerformed(ActionEvent e) {
				cargarDatosPrueba();
			}
		});
		frmMuebleria.getContentPane().add(btnCargarDatosIniciales);
	}

	private void iniciarApp() {
//		VentanaPrincipal v = new VentanaPrincipal(this);
//		v.setLocationRelativeTo(null);
		frmMuebleria.setVisible(false);
//		v.setVisible(true);
		DialogoInicioSesion dis = new DialogoInicioSesion();
		dis.setLocationRelativeTo(null);
		dis.setVisible(true);
	}

	private void iniciarBDVacia() {
		Database db = new Database();
		db.createDatabase(false);
	}

	private void cargarDatosPrueba() {
		Database db = new Database();
		db.createDatabase(false);
		db.loadDatabase();
	}
}
