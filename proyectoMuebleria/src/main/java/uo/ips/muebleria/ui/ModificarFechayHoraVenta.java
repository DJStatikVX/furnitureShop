package uo.ips.muebleria.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ModificarFechayHoraVenta extends DialogoFechaHoraEntrega  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VentanaPrincipal vp;

	/**
	 * Create the dialog.
	 */
	public ModificarFechayHoraVenta(VentanaPrincipal vp) {
		super(vp);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ModificarFechayHoraVenta.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		this.vp = vp;
		
	}

	@Override
	public JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton("Confirmar");
			okButton.setForeground(new Color(255, 255, 255));
			okButton.setBackground(new Color(0, 128, 0));
			okButton.setFont(new Font("Verdana", Font.BOLD, 12));
			okButton.setActionCommand("Confirmar");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (comprobarFechaEntrega() && comprobarHoraEntrega()) {
						pedirConfirmacion();
					}
				}
			});
		}
		
		return okButton;
	}
	@Override
	protected void pedirConfirmacion() {
		if (JOptionPane.showConfirmDialog(this, "¿Confirmas la modificacion de la nueva fecha y hora de entrega?", 
				"Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
			vp.reasignarTransporte(textFieldFechaEntrega.getText(), textFieldHoraEntrega.getText());
			dispose();
	}
	}
}
