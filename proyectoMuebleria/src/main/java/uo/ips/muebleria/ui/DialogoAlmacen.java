package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import uo.ips.muebleria.logic.almacen.AlmacenConsultas;
import uo.ips.muebleria.logic.almacen.GestionAlmacen;
import uo.ips.muebleria.logic.producto.Producto;
import uo.ips.muebleria.util.swing.SwingUtil;

public class DialogoAlmacen extends JDialog {
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
	
	private final static String[] COLUMNAS = new String[] {"Datos","Valor"};
	
	private String codigo;

	public DialogoAlmacen(String codigo) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DialogoAlmacen.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		this.codigo = codigo;
		getContentPane().setBackground(Color.WHITE);
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 450, 225);
		getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		getContentPane().add(getPanelCentral());
		
		inicializar();
	}

	private JPanel getPanelCentral() {
		if (panelCentral == null) {
			panelCentral = new JPanel();
			panelCentral.setBackground(Color.WHITE);
			panelCentral.setLayout(new BorderLayout(0, 0));
			panelCentral.add(getPanelTabla(), BorderLayout.CENTER);
			panelCentral.add(getPanelBotones(), BorderLayout.SOUTH);
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
		GestionAlmacen ga = new GestionAlmacen();
		Producto p = ga.getProductosEnAlmacen(codigo);
		modelo.addRow(new String[] {"Codigo",p.getCodigo()});
		modelo.addRow(new String[] {"Nombre",p.getNombre()});
		modelo.addRow(new String[] {"Categoria",p.getTipo()});
		modelo.addRow(new String[] {"Precio de venta",p.getPrecio()+""});
		modelo.addRow(new String[] {"Stock",p.getUnidades()+""});
		modelo.addRow(new String[] {"Unidades pendientes",AlmacenConsultas.getUnidadesPendientes(p.getCodigo())+""});
		modelo.addRow(new String[] {"Unidades vendidas",AlmacenConsultas.getUnidadesVendidas(p.getCodigo())+""});
		SwingUtil.autoAdjustColumns(table);
	}
}
