package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import uo.ips.muebleria.logic.cliente.Cliente;
import uo.ips.muebleria.logic.cliente.ClienteConsultas;
import uo.ips.muebleria.logic.log.Logger;
import uo.ips.muebleria.logic.trabajador.Trabajador;


public class CrearCliente extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txfDni;
	private JTextField txfNombre;
	private JTextField txfApellidos;
	private JTextField txfDireccion;
	private JTextField txfTelefono;
	private DefaultTableModel listaClientes;
	private JTextField textFieldCorreo;
	private Logger log;

	/**
	 * Create the dialog.
	 */
	public CrearCliente(DefaultTableModel lista, Trabajador usuarioActivo) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				CrearCliente.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		setResizable(false);
		setModal(true);
		setTitle("Añadir un Cliente nuevo");
		setBounds(100, 100, 600, 370);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(6, 1, 0, 5));
		setLocationRelativeTo(null);
		listaClientes = lista;
		log = new Logger();
		{
			JPanel pnDni = new JPanel();
			contentPanel.add(pnDni);
			pnDni.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel lblDni = new JLabel("DNI:");
				lblDni.setHorizontalAlignment(SwingConstants.CENTER);
				pnDni.add(lblDni);
			}
			{
				txfDni = new JTextField();
				pnDni.add(txfDni);
				txfDni.setColumns(40);
			}
		}
		{
			JPanel pnNombre = new JPanel();
			contentPanel.add(pnNombre);
			pnNombre.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel lbNombre = new JLabel("Nombre:");
				pnNombre.add(lbNombre);
				lbNombre.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				txfNombre = new JTextField();
				txfNombre.setColumns(40);
				pnNombre.add(txfNombre);
			}
		}
		{
			JPanel pnApellidos = new JPanel();
			contentPanel.add(pnApellidos);
			pnApellidos.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel lbApellidos = new JLabel("Apellidos:");
				pnApellidos.add(lbApellidos);
				lbApellidos.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				txfApellidos = new JTextField();
				txfApellidos.setColumns(40);
				pnApellidos.add(txfApellidos);
			}
		}
		{
			JPanel pnDireccion = new JPanel();
			contentPanel.add(pnDireccion);
			pnDireccion.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel lbDireccion = new JLabel("Direccion:");
				lbDireccion.setHorizontalAlignment(SwingConstants.CENTER);
				pnDireccion.add(lbDireccion);
			}
			{
				txfDireccion = new JTextField();
				txfDireccion.setColumns(40);
				pnDireccion.add(txfDireccion);
			}
		}
		{
			JPanel pnTelefono = new JPanel();
			contentPanel.add(pnTelefono);
			pnTelefono.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel lbTelefono = new JLabel("Telefono:");
				lbTelefono.setHorizontalAlignment(SwingConstants.CENTER);
				pnTelefono.add(lbTelefono);
			}
			{
				txfTelefono = new JTextField();
				txfTelefono.setColumns(40);
				pnTelefono.add(txfTelefono);
			}
		}
		{
			JPanel pnCorreo = new JPanel();
			contentPanel.add(pnCorreo);
			pnCorreo.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			{
				JLabel lbCorreo = new JLabel("Correo electrónico:");
				lbCorreo.setHorizontalAlignment(SwingConstants.CENTER);
				pnCorreo.add(lbCorreo);
			}
			{
				textFieldCorreo = new JTextField();
				textFieldCorreo.setColumns(40);
				pnCorreo.add(textFieldCorreo);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setBackground(new Color(0, 128, 0));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (camposRellenados()) {
							try {
								Cliente c = crearCliente();
								ClienteConsultas.añadirCliente(c);
								listaClientes.addRow(new String[] { c.getDni(), c.getNombre(), c.getApellidos() });
								dispose();
								log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.AsignarClienteAPresupuesto.CreacionCliente", "Creacion cliente " + c.getDni());
							} catch (NumberFormatException nfe) {
								JOptionPane.showMessageDialog(null,
										"El campo 'teléfono' debe ser un número de teléfono válido");	
							}
						}
						else {
							JOptionPane.showMessageDialog(null,
									"Debes rellenar todos los datos");	
						}
					}
				});
				{
					JButton cancelButton = new JButton("Cancel");
					cancelButton.setBackground(new Color(128, 0, 0));
					cancelButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	private boolean camposRellenados() {
		if (txfDni.getText().isEmpty() || txfNombre.getText().isEmpty() || txfApellidos.getText().isEmpty() || 
				txfDireccion.getText().isEmpty() || txfTelefono.getText().isEmpty()
				|| textFieldCorreo.getText().isEmpty())
			return false;
		return true;
	}

	private Cliente crearCliente() {
		String dni = txfDni.getText();
		String nombre = txfNombre.getText();
		String apellidos = txfApellidos.getText();
		String direccion = txfDireccion.getText();
		long telefono = Long.parseLong(txfTelefono.getText());
		String correo = textFieldCorreo.getText();
		Cliente cliente = new Cliente(dni, nombre, apellidos, direccion, telefono, correo);
		return cliente;
	}

}
