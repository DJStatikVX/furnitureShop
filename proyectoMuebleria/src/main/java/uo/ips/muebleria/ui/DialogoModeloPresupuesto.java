package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import uo.ips.muebleria.logic.presupuesto.cp.CrearPresupuesto;
import uo.ips.muebleria.logic.producto.Producto;
import uo.ips.muebleria.util.swing.SwingUtil;
import javax.swing.border.LineBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class DialogoModeloPresupuesto extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panelCentral;
	private JPanel panelTabla;
	private JPanel panelBotones;
	private JButton btnAlmacenAtras;
	private JScrollPane scrollPane;
	private JTable table;

	private DefaultTableModel modelo;

	private final static String[] COLUMNAS = new String[] { "Código", "Nombre", "Tipo", "Precio", "Unidades" };

	private String codigo;
	private String nombre;
	private VentanaPrincipal vp;
	private boolean cambios;
	private JPanel panelCentro;
	private JPanel panelNombre;
	private JLabel lblNombre;
	private JButton btnCambiar;

	public DialogoModeloPresupuesto(String codigo, String nombre, VentanaPrincipal vp) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DialogoModeloPresupuesto.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		setBackground(Color.WHITE);
		this.codigo = codigo;
		this.nombre = nombre;
		this.vp = vp;
		this.cambios = false;
		getContentPane().setBackground(Color.WHITE);
		setTitle("  " + nombre);
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 600, 300);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		getContentPane().add(getPanelCentral());
		setLocationRelativeTo(vp);
		inicializar();
	}

	private JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setBackground(Color.WHITE);
			panelCentral.setLayout(new BorderLayout(0, 0));
			panelCentral.add(getPanelBotones(), BorderLayout.SOUTH);
			panelCentral.add(getPanelCentro(), BorderLayout.CENTER);
		}
		return panelCentral;
	}

	private JPanel getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new JPanel();
			panelTabla.setBackground(Color.WHITE);
			panelTabla.setLayout(new GridLayout(0, 1, 0, 0));
			panelTabla.add(getScrollPane());
		}
		return panelTabla;
	}

	private JPanel getPanelBotones() {
		if (panelBotones == null) {
			panelBotones = new JPanel();
			panelBotones.setBorder(new LineBorder(Color.GRAY));
			FlowLayout flowLayout = (FlowLayout) panelBotones.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelBotones.setBackground(Color.WHITE);
			panelBotones.add(getBtnAlmacenAtras());
		}
		return panelBotones;
	}

	private JButton getBtnAlmacenAtras() {
		if (btnAlmacenAtras == null) {
			btnAlmacenAtras = new JButton("Atrás");
			btnAlmacenAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (cambios == true) {
						vp.actualizarModelos();
					}
					dispose();
				}
			});
			btnAlmacenAtras.setForeground(Color.WHITE);
			btnAlmacenAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnAlmacenAtras.setBackground(new Color(128, 0, 0));
		}
		return btnAlmacenAtras;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}

	private JTable getTable() {
		if (table == null) {
			modelo = new DefaultTableModel(new String[][] {}, COLUMNAS);
			table = new JTable(modelo);
			table.setBackground(Color.WHITE);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setDefaultEditor(Object.class, null);
			table.setFillsViewportHeight(true);
		}
		return table;
	}

	private void inicializar() {
		List<Producto> list = new CrearPresupuesto().getProductosModelo(codigo);
		for (Producto p : list)
			modelo.addRow(new String[] { p.getCodigo(), p.getNombre(), p.getTipo(),
					String.format("%.2f", p.getPrecio()), p.getUnidades() + "" });
		SwingUtil.autoAdjustColumns(table);
		lblNombre.setText("Nombre del modelo: "+nombre);
	}

	private JPanel getPanelCentro() {
		if (panelCentro == null) {
			panelCentro = new JPanel();
			panelCentro.setBackground(Color.WHITE);
			panelCentro.setLayout(new BorderLayout(0, 0));
			panelCentro.add(getPanelTabla(), BorderLayout.CENTER);
			panelCentro.add(getPanelNombre(), BorderLayout.SOUTH);
		}
		return panelCentro;
	}

	private JPanel getPanelNombre() {
		if (panelNombre == null) {
			panelNombre = new JPanel();
			panelNombre.setBorder(new LineBorder(Color.GRAY));
			panelNombre.setBackground(Color.WHITE);
			panelNombre.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
			panelNombre.add(getLblNombre());
			panelNombre.add(getBtnCambiar());
		}
		return panelNombre;
	}

	private JLabel getLblNombre() {
		if (lblNombre == null) {
			lblNombre = new JLabel("Nombre del modelo: ");
			lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lblNombre;
	}

	private JButton getBtnCambiar() {
		if (btnCambiar == null) {
			btnCambiar = new JButton("Cambiar Nombre");
			btnCambiar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					cambiarNombre();
				}
			});
			btnCambiar.setFocusPainted(false);
			btnCambiar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnCambiar;
	}

	private void cambiarNombre() {
		String n = JOptionPane.showInputDialog("Escribe el nombre del modelo por favor.");
		if(n==null)
			return;
		if(n.isBlank() || n.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Error: el nombre introducido no es valido.", "Nombre no valido", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.nombre = n;
		lblNombre.setText("Nombre del modelo: "+nombre);
		new CrearPresupuesto().renombrar(codigo, nombre);
		cambios = true;
	}
}
