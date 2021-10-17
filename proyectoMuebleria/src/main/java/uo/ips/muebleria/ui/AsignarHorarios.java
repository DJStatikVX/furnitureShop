package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import uo.ips.muebleria.logic.trabajador.Trabajador;
import java.awt.Toolkit;

public class AsignarHorarios extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txInicio;
	private JTextField txFinal;

	private Trabajador trabajador;

	/**
	 * Create the dialog.
	 */
	public AsignarHorarios(Trabajador t) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AsignarHorarios.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		trabajador = t;
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
					JLabel lblHoraDeInicio = new JLabel("Hora de inicio:");
					panel.add(lblHoraDeInicio);
				}
			}
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				pnInicio.add(panel);
				{
					txInicio = new JTextField();
					panel.add(txInicio);
					txInicio.setColumns(15);
					if (trabajador.getHoraInicioJornada()!=null) {
						txInicio.setText(trabajador.getHoraInicioJornada());
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
					JLabel lblHoraFinal = new JLabel("Hora de final de jornada:");
					panel.add(lblHoraFinal);
				}
			}
			{
				JPanel panel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) panel.getLayout();
				flowLayout.setAlignment(FlowLayout.LEFT);
				pnFin.add(panel);
				{
					txFinal = new JTextField();
					panel.add(txFinal);
					txFinal.setColumns(15);
					if (trabajador.getHoraFinJornada()!=null) {
						txFinal.setText(trabajador.getHoraFinJornada());
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (comprobarCampos()) {
							trabajador.setHoraInicioJornada(txInicio.getText());
							trabajador.setHoraFinJornada(txFinal.getText());
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
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

	private boolean comprobarCampos() {
		if (txInicio.getText().isBlank() || txFinal.getText().isBlank()) {
			JOptionPane.showMessageDialog(null, "Debes rellenar todos los campos");
			return false;
		} else {
			try {
				String[] horaMinInicio = txInicio.getText().split(":");
				String[] horaMinFinal = txFinal.getText().split(":");
				
				if (horaMinInicio.length != 2 || horaMinFinal.length != 2 
						|| horaMinInicio[0].length() != 2 || horaMinFinal[0].length() != 2
						|| horaMinInicio[1].length() != 2 || horaMinFinal[1].length() != 2) {
					throw new NumberFormatException();
				}
				
				long horaIn = Long.parseLong(horaMinInicio[0]);
				long minIn = Long.parseLong(horaMinInicio[1]);
				long horaFin = Long.parseLong(horaMinFinal[0]);
				long minFin = Long.parseLong(horaMinFinal[1]);
				
				if (horaIn<0 || horaIn>23 || minIn<0 || minIn>59
						|| horaFin<0 || horaFin>23 || minFin<0 || minFin>59) {
					JOptionPane.showMessageDialog(null, "Por favor, introduzca una hora válida");
					return false;
				}
				
				if (horaIn>horaFin) {
					throw new IllegalArgumentException();
				}
				else {
					if (horaIn==horaFin && minIn>=minFin) {
						throw new IllegalArgumentException();
					}
				}
				
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "Los campos deben estar en el formato 'HH:MM'");
				return false;
			} catch (IllegalArgumentException e) {
				JOptionPane.showMessageDialog(null, "La hora de inicio debe ser menor que la hora del final de la jornada");
				return false;
			}
			return true;
		}
	}

}
