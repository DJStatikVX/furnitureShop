package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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

public class AsignarUsuarioContraseña extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txUsuario;
	private JPasswordField txContraseña;

	private Trabajador trabajador;

	/**
	 * Create the dialog.
	 */
	public AsignarUsuarioContraseña(Trabajador trabajadorACrear) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AsignarUsuarioContraseña.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		trabajador = trabajadorACrear;
		setModal(true);
		setBounds(100, 100, 432, 227);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(2, 1, 0, 0));
		{
			JPanel pnInicio = new JPanel();
			contentPanel.add(pnInicio);
			pnInicio.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				pnInicio.add(panel);
				{
					JLabel lblHoraDeInicio = new JLabel("Nombre de usuario:");
					panel.add(lblHoraDeInicio);
				}
			}
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				pnInicio.add(panel);
				{
					txUsuario = new JTextField();
					panel.add(txUsuario);
					txUsuario.setColumns(15);
					if (trabajador.getUsuarioTrabajador() != null) {
						txUsuario.setText(trabajador.getUsuarioTrabajador());
					}
				}
			}
		}
		{
			JPanel pnFin = new JPanel();
			contentPanel.add(pnFin);
			pnFin.setLayout(new GridLayout(0, 2, 0, 0));
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				pnFin.add(panel);
				{
					JLabel lblHoraFinal = new JLabel("Contraseña:");
					panel.add(lblHoraFinal);
				}
			}
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				pnFin.add(panel);
				{
					txContraseña = new JPasswordField();
					panel.add(txContraseña);
					txContraseña.setColumns(15);
					if (trabajador.getContraseñaTrabajador() != null) {
						txContraseña.setText(trabajador.getContraseñaTrabajador());
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Aceptar");
				okButton.addActionListener(new ActionListener() {
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
						if (comprobarCampos()) {
							trabajador.setUsuarioTrabajador(txUsuario.getText());
							trabajador.setContraseñaTrabajador(txContraseña.getText());
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	@SuppressWarnings("deprecation")
	private boolean comprobarCampos() {
		String usuario = txUsuario.getText();
		String contraseña = txContraseña.getText();
		
		if (usuario.isBlank() || usuario.isEmpty()) {
			JOptionPane.showMessageDialog(null, 
					"El nombre de usuario proporcionado es vacío o inválido. "
					+ "Por favor, inténtelo de nuevo.", "Información", 
					JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		List<String> usuarios = UtilLogin.recuperarUsuarios();
		
		for (String u : usuarios) {
			if (usuario.equals(u)) {
				JOptionPane.showMessageDialog(null, 
						"El nombre de usuario proporcionado ya se encuentra registrado. "
						+ "Por favor, inténtelo de nuevo.", "Información", 
						JOptionPane.ERROR_MESSAGE);
				
				return false;
			}
		}
		
		if (contraseña.isBlank() || contraseña.isEmpty()) {
			JOptionPane.showMessageDialog(null, 
					"La contraseña proporcionada es vacía o inválida. "
					+ "Por favor, inténtelo de nuevo.", "Información", 
					JOptionPane.ERROR_MESSAGE);
			
			return false;
		}
		
		return true;
	}

}
