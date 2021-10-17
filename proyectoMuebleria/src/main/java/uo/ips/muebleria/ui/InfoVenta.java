package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import uo.ips.muebleria.logic.log.Logger;
import uo.ips.muebleria.logic.transporte.GestionTransporte;
import uo.ips.muebleria.logic.transporte.Transportista;
import uo.ips.muebleria.logic.venta.Venta;

public class InfoVenta extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelInfoVenta;
	private JLabel lblInfoVenta;
	private JPanel panelInfo;
	private JLabel lblFechaDeEntrega;
	private JLabel lblEstado;
	private JPanel panelBotones;
	private JButton btnNewButton;
	private JPanel panelMarcarComo;
	private JLabel lblMarcarComo;
	private JPanel panel_3;
	private JButton btnEnEntrega;
	private JButton btnFinalizado;
	private JTextField txtFechaEntrega;
	private JButton btnModificarFecha;
	private JTextField txtEstadoVenta;
	private VentanaPrincipal vP;
	private Logger log;

	/**
	 * Create the frame.
	 */
	public InfoVenta(VentanaPrincipal vP) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				InfoVenta.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		this.vP = vP;
		log = new Logger();
		setTitle("Información Venta");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 470, 270);
		panelInfoVenta = new JPanel();
		panelInfoVenta.setBorder(new EmptyBorder(5, 5, 5, 5));
		panelInfoVenta.setLayout(new BorderLayout(0, 0));
		setContentPane(panelInfoVenta);
		panelInfoVenta.add(getLblInfoVenta(), BorderLayout.NORTH);
		panelInfoVenta.add(getPanelInfo(), BorderLayout.CENTER);
		panelInfoVenta.add(getPanelBotones(), BorderLayout.SOUTH);
		this.setLocationRelativeTo(null);
		
	}

	private JLabel getLblInfoVenta() {
		if (lblInfoVenta == null) {
			lblInfoVenta = new JLabel("Estado venta:" + " " + vP.getVentaSeleccionadaHistorial().getCodigoVenta());
			lblInfoVenta.setFont(new Font("Tahoma", Font.BOLD, 14));
		}
		return lblInfoVenta;
	}
	private JPanel getPanelInfo() {
		if (panelInfo == null) {
			panelInfo = new JPanel();
			panelInfo.setLayout(null);
			panelInfo.add(getLblEstado());
			panelInfo.add(getLblFechaDeEntrega());
			panelInfo.add(getTxtFechaEntrega());
			panelInfo.add(getBtnModificarFecha());
			panelInfo.add(getTxtEstadoVenta());
		}
		return panelInfo;
	}
	private JLabel getLblFechaDeEntrega() {
		if (lblFechaDeEntrega == null) {
			lblFechaDeEntrega = new JLabel("Fecha de entrega:");
			lblFechaDeEntrega.setBounds(36, 49, 111, 14);
		}
		return lblFechaDeEntrega;
	}
	private JLabel getLblEstado() {
		if (lblEstado == null) {
			lblEstado = new JLabel("Estado:");
			lblEstado.setBounds(88, 74, 59, 14);
		}
		return lblEstado;
	}
	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setLayout(new BorderLayout(0, 0));
			panelBotones.add(getBtnNewButton(), BorderLayout.WEST);
			panelBotones.add(getPanelMarcarComo(), BorderLayout.EAST);
		}
		return panelBotones;
	}
	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Atras");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					vP.inicializarHistorialVentas();
					dispose();
				}
			});
		}
		return btnNewButton;
	}
	private JPanel getPanelMarcarComo() {
		if (panelMarcarComo == null) {
			panelMarcarComo = new JPanel();
			panelMarcarComo.setLayout(new BorderLayout(0, 0));
			panelMarcarComo.add(getLblMarcarComo(), BorderLayout.NORTH);
			panelMarcarComo.add(getPanel_3(), BorderLayout.SOUTH);
		}
		return panelMarcarComo;
	}
	private JLabel getLblMarcarComo() {
		if (lblMarcarComo == null) {
			lblMarcarComo = new JLabel("Marcar como:");
		}
		return lblMarcarComo;
	}
	private JPanel getPanel_3() {
		if (panel_3 == null) {
			panel_3 = new JPanel();
			panel_3.add(getBtnEnEntrega());
			panel_3.add(getBtnFinalizado());
		}
		return panel_3;
	}
	private JButton getBtnEnEntrega() {
		if (btnEnEntrega == null) {
			btnEnEntrega = new JButton("En entrega");
			btnEnEntrega.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Venta ventaS = vP.getVentaSeleccionadaHistorial();
					if(ventaS.getEstadoVenta().equals("En entrega") || ventaS.getEstadoVenta().equals("Finalizado")) {
						JOptionPane.showMessageDialog(null, "Esta venta ya esta en estado " + ventaS.getEstadoVenta());
					}
					else if(ventaS.getEstadoVenta().equals("Retrasado")) {
						JOptionPane.showMessageDialog(null, "Esta venta ya esta retrasada, modifique la fecha de entrega para que pase a 'En entrega'");
					}else {
						actualizarEstadoVenta(ventaS, "En entrega");
					}
				}
			});
		}
		return btnEnEntrega;
	}
	protected void actualizarEstadoVenta(Venta ventaS, String estado) {
		vP.getVentaSeleccionadaHistorial().marcarComo(ventaS.getCodigoVenta(), estado);
		ventaS.setEstadoVenta(estado);
		JOptionPane.showMessageDialog(null, "La venta ha sido marcada como " + estado);
		getTxtEstadoVenta().setText(estado);
		log.saveLog(vP.getUsuarioActivo().getCodigo(), "VentanaPrincipal.HistorialVentas.InfoVenta", "Pasar estado venta " + vP.getVentaSeleccionadaHistorial() + " a " + estado);
		
		if (estado.equals("En entrega"))
			vP.enviarMailACliente(ventaS);
	}

	private JButton getBtnFinalizado() {
		if (btnFinalizado == null) {
			btnFinalizado = new JButton("Finalizado");
			btnFinalizado.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(vP.getVentaSeleccionadaHistorial().getEstadoVenta().equals("Finalizado")) {
						JOptionPane.showMessageDialog(null, "Esta venta ya esta en estado Finalizado");
					}else if(vP.getVentaSeleccionadaHistorial().getEstadoVenta().equals("Retrasado")) {
						JOptionPane.showMessageDialog(null, "Esta venta ya esta retrasada, modifique la fecha de entrega para pasar a 'En entrega'");
					}else {
						actualizarEstadoVenta(vP.getVentaSeleccionadaHistorial(), "Finalizado");	
					}
				}
			});
		}
		return btnFinalizado;
	}
	private JTextField getTxtFechaEntrega() {
		if (txtFechaEntrega == null) {
			txtFechaEntrega = new JTextField();
			txtFechaEntrega.setEditable(false);
			txtFechaEntrega.setBounds(157, 46, 86, 20);
			txtFechaEntrega.setColumns(10);
			txtFechaEntrega.setText(vP.getVentaSeleccionadaHistorial().getFechaEntregaDomicilio());
		}
		return txtFechaEntrega;
	}
	private JButton getBtnModificarFecha() {
		if (btnModificarFecha == null) {
			btnModificarFecha = new JButton("Modificar");
			btnModificarFecha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					modificarFechaHora();
				}

			});
			btnModificarFecha.setBounds(253, 45, 89, 23);
		}
		return btnModificarFecha;
	}

	private void modificarFechaHora() {
		if(!vP.getVentaSeleccionadaHistorial().getEstadoVenta().equals("Retrasado")) {
			JOptionPane.showMessageDialog(null, "No se puede modificar la fecha de una venta que no este retrasada");
		}else {
			List<Transportista> transportistas = new ArrayList<Transportista>();
			GestionTransporte gT = new GestionTransporte("");
			transportistas = gT.getTransportistas();
			String codTransportista = vP.getVentaSeleccionadaHistorial().getCodigoTransportista();
			for(Transportista t : transportistas) {
				if(t.getCodigo().equals(codTransportista)) {
					vP.setTransportista(t);
					log.saveLog(vP.getUsuarioActivo().getCodigo(), "VentanaPrincipal.HistorialVentas.InfoVenta", "Modificacion de fecha de entrega de la venta " + vP.getVentaSeleccionadaHistorial().getCodigoVenta() + " del historial de ventas");
					break;
				}
			}
			String fechaInicial = vP.getVentaSeleccionadaHistorial().getFechaEntregaDomicilio();
			ModificarFechayHoraVenta mFyHV = new ModificarFechayHoraVenta(vP);
			mFyHV.setVisible(true);
			if(!fechaInicial.equals(vP.getVentaSeleccionadaHistorial().getFechaEntregaDomicilio())) {
				actualizarEstadoVenta(vP.getVentaSeleccionadaHistorial(), "En entrega");
			}
			
		}
	}
	private JTextField getTxtEstadoVenta() {
		if (txtEstadoVenta == null) {
			txtEstadoVenta = new JTextField();
			txtEstadoVenta.setEditable(false);
			txtEstadoVenta.setBounds(157, 71, 86, 20);
			txtEstadoVenta.setColumns(10);
			txtEstadoVenta.setText(vP.getVentaSeleccionadaHistorial().getEstadoVenta());
		}
		return txtEstadoVenta;
	}

}
