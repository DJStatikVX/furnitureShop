package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import uo.ips.muebleria.logic.log.Logger;

public class DialogoFechaHoraEntrega extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private VentanaPrincipal vp;
	private Logger log;
	protected HintTextField textFieldFechaEntrega;
	protected HintTextField textFieldHoraEntrega;
	
	protected JButton okButton;
	
	private boolean checkOption = true;
	
	class HintTextField extends JTextField implements FocusListener {

		  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final String hint;
		  private boolean showingHint;

		  public HintTextField(final String hint) {
		    super(hint);
		    super.setForeground(Color.GRAY);
		    this.hint = hint;
		    this.showingHint = true;
		    super.addFocusListener(this);
		  }

		  public void focusGained(FocusEvent e) {
		    if(this.getText().isEmpty()) {
		      super.setText("");
		      super.setForeground(Color.BLACK);
		      showingHint = false;
		    }
		  }
		  public void focusLost(FocusEvent e) {
		    if(this.getText().isEmpty()) {
		      super.setText(hint);
		      super.setForeground(Color.GRAY);
		      showingHint = true;
		    }
		  }

		  @Override
		  public String getText() {
		    return showingHint ? "" : super.getText();
		  }
		  
		  public String getHint() {
			  return hint;
		  }
		}

	/**
	 * Create the dialog.
	 */
	public DialogoFechaHoraEntrega(VentanaPrincipal vp) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DialogoFechaHoraEntrega.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		setTitle("Concertar Fecha y Hora de Entrega");
		this.vp = vp;
		log = new Logger();
		
		setBounds(100, 100, 500, 200);
		setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblHorarioTransportista = new JLabel(String.format(
					"%s reparte de lunes a sábado (%s - %s)",
					vp.getTransportista().getNombre(), 
					vp.getTransportista().getHoraInicioJornada(), 
					vp.getTransportista().getHoraFinJornada()));
			lblHorarioTransportista.setFont(new Font("Verdana", Font.BOLD, 12));
			
			contentPanel.add(lblHorarioTransportista, BorderLayout.NORTH);
			{
				JPanel panelFormularioEntrega = new JPanel();
				panelFormularioEntrega.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
				contentPanel.add(panelFormularioEntrega, BorderLayout.CENTER);
				panelFormularioEntrega.setLayout(null);
				{
					JLabel lblFechaEntrega = new JLabel("Fecha de Entrega:");
					lblFechaEntrega.setFont(new Font("Verdana", Font.BOLD, 12));
					lblFechaEntrega.setBounds(97, 11, 131, 43);
					panelFormularioEntrega.add(lblFechaEntrega);
				}
				{
					textFieldFechaEntrega = new HintTextField("DD-MM-YYYY");
					textFieldFechaEntrega.setFont(new Font("Monospaced", Font.PLAIN, 12));
					textFieldFechaEntrega.setBounds(250, 23, 100, 20);
					panelFormularioEntrega.add(textFieldFechaEntrega);
					textFieldFechaEntrega.setColumns(10);
				}
				{
					JLabel lblHoraEntrega = new JLabel("Hora de Entrega:");
					lblHoraEntrega.setFont(new Font("Verdana", Font.BOLD, 12));
					lblHoraEntrega.setBounds(97, 46, 130, 43);
					panelFormularioEntrega.add(lblHoraEntrega);
				}
				{
					textFieldHoraEntrega = new HintTextField("HH:MM");
					textFieldHoraEntrega.setFont(new Font("Monospaced", Font.PLAIN, 12));
					textFieldHoraEntrega.setBounds(250, 58, 100, 20);
					panelFormularioEntrega.add(textFieldHoraEntrega);
					textFieldHoraEntrega.setColumns(10);
				}
			}
			
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setForeground(new Color(255, 255, 255));
				cancelButton.setBackground(new Color(128, 0, 0));
				cancelButton.setFont(new Font("Verdana", Font.BOLD, 12));
				cancelButton.setActionCommand("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						checkOption = false;
						dispose();
					}
				});
				
				buttonPane.add(cancelButton);
				buttonPane.add(getOkButton());
				getRootPane().setDefaultButton(okButton);
			}
			
		}
	}
}

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
	
	protected void pedirConfirmacion() {
		if (JOptionPane.showConfirmDialog(this, "¿Confirmas la asignación del transportista "
				+ vp.getTransportista().getNombre() + " a esta venta en la fecha y hora seleccionadas?", 
				"Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
			vp.asignarTransporte(textFieldFechaEntrega.getText(), textFieldHoraEntrega.getText());
			vp.confirmarProductosVenta();
			vp.marcarPresupuestoTramitado();
			log.saveLog(vp.getUsuarioActivo().getCodigo(),
					"VentanaPrincipal.AsignarTransporte.DialogoFechaHoraEntrega",
					"Confirmacion de asignacion del transportista " + vp.getTransportista().getNombre() + " a la venta " + vp.getVentaSeleccionada().getCodigoVenta());
			
			if (vp.getGestionAlmacen().seNecesitaPedidoAutomatico())
				vp.crearPedidoAutomatico();
			
			vp.reiniciar();
			checkOption = true;
			dispose();
		}
	}
	
	protected boolean comprobarFechaEntrega() {
		if (textFieldFechaEntrega.getText().equals(textFieldFechaEntrega.getHint())
				|| textFieldFechaEntrega.getText().strip().length() == 0) {
			JOptionPane.showMessageDialog(this, "No se ha introducido ninguna fecha. "
					+ "Por favor, rellene los campos e inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Date hoy = cal1.getTime();
		Date entradaFecha;
		
		try {
			entradaFecha = new SimpleDateFormat("dd-M-yyyy").parse(textFieldFechaEntrega.getText());
			cal1.setTime(hoy);
			cal2.setTime(entradaFecha);

			if (entradaFecha.before(hoy) || cal1.get(Calendar.DAY_OF_YEAR) 
					== cal2.get(Calendar.DAY_OF_YEAR) 
					&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
				JOptionPane.showMessageDialog(this, "La fecha de entrega debe ser posterior a la actual. "
						+ "Por favor, inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			if (cal2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				JOptionPane.showMessageDialog(this, "Los domingos no son días "
						+ "laborales para realizar entregas. Por favor, "
						+ "seleccione otra fecha.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "El formato de fecha de entrega no es adecuado. "
					+ "Por favor, inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
				
		return true;
	}
	
	protected boolean comprobarHoraEntrega() {
		if (textFieldHoraEntrega.getText().equals(textFieldHoraEntrega.getHint())
				|| textFieldHoraEntrega.getText().strip().length() == 0) {
			JOptionPane.showMessageDialog(this, "No se ha introducido ninguna hora. "
					+ "Por favor, rellene los campos e inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		Date horaInicioJornada, horaFinJornada;
		Date entradaHora;
		try {
			horaInicioJornada = format.parse(vp.getTransportista().getHoraInicioJornada());
			horaFinJornada = format.parse(vp.getTransportista().getHoraFinJornada());
			entradaHora = format.parse(textFieldHoraEntrega.getText());
			
			if (!(horaInicioJornada.compareTo(entradaHora) * entradaHora.compareTo(horaFinJornada) >= 0)) {
				JOptionPane.showMessageDialog(this, vp.getTransportista().getNombre() + " no reparte a la hora especificada. "
						+ "Por favor, seleccione otra hora de entrega.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(this, "El formato de hora de entrega no es adecuado. "
					+ "Por favor, inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}
	
	public boolean getCheck() {
		return checkOption;
	}
}