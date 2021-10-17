package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import uo.ips.muebleria.logic.trabajador.Trabajador;
import uo.ips.muebleria.util.UtilLogin;

public class DialogoInicioSesion extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblNombreUsuario;
	private JTextField textFieldNombreUsuario;
	private JLabel lblContraseña;
	private JPasswordField textFieldContraseña;
	private JLabel lblIconoUsuario;
	private JLabel lblIconoContraseña;
	
	private Trabajador usuarioActivo;
	
	private WindowAdapter terminarAplicacion;

	/**
	 * Create the dialog.
	 */
	public DialogoInicioSesion() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DialogoInicioSesion.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("Mueblería - Acceso");
		setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 430, 230);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		contentPanel.add(getLblNombreUsuario());
		contentPanel.add(getTextFieldNombreUsuario());
		contentPanel.add(getLblContraseña());
		contentPanel.add(getTextFieldContraseña());
		contentPanel.add(getLblIconoUsuario());
		contentPanel.add(getLblIconoContraseña());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Iniciar Sesión");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (comprobarCampos()) {
							eliminarEventoCierre();
							dispose();
							darBienvenida();
							lanzarVentana();
						}
					}
				});
				okButton.setBackground(new Color(0, 128, 0));
				okButton.setForeground(Color.WHITE);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Salir");
				cancelButton.setBackground(new Color(128, 0, 0));
				cancelButton.setForeground(Color.WHITE);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		terminarAplicacion = new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		};
		
		this.addWindowListener(terminarAplicacion);
	}
	
	private void eliminarEventoCierre() {
		this.removeWindowListener(terminarAplicacion);
	}
	
	private void lanzarVentana() {
		Runnable r = new Runnable() {
			public void run() {
				VentanaPrincipal vp = new VentanaPrincipal(usuarioActivo);
				vp.setVisible(true);
			}
		};

		new Thread(r).start();
	}
	
	private JLabel getLblNombreUsuario() {
		if (lblNombreUsuario == null) {
			lblNombreUsuario = new JLabel("Nombre de usuario:");
			lblNombreUsuario.setFont(new Font("Verdana", Font.PLAIN, 11));
			lblNombreUsuario.setBounds(35, 45, 127, 14);
		}
		return lblNombreUsuario;
	}
	private JTextField getTextFieldNombreUsuario() {
		if (textFieldNombreUsuario == null) {
			textFieldNombreUsuario = new JTextField();
			textFieldNombreUsuario.setFont(new Font("Verdana", Font.PLAIN, 11));
			textFieldNombreUsuario.setBounds(172, 42, 140, 20);
			textFieldNombreUsuario.setColumns(10);
		}
		return textFieldNombreUsuario;
	}
	private JLabel getLblContraseña() {
		if (lblContraseña == null) {
			lblContraseña = new JLabel("Contraseña:");
			lblContraseña.setFont(new Font("Verdana", Font.PLAIN, 11));
			lblContraseña.setBounds(78, 95, 80, 14);
		}
		return lblContraseña;
	}
	private JPasswordField getTextFieldContraseña() {
		if (textFieldContraseña == null) {
			textFieldContraseña = new JPasswordField();
			textFieldContraseña.setFont(new Font("Verdana", Font.PLAIN, 11));
			textFieldContraseña.setColumns(10);
			textFieldContraseña.setBounds(172, 93, 140, 20);
		}
		return textFieldContraseña;
	}
	private JLabel getLblIconoUsuario() {
		if (lblIconoUsuario == null) {
			lblIconoUsuario = new JLabel("");
			lblIconoUsuario.setIcon(new ImageIcon(new ImageIcon(
					DialogoInicioSesion.class.getResource("/uo/ips/muebleria/img/user.png"))
					.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH)));
			lblIconoUsuario.setBounds(322, 34, 35, 35);
		}
		return lblIconoUsuario;
	}
	private JLabel getLblIconoContraseña() {
		if (lblIconoContraseña == null) {
			lblIconoContraseña = new JLabel("");
			lblIconoContraseña.setIcon(new ImageIcon(new ImageIcon(
					DialogoInicioSesion.class.getResource("/uo/ips/muebleria/img/password.png"))
					.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
			lblIconoContraseña.setBounds(327, 86, 35, 35);
		}
		return lblIconoContraseña;
	}
	
	@SuppressWarnings("deprecation")
	private boolean comprobarCampos() {
		String usuario = textFieldNombreUsuario.getText();
		String contraseña = textFieldContraseña.getText();
		
		if (usuario.isBlank() || usuario.isEmpty()) {
			JOptionPane.showMessageDialog(null, 
					"El nombre de usuario proporcionado es vacío o inválido. "
					+ "Por favor, inténtelo de nuevo.", "Información", 
					JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		List<String> usuarios = UtilLogin.recuperarUsuarios();
		
		boolean existeUsuario = false;
		
		for (String u : usuarios) {
			if (usuario.equals(u))
				existeUsuario = true;
		}
		
		if (!existeUsuario) {
			JOptionPane.showMessageDialog(null, 
					"El nombre de usuario proporcionado no existe. "
					+ "Por favor, inténtelo de nuevo.", "Información", 
					JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		if (contraseña.isBlank() || contraseña.isEmpty()) {
			JOptionPane.showMessageDialog(null, 
					"La contraseña proporcionada es vacía o inválida. "
					+ "Por favor, inténtelo de nuevo.", "Información", 
					JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		String contraseñaCorrecta = UtilLogin.recuperarContraseña(usuario);
		
		if (!UtilLogin.encriptarContraseña(contraseña).equals(contraseñaCorrecta)) {
			JOptionPane.showMessageDialog(null, 
					"La contraseña proporcionada es incorrecta. "
					+ "Por favor, inténtelo de nuevo.", "Información", 
					JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		recuperarDatosTrabajador(usuario);
				
		return true;
	}
	
	private void recuperarDatosTrabajador(String usuario) {
		usuarioActivo = UtilLogin.recuperarDatosTrabajador(usuario);
	}
	
	private void darBienvenida() {
		JOptionPane.showMessageDialog(null, "¡Bienvenid@, " + usuarioActivo.getUsuarioTrabajador() + "! "
				+ "Has iniciado sesión como " + usuarioActivo.getDepartamento() 
				+ ".", "Inicio de sesión con éxito", JOptionPane.INFORMATION_MESSAGE);
	}
	
}