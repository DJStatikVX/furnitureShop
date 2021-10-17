package uo.ips.muebleria.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;

import uo.ips.muebleria.jdbc.Jdbc;
import uo.ips.muebleria.logic.almacen.GestionAlmacen;
import uo.ips.muebleria.logic.cliente.Cliente;
import uo.ips.muebleria.logic.cliente.ClienteConsultas;
import uo.ips.muebleria.logic.facturaPDF.GeneracionDeFacturaPDF;
import uo.ips.muebleria.logic.log.Logger;
import uo.ips.muebleria.logic.grafico.GraficoData;
import uo.ips.muebleria.logic.presupuesto.ContieneProducto;
import uo.ips.muebleria.logic.presupuesto.Presupuesto;
import uo.ips.muebleria.logic.presupuesto.PresupuestosConsultas;
import uo.ips.muebleria.logic.presupuesto.cp.CrearPresupuesto;
import uo.ips.muebleria.logic.producto.Producto;
import uo.ips.muebleria.logic.producto.ProductoDelPedidoConsultas;
import uo.ips.muebleria.logic.producto.ProductoVenta;
import uo.ips.muebleria.logic.producto.ProductosConsultas;
import uo.ips.muebleria.logic.trabajador.EstadisticasVendedor;
import uo.ips.muebleria.logic.trabajador.Trabajador;
import uo.ips.muebleria.logic.trabajador.TrabajadorConsultas;
import uo.ips.muebleria.logic.trabajador.Vendedor;
import uo.ips.muebleria.logic.transporte.GestionTransporte;
import uo.ips.muebleria.logic.transporte.Pedido;
import uo.ips.muebleria.logic.transporte.SeguimientoPedido;
import uo.ips.muebleria.logic.transporte.Transportista;
import uo.ips.muebleria.logic.venta.HistorialVentas;
import uo.ips.muebleria.logic.venta.Venta;
import uo.ips.muebleria.logic.venta.VentaConsultas;
import uo.ips.muebleria.util.Util;
import uo.ips.muebleria.util.UtilCorreo;
import uo.ips.muebleria.util.UtilLogin;
import uo.ips.muebleria.util.swing.JTableAlmacen;
import uo.ips.muebleria.util.swing.JTableModelos;
import uo.ips.muebleria.util.swing.JTablePresupuesto;
import uo.ips.muebleria.util.swing.ModeloTablaNoEditable;
import uo.ips.muebleria.util.swing.ModeloTablaTransporte;
import uo.ips.muebleria.util.swing.SpinnerCell;
import uo.ips.muebleria.util.swing.SwingUtil;
import uo.ips.muebleria.util.swing.graficos.balance.GraficoAreaBalance;
import uo.ips.muebleria.util.swing.graficos.balance.GraficoBarrasBalance;
import uo.ips.muebleria.util.swing.graficos.balance.GraficoLineasBalance;
import uo.ips.muebleria.util.swing.graficos.balance.GraficoSectoresBalance;
import uo.ips.muebleria.util.swing.graficos.vendedores.GraficoAreaVendedores;
import uo.ips.muebleria.util.swing.graficos.vendedores.GraficoBarrasVendedores;
import uo.ips.muebleria.util.swing.graficos.vendedores.GraficoLineasVendedores;
import uo.ips.muebleria.util.swing.graficos.vendedores.GraficoSectoresVendedores;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	public final static String[] COLUMNAS_CP = new String[] { "Código", "Nombre", "Tipo", "Precio" };
	public final static String[] COLUMNAS_CP2 = new String[] { "Código", "Nombre", "Tipo", "Precio", "Unidades" };

	private CrearPresupuesto cp;
	private GestionAlmacen ga;
	private DefaultTableModel modeloTableProdCatalogo;
	private DefaultTableModel modeloTableProdPresupuesto;

	private final static String[] COLUMNAS_HV = new String[] { "Id", "Fecha", "Total + Montaje", "Estado" };
	private final static String[] COLUMNAS_HVprod = new String[] { "Nombre", "Montaje", "Tipo de Entrega",
			"Fecha Entrega", "Unidades" };

	private HistorialVentas hv;
	private DefaultTableModel modeloTableHistorialVentas;
	private DefaultTableModel modeloTableProductosHv;
	private Venta ventaSeleccionadaHistorial;

	private final static String[] COLUMNAS_SPped = new String[] { "Id Pedido", "Tipo Pedido", "Estado" };
	private final static String[] COLUMNAS_SPprod = new String[] { "Producto", "Unidades" };

	private SeguimientoPedido sp;
	private DefaultTableModel modeloTableSeguimientoPedidos;
	private DefaultTableModel modeloTableSPproductos;
	public static Logger log;
	static GeneracionDeFacturaPDF factPdf;

	private Trabajador usuarioActivo = new Vendedor("prueba");

	@SuppressWarnings("unused")
	private Main main;

	private JPanel panelPrincipal;
	private JPanel panelMenu;
	private JButton btnMenuCrearPresupuesto;
	private JButton btnMenuAsignarClienteAPresupuesto;
	private JButton btnMenuCrearVenta;
	private JButton btnMenuHistorialVentas;
	private JButton btnSeguimientoPedido;
	private JPanel panelCP;
	private JPanel panelCPFiltro;
	private JPanel panelCPListas;
	private JPanel panelCPBotones;
	private JButton btnCPAtras;
	private JButton btnCPConfirmar;
	private JPanel panelCPListasNorte;
	private JPanel panelCPListasCentro;
	private JPanel panelCPListasSur;
	private JScrollPane scrollPaneCPDerecha;
	private JScrollPane scrollPaneCPIzquierda;
	private JButton btnCPAñadir;
	private JButton btnCPEliminar;
	private JLabel lblCPCatalogo;
	private JLabel lblCPPresupuesto;
	private JTable tableCPDerecha;
	private JTable tableCPIzquierda;
	private JPanel panelCPFiltroDerecha;
	private JButton btnCPAplicarFiltro;
	private JLabel lblCPFiltroTipo;
	private JComboBox<String> comboBoxCPTipos;
	private JPanel panelCPFiltroBotones;
	private JButton btnCPQuitarFiltro;
	private JLabel lblCPPrecio;
	private JTextField textFieldCPPrecio;
	private JRadioButton rdbtnMayor;
	private JRadioButton rdbtnNewRadioButton;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JPanel panelHV;
	private JPanel panelFiltroHV;
	private JPanel panelHVLista;
	private JPanel panelHVBotones;
	private JScrollPane scrollPaneHV;
	private JLabel lblFiltroHV;
	private JTable tableHV;
	private JButton btnNewButton;
	private JSpinner spHVFechaIn;
	private JLabel lblFiltroHVa;
	private JSpinner spHVFechaFin;
	private JButton btnHVFiltrar;
	private JButton btnHVFiltrarOff;
	private JPanel panelSP;
	private JPanel panelListaPedidos;
	private JScrollPane scrollPanePedidos;
	private JScrollPane scrollPaneProductosPedido;
	private JTable tablePedidos;
	private JTable tableProductosPedido;
	private JPanel panelTitulosListasPedido;
	private JLabel lblPedidosProveedor;
	private JLabel lblProductosPedido;
	private JPanel panelBotonesSP;
	private JButton btnAtrasSP;
	private JButton btnConfirmarRecepcionPedido;

	private JPanel mostrarPresupuestosClientes;
	private JPanel mostrarPresupuestosVentas;

	private Venta ventaSeleccionada;

	/**
	 * Create the frame.
	 * 
	 * @wbp.parser.constructor
	 */
	public VentanaPrincipal(Trabajador usuarioActivo) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal", "Cerrar sesion");
			}
		});
		setTitle("Muebleria");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				VentanaPrincipal.class.getResource("/uo/ips/muebleria/img/logo.PNG")));
		setResizable(false);
		this.usuarioActivo = usuarioActivo;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		panelPrincipal = new JPanel();
		panelPrincipal.setBackground(Color.WHITE);
		panelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panelPrincipal);
		inicializarLayout();
		this.setLocationRelativeTo(null);
		hv = new HistorialVentas();
		log = new Logger();
		factPdf = new GeneracionDeFacturaPDF();
	}

	private void inicializarLayout() {
		panelPrincipal.setLayout(new CardLayout(0, 0));
		panelPrincipal.add(getPanelContenedorMenu(), "menu");
		panelPrincipal.add(getPanelCP(), "crearPresupuesto");
		panelPrincipal.add(getAsignarClientes(), "asignarCliente");
		panelPrincipal.add(getMostrarPresupuestos(), "mostrarPresupuestosClientes");
		panelPrincipal.add(getMostrarPresupuestosVentas(), "mostrarPresupuestosVentas");
		panelPrincipal.add(getPanelAsignarTransporte(), "asignarTransporte");
		panelPrincipal.add(getPanelHV(), "historialVentas");
		panelPrincipal.add(getPanelSP(), "seguimientoPedido");
		panelPrincipal.add(getAñadirTrabajador(), "añadirTrabajador");
		panelPrincipal.add(getRealizarPedido(), "realizarPedidoAlProveedor");
		panelPrincipal.add(getConfirmarPedido(), "confirmarPedidoAlProveedor");
		panelPrincipal.add(getPanelAlmacen(), "almacen");
		panelPrincipal.add(getPanelGraficos(), "graficos");
		panelPrincipal.add(getCambiarPrecio(), "cambioPrecioProducto");
		panelPrincipal.add(getEstadisticasVendedores(), "estadisticasVendedores");
		panelPrincipal.add(getPanelMenuEstadisticas(), "menu_estadisticas");
		panelPrincipal.add(getPanelMenuGP(), "menu_gp");
		panelPrincipal.add(getPanelGPP(), "menu_gpp");

		if (usuarioActivo.getDepartamento().equals("Transportista"))
			esconderBotones();
	}

	private void esconderBotones() {
		getBtnGestinDePresupuestos().setVisible(false);
		getBtnGP().setVisible(false);
		getBtnAñadirTrabajador().setVisible(false);
		getBtnEstadisticas().setVisible(false);
		getPanelMenu().setLayout(new GridLayout(0, 1, 100, 20));
		getPanelMenu().setBorder(new EmptyBorder(0, 300, 0, 300));
	}

	public void mostrarPanel(String panel) {
		CardLayout cl = (CardLayout) panelPrincipal.getLayout();
		cl.show(panelPrincipal, panel);
	}

	private JPanel getPanelMenu() {
		if (panelMenu == null) {
			panelMenu = new JPanel();
			panelMenu.setBorder(new EmptyBorder(10, 0, 0, 0));
			panelMenu.setBackground(Color.WHITE);
			panelMenu.setLayout(new GridLayout(0, 1, 50, 20));
			panelMenu.add(getBtnGestinDePresupuestos());
			panelMenu.add(getBtnGP());
			panelMenu.add(getBtnMenuHistorialVentas());
			panelMenu.add(getBtnAñadirTrabajador());
			panelMenu.add(getBtnEstadisticas());
		}
		return panelMenu;
	}

	private JButton getBtnMenuCrearPresupuesto() {
		if (btnMenuCrearPresupuesto == null) {
			btnMenuCrearPresupuesto = new JButton("Crear Presupuesto");
			btnMenuCrearPresupuesto.setFocusPainted(false);
			btnMenuCrearPresupuesto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionPresupuestos",
							"Solicitud de crear presupuesto");
					mostrarPanel("crearPresupuesto");
					inicializarCrearPresupuesto();
				}
			});
			btnMenuCrearPresupuesto.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnMenuCrearPresupuesto;
	}

	private void inicializarCrearPresupuesto() {
		// se crea logica
		cp = new CrearPresupuesto();
		cp.setUsuario(usuarioActivo);
		// se resetea las tablas creando unas nuevas
		actualizarCatalogo();
		actualizarPresupuesto();
		actualizarModelos();
		// combobox
		DefaultComboBoxModel<String> dcbm = new DefaultComboBoxModel<String>();
		dcbm.addElement("");
		dcbm.addAll(cp.getTipos());
		comboBoxCPTipos.setModel(dcbm);
	}

	protected void actualizarModelos() {
		List<Presupuesto> presupuestos = cp.getModelos();
		tableCPModelos = null;
		scrollPaneModelos.setViewportView(getTableCPModelos());
		for (Presupuesto p : presupuestos)
			tableCPModelos.addRow(new String[] { p.getCodigoPresupuesto(), p.getNombre() });
		SwingUtil.autoAdjustColumns(tableCPModelos);
	}

	private JButton getBtnMenuAsignarClienteAPresupuesto() {
		if (btnMenuAsignarClienteAPresupuesto == null) {
			btnMenuAsignarClienteAPresupuesto = new JButton("Asignar cliente a presupuesto");
			btnMenuAsignarClienteAPresupuesto.setFocusPainted(false);
			btnMenuAsignarClienteAPresupuesto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					asignarPresupuestos(modeloListaPresupuestos, obtenerPresupuestosSinClientes());
					if (modeloListaPresupuestos.getRowCount() == 0) {
						JOptionPane.showMessageDialog(null,
								"No se ha encontrado ningún presupuesto que no tenga un cliente asignado");
					} else {
						asignarPresupuestos(modeloListaPresupuestos, obtenerPresupuestosSinClientes());
						SwingUtil.autoAdjustColumns(tablePresupuestoCliente);
						SwingUtil.autoAdjustColumns(tableSeleccionPresupuestoCliente);
						mostrarPanel("mostrarPresupuestosClientes");
						presupuestoSeleccionado = null;
						cliente = null;
						log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionPresupuestos",
								"Solicitud de asignacion de cliente a presupuesto");
					}
				}
			});
			btnMenuAsignarClienteAPresupuesto.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnMenuAsignarClienteAPresupuesto;
	}

	private JButton getBtnMenuCrearVenta() {
		if (btnMenuCrearVenta == null) {
			btnMenuCrearVenta = new JButton("Crear Venta");
			btnMenuCrearVenta.setFocusPainted(false);
			btnMenuCrearVenta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					asignarPresupuestos(modeloListaPresupuestosVentas, obtenerPresupuestos());
					if (modeloListaPresupuestosVentas.getRowCount() == 0) {
						JOptionPane.showMessageDialog(null,
								"No se ha encontrado ningún presupuesto que no esté aceptado y que tenga un cliente asignado");
					} else {
						asignarPresupuestos(modeloListaPresupuestosVentas, obtenerPresupuestos());
						mostrarPanel("mostrarPresupuestosVentas");
						log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionPresupuestos",
								"Solicitud de creacion de venta");
					}
				}
			});
			btnMenuCrearVenta.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnMenuCrearVenta;
	}

	private void asignarPresupuestos(DefaultTableModel a, DefaultTableModel b) {
		limpiarTabla(a);
		for (int i = 0; i < b.getRowCount(); i++) {
			a.addRow(new String[] { b.getValueAt(i, 0).toString(), b.getValueAt(i, 1).toString(),
					b.getValueAt(i, 2).toString() });
		}
	}

	private void limpiarTabla(DefaultTableModel tabla) {
		int filas = tabla.getRowCount();
		for (int i = filas - 1; i >= 0; i--) {
			tabla.removeRow(i);
		}
	}

	private JButton getBtnMenuHistorialVentas() {
		if (btnMenuHistorialVentas == null) {
			btnMenuHistorialVentas = new JButton("Historial de ventas");
			btnMenuHistorialVentas.setFocusPainted(false);
			btnMenuHistorialVentas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal",
							"Solicitud de consulta del historial de ventas");
					mostrarPanel("historialVentas");
					inicializarHistorialVentas();
				}
			});
			btnMenuHistorialVentas.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnMenuHistorialVentas;
	}

	protected void inicializarHistorialVentas() {
		hv = new HistorialVentas();
		actualizarHistorialVentas(hv.getHistorialVentas());

	}

	protected void actualizarHistorialVentas(List<Venta> historialVentas) {
		tableHV = null;
		tableProductosHV = null;
		scrollPaneHV.setViewportView(getTableHV());
		scrollPaneProductosHV.setViewportView(getTableProductosHV());
		List<Venta> list = historialVentas;

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		Date hoy = cal1.getTime();
		Date fechaEntrega;

		for (Venta v : list) {
			if (!v.getFechaEntregaDomicilio().equals("")) {
				fechaEntrega = Util.isoStringToDate(v.getFechaEntregaDomicilio());
				cal1.setTime(hoy);
				cal2.setTime(fechaEntrega);
				if (fechaEntrega.before(hoy)) {
					v.marcarComo(v.getCodigoVenta(), "Retrasado");
					v.setEstadoVenta("Retrasado");
				}
			}
			modeloTableHistorialVentas.addRow(new String[] { v.getCodigoVenta(), v.getFechaVentaCreada(),
					String.format("%.2f", v.getImporteVentaMasMontaje()), v.getEstadoVenta() });

		}
		SwingUtil.autoAdjustColumns(tableHV);

	}

	private JButton getBtnSeguimientoPedido() {
		if (btnSeguimientoPedido == null) {
			btnSeguimientoPedido = new JButton("Seguimiento de pedidos");
			btnSeguimientoPedido.setFocusPainted(false);
			btnSeguimientoPedido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionDeProductos",
							"Solicitud de consulta de seguimiento de pedidos");
					ga = new GestionAlmacen();
					mostrarPanel("seguimientoPedido");
					inicializarPanelSeguimiento();
				}
			});
			btnSeguimientoPedido.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnSeguimientoPedido;
	}

	protected void inicializarPanelSeguimiento() {
		sp = new SeguimientoPedido();
		actualizarSeguimientoPedidos();

	}

	private void actualizarSeguimientoPedidos() {
		tablePedidos = null;
		tableProductosPedido = null;
		scrollPanePedidos.setViewportView(getTablePedidos());
		scrollPaneProductosPedido.setViewportView(getTableProductosPedido());
		sp.cargarPedidos();
		List<Pedido> list = sp.getSegumientoPedidos();
		for (Pedido p : list) {
			modeloTableSeguimientoPedidos
					.addRow(new String[] { p.getCodigoPedido(), p.getTipoPedido(), p.getEstadoPedido() });
		}

		SwingUtil.autoAdjustColumns(tablePedidos);
	}

	private JButton getBtnAñadirTrabajador() {
		if (btnAñadirTrabajador == null) {
			btnAñadirTrabajador = new JButton("Añadir trabajador");
			btnAñadirTrabajador.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal", "Añadir trabajador");
					mostrarPanel("añadirTrabajador");
				}
			});
			btnAñadirTrabajador.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnAñadirTrabajador;
	}

	private JButton getBtnRealizarPedidoAProveedor() {
		if (btnRealizarPedidoAProveedor == null) {
			btnRealizarPedidoAProveedor = new JButton("Realizar pedido a proveedor");
			btnRealizarPedidoAProveedor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionDeProductos",
							"Solicitud de realizar pedido al proveedor");
					mostrarPanel("realizarPedidoAlProveedor");
				}
			});
			btnRealizarPedidoAProveedor.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnRealizarPedidoAProveedor;
	}

	private JButton getBtnCambiarPrecioProducto() {
		if (btnCambiarPrecioProducto == null) {
			btnCambiarPrecioProducto = new JButton("Cambiar precio de un producto");
			btnCambiarPrecioProducto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					asignarProductosATabla(modeloProductosCambioPrecio, ProductosConsultas.getProductosCatalogo());
					mostrarPanel("cambioPrecioProducto");
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionDeProductos",
							"Solicitud de cambio de precio de productos");
				}
			});
			btnCambiarPrecioProducto.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnCambiarPrecioProducto;
	}

	private JPanel getPanelCP() {
		if (panelCP == null) {
			panelCP = new JPanel();
			panelCP.setBackground(Color.WHITE);
			panelCP.setLayout(new BorderLayout(10, 10));
			panelCP.add(getPanelCPFiltro(), BorderLayout.NORTH);
			panelCP.add(getPanelCPBotones(), BorderLayout.SOUTH);
			panelCP.add(getTabbedPaneCP(), BorderLayout.CENTER);
		}
		return panelCP;
	}

	private JPanel getPanelCPFiltro() {
		if (panelCPFiltro == null) {
			panelCPFiltro = new JPanel();
			panelCPFiltro.setBorder(new LineBorder(Color.GRAY));
			panelCPFiltro.setBackground(Color.WHITE);
			panelCPFiltro.setLayout(new BorderLayout(0, 0));
			panelCPFiltro.add(getPanelCPFiltroDerecha(), BorderLayout.CENTER);
			panelCPFiltro.add(getPanelCPFiltroBotones(), BorderLayout.EAST);
		}
		return panelCPFiltro;
	}

	private JPanel getPanelCPListas() {
		if (panelCPListas == null) {
			panelCPListas = new JPanel();
			panelCPListas.setBackground(Color.WHITE);
			panelCPListas.setLayout(new BorderLayout(0, 0));
			panelCPListas.add(getPanelCPListasNorte(), BorderLayout.NORTH);
			panelCPListas.add(getPanelCPListasCentro(), BorderLayout.CENTER);
			panelCPListas.add(getPanelCPListasSur(), BorderLayout.SOUTH);
		}
		return panelCPListas;
	}

	private JPanel getPanelCPBotones() {
		if (panelCPBotones == null) {
			panelCPBotones = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelCPBotones.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelCPBotones.setBackground(Color.WHITE);
			panelCPBotones.add(getBtnCPAtras());
			panelCPBotones.add(getBtnCPConfirmar());
		}
		return panelCPBotones;
	}

	private JButton getBtnCPAtras() {
		if (btnCPAtras == null) {
			btnCPAtras = new JButton("Atrás");
			btnCPAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu_gpp");
				}
			});
			btnCPAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnCPAtras.setForeground(new Color(255, 255, 255));
			btnCPAtras.setBackground(new Color(128, 0, 0));
		}
		return btnCPAtras;
	}

	private JButton getBtnCPConfirmar() {
		if (btnCPConfirmar == null) {
			btnCPConfirmar = new JButton("Confirmar");
			btnCPConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (cp.getProductosPresupuesto().size() > 0) {
						// se prepara la creacion del presupuesto
						((JTablePresupuesto) tableCPIzquierda).stopEditions();
						// se asignan las unidades a los productos
						for (int i = 0; i < modeloTableProdPresupuesto.getRowCount(); i++) {
							String code = modeloTableProdPresupuesto.getValueAt(i, 0).toString();
							Integer unidades = (Integer) modeloTableProdPresupuesto.getValueAt(i, 4);
							cp.asignarUnidades(code, unidades.intValue());
						}
						// esto devuelve el codigo del presupesto
						presupuestoSeleccionado = cp.crearPresupuesto();
						cliente = null;
						cp.resetPresupuesto();
						actualizarPresupuesto();

						log.saveLog(usuarioActivo.getCodigo(),
								"VentanaPrincipal.GestionPresupuestos.CreacionPresupuesto",
								"Confirmacion de creacion del presupuesto "
										+ presupuestoSeleccionado.getCodigoPresupuesto());

						int n = JOptionPane.showConfirmDialog(null, "¿Desea asignar un cliente al presupuesto ahora?",
								"Asignar cliente", JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION) {
							mostrarPanel("asignarCliente");
						} else if (n == JOptionPane.NO_OPTION) {
							n = JOptionPane.showConfirmDialog(null, "¿Desea guardar el presupuesto como modelo?",
									"Asignar cliente", JOptionPane.YES_NO_OPTION);
							if (n == JOptionPane.YES_OPTION) {
								cp.marcarComoModelo(presupuestoSeleccionado.getCodigoPresupuesto());
								String nombre = JOptionPane.showInputDialog("Escribe el nombre del modelo por favor.");
								if (nombre != null) {
									if (nombre.isEmpty() || nombre.isBlank())
										JOptionPane.showMessageDialog(null,
												"El nombre introducido para el modelo no consta de un formato valido. Se guardo el modelo con el nombre por defecto.");
									else
										cp.renombrar(presupuestoSeleccionado.getCodigoPresupuesto(), nombre);
									log.saveLog(usuarioActivo.getCodigo(),
											"VentanaPrincipal.GestionPresupuestos.CreacionPresupuesto",
											"Presupuesto " + presupuestoSeleccionado.getCodigoPresupuesto()
													+ " ha sido guardado como presupuesto modelo");
								}
							}
						}
						actualizarModelos();
					} else {
						JOptionPane.showMessageDialog(null,
								"No se puede crear un presupuesto si no hay productos añadidos");
					}
				}
			});
			btnCPConfirmar.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnCPConfirmar.setForeground(new Color(255, 255, 255));
			btnCPConfirmar.setBackground(new Color(0, 128, 0));
		}
		return btnCPConfirmar;
	}

	private JPanel getPanelCPListasNorte() {
		if (panelCPListasNorte == null) {
			panelCPListasNorte = new JPanel();
			panelCPListasNorte.setBackground(Color.WHITE);
			panelCPListasNorte.setLayout(new GridLayout(0, 2, 10, 0));
			panelCPListasNorte.add(getLblCPCatalogo());
			panelCPListasNorte.add(getLblCPPresupuesto());
		}
		return panelCPListasNorte;
	}

	private JPanel getPanelCPListasCentro() {
		if (panelCPListasCentro == null) {
			panelCPListasCentro = new JPanel();
			panelCPListasCentro.setBackground(Color.WHITE);
			panelCPListasCentro.setLayout(new GridLayout(0, 2, 10, 0));
			panelCPListasCentro.add(getScrollPaneCPDerecha());
			panelCPListasCentro.add(getScrollPaneCPIzquierda());
		}
		return panelCPListasCentro;
	}

	private JPanel getPanelCPListasSur() {
		if (panelCPListasSur == null) {
			panelCPListasSur = new JPanel();
			panelCPListasSur.setBackground(Color.WHITE);
			panelCPListasSur.setLayout(new GridLayout(0, 2, 10, 0));
			panelCPListasSur.add(getBtnCPAñadir());
			panelCPListasSur.add(getBtnCPEliminar());
		}
		return panelCPListasSur;
	}

	private JScrollPane getScrollPaneCPDerecha() {
		if (scrollPaneCPDerecha == null) {
			scrollPaneCPDerecha = new JScrollPane();
			scrollPaneCPDerecha.setBackground(Color.WHITE);
			scrollPaneCPDerecha.setViewportView(getTableCPDerecha());
		}
		return scrollPaneCPDerecha;
	}

	private JScrollPane getScrollPaneCPIzquierda() {
		if (scrollPaneCPIzquierda == null) {
			scrollPaneCPIzquierda = new JScrollPane();
			scrollPaneCPIzquierda.setBackground(Color.WHITE);
			scrollPaneCPIzquierda.setViewportView(getTableCPIzquierda());
		}
		return scrollPaneCPIzquierda;
	}

	private JButton getBtnCPAñadir() {
		if (btnCPAñadir == null) {
			btnCPAñadir = new JButton("Añadir");
			btnCPAñadir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int index = tableCPDerecha.getSelectedRow();
					if (index < 0)
						return;
					else {
						Producto p = cp.addPresupuesto(modeloTableProdCatalogo.getValueAt(index, 0).toString());
						if (p != null) {
							actualizarPresupuestoAñadir(p);
							log.saveLog(usuarioActivo.getCodigo(),
									"VentanaPrincipal.GestionPresupuestos.CreacionPresupuesto",
									"Añade producto " + p.getCodigo() + " al presupuesto");
						}
							
					}
				}
			});
			btnCPAñadir.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnCPAñadir;
	}

	private JButton getBtnCPEliminar() {
		if (btnCPEliminar == null) {
			btnCPEliminar = new JButton("Eliminar");
			btnCPEliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int index = tableCPIzquierda.getSelectedRow();
					if (index < 0)
						return;
					else {
						cp.removePresupuesto(modeloTableProdPresupuesto.getValueAt(index, 0).toString());
						String productoP = modeloTableProdPresupuesto.getValueAt(index, 0).toString();
						actualizarPresupuestoEliminar(index);
						log.saveLog(usuarioActivo.getCodigo(),
								"VentanaPrincipal.GestionPresupuestos.CreacionPresupuesto",
								"Elimina producto " + productoP + " al presupuesto");
					}
				}
			});
			btnCPEliminar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnCPEliminar;
	}

	private void actualizarPrecio() {
		((JTablePresupuesto) tableCPIzquierda).stopEditions();
		for (int i = 0; i < modeloTableProdPresupuesto.getRowCount(); i++) {
			String code = modeloTableProdPresupuesto.getValueAt(i, 0).toString();
			Integer unidades = (Integer) modeloTableProdPresupuesto.getValueAt(i, 4);
			cp.asignarUnidades(code, unidades.intValue());
			modeloTableProdPresupuesto.setValueAt(new SpinnerCell(unidades), i, 4);
		}
		double precio = cp.getTotal();
		lblCPPresupuesto.setText("  Productos Presupuesto (" + String.format("%.2f", precio) + "€)");
		lblCPPresupuesto2.setText("  Productos Presupuesto (" + String.format("%.2f", precio) + "€)");
	}

	private void actualizarCatalogo() {
		tableCPDerecha = null;
		scrollPaneCPDerecha.setViewportView(getTableCPDerecha());
		List<Producto> list = cp.getProductosCatalogo();
		for (Producto p : list)
			modeloTableProdCatalogo.addRow(
					new String[] { p.getCodigo(), p.getNombre(), p.getTipo(), String.format("%.2f", p.getPrecio()) });
		SwingUtil.autoAdjustColumns(tableCPDerecha);
	}

	private void actualizarPresupuesto() {
		tableCPIzquierda = null;
		scrollPaneCPIzquierda.setViewportView(getTableCPIzquierda());
		actualizarPrecio();
		SwingUtil.autoAdjustColumns(tableCPIzquierda);
	}

	private void actualizarPresupuestoAñadir(Producto p) {
		modeloTableProdPresupuesto.addRow(new Object[] { p.getCodigo(), p.getNombre(), p.getTipo(),
				String.format("%.2f", p.getPrecio()), new SpinnerCell() });
		tableCPIzquierda.setRowSelectionInterval(tableCPIzquierda.getRowCount() - 1,
				tableCPIzquierda.getRowCount() - 1);
		actualizarPrecio();
		SwingUtil.autoAdjustColumns(tableCPIzquierda);
	}

	private void actualizarPresupuestoEliminar(int i) {
		modeloTableProdPresupuesto.removeRow(i);
		actualizarPrecio();
		SwingUtil.autoAdjustColumns(tableCPIzquierda);
	}

	private JLabel getLblCPCatalogo() {
		if (lblCPCatalogo == null) {
			lblCPCatalogo = new JLabel("  Productos Catálogo");
			lblCPCatalogo.setHorizontalAlignment(SwingConstants.LEFT);
			lblCPCatalogo.setFont(new Font("Tahoma", Font.BOLD, 16));
		}
		return lblCPCatalogo;
	}

	private JLabel getLblCPPresupuesto() {
		if (lblCPPresupuesto == null) {
			lblCPPresupuesto = new JLabel("  Productos Presupuesto");
			lblCPPresupuesto.setHorizontalAlignment(SwingConstants.LEFT);
			lblCPPresupuesto.setFont(new Font("Tahoma", Font.BOLD, 16));
		}
		return lblCPPresupuesto;
	}

	private JTable getTableCPDerecha() {
		if (tableCPDerecha == null) {
			modeloTableProdCatalogo = new DefaultTableModel(new Object[][] {}, COLUMNAS_CP);
			tableCPDerecha = new JTable(modeloTableProdCatalogo);
			tableCPDerecha.setBackground(Color.WHITE);
			tableCPDerecha.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableCPDerecha.setDefaultEditor(Object.class, null);
			tableCPDerecha.setFillsViewportHeight(true);
		}
		return tableCPDerecha;
	}

	private JTable getTableCPIzquierda() {
		if (tableCPIzquierda == null) {
			modeloTableProdPresupuesto = new DefaultTableModel(new Object[][] {}, COLUMNAS_CP2);
			tableCPIzquierda = new JTablePresupuesto(modeloTableProdPresupuesto);
			tableCPIzquierda.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					actualizarPrecio();
				};
			});
			tableCPIzquierda.setBackground(Color.WHITE);
			tableCPIzquierda.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableCPIzquierda.setDefaultEditor(Object.class, null);
			tableCPIzquierda.setFillsViewportHeight(true);
		}
		return tableCPIzquierda;
	}

	private JPanel getPanelCPFiltroDerecha() {
		if (panelCPFiltroDerecha == null) {
			panelCPFiltroDerecha = new JPanel();
			panelCPFiltroDerecha.setBackground(Color.WHITE);
			FlowLayout fl_panelCPFiltroDerecha = new FlowLayout(FlowLayout.RIGHT, 5, 5);
			panelCPFiltroDerecha.setLayout(fl_panelCPFiltroDerecha);
			panelCPFiltroDerecha.add(getLblCPPrecio());
			panelCPFiltroDerecha.add(getTextFieldCPPrecio());
			panelCPFiltroDerecha.add(getRdbtnMayor());
			panelCPFiltroDerecha.add(getRdbtnNewRadioButton());
			panelCPFiltroDerecha.add(getLblCPFiltroTipo());
			panelCPFiltroDerecha.add(getComboBoxCPTipos());
		}
		return panelCPFiltroDerecha;
	}

	private JButton getBtnCPAplicarFiltro() {
		if (btnCPAplicarFiltro == null) {
			btnCPAplicarFiltro = new JButton("Filtrar Catálogo");
			btnCPAplicarFiltro.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnCPAplicarFiltro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					String categoria;
					Double d;
					if (comboBoxCPTipos.getSelectedIndex() > 0)
						categoria = comboBoxCPTipos.getItemAt(comboBoxCPTipos.getSelectedIndex());
					else
						categoria = null;
					if (textFieldCPPrecio.getText().isEmpty() || textFieldCPPrecio.getText().isBlank())
						d = null;
					else {
						try {
							d = Double.parseDouble(textFieldCPPrecio.getText());
						} catch (NumberFormatException e) {
							JOptionPane.showMessageDialog(null, "Error, valor del precio para el filtro no valido");
							return;
						}
					}
					if (rdbtnMayor.isSelected()) {
						log.saveLog(usuarioActivo.getCodigo(),
								"VentanaPrincipal.GestionPresupuestos.CreacionPresupuesto",
								"Solicitud de filtrado de productos");
						cp.filtrar(categoria, d, CrearPresupuesto.FILTRAR_MAYOR);
					} else
						cp.filtrar(categoria, d, CrearPresupuesto.FILTRAR_MENOR);
					actualizarCatalogo();
				}
			});
		}
		return btnCPAplicarFiltro;
	}

	private JLabel getLblCPFiltroTipo() {
		if (lblCPFiltroTipo == null) {
			lblCPFiltroTipo = new JLabel("Categoria");
			lblCPFiltroTipo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lblCPFiltroTipo;
	}

	private JComboBox<String> getComboBoxCPTipos() {
		if (comboBoxCPTipos == null) {
			comboBoxCPTipos = new JComboBox<String>();
			comboBoxCPTipos.setBackground(Color.WHITE);
			comboBoxCPTipos.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return comboBoxCPTipos;
	}

	private JPanel getPanelCPFiltroBotones() {
		if (panelCPFiltroBotones == null) {
			panelCPFiltroBotones = new JPanel();
			panelCPFiltroBotones.setBackground(Color.WHITE);
			panelCPFiltroBotones.setLayout(new GridLayout(0, 2, 0, 0));
			panelCPFiltroBotones.add(getBtnCPAplicarFiltro());
			panelCPFiltroBotones.add(getBtnCPQuitarFiltro());
		}
		return panelCPFiltroBotones;
	}

	private JButton getBtnCPQuitarFiltro() {
		if (btnCPQuitarFiltro == null) {
			btnCPQuitarFiltro = new JButton("Quitar filtro");
			btnCPQuitarFiltro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionPresupuestos.CreacionPresupuesto",
							"Quitar filtro catalogo");
					cp.filtrar(null, null, null);
					textFieldCPPrecio.setText("");
					actualizarCatalogo();
				}
			});
			btnCPQuitarFiltro.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnCPQuitarFiltro;
	}

	private JLabel getLblCPPrecio() {
		if (lblCPPrecio == null) {
			lblCPPrecio = new JLabel("Precio");
			lblCPPrecio.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return lblCPPrecio;
	}

	private JTextField getTextFieldCPPrecio() {
		if (textFieldCPPrecio == null) {
			textFieldCPPrecio = new JTextField();
			textFieldCPPrecio.setFont(new Font("Tahoma", Font.PLAIN, 14));
			textFieldCPPrecio.setColumns(10);
		}
		return textFieldCPPrecio;
	}

	private JRadioButton getRdbtnMayor() {
		if (rdbtnMayor == null) {
			rdbtnMayor = new JRadioButton("Mayor");
			rdbtnMayor.setBackground(Color.WHITE);
			rdbtnMayor.setSelected(true);
			buttonGroup.add(rdbtnMayor);
			rdbtnMayor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return rdbtnMayor;
	}

	private JRadioButton getRdbtnNewRadioButton() {
		if (rdbtnNewRadioButton == null) {
			rdbtnNewRadioButton = new JRadioButton("Menor");
			buttonGroup.add(rdbtnNewRadioButton);
			rdbtnNewRadioButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return rdbtnNewRadioButton;
	}

	/*
	 * --------- Asignar clientes -----------
	 */

	/*
	 * --------- MostrarPresupuestos -----------
	 */

	private JPanel pnBotonesSeleccionPresupuesto;
	private JButton btnSiguienteSeleccionPresupuesto;
	private JButton btnCancelarSeleccionPresupuesto;
	private JPanel pnSeleccionarPresupuesto;
	private JPanel pnBotonSeleccionar;
	private JButton btnSeleccionar;
	private JPanel pnRelleno;
	private JLabel lbPresupuestoSeleccionado;
	private JPanel pnPresupuestoSeleccionado;

	private ModeloTablaNoEditable modeloListaPresupuestos = null;
	private ModeloTablaNoEditable modeloPresupuestoSeleccionado = null;
	private final static String[] COLUMNAS_PRESUPUESTO = new String[] { "Codigo", "Cliente", "Fecha creación" };
	private JScrollPane scrollSeleccionarPresupuesto;
	private JPanel pnScrollSeleccionarPresupuesto;
	private JScrollPane scrollPresupuestoSeleccionado;

	private JTable tablePresupuestoCliente;
	private JTable tableSeleccionPresupuestoCliente;
	private JTable tablePresupuestosVentas;
	private JTable tableSeleccionarPresupuestoVentas;
	private JPanel panelNortePresupuestoSeleccionado;
	private JPanel pnSeleccion;

	/**
	 * Create the frame.
	 */
	public JPanel getMostrarPresupuestos() {
		modeloListaPresupuestos = obtenerPresupuestosSinClientes(); // TODO comentar para que aparezca
		if (mostrarPresupuestosClientes == null) {
			modeloPresupuestoSeleccionado = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_PRESUPUESTO);

			mostrarPresupuestosClientes = new JPanel();
			mostrarPresupuestosClientes.setBorder(new EmptyBorder(5, 5, 5, 5));
			mostrarPresupuestosClientes.setLayout(new BorderLayout(2, 0));
			mostrarPresupuestosClientes.add(getPnBotonesSeleccionPresupuesto(), BorderLayout.SOUTH);
			mostrarPresupuestosClientes.add(getPanelNortePresupuestoSeleccionado(), BorderLayout.CENTER);
			mostrarPresupuestosClientes.add(getPnSeleccion(), BorderLayout.WEST);
			SwingUtil.autoAdjustColumns(tablePresupuestoCliente);
			SwingUtil.autoAdjustColumns(tableSeleccionPresupuestoCliente);
		}
		return mostrarPresupuestosClientes;
	}

	private ModeloTablaNoEditable obtenerPresupuestosSinClientes() {
		DefaultListModel<Presupuesto> lista = PresupuestosConsultas.getPresupuestosSinCliente();
		ModeloTablaNoEditable tabla = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_PRESUPUESTO);
		if (lista == null) {
			return tabla;
		} else {
			asignarPresupuestosATabla(tabla, lista);
		}
		if (tableSeleccionPresupuestoCliente != null)
			SwingUtil.autoAdjustColumns(tableSeleccionPresupuestoCliente);
		return tabla;
	}

	private void asignarPresupuestosATabla(DefaultTableModel tabla, DefaultListModel<Presupuesto> lista) {
		for (int i = 0; i < lista.size(); i++) {
			Presupuesto p = lista.get(i);
			tabla.addRow(new String[] { p.getCodigoPresupuesto(), p.getDniCliente(), p.getFechaPresupuesto() });
		}
	}

	private JPanel getPnBotonesSeleccionPresupuesto() {
		if (pnBotonesSeleccionPresupuesto == null) {
			pnBotonesSeleccionPresupuesto = new JPanel();
			pnBotonesSeleccionPresupuesto.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			pnBotonesSeleccionPresupuesto.add(getBtnCancelarSeleccionPresupuesto());
			pnBotonesSeleccionPresupuesto.add(getBtnSiguienteSeleccionPresupuesto());
		}
		return pnBotonesSeleccionPresupuesto;
	}

	private JButton getBtnSiguienteSeleccionPresupuesto() {
		if (btnSiguienteSeleccionPresupuesto == null) {
			btnSiguienteSeleccionPresupuesto = new JButton("Siguiente");
			btnSiguienteSeleccionPresupuesto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (modeloPresupuestoSeleccionado.getRowCount() == 0) {
						JOptionPane.showMessageDialog(null, "Debes seleccionar primero un presupuesto");
					} else {
						presupuestoSeleccionado = PresupuestosConsultas
								.findByCode(modeloPresupuestoSeleccionado.getValueAt(0, 0).toString());
						limpiarTabla(modeloPresupuestoSeleccionado);
						asignarPresupuestos(listaClientes, obtenerClientes());
						mostrarPanel("asignarCliente");
						log.saveLog(usuarioActivo.getCodigo(),
								"VentanaPrincipal.GestionPresupuestos.AsignarClienteAPresupuesto",
								"Seleccion de presupuesto " + presupuestoSeleccionado.getCodigoPresupuesto());
					}
				}
			});
			btnSiguienteSeleccionPresupuesto.setBackground(new Color(0, 128, 0));
			btnSiguienteSeleccionPresupuesto.setForeground(Color.WHITE);
		}
		return btnSiguienteSeleccionPresupuesto;
	}

	private JButton getBtnCancelarSeleccionPresupuesto() {
		if (btnCancelarSeleccionPresupuesto == null) {
			btnCancelarSeleccionPresupuesto = new JButton("Cancelar");
			btnCancelarSeleccionPresupuesto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					limpiarTabla(modeloPresupuestoSeleccionado);
					mostrarPanel("menu_gpp");
				}
			});
			btnCancelarSeleccionPresupuesto.setBackground(new Color(128, 0, 0));
			btnCancelarSeleccionPresupuesto.setForeground(Color.WHITE);
		}
		return btnCancelarSeleccionPresupuesto;
	}

	private JPanel getPnSeleccionarPresupuesto() {
		if (pnSeleccionarPresupuesto == null) {
			pnSeleccionarPresupuesto = new JPanel();
			pnSeleccionarPresupuesto.setLayout(new BorderLayout(0, 0));
			pnSeleccionarPresupuesto.add(getPnBotonSeleccionar(), BorderLayout.SOUTH);
			pnSeleccionarPresupuesto.add(getPnScrollSeleccionarPresupuesto());
			// pnSeleccionarPresupuesto.add(getPnListaPresupuestos(), BorderLayout.NORTH);
		}
		return pnSeleccionarPresupuesto;
	}

	private JPanel getPnBotonSeleccionar() {
		if (pnBotonSeleccionar == null) {
			pnBotonSeleccionar = new JPanel();
			pnBotonSeleccionar.add(getBtnSeleccionar());
		}
		return pnBotonSeleccionar;
	}

	private JButton getBtnSeleccionar() {
		if (btnSeleccionar == null) {
			btnSeleccionar = new JButton("Seleccionar");
			btnSeleccionar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (modeloPresupuestoSeleccionado.getRowCount() != 0) {
						limpiarTabla(modeloPresupuestoSeleccionado);
					}
					int index = getTablePresupuestoCliente().getSelectedRow();
					if (index < 0) {
						JOptionPane.showMessageDialog(null, "Deber seleccionar primero un presupuesto de la lista");
					} else {
						modeloPresupuestoSeleccionado
								.addRow(new String[] { (String) modeloListaPresupuestos.getValueAt(index, 0),
										(String) modeloListaPresupuestos.getValueAt(index, 1),
										(String) modeloListaPresupuestos.getValueAt(index, 2) });
					}
					SwingUtil.autoAdjustColumns(tableSeleccionPresupuestoCliente);
				}
			});
		}
		return btnSeleccionar;
	}

	private JPanel getPnRelleno() {
		if (pnRelleno == null) {
			pnRelleno = new JPanel();
			pnRelleno.setLayout(new BorderLayout(0, 0));
			pnRelleno.add(getLbPresupuestoSeleccionado(), BorderLayout.SOUTH);
		}
		return pnRelleno;
	}

	private JLabel getLbPresupuestoSeleccionado() {
		if (lbPresupuestoSeleccionado == null) {
			lbPresupuestoSeleccionado = new JLabel("Presupuesto seleccionado:");
			lbPresupuestoSeleccionado.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lbPresupuestoSeleccionado;
	}

	private JPanel getPnPresupuestoSeleccionado() {
		if (pnPresupuestoSeleccionado == null) {
			pnPresupuestoSeleccionado = new JPanel();
			pnPresupuestoSeleccionado.setLayout(new BorderLayout(0, 0));
			pnPresupuestoSeleccionado.add(getScrollPresupuestoSeleccionado(), BorderLayout.CENTER);
		}
		return pnPresupuestoSeleccionado;
	}

	private JScrollPane getScrollSeleccionarPresupuesto() {
		if (scrollSeleccionarPresupuesto == null) {
			scrollSeleccionarPresupuesto = new JScrollPane(getTablePresupuestoCliente());
		}
		return scrollSeleccionarPresupuesto;
	}

	private JTable getTablePresupuestoCliente() {
		if (tablePresupuestoCliente == null) {
			tablePresupuestoCliente = new JTable(modeloListaPresupuestos);
			// modeloTableProdCatalogo = new DefaultTableModel(new String[][] {},
			// COLUMNAS_CP);
			// tableCPDerecha = new JTable(modeloTableProdCatalogo);
			// tableCPDerecha.setBackground(Color.WHITE);
			tablePresupuestoCliente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tablePresupuestoCliente.setDefaultEditor(Object.class, null);
			tablePresupuestoCliente.setFillsViewportHeight(true);
		}
		return tablePresupuestoCliente;
	}

	private JPanel getPnScrollSeleccionarPresupuesto() {
		if (pnScrollSeleccionarPresupuesto == null) {
			pnScrollSeleccionarPresupuesto = new JPanel();
			pnScrollSeleccionarPresupuesto.setLayout(new BorderLayout(0, 0));
			pnScrollSeleccionarPresupuesto.add(getScrollSeleccionarPresupuesto());
		}
		return pnScrollSeleccionarPresupuesto;
	}

	private JScrollPane getScrollPresupuestoSeleccionado() {
		if (scrollPresupuestoSeleccionado == null) {
			scrollPresupuestoSeleccionado = new JScrollPane(getTableSeleccionPresupuestoCliente());
		}
		return scrollPresupuestoSeleccionado;
	}

	private JTable getTableSeleccionPresupuestoCliente() {
		if (tableSeleccionPresupuestoCliente == null) {
			tableSeleccionPresupuestoCliente = new JTable(modeloPresupuestoSeleccionado);
			tableSeleccionPresupuestoCliente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableSeleccionPresupuestoCliente.setDefaultEditor(Object.class, null);
			tableSeleccionPresupuestoCliente.setFillsViewportHeight(true);
		}
		return tableSeleccionPresupuestoCliente;
	}

	private JPanel getPanelNortePresupuestoSeleccionado() {
		if (panelNortePresupuestoSeleccionado == null) {
			panelNortePresupuestoSeleccionado = new JPanel();
			panelNortePresupuestoSeleccionado.setLayout(new GridLayout(3, 1, 0, 0));
			panelNortePresupuestoSeleccionado.add(getPnRelleno());
			panelNortePresupuestoSeleccionado.add(getPnPresupuestoSeleccionado());
		}
		return panelNortePresupuestoSeleccionado;
	}

	private JPanel getPnSeleccion() {
		if (pnSeleccion == null) {
			pnSeleccion = new JPanel();
			pnSeleccion.setLayout(new BorderLayout(0, 0));
			pnSeleccion.add(getPnSeleccionarPresupuesto());
		}
		return pnSeleccion;
	}

	/*
	 * --------- Asignacion de cliente a presupuesto -----------
	 */

	private JPanel asignarClientes;
	private JPanel pnBotones;
	private JButton btnSiguiente;
	private JButton btnVolver;
	private JPanel pnCentro1;
	private JButton btnAsignar;
	private JPanel pnListaClientes;
	private JPanel pnCentro;
	private JPanel pnCentro2;
	private JButton btnNuevoCliente;

	private ModeloTablaNoEditable listaClientes = null;
	private ModeloTablaNoEditable clienteSeleccionado = null;
	private final static String[] COLUMNAS_CLIENTE = new String[] { "Dni", "Nombre", "Apellidos" };
	private JLabel lblClienteAsignado;
	private Presupuesto presupuestoSeleccionado;
	private Cliente cliente;
	private JTable tableSeleccionarCliente;
	private JScrollPane scrollMostrarClientes;
	private JPanel pnClienteSeleccionado;
	private JScrollPane scrollClienteAsignado;
	private JTable tableClienteSeleccionado;

	public JPanel getAsignarClientes() {
		listaClientes = obtenerClientes(); // TODO comentar para que aparezca
		if (asignarClientes == null) {
			clienteSeleccionado = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_CLIENTE);
			asignarClientes = new JPanel();
			asignarClientes.setBorder(new EmptyBorder(5, 5, 5, 5));
			asignarClientes.setLayout(new BorderLayout(0, 0));
			asignarClientes.add(getPnBotones(), BorderLayout.SOUTH);
			asignarClientes.add(getPnCentro(), BorderLayout.CENTER);
			SwingUtil.autoAdjustColumns(tableSeleccionarCliente);
			SwingUtil.autoAdjustColumns(tableClienteSeleccionado);
		}
		return asignarClientes;
	}

	private ModeloTablaNoEditable obtenerClientes() {
		DefaultListModel<Cliente> lista = ClienteConsultas.getListaClientes();
		ModeloTablaNoEditable tabla = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_CLIENTE);
		if (lista == null) {
			return tabla;
		} else {
			asignarClientesATabla(tabla, lista);
		}
		if (tableSeleccionarCliente != null)
			SwingUtil.autoAdjustColumns(tableSeleccionarCliente);
		return tabla;
	}

	private void asignarClientesATabla(DefaultTableModel tabla, DefaultListModel<Cliente> lista) {
		for (int i = 0; i < lista.size(); i++) {
			Cliente c = lista.get(i);
			tabla.addRow(new String[] { c.getDni(), c.getNombre(), c.getApellidos() });
		}
	}

	private JPanel getPnBotones() {
		if (pnBotones == null) {
			pnBotones = new JPanel();
			pnBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			pnBotones.add(getBtnVolver());
			pnBotones.add(getBtnSiguiente());
		}
		return pnBotones;
	}

	private JButton getBtnSiguiente() {
		if (btnSiguiente == null) {
			btnSiguiente = new JButton("Siguiente");
			btnSiguiente.setForeground(Color.WHITE);
			btnSiguiente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (clienteSeleccionado.getRowCount() != 0) {
						cliente = ClienteConsultas.findByDni(clienteSeleccionado.getValueAt(0, 0).toString());
						presupuestoSeleccionado.setCliente(cliente.getDni());
						PresupuestosConsultas.update(presupuestoSeleccionado);
						limpiarTabla(clienteSeleccionado);
						mostrarPanel("menu_gpp");
						log.saveLog(usuarioActivo.getCodigo(),
								"VentanaPrincipal.GestionPresupuestos.AsignarClienteAPresupuesto",
								"Asignacion de presupuesto " + presupuestoSeleccionado.getCodigoPresupuesto()
										+ " al cliente " + cliente.getDni());
					} else {
						JOptionPane.showMessageDialog(null, "Debes seleccionar primero un cliente");
					}
				}
			});
			btnSiguiente.setBackground(new Color(0, 128, 0));
		}
		return btnSiguiente;
	}

	private JButton getBtnVolver() {
		if (btnVolver == null) {
			btnVolver = new JButton("Volver");
			btnVolver.setForeground(Color.WHITE);
			btnVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarPanel("menu_gpp");
				}
			});
			btnVolver.setBackground(new Color(128, 0, 0));
		}
		return btnVolver;
	}

	private JPanel getPnCentro1() {
		if (pnCentro1 == null) {
			pnCentro1 = new JPanel();
			pnCentro1.setLayout(new BorderLayout(0, 0));
			pnCentro1.add(getPnListaClientes());
			pnCentro1.add(getBtnAsignar(), BorderLayout.SOUTH);
		}
		return pnCentro1;
	}

	private JButton getBtnAsignar() {
		if (btnAsignar == null) {
			btnAsignar = new JButton("Asignar");
			btnAsignar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (clienteSeleccionado.getRowCount() != 0) {
						limpiarTabla(clienteSeleccionado);
					}
					int index = getTableSeleccionarCliente().getSelectedRow();
					if (index < 0) {
						JOptionPane.showMessageDialog(null, "Deber seleccionar primero un presupuesto de la lista");
					} else {
						clienteSeleccionado.addRow(new String[] { (String) listaClientes.getValueAt(index, 0),
								(String) listaClientes.getValueAt(index, 1),
								(String) listaClientes.getValueAt(index, 2) });
						cliente = ClienteConsultas.findByDni(listaClientes.getValueAt(index, 0).toString());
					}
					SwingUtil.autoAdjustColumns(tableClienteSeleccionado);
				}
			});
		}
		return btnAsignar;
	}

	private JPanel getPnListaClientes() {
		if (pnListaClientes == null) {
			pnListaClientes = new JPanel();
			pnListaClientes.setLayout(new BorderLayout(0, 0));
			pnListaClientes.add(getScrollMostrarClientes(), BorderLayout.CENTER);
		}
		return pnListaClientes;
	}

	private JPanel getPnCentro() {
		if (pnCentro == null) {
			pnCentro = new JPanel();
			pnCentro.setLayout(new GridLayout(0, 2, 4, 0));
			pnCentro.add(getPnCentro1());
			pnCentro.add(getPnCentro2());
		}
		return pnCentro;
	}

	private JPanel getPnCentro2() {
		if (pnCentro2 == null) {
			pnCentro2 = new JPanel();
			pnCentro2.setLayout(new BorderLayout(0, 0));
			pnCentro2.add(getBtnNuevoCliente(), BorderLayout.SOUTH);
			pnCentro2.add(getPnClienteSeleccionado(), BorderLayout.CENTER);
		}
		return pnCentro2;
	}

	private JButton getBtnNuevoCliente() {
		if (btnNuevoCliente == null) {
			btnNuevoCliente = new JButton("Nuevo cliente");
			btnNuevoCliente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					CrearCliente cl = new CrearCliente(listaClientes, usuarioActivo);
					log.saveLog(usuarioActivo.getCodigo(),
							"VentanaPrincipal.GestionPresupuestos.AsignarClienteAPresupuesto",
							"Solicitud de creacion de nuevo cliente");
					cl.setVisible(true);
				}
			});
		}
		return btnNuevoCliente;
	}

	private JLabel getLblClienteAsignado() {
		if (lblClienteAsignado == null) {
			lblClienteAsignado = new JLabel("Cliente asignado:");
			lblClienteAsignado.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lblClienteAsignado;
	}

	private JScrollPane getScrollMostrarClientes() {
		if (scrollMostrarClientes == null) {
			scrollMostrarClientes = new JScrollPane(getTableSeleccionarCliente());
		}
		return scrollMostrarClientes;
	}

	private JTable getTableSeleccionarCliente() {
		if (tableSeleccionarCliente == null) {
			tableSeleccionarCliente = new JTable(listaClientes);
			tableSeleccionarCliente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableSeleccionarCliente.setFillsViewportHeight(true);
		}
		return tableSeleccionarCliente;
	}

	private JPanel getPnClienteSeleccionado() {
		if (pnClienteSeleccionado == null) {
			pnClienteSeleccionado = new JPanel();
			pnClienteSeleccionado.setLayout(new GridLayout(3, 1, 0, 0));
			pnClienteSeleccionado.add(getPnLabelClienteAsignado());
			pnClienteSeleccionado.add(getScrollClienteAsignado());
		}
		return pnClienteSeleccionado;
	}

	private JScrollPane getScrollClienteAsignado() {
		if (scrollClienteAsignado == null) {
			scrollClienteAsignado = new JScrollPane(getTableClienteSeleccionado());
		}
		return scrollClienteAsignado;
	}

	private JTable getTableClienteSeleccionado() {
		if (tableClienteSeleccionado == null) {
			tableClienteSeleccionado = new JTable(clienteSeleccionado);
			tableClienteSeleccionado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableClienteSeleccionado.setFillsViewportHeight(true);
		}
		return tableClienteSeleccionado;
	}

	private JPanel getPnLabelClienteAsignado() {
		if (pnLabelClienteAsignado == null) {
			pnLabelClienteAsignado = new JPanel();
			pnLabelClienteAsignado.setLayout(new BorderLayout(0, 0));
			pnLabelClienteAsignado.add(getLblClienteAsignado(), BorderLayout.SOUTH);
		}
		return pnLabelClienteAsignado;
	}

	// ----------- Crear ventas ------------

	// --- Seleccionar un presupuesto ---

	private JPanel pnBotonesSeleccionPresupuesto2;
	private JPanel pnSeleccion2;
	private JButton btnSiguienteSeleccionPresupuesto2;
	private JButton btnCancelarSeleccionPresupuesto2;
	private JPanel pnSeleccionarPresupuesto2;
	private JPanel pnMostrarPresupuestos2;
	private JPanel pnBotonSeleccionar2;
	private JButton btnSeleccionar2;
	private JPanel pnRelleno2;
	private JLabel lbPresupuestoSeleccionado2;
	private JPanel pnPresupuestoSeleccionado2;

	private JScrollPane scrollSeleccionarPresupuesto2;
	private JPanel pnScrollSeleccionarPresupuesto2;
	private JScrollPane scrollPresupuestoSeleccionado2;

	private ModeloTablaNoEditable modeloPresupuestoSeleccionadoCreacionDeVentas = null;
	private ModeloTablaNoEditable modeloListaPresupuestosVentas = null;

	public JPanel getMostrarPresupuestosVentas() {
		modeloListaPresupuestosVentas = obtenerPresupuestos();
		if (mostrarPresupuestosVentas == null) {
			modeloPresupuestoSeleccionadoCreacionDeVentas = new ModeloTablaNoEditable(new String[][] {},
					COLUMNAS_PRESUPUESTO);

			mostrarPresupuestosVentas = new JPanel();
			mostrarPresupuestosVentas.setBorder(new EmptyBorder(5, 5, 5, 5));
			mostrarPresupuestosVentas.setLayout(new BorderLayout(0, 0));
			mostrarPresupuestosVentas.add(getPnBotonesSeleccionPresupuesto2(), BorderLayout.SOUTH);
			mostrarPresupuestosVentas.add(getPnSeleccion2(), BorderLayout.CENTER);
			SwingUtil.autoAdjustColumns(tableSeleccionarPresupuestoVentas);
			SwingUtil.autoAdjustColumns(tablePresupuestosVentas);
		}
		return mostrarPresupuestosVentas;
	}

	private ModeloTablaNoEditable obtenerPresupuestos() {
		DefaultListModel<Presupuesto> lista = PresupuestosConsultas.getPresupuestosNoAceptadosConCliente();
		ModeloTablaNoEditable tabla = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_PRESUPUESTO);
		if (lista == null) {
			return tabla;
		} else {
			asignarPresupuestosATabla(tabla, lista);
		}
		if (tableSeleccionarPresupuestoVentas != null)
			SwingUtil.autoAdjustColumns(tableSeleccionarPresupuestoVentas);
		return tabla;
	}

	private JPanel getPnBotonesSeleccionPresupuesto2() {
		if (pnBotonesSeleccionPresupuesto2 == null) {
			pnBotonesSeleccionPresupuesto2 = new JPanel();
			pnBotonesSeleccionPresupuesto2.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			pnBotonesSeleccionPresupuesto2.add(getBtnCancelarSeleccionPresupuesto2());
			pnBotonesSeleccionPresupuesto2.add(getBtnSiguienteSeleccionPresupuesto2());
		}
		return pnBotonesSeleccionPresupuesto2;
	}

	private JPanel getPnSeleccion2() {
		if (pnSeleccion2 == null) {
			pnSeleccion2 = new JPanel();
			pnSeleccion2.setLayout(new GridLayout(0, 2, 5, 0));
			pnSeleccion2.add(getPnSeleccionarPresupuesto2());
			pnSeleccion2.add(getPnMostrarPresupuestos2());
		}
		return pnSeleccion2;
	}

	private JButton getBtnSiguienteSeleccionPresupuesto2() {
		if (btnSiguienteSeleccionPresupuesto2 == null) {
			btnSiguienteSeleccionPresupuesto2 = new JButton("Siguiente");
			btnSiguienteSeleccionPresupuesto2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (modeloPresupuestoSeleccionadoCreacionDeVentas.getRowCount() == 0) {
						JOptionPane.showMessageDialog(null, "Debes seleccionar primero un presupuesto");
					} else {
						int index = tableSeleccionarPresupuestoVentas.getSelectedRow();
						presupuestoSeleccionado = PresupuestosConsultas
								.findByCode(modeloListaPresupuestosVentas.getValueAt(index, 0).toString());
						limpiarTabla(modeloPresupuestoSeleccionadoCreacionDeVentas);
						crearVentaConPresupuestoSeleccionado();
						cargarProductosVenta(presupuestoSeleccionado.getCodVendedor());
						cargarTransportistas();
						ga = new GestionAlmacen();
						mostrarPanel("asignarTransporte");
					}
				}
			});
			btnSiguienteSeleccionPresupuesto2.setBackground(new Color(0, 128, 0));
			btnSiguienteSeleccionPresupuesto2.setForeground(Color.WHITE);
		}
		return btnSiguienteSeleccionPresupuesto2;
	}

	private void crearVentaConPresupuestoSeleccionado() {
		ventaSeleccionada = new Venta();
		ventaSeleccionada.setCodigoVenta(UUID.randomUUID().toString());
		ventaSeleccionada.setDniCliente(presupuestoSeleccionado.getDniCliente());
		ventaSeleccionada
				.añadirProductos(PresupuestosConsultas.getProductos(presupuestoSeleccionado.getCodigoPresupuesto()));
		ventaSeleccionada.setImporteVenta(presupuestoSeleccionado.getImportePresupuesto());
	}

	private JButton getBtnCancelarSeleccionPresupuesto2() {
		if (btnCancelarSeleccionPresupuesto2 == null) {
			btnCancelarSeleccionPresupuesto2 = new JButton("Cancelar");
			btnCancelarSeleccionPresupuesto2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					limpiarTabla(modeloPresupuestoSeleccionadoCreacionDeVentas);
					mostrarPanel("menu_gpp");
				}
			});
			btnCancelarSeleccionPresupuesto2.setBackground(new Color(128, 0, 0));
			btnCancelarSeleccionPresupuesto2.setForeground(Color.WHITE);
		}
		return btnCancelarSeleccionPresupuesto2;
	}

	private JPanel getPnSeleccionarPresupuesto2() {
		if (pnSeleccionarPresupuesto2 == null) {
			pnSeleccionarPresupuesto2 = new JPanel();
			pnSeleccionarPresupuesto2.setLayout(new BorderLayout(0, 0));
			pnSeleccionarPresupuesto2.add(getPnBotonSeleccionar2(), BorderLayout.SOUTH);
			pnSeleccionarPresupuesto2.add(getPnScrollSeleccionarPresupuesto2(), BorderLayout.CENTER);
			// pnSeleccionarPresupuesto.add(getPnListaPresupuestos(), BorderLayout.NORTH);
		}
		return pnSeleccionarPresupuesto2;
	}

	private JPanel getPnMostrarPresupuestos2() {
		if (pnMostrarPresupuestos2 == null) {
			pnMostrarPresupuestos2 = new JPanel();
			pnMostrarPresupuestos2.setLayout(new GridLayout(3, 1, 0, 0));
			pnMostrarPresupuestos2.add(getPnRelleno2());
			pnMostrarPresupuestos2.add(getPnPresupuestoSeleccionado2());
		}
		return pnMostrarPresupuestos2;
	}

	private JPanel getPnBotonSeleccionar2() {
		if (pnBotonSeleccionar2 == null) {
			pnBotonSeleccionar2 = new JPanel();
			pnBotonSeleccionar2.add(getBtnSeleccionar2());
		}
		return pnBotonSeleccionar2;
	}

	private JButton getBtnSeleccionar2() {
		if (btnSeleccionar2 == null) {
			btnSeleccionar2 = new JButton("Seleccionar");
			btnSeleccionar2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (modeloPresupuestoSeleccionadoCreacionDeVentas.getRowCount() != 0) {
						limpiarTabla(modeloPresupuestoSeleccionadoCreacionDeVentas);
					}
					int index = getTableSeleccionarPresupuestoVentas().getSelectedRow();
					if (index < 0) {
						JOptionPane.showMessageDialog(null, "Deber seleccionar primero un presupuesto de la lista");
					} else {
						modeloPresupuestoSeleccionadoCreacionDeVentas
								.addRow(new String[] { (String) modeloListaPresupuestosVentas.getValueAt(index, 0),
										(String) modeloListaPresupuestosVentas.getValueAt(index, 1),
										(String) modeloListaPresupuestosVentas.getValueAt(index, 2) });
					}
					SwingUtil.autoAdjustColumns(tablePresupuestosVentas);
				}
			});
		}
		return btnSeleccionar2;
	}

	private JPanel getPnRelleno2() {
		if (pnRelleno2 == null) {
			pnRelleno2 = new JPanel();
			pnRelleno2.setLayout(new BorderLayout(0, 0));
			pnRelleno2.add(getLbPresupuestoSeleccionado2(), BorderLayout.SOUTH);
		}
		return pnRelleno2;
	}

	private JLabel getLbPresupuestoSeleccionado2() {
		if (lbPresupuestoSeleccionado2 == null) {
			lbPresupuestoSeleccionado2 = new JLabel("Presupuesto seleccionado:");
			lbPresupuestoSeleccionado2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		}
		return lbPresupuestoSeleccionado2;
	}

	private JPanel getPnPresupuestoSeleccionado2() {
		if (pnPresupuestoSeleccionado2 == null) {
			pnPresupuestoSeleccionado2 = new JPanel();
			pnPresupuestoSeleccionado2.setLayout(new BorderLayout(0, 0));
			pnPresupuestoSeleccionado2.add(getScrollPresupuestoSeleccionado2(), BorderLayout.CENTER);
		}
		return pnPresupuestoSeleccionado2;
	}

	private JScrollPane getScrollSeleccionarPresupuesto2() {
		if (scrollSeleccionarPresupuesto2 == null) {
			scrollSeleccionarPresupuesto2 = new JScrollPane(getTableSeleccionarPresupuestoVentas());
		}
		return scrollSeleccionarPresupuesto2;
	}

	private JTable getTableSeleccionarPresupuestoVentas() {
		if (tableSeleccionarPresupuestoVentas == null) {
			tableSeleccionarPresupuestoVentas = new JTable(modeloListaPresupuestosVentas);
			tableSeleccionarPresupuestoVentas.setFillsViewportHeight(true);
		}
		return tableSeleccionarPresupuestoVentas;
	}

	private JPanel getPnScrollSeleccionarPresupuesto2() {
		if (pnScrollSeleccionarPresupuesto2 == null) {
			pnScrollSeleccionarPresupuesto2 = new JPanel();
			pnScrollSeleccionarPresupuesto2.setLayout(new BorderLayout(0, 0));
			pnScrollSeleccionarPresupuesto2.add(getScrollSeleccionarPresupuesto2());
		}
		return pnScrollSeleccionarPresupuesto2;
	}

	private JScrollPane getScrollPresupuestoSeleccionado2() {
		if (scrollPresupuestoSeleccionado2 == null) {
			scrollPresupuestoSeleccionado2 = new JScrollPane(getTablePresupuestosVentas());
		}
		return scrollPresupuestoSeleccionado2;
	}

	private JTable getTablePresupuestosVentas() {
		if (tablePresupuestosVentas == null) {
			tablePresupuestosVentas = new JTable(modeloPresupuestoSeleccionadoCreacionDeVentas);
			tablePresupuestosVentas.setFillsViewportHeight(true);
		}
		return tablePresupuestosVentas;
	}

	/**
	 * ASIGNAR TRANSPORTE A VENTA
	 */

	/**
	 * Columnas de la tabla de productos a transportar
	 */
	public static final Object[] COLUMNAS_PRODUCTOS_A_TRANSPORTAR = new String[] { "Código", "Uds.", "Nombre", "Precio",
			"Montaje" };

	public static final int COLUMNA_CODIGO = 0;

	public static final int COLUMNA_UNIDADES = 1;

	public static final int COLUMNA_NOMBRE = 2;

	public static final int COLUMNA_PRECIO = 3;

	public static final int COLUMNA_MONTAJE = 4;

	private GestionTransporte gt;
	private Transportista transportista;

	private JPanel panelContenidoVenta;
	private JLabel lblProductosARecoger;
	private JScrollPane scrollPaneProductosVenta;
	private JLabel lblTitulo;
	private JPanel panelProductosATransportar;
	private JLabel lblProductosATransportar;
	private JPanel panelTransportistas;
	private JLabel lblTransportistas;
	private JScrollPane scrollPaneTransportistas;
	private JList<Transportista> listTransportistas;
	private DefaultListModel<Transportista> listaModeloTransportistas;
	private JPanel panelInferiorBotones;
	private JPanel panelInferiorBotonesEste;
	private JButton btnConfirmar;

	private DefaultListModel<Producto> modeloProductosVenta;
	private JList<Producto> listProductosVenta;
	private JButton btnAñadirATransporte;
	private JButton btnTransporteVolver;
	private JScrollPane scrollPaneProductosATransportar;
	private JTable tableProductosATransportar;

	private DefaultTableModel modeloTablaProductosATransportar;
	private JButton btnRecogerEnTienda;

	private Pedido pedidoActual;

	private JPanel panelAsignarTransporte;
	private JPanel pnLabelClienteAsignado;
	private JScrollPane scrollPaneProductosHV;
	private JTable tableProductosHV;

	private JPanel getPanelAsignarTransporte() {
		if (panelAsignarTransporte == null) {
			panelAsignarTransporte = new JPanel();
			panelAsignarTransporte.setBorder(new EmptyBorder(5, 5, 5, 5));
			panelAsignarTransporte.setLayout(new BorderLayout(20, 10));
			panelAsignarTransporte.add(getPanelContenidoVenta(), BorderLayout.WEST);
			panelAsignarTransporte.add(getLblTitulo(), BorderLayout.NORTH);
			panelAsignarTransporte.add(getPanelProductosATransportar(), BorderLayout.CENTER);
			panelAsignarTransporte.add(getPanelTransportistas(), BorderLayout.EAST);
			panelAsignarTransporte.add(getPanelInferiorBotones(), BorderLayout.SOUTH);
		}
		return panelAsignarTransporte;
	}

	private void cargarProductosVenta(String codVendedor) {
		gt = new GestionTransporte(codVendedor);
		modeloProductosVenta = new DefaultListModel<Producto>();
		modeloProductosVenta.addAll(gt.cargarProductosVenta(ventaSeleccionada));
		listProductosVenta.setModel(modeloProductosVenta);
	}

	private void cargarTransportistas() {
		listaModeloTransportistas = new DefaultListModel<Transportista>();

		for (Transportista tr : gt.getTransportistas()) {
			listaModeloTransportistas.addElement(tr);
			;
		}

		listTransportistas.setModel(listaModeloTransportistas);
	}

	private JPanel getPanelContenidoVenta() {
		if (panelContenidoVenta == null) {
			panelContenidoVenta = new JPanel();
			panelContenidoVenta.setBorder(new LineBorder(new Color(0, 0, 0)));
			panelContenidoVenta.setLayout(new BorderLayout(0, 0));
			panelContenidoVenta.add(getLblProductosARecoger(), BorderLayout.NORTH);
			panelContenidoVenta.add(getScrollPaneProductosVenta());
			panelContenidoVenta.add(getBtnAñadirATransporte(), BorderLayout.SOUTH);
		}
		return panelContenidoVenta;
	}

	private JLabel getLblProductosARecoger() {
		if (lblProductosARecoger == null) {
			lblProductosARecoger = new JLabel("Productos a Recoger en Tienda:");
			lblProductosARecoger.setFont(new Font("Verdana", Font.BOLD, 14));
			lblProductosARecoger.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			lblProductosARecoger.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblProductosARecoger;
	}

	private JScrollPane getScrollPaneProductosVenta() {
		if (scrollPaneProductosVenta == null) {
			scrollPaneProductosVenta = new JScrollPane();
			scrollPaneProductosVenta.setBorder(null);
			scrollPaneProductosVenta.getViewport().setBackground(new Color(240, 240, 240));
			scrollPaneProductosVenta.setViewportView(getListProductosVenta());
		}
		return scrollPaneProductosVenta;
	}

	private JLabel getLblTitulo() {
		if (lblTitulo == null) {
			lblTitulo = new JLabel("Asignar Transporte a Venta");
			lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lblTitulo.setFont(new Font("Arial Black", Font.PLAIN, 24));
		}
		return lblTitulo;
	}

	private JPanel getPanelProductosATransportar() {
		if (panelProductosATransportar == null) {
			panelProductosATransportar = new JPanel();
			panelProductosATransportar.setBorder(new LineBorder(new Color(0, 0, 0)));
			panelProductosATransportar.setLayout(new BorderLayout(0, 0));
			panelProductosATransportar.add(getLblProductosATransportar(), BorderLayout.NORTH);
			panelProductosATransportar.add(getScrollPaneProductosATransportar(), BorderLayout.CENTER);
			panelProductosATransportar.add(getBtnRecogerEnTienda(), BorderLayout.SOUTH);
		}
		return panelProductosATransportar;
	}

	private JLabel getLblProductosATransportar() {
		if (lblProductosATransportar == null) {
			lblProductosATransportar = new JLabel("Productos a Transportar:");
			lblProductosATransportar.setFont(new Font("Verdana", Font.BOLD, 14));
			lblProductosATransportar.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			lblProductosATransportar.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblProductosATransportar;
	}

	private JPanel getPanelTransportistas() {
		if (panelTransportistas == null) {
			panelTransportistas = new JPanel();
			panelTransportistas.setBorder(new LineBorder(new Color(0, 0, 0)));
			panelTransportistas.setLayout(new BorderLayout(0, 0));
			panelTransportistas.add(getLblTransportistas(), BorderLayout.NORTH);
			panelTransportistas.add(getScrollPaneTransportistas());
		}
		return panelTransportistas;
	}

	private JLabel getLblTransportistas() {
		if (lblTransportistas == null) {
			lblTransportistas = new JLabel("Seleccione un Transportista:");
			lblTransportistas.setFont(new Font("Verdana", Font.BOLD, 14));
			lblTransportistas.setBorder(new LineBorder(new Color(0, 0, 0)));
			lblTransportistas.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return lblTransportistas;
	}

	private JScrollPane getScrollPaneTransportistas() {
		if (scrollPaneTransportistas == null) {
			scrollPaneTransportistas = new JScrollPane();
			scrollPaneTransportistas.setBorder(null);
			scrollPaneTransportistas.setViewportView(getListTransportistas());
		}
		return scrollPaneTransportistas;
	}

	private JList<Transportista> getListTransportistas() {
		if (listTransportistas == null) {
			listTransportistas = new JList<Transportista>();
			listTransportistas.setFont(new Font("Verdana", Font.PLAIN, 10));
			listTransportistas.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			listTransportistas.setBackground(new Color(240, 240, 240));
			DefaultListCellRenderer renderer = (DefaultListCellRenderer) listTransportistas.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return listTransportistas;
	}

	private JPanel getPanelInferiorBotones() {
		if (panelInferiorBotones == null) {
			panelInferiorBotones = new JPanel();
			panelInferiorBotones.setLayout(new BoxLayout(panelInferiorBotones, BoxLayout.X_AXIS));
			panelInferiorBotones.add(getPanelInferiorBotonesEste());
		}
		return panelInferiorBotones;
	}

	private JPanel getPanelInferiorBotonesEste() {
		if (panelInferiorBotonesEste == null) {
			panelInferiorBotonesEste = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelInferiorBotonesEste.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelInferiorBotonesEste.add(getbtnTransporteVolver());
			panelInferiorBotonesEste.add(getBtnConfirmar());
		}
		return panelInferiorBotonesEste;
	}

	private JButton getBtnConfirmar() {
		if (btnConfirmar == null) {
			btnConfirmar = new JButton("Confirmar");
			btnConfirmar.setFocusPainted(false);
			btnConfirmar.setFont(new Font("Verdana", Font.BOLD, 13));
			btnConfirmar.setForeground(new Color(255, 255, 255));
			btnConfirmar.setBackground(new Color(0, 128, 0));
			btnConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (tableProductosATransportar.getRowCount() != 0 && comprobarAsignarTransportista()) {
						concertarFechaHoraEntrega();
					} else if (tableProductosATransportar.getRowCount() == 0) {
						confirmarVentaSinTransporte();
					}
				}
			});
		}
		return btnConfirmar;
	}

	private void confirmarVentaSinTransporte() {
		if (JOptionPane.showConfirmDialog(this,
				"¿Confirmar la creación de esta venta " + "con todos sus productos a recoger en tienda?",
				"Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

			gt.confirmarVentaSinTransporte(ventaSeleccionada);
			confirmarProductosVenta();
			marcarPresupuestoTramitado();

			log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionPresupuestos.ProductosATransportar",
					"Confirmacion venta " + ventaSeleccionada.getCodigoVenta() + " para recogida en tienda completa");
			// Generar factura PDF
			factPdf.generaFacturaPDF(ventaSeleccionada.getCodigoVenta(), ventaSeleccionada.getDniCliente());
			if (ga.seNecesitaPedidoAutomatico())
				crearPedidoAutomatico();

			// Añadimos el importe mas el montaje que en este caso no tiene
			hv.añadirPrecioMontaje(ventaSeleccionada.getCodigoVenta());
			ventaSeleccionada.setImporteVentaMasMontaje(hv.getImporteMasMontaje(ventaSeleccionada.getCodigoVenta()));

			reiniciar();
		}
	}

	protected void marcarPresupuestoTramitado() {
		PresupuestosConsultas.marcarComoTramitado(presupuestoSeleccionado);
	}

	protected void confirmarProductosVenta() {
		Connection con = null;
		String codigo;
		boolean montar;
		int unidades;

		try {
			con = Jdbc.getConnection();

			for (int i = 0; i < modeloProductosVenta.size(); i++) {
				codigo = modeloProductosVenta.getElementAt(i).getCodigo();
				ga.añadirProducto(codigo, modeloProductosVenta.getElementAt(i).getUnidades());
				gt.insertarProductoVenta(con, codigo, ventaSeleccionada.getCodigoVenta(), "Recogida en Tienda", false,
						modeloProductosVenta.getElementAt(i).getUnidades());
			}

			for (int i = 0; i < modeloTablaProductosATransportar.getRowCount(); i++) {
				codigo = modeloTablaProductosATransportar.getValueAt(i, COLUMNA_CODIGO).toString();
				montar = (Boolean) modeloTablaProductosATransportar.getValueAt(i, COLUMNA_MONTAJE);
				unidades = Integer
						.parseInt(modeloTablaProductosATransportar.getValueAt(i, COLUMNA_UNIDADES).toString());
				ga.añadirProducto(codigo, unidades);
				gt.insertarProductoVenta(con, codigo, ventaSeleccionada.getCodigoVenta(), "Transporte", montar,
						unidades);
			}

			ga.restarUnidadesAlmacen();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			Jdbc.close(con);
		}
	}

	protected void crearPedidoAutomatico() {
		pedidoActual = new Pedido();
		List<Producto> productosPedido = ga.getProductosPedidoAutomatico();

		gt.crearPedido(pedidoActual, productosPedido);

		StringBuilder msg = new StringBuilder(
				"Se ha creado un pedido automático " + "al proveedor (" + pedidoActual.getCodigoPedido() + "):\n");

		for (Producto p : productosPedido) {
			msg.append("\n- " + p.getNombre());
			if (p.getUnidades() == 1)
				msg.append(" (" + p.getUnidades() + " ud.)");
			else
				msg.append(" (" + p.getUnidades() + " uds.)");
		}

		JOptionPane.showMessageDialog(this, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
	}

	private void concertarFechaHoraEntrega() {
		DialogoFechaHoraEntrega dfhe = new DialogoFechaHoraEntrega(this);
		dfhe.setVisible(true);
		if (dfhe.getCheck()) {
			// Añadimos el importe mas el montaje
			hv.añadirPrecioMontaje(ventaSeleccionada.getCodigoVenta());
			ventaSeleccionada.setImporteVentaMasMontaje(hv.getImporteMasMontaje(ventaSeleccionada.getCodigoVenta()));
			
			//Crear factura PDF
			VentanaPrincipal.factPdf.generaFacturaPDF(ventaSeleccionada.getCodigoVenta(), ventaSeleccionada.getDniCliente());
		}
		log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionPresupuestos.ProductosATransportar",
				"Confirmacion venta" + ventaSeleccionada.getCodigoVenta() + "con transporte de productos");

	}

	private boolean comprobarAsignarTransportista() {
		if (listTransportistas.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(this,
					"No se ha seleccionado ningún Transportista. " + "Por favor, inténtelo de nuevo.", "Atención",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		transportista = listTransportistas.getSelectedValue();

		return true;
	}

	protected void asignarTransporte(String fechaEntrega, String horaEntrega) {
		gt.asignarTransporteAVenta(ventaSeleccionada, transportista, fechaEntrega, horaEntrega);
	}

	protected void reasignarTransporte(String fechaEntrega, String horaEntrega) {
		if (gt == null) {
			gt = new GestionTransporte("");
		}
		gt.actualizarTransporte(ventaSeleccionadaHistorial, fechaEntrega, horaEntrega);
	}

	private JList<Producto> getListProductosVenta() {
		if (listProductosVenta == null) {
			listProductosVenta = new JList<Producto>();
			listProductosVenta.setBorder(new LineBorder(new Color(0, 0, 0)));
			listProductosVenta.setBackground(new Color(240, 240, 240));
			DefaultListCellRenderer renderer = (DefaultListCellRenderer) listProductosVenta.getCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
		}
		return listProductosVenta;
	}

	private JButton getBtnAñadirATransporte() {
		if (btnAñadirATransporte == null) {
			btnAñadirATransporte = new JButton("Transportar");
			btnAñadirATransporte.setFocusPainted(false);
			btnAñadirATransporte.setFont(new Font("Verdana", Font.BOLD, 12));
			btnAñadirATransporte.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moverSeleccionATransporte();

				}
			});

		}
		return btnAñadirATransporte;
	}

	private void moverSeleccionATransporte() {
		for (Producto p : listProductosVenta.getSelectedValuesList()) {
			modeloTablaProductosATransportar.addRow(new Object[] { p.getCodigo(), "" + p.getUnidades(), p.getNombre(),
					"" + p.getPrecio() + "€", false });
			modeloProductosVenta.removeElement(p);
			log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionPresupuestos.ProductosATransportar",
					"Mueve producto " + p.getCodigo() + " a transporte");
		}
	}

	private void moverSeleccionARecogidaEnTienda() {
		String codigo;
		int fila;

		while (tableProductosATransportar.getSelectedRowCount() > 0) {
			fila = tableProductosATransportar.getSelectedRow();
			codigo = tableProductosATransportar.getValueAt(fila, COLUMNA_CODIGO).toString();

			for (Producto p : ventaSeleccionada.getProductos()) {
				if (p.getCodigo().equals(codigo)) {
					modeloProductosVenta.addElement(p);
					modeloTablaProductosATransportar.removeRow(fila);
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionPresupuestos.ProductosATransportar",
							"Mueve producto " + p.getCodigo() + " a recogida en tienda");
					break;
				}
			}
		}
	}

	private JButton getbtnTransporteVolver() {
		if (btnTransporteVolver == null) {
			btnTransporteVolver = new JButton("Volver");
			btnTransporteVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					reiniciar();
					mostrarPanel("mostrarPresupuestosVentas");
				}
			});
			btnTransporteVolver.setFocusPainted(false);
			btnTransporteVolver.setFont(new Font("Verdana", Font.BOLD, 13));
			btnTransporteVolver.setForeground(new Color(255, 255, 255));
			btnTransporteVolver.setBackground(new Color(128, 0, 0));
		}
		return btnTransporteVolver;
	}

	private JScrollPane getScrollPaneProductosATransportar() {
		if (scrollPaneProductosATransportar == null) {
			scrollPaneProductosATransportar = new JScrollPane();
			scrollPaneProductosATransportar.setBorder(new LineBorder(new Color(0, 0, 0)));
			scrollPaneProductosATransportar.setViewportView(getTableProductosATransportar());
		}
		return scrollPaneProductosATransportar;
	}

	private JTable getTableProductosATransportar() {
		if (tableProductosATransportar == null) {
			modeloTablaProductosATransportar = new ModeloTablaTransporte();
			tableProductosATransportar = new JTable(modeloTablaProductosATransportar);
			tableProductosATransportar.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			tableProductosATransportar.getColumnModel().getColumn(COLUMNA_CODIGO).setPreferredWidth(50);
			tableProductosATransportar.getColumnModel().getColumn(COLUMNA_UNIDADES).setPreferredWidth(40);
			tableProductosATransportar.getColumnModel().getColumn(COLUMNA_NOMBRE).setPreferredWidth(133);
			tableProductosATransportar.getColumnModel().getColumn(COLUMNA_PRECIO).setPreferredWidth(60);
			tableProductosATransportar.getColumnModel().getColumn(COLUMNA_MONTAJE).setPreferredWidth(60);
			tableProductosATransportar.setBorder(null);
			tableProductosATransportar.setBackground(Color.WHITE);
			tableProductosATransportar.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			tableProductosATransportar.setFillsViewportHeight(true);
		}
		return tableProductosATransportar;
	}

	private JButton getBtnRecogerEnTienda() {
		if (btnRecogerEnTienda == null) {
			btnRecogerEnTienda = new JButton("Recoger en Tienda");
			btnRecogerEnTienda.setFocusPainted(false);
			btnRecogerEnTienda.setFont(new Font("Verdana", Font.BOLD, 12));
			btnRecogerEnTienda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moverSeleccionARecogidaEnTienda();
				}
			});
		}
		return btnRecogerEnTienda;
	}

	public Transportista getTransportista() {
		return transportista;
	}

	protected void reiniciar() {
		for (int i = modeloTablaProductosATransportar.getRowCount() - 1; i >= 0; i--) {
			modeloTablaProductosATransportar.removeRow(i);
		}

		modeloProductosVenta.clear();
		// cargarProductosVenta();
		listTransportistas.clearSelection();
		mostrarPanel("menu");
	}

	private JPanel getPanelHV() {
		if (panelHV == null) {
			panelHV = new JPanel();
			panelHV.setLayout(new BorderLayout(0, 0));
			panelHV.add(getPanelFiltroHV(), BorderLayout.NORTH);
			panelHV.add(getPanelHVLista(), BorderLayout.CENTER);
			panelHV.add(getPanelHVBotones(), BorderLayout.SOUTH);
		}
		return panelHV;
	}

	private JPanel getPanelFiltroHV() {
		if (panelFiltroHV == null) {
			panelFiltroHV = new JPanel();
			panelFiltroHV.add(getLblFiltroHV());
			panelFiltroHV.add(getSpHVFechaIn());
			panelFiltroHV.add(getLblFiltroHVa());
			panelFiltroHV.add(getSpHVFechaFin());
			panelFiltroHV.add(getBtnHVFiltrar());
			panelFiltroHV.add(getBtnHVFiltrarOff());
		}
		return panelFiltroHV;
	}

	private JPanel getPanelHVLista() {
		if (panelHVLista == null) {
			panelHVLista = new JPanel();
			panelHVLista.setLayout(new GridLayout(1, 0, 0, 0));
			panelHVLista.add(getScrollPaneHV());
			panelHVLista.add(getScrollPaneProductosHV());
		}
		return panelHVLista;
	}

	private JPanel getPanelHVBotones() {
		if (panelHVBotones == null) {
			panelHVBotones = new JPanel();
			panelHVBotones.add(getBtnNewButton());
		}
		return panelHVBotones;
	}

	private JScrollPane getScrollPaneHV() {
		if (scrollPaneHV == null) {
			scrollPaneHV = new JScrollPane();
			scrollPaneHV.setViewportView(getTableHV());
		}
		return scrollPaneHV;
	}

	private JLabel getLblFiltroHV() {
		if (lblFiltroHV == null) {
			lblFiltroHV = new JLabel("Introduce fechas: ");
		}
		return lblFiltroHV;
	}

	private JTable getTableHV() {
		if (tableHV == null) {
			modeloTableHistorialVentas = new DefaultTableModel(new String[][] {}, COLUMNAS_HV);
			tableHV = new JTable(modeloTableHistorialVentas);
			tableHV.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int index = getTableHV().getSelectedRow();
					if (index < 0)
						return;
					else {
						actualizarProductosVenta(String.valueOf(modeloTableHistorialVentas.getValueAt(index, 0)));
						log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.HistorialVentas", "Seleccionada venta "
								+ modeloTableHistorialVentas.getValueAt(index, 0) + " del historial de ventas");
					}
					if (e.getClickCount() == 2) {
						guardarVentaSeleccionadaHistorial(index);
						if (getVentaSeleccionadaHistorial().getFechaEntregaDomicilio().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Esta venta es de recogida en tienda");
						} else {
							crearVentanaInfo();
							log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.HistorialVentas",
									"Consulta información de venta " + modeloTableHistorialVentas.getValueAt(index, 0)
											+ " del historial de ventas");
						}

					}
				}
			});
			tableHV.setDefaultEditor(Object.class, null);
			tableHV.setFillsViewportHeight(true);
		}
		return tableHV;
	}

	protected void guardarVentaSeleccionadaHistorial(int index) {
		ventaSeleccionadaHistorial = new Venta();
		ventaSeleccionadaHistorial = hv.getVentaBBDD(String.valueOf(modeloTableHistorialVentas.getValueAt(index, 0)));

	}

	protected void crearVentanaInfo() {
		InfoVenta iV = new InfoVenta(this);
		iV.setVisible(true);

	}

	protected void actualizarProductosVenta(String codigoVenta) {
		tableProductosHV = null;
		scrollPaneProductosHV.setViewportView(getTableProductosHV());
		hv.cargarProductosVenta(codigoVenta);
		List<ProductoVenta> list = hv.getProductosVenta();
		String montaje = null;
		String fechaEntrega = null;
		for (ProductoVenta p : list) {
			if (p.getMontaje() == 1) {
				montaje = "Si";
			} else {
				montaje = "No";
			}
			if (p.getFechaEntrega().equals("")) {
				fechaEntrega = "Recogida en tienda";
			} else {
				fechaEntrega = p.getFechaEntrega();
			}
			modeloTableProductosHv.addRow(new String[] { p.getNombre(), montaje, p.getTransporte(), fechaEntrega,
					String.valueOf(p.getUnidades()) });
		}
		SwingUtil.autoAdjustColumns(tableProductosHV);
	}

	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("Atras");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarPanel("menu");
				}
			});
		}
		return btnNewButton;
	}

	private JSpinner getSpHVFechaIn() {
		if (spHVFechaIn == null) {
			spHVFechaIn = new JSpinner();
			spHVFechaIn.setModel(
					new SpinnerDateModel(new Date(631148400000L), new Date(631148400000L), null, Calendar.DAY_OF_YEAR));
		}
		return spHVFechaIn;
	}

	private JLabel getLblFiltroHVa() {
		if (lblFiltroHVa == null) {
			lblFiltroHVa = new JLabel(" a ");
		}
		return lblFiltroHVa;
	}

	private JSpinner getSpHVFechaFin() {
		if (spHVFechaFin == null) {
			spHVFechaFin = new JSpinner();
			spHVFechaFin.setModel(new SpinnerDateModel(new Date(1602799200000L), new Date(631148400000L), null,
					Calendar.DAY_OF_YEAR));
		}
		return spHVFechaFin;
	}

	private JButton getBtnHVFiltrar() {
		if (btnHVFiltrar == null) {
			btnHVFiltrar = new JButton("Filtrar");
			btnHVFiltrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Date fechaIn = (Date) getSpHVFechaIn().getValue();
					Date fechaFin = (Date) getSpHVFechaFin().getValue();
					if (fechaIn.after(fechaFin)) {
						JOptionPane.showMessageDialog(null, "La fecha inicial no puede ser mayor a la final");
					} else {
						hv.filtrarFecha(hv.getHistorialVentas(), fechaIn, fechaFin);
						actualizarHistorialVentas(hv.getHistorialVentasFiltrado());
						log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.HistorialVentas",
								"Filtrado de ventas por fecha y hora");
					}

				}
			});
		}
		return btnHVFiltrar;
	}

	private JButton getBtnHVFiltrarOff() {
		if (btnHVFiltrarOff == null) {
			btnHVFiltrarOff = new JButton("Quitar Filtro");
			btnHVFiltrarOff.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					actualizarHistorialVentas(hv.getHistorialVentas());
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.HistorialVentas",
							"Quitar filtro historial de ventas");
				}
			});
		}
		return btnHVFiltrarOff;
	}

	private JPanel getPanelSP() {
		if (panelSP == null) {
			panelSP = new JPanel();
			panelSP.setLayout(new BorderLayout(0, 0));
			panelSP.add(getPanelListaPedidos(), BorderLayout.CENTER);
			panelSP.add(getPanelTitulosListasPedido(), BorderLayout.NORTH);
			panelSP.add(getPanelBotonesSP(), BorderLayout.SOUTH);
		}
		return panelSP;
	}

	private JPanel getPanelListaPedidos() {
		if (panelListaPedidos == null) {
			panelListaPedidos = new JPanel();
			panelListaPedidos.setLayout(new GridLayout(0, 2, 0, 0));
			panelListaPedidos.add(getScrollPanePedidos());
			panelListaPedidos.add(getScrollPaneProductosPedido());
		}
		return panelListaPedidos;
	}

	private JScrollPane getScrollPanePedidos() {
		if (scrollPanePedidos == null) {
			scrollPanePedidos = new JScrollPane();
			scrollPanePedidos.setViewportView(getTablePedidos());
		}
		return scrollPanePedidos;
	}

	private JScrollPane getScrollPaneProductosPedido() {
		if (scrollPaneProductosPedido == null) {
			scrollPaneProductosPedido = new JScrollPane();
			scrollPaneProductosPedido.setViewportView(getTableProductosPedido());
		}
		return scrollPaneProductosPedido;
	}

	private JTable getTablePedidos() {
		if (tablePedidos == null) {
			modeloTableSeguimientoPedidos = new DefaultTableModel(new String[][] {}, COLUMNAS_SPped);
			tablePedidos = new JTable(modeloTableSeguimientoPedidos);
			tablePedidos.setDefaultEditor(Object.class, null);
			tablePedidos.setFillsViewportHeight(true);
			tablePedidos.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int index = getTablePedidos().getSelectedRow();
					if (index < 0)
						return;
					else {
						// sp.cargarProductosPedido();
						actualizarProductosPedido(String.valueOf(modeloTableSeguimientoPedidos.getValueAt(index, 0)));
					}
				}
			});

		}
		return tablePedidos;
	}

	protected void actualizarProductosPedido(String codigoPedido) {
		tableProductosPedido = null;
		scrollPaneProductosPedido.setViewportView(getTableProductosPedido());
		sp.cargarProductosPedido(codigoPedido);
		List<Producto> list = sp.getProductosPedido();
		for (Producto p : list) {
			modeloTableSPproductos.addRow(new String[] { p.getNombre(), String.valueOf(p.getUnidades()) });
		}

		SwingUtil.autoAdjustColumns(tableProductosPedido);
	}

	private JTable getTableProductosPedido() {
		if (tableProductosPedido == null) {
			modeloTableSPproductos = new DefaultTableModel(new String[][] {}, COLUMNAS_SPprod);
			tableProductosPedido = new JTable(modeloTableSPproductos);
			tableProductosPedido.setDefaultEditor(Object.class, null);
			tableProductosPedido.setFillsViewportHeight(true);
		}
		return tableProductosPedido;
	}

	private JPanel getPanelTitulosListasPedido() {
		if (panelTitulosListasPedido == null) {
			panelTitulosListasPedido = new JPanel();
			panelTitulosListasPedido.setLayout(new GridLayout(1, 0, 0, 0));
			panelTitulosListasPedido.add(getLblPedidosProveedor());
			panelTitulosListasPedido.add(getLblProductosPedido());
		}
		return panelTitulosListasPedido;
	}

	private JLabel getLblPedidosProveedor() {
		if (lblPedidosProveedor == null) {
			lblPedidosProveedor = new JLabel("Pedidos:");
			lblPedidosProveedor.setFont(new Font("Tahoma", Font.BOLD, 14));
		}
		return lblPedidosProveedor;
	}

	private JLabel getLblProductosPedido() {
		if (lblProductosPedido == null) {
			lblProductosPedido = new JLabel("Productos Pedido");
			lblProductosPedido.setFont(new Font("Tahoma", Font.BOLD, 14));
		}
		return lblProductosPedido;
	}

	private JPanel getPanelBotonesSP() {
		if (panelBotonesSP == null) {
			panelBotonesSP = new JPanel();
			panelBotonesSP.add(getBtnAtrasSP());
			panelBotonesSP.add(getBtnConfirmarRecepcionPedido());
		}
		return panelBotonesSP;
	}

	private JButton getBtnAtrasSP() {
		if (btnAtrasSP == null) {
			btnAtrasSP = new JButton("Atras");
			btnAtrasSP.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarPanel("menu_gp");
				}
			});
		}
		return btnAtrasSP;
	}

	private JButton getBtnConfirmarRecepcionPedido() {
		if (btnConfirmarRecepcionPedido == null) {
			btnConfirmarRecepcionPedido = new JButton("Confirmar Recepcion");
			btnConfirmarRecepcionPedido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int index = tablePedidos.getSelectedRow();
					if (index < 0) {
						return;
					} else if (modeloTableSeguimientoPedidos.getValueAt(index, 2).equals("solicitado")) {
						String codigoPedido = String.valueOf(modeloTableSeguimientoPedidos.getValueAt(index, 0));
						sp.confirmarRecepcionPedido(codigoPedido);

						log.saveLog(usuarioActivo.getCodigo(),
								"VentanaPrincipal.GestionDeProductos.SeguimientoDePedidos",
								"Confirmacion de recepcion de pedido "
										+ modeloTableSeguimientoPedidos.getValueAt(index, 0));

						if (modeloTableSeguimientoPedidos.getValueAt(index, 1).toString().toLowerCase()
								.equals("manual"))
							ga.actualizarStock(codigoPedido);
						actualizarSeguimientoPedidos();
					} else {
						JOptionPane.showMessageDialog(null, "El pedido selecionado ya ha sido marcado como recibdo");
					}
				}
			});
			btnConfirmarRecepcionPedido.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return btnConfirmarRecepcionPedido;
	}

	private JButton btnMenuAlmacen;
	private JPanel panelAlmacen;
	private JPanel pnAlmacenNorte;
	private JPanel pnAlmacenCentro;
	private JPanel pnAlmacenSur;
	private JButton btnAlmacenAtras;
	private JLabel lblAlmacenTitulo;
	private JScrollPane scrollPaneAlmacen;
	private JTableAlmacen tableAlmacen;

	protected GestionAlmacen getGestionAlmacen() {
		return ga;
	}

	protected void enviarMailACliente(final Venta venta) {
		Runnable r = new Runnable() {
			public void run() {
				UtilCorreo.enviarCorreoEnEntrega(venta);
			}
		};

		new Thread(r).start();
	}

	public Venta getVentaSeleccionadaHistorial() {
		return ventaSeleccionadaHistorial;
	}

	private JScrollPane getScrollPaneProductosHV() {
		if (scrollPaneProductosHV == null) {
			scrollPaneProductosHV = new JScrollPane();
			scrollPaneProductosHV.setViewportView(getTableProductosHV());
		}
		return scrollPaneProductosHV;
	}

	private JTable getTableProductosHV() {
		if (tableProductosHV == null) {
			modeloTableProductosHv = new DefaultTableModel(new String[][] {}, COLUMNAS_HVprod);
			tableProductosHV = new JTable(modeloTableProductosHv);
			tableProductosHV.setDefaultEditor(Object.class, null);
			tableProductosHV.setFillsViewportHeight(true);
		}
		return tableProductosHV;
	}

	// Crear un nuevo trabajador

	private JPanel añadirTrabajador;
	private JPanel pnCamposCrearTrabajador;
	private JPanel pnBotonesCrearTrabajador;
	private JButton btnCancelarCrearTrabajadores;
	private JButton btnAceptarCrearTrabajadores;
	private JPanel pnNIFCrearTrabajador;
	private JPanel pnNombreTrabajador;
	private JPanel pnApellidosTrabajador;
	private JPanel pnDepartamentoTrabajador;
	private JPanel pnTelefonoTrabajador;
	private JPanel pnDatosAdicionalesTrabajador;
	private JLabel lblNombre;
	private JLabel lblNif;
	private JLabel lblApellidos;
	private JLabel lblDepartamento;
	private JLabel lblTelefono;
	private JButton btnEstablecerHorario;
	private JTextField txfNifTrabajador;
	private JTextField txfNombreTrabajador;
	private JTextField txfApellidosTrabajador;
	private JComboBox<String> cbDepartamento;
	private JTextField txfTelefonoTrabajador;

	private String[] tiposTrabajador = { "Transportista", "Personal de almacen", "Vendedor" };
	private Trabajador trabajadorACrear;

	/**
	 * Create the frame.
	 */
	public JPanel getAñadirTrabajador() {
		if (añadirTrabajador == null) {
			trabajadorACrear = new Trabajador();

			añadirTrabajador = new JPanel();
			añadirTrabajador.setBorder(new EmptyBorder(5, 5, 5, 5));
			añadirTrabajador.setLayout(new BorderLayout(0, 0));
			añadirTrabajador.add(getPnCamposCrearTrabajador(), BorderLayout.CENTER);
			añadirTrabajador.add(getPnBotonesCrearTrabajador(), BorderLayout.SOUTH);
		}
		return añadirTrabajador;
	}

	private JPanel getPnCamposCrearTrabajador() {
		if (pnCamposCrearTrabajador == null) {
			pnCamposCrearTrabajador = new JPanel();
			pnCamposCrearTrabajador.setLayout(new GridLayout(6, 1, 0, 0));
			pnCamposCrearTrabajador.add(getPnNIFCrearTrabajador());
			pnCamposCrearTrabajador.add(getPnNombreTrabajador());
			pnCamposCrearTrabajador.add(getPnApellidosTrabajador());
			pnCamposCrearTrabajador.add(getPnDepartamentoTrabajador());
			pnCamposCrearTrabajador.add(getPnTelefonoTrabajador());
			pnCamposCrearTrabajador.add(getPnDatosAdicionalesTrabajador());
		}
		return pnCamposCrearTrabajador;
	}

	private JPanel getPnBotonesCrearTrabajador() {
		if (pnBotonesCrearTrabajador == null) {
			pnBotonesCrearTrabajador = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnBotonesCrearTrabajador.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			pnBotonesCrearTrabajador.add(getBtnCancelarCrearTrabajadores());
			pnBotonesCrearTrabajador.add(getBtnAceptarCrearTrabajadores());
		}
		return pnBotonesCrearTrabajador;
	}

	private JButton getBtnCancelarCrearTrabajadores() {
		if (btnCancelarCrearTrabajadores == null) {
			btnCancelarCrearTrabajadores = new JButton("Cancelar");
			btnCancelarCrearTrabajadores.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					limpiarCamposCrearTrabajador();
					mostrarPanel("menu");
				}
			});
		}
		return btnCancelarCrearTrabajadores;
	}

	private JButton getBtnAceptarCrearTrabajadores() {
		if (btnAceptarCrearTrabajadores == null) {
			btnAceptarCrearTrabajadores = new JButton("Aceptar");
			btnAceptarCrearTrabajadores.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (comprobarCampos()) {
						establecerDatosDelTrabajador();
						trabajadorACrear.setContraseñaTrabajador(
								UtilLogin.encriptarContraseña(trabajadorACrear.getContraseñaTrabajador()));
						TrabajadorConsultas.add(trabajadorACrear);
						limpiarCamposCrearTrabajador();
						mostrarPanel("menu");
						log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.HistorialVentas",
								"Creacion de trabajador " + trabajadorACrear.getCodigo());
					}
				}
			});
		}
		return btnAceptarCrearTrabajadores;
	}

	private void establecerDatosDelTrabajador() {
		trabajadorACrear.setCodigo(UUID.randomUUID().toString());
		trabajadorACrear.setDni(txfNifTrabajador.getText());
		trabajadorACrear.setNombre(txfNombreTrabajador.getText());
		trabajadorACrear.setApellidos(txfApellidosTrabajador.getText());
		trabajadorACrear.setDepartamento(cbDepartamento.getSelectedItem().toString());
		trabajadorACrear.setTelefono(Long.parseLong(txfTelefonoTrabajador.getText()));
	}

	private boolean comprobarCampos() {
		if (txfNifTrabajador.getText().isBlank() || txfNombreTrabajador.getText().isBlank()
				|| txfApellidosTrabajador.getText().isBlank() || txfTelefonoTrabajador.getText().isBlank()
				|| trabajadorACrear.getHoraFinJornada() == null || trabajadorACrear.getHoraInicioJornada() == null
				|| trabajadorACrear.getUsuarioTrabajador() == null
				|| trabajadorACrear.getContraseñaTrabajador() == null) {
			JOptionPane.showMessageDialog(null, "Debes rellenar todos los campos");
			return false;
		} else {
			try {
				Long.parseLong(txfTelefonoTrabajador.getText());
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(null, "El número de teléfono debe estar formado por números");
				return false;
			}
			return true;
		}
	}

	private void limpiarCamposCrearTrabajador() {
		txfNifTrabajador.setText("");
		txfNombreTrabajador.setText("");
		txfApellidosTrabajador.setText("");
		txfTelefonoTrabajador.setText("");
		cbDepartamento.setSelectedIndex(0);
		trabajadorACrear.setHoraInicioJornada(null);
		trabajadorACrear.setHoraFinJornada(null);
		trabajadorACrear.setUsuarioTrabajador(null);
		trabajadorACrear.setContraseñaTrabajador(null);
	}

	private JPanel getPnNIFCrearTrabajador() {
		if (pnNIFCrearTrabajador == null) {
			pnNIFCrearTrabajador = new JPanel();
			pnNIFCrearTrabajador.add(getLblNif());
			pnNIFCrearTrabajador.add(getTxfNifTrabajador());
		}
		return pnNIFCrearTrabajador;
	}

	private JPanel getPnNombreTrabajador() {
		if (pnNombreTrabajador == null) {
			pnNombreTrabajador = new JPanel();
			pnNombreTrabajador.add(getLblNombre());
			pnNombreTrabajador.add(getTxfNombreTrabajador());
		}
		return pnNombreTrabajador;
	}

	private JPanel getPnApellidosTrabajador() {
		if (pnApellidosTrabajador == null) {
			pnApellidosTrabajador = new JPanel();
			pnApellidosTrabajador.add(getLblApellidos());
			pnApellidosTrabajador.add(getTxfApellidosTrabajador());
		}
		return pnApellidosTrabajador;
	}

	private JPanel getPnDepartamentoTrabajador() {
		if (pnDepartamentoTrabajador == null) {
			pnDepartamentoTrabajador = new JPanel();
			pnDepartamentoTrabajador.add(getLblDepartamento());
			pnDepartamentoTrabajador.add(getCbDepartamento());
		}
		return pnDepartamentoTrabajador;
	}

	private JPanel getPnTelefonoTrabajador() {
		if (pnTelefonoTrabajador == null) {
			pnTelefonoTrabajador = new JPanel();
			pnTelefonoTrabajador.add(getLblTelefono());
			pnTelefonoTrabajador.add(getTxfTelefonoTrabajador());
		}
		return pnTelefonoTrabajador;
	}

	private JPanel getPnDatosAdicionalesTrabajador() {
		if (pnDatosAdicionalesTrabajador == null) {
			pnDatosAdicionalesTrabajador = new JPanel();
			pnDatosAdicionalesTrabajador.add(getBtnEstablecerHorario());
			pnDatosAdicionalesTrabajador.add(getBtnEstablecerUsuarioContraseña());
		}
		return pnDatosAdicionalesTrabajador;
	}

	private JLabel getLblNombre() {
		if (lblNombre == null) {
			lblNombre = new JLabel("Nombre:");
		}
		return lblNombre;
	}

	private JLabel getLblNif() {
		if (lblNif == null) {
			lblNif = new JLabel("NIF:");
		}
		return lblNif;
	}

	private JLabel getLblApellidos() {
		if (lblApellidos == null) {
			lblApellidos = new JLabel("Apellidos:");
		}
		return lblApellidos;
	}

	private JLabel getLblDepartamento() {
		if (lblDepartamento == null) {
			lblDepartamento = new JLabel("Departamento:");
		}
		return lblDepartamento;
	}

	private JLabel getLblTelefono() {
		if (lblTelefono == null) {
			lblTelefono = new JLabel("Numero de contacto:");
		}
		return lblTelefono;
	}

	private JButton getBtnEstablecerHorario() {
		if (btnEstablecerHorario == null) {
			btnEstablecerHorario = new JButton("Establecer horario de trabajo");
			btnEstablecerHorario.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AsignarHorarios ah = new AsignarHorarios(trabajadorACrear);
					ah.setVisible(true);
				}
			});
		}
		return btnEstablecerHorario;
	}

	private JTextField getTxfNifTrabajador() {
		if (txfNifTrabajador == null) {
			txfNifTrabajador = new JTextField();
			txfNifTrabajador.setColumns(40);
		}
		return txfNifTrabajador;
	}

	private JTextField getTxfNombreTrabajador() {
		if (txfNombreTrabajador == null) {
			txfNombreTrabajador = new JTextField();
			txfNombreTrabajador.setColumns(40);
		}
		return txfNombreTrabajador;
	}

	private JTextField getTxfApellidosTrabajador() {
		if (txfApellidosTrabajador == null) {
			txfApellidosTrabajador = new JTextField();
			txfApellidosTrabajador.setColumns(40);
		}
		return txfApellidosTrabajador;
	}

	private JComboBox<String> getCbDepartamento() {
		if (cbDepartamento == null) {
			cbDepartamento = new JComboBox<String>();
			cbDepartamento.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cbDepartamento.getSelectedIndex() == 1)
						getBtnEstablecerUsuarioContraseña().setEnabled(false);
					else
						getBtnEstablecerUsuarioContraseña().setEnabled(true);
				}
			});
			for (int i = 0; i < tiposTrabajador.length; i++)
				cbDepartamento.addItem(tiposTrabajador[i]);
		}
		return cbDepartamento;
	}

	private JTextField getTxfTelefonoTrabajador() {
		if (txfTelefonoTrabajador == null) {
			txfTelefonoTrabajador = new JTextField();
			txfTelefonoTrabajador.setColumns(40);
		}
		return txfTelefonoTrabajador;
	}

	// Realizar pedido al proveedor

	// Seleccionar productos para el pedido

	private JPanel realizarPedidoAlProveedor;
	private JPanel pnRealizarPedido;
	private JPanel pnBotonesRealizarPedidoAProveedor;
	private JButton btnSiguienteRealizarPedidoAProveedor;
	private JButton btnCancelarRealizarPedidoAProveedor;
	private JPanel pnRealizarPedidoTablas;
	private JPanel pnBotonesRealizarPedidoInteracturaConTablas;
	private JPanel pnBotonesRealizarPedidoAsignarProductos;
	private JPanel pnBotonRealizarPedidoEliminarProducto;
	private JButton btnEliminarRealizarPedidoAProveedor;
	private JPanel pnEstablecerCantidadProductoAPedido;
	private JPanel pnRealizarPedidoAñadir;
	private JButton btnAñadirRealizarPedidoAProveedor;
	private JLabel lblEstablecerCatidadProductoAPedido;
	private JSpinner spEstablecerCantidadProductoAPedido;
	private JPanel pnProductosAsignarRealizarPedido;
	private JPanel pnProductosAsignadosRealizarPedido;
	private JScrollPane scrollProductosAsignarRealizarPedido;
	private JScrollPane scrollProductosAsignadosRealizarPedido;
	private JTable tableProductosAsignarRealizarPedido;
	private JTable tableProductosAsignadosRealizarPedido;

	private final static String[] COLUMNAS_PRODUCTOSDELPROVEEDOR = new String[] { "Código", "Nombre", "Tipo",
			"Precio" };
	private final static String[] COLUMNAS_PRODUCTOSASIGNADOS = new String[] { "Código", "Nombre", "Tipo", "Precio",
			"Cantidad" };
	private ModeloTablaNoEditable productosDelProveedor = null;
	private ModeloTablaNoEditable productosAsignados = null;
	private List<Producto> listaDeProductosDelProveedor;

	private Map<String, Double> descuentos = new HashMap<>();

	public JPanel getRealizarPedido() {
		if (realizarPedidoAlProveedor == null) {
			listaDeProductosDelProveedor = ProductoDelPedidoConsultas.getProductosDeProveedor();
			productosAsignados = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_PRODUCTOSASIGNADOS);
			productosDelProveedor = obtenerProductosDelProveedor();
			realizarPedidoAlProveedor = new JPanel();
			realizarPedidoAlProveedor.setBorder(new EmptyBorder(5, 5, 5, 5));
			realizarPedidoAlProveedor.setLayout(new BorderLayout(0, 0));
			realizarPedidoAlProveedor.add(getPnRealizarPedido(), BorderLayout.CENTER);
			realizarPedidoAlProveedor.add(getPnBotonesRealizarPedidoAProveedor(), BorderLayout.SOUTH);
			SwingUtil.autoAdjustColumns(tableProductosAsignarRealizarPedido);
			SwingUtil.autoAdjustColumns(tableProductosAsignadosRealizarPedido);
		}
		return realizarPedidoAlProveedor;
	}

	private ModeloTablaNoEditable obtenerProductosDelProveedor() {
		ModeloTablaNoEditable resultado = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_PRODUCTOSDELPROVEEDOR);

		for (Producto p : listaDeProductosDelProveedor) {
			resultado.addRow(new String[] { p.getCodigo(), p.getNombre(), p.getTipo(), p.getPrecio() + "" });
		}

		return resultado;
	}

	private JPanel getPnRealizarPedido() {
		if (pnRealizarPedido == null) {
			pnRealizarPedido = new JPanel();
			pnRealizarPedido.setLayout(new BorderLayout(0, 0));
			pnRealizarPedido.add(getPnRealizarPedidoTablas(), BorderLayout.CENTER);
			pnRealizarPedido.add(getPnBotonesRealizarPedidoInteracturaConTablas(), BorderLayout.SOUTH);
		}
		return pnRealizarPedido;
	}

	private JPanel getPnBotonesRealizarPedidoAProveedor() {
		if (pnBotonesRealizarPedidoAProveedor == null) {
			pnBotonesRealizarPedidoAProveedor = new JPanel();
			BorderLayout bl_pnBotonesRealizarPedidoAProveedor = new BorderLayout();
			bl_pnBotonesRealizarPedidoAProveedor.setHgap(110);
			pnBotonesRealizarPedidoAProveedor.setLayout(bl_pnBotonesRealizarPedidoAProveedor);
			pnBotonesRealizarPedidoAProveedor.add(getButtonDescuentosAplicables(), BorderLayout.CENTER);
			pnBotonesRealizarPedidoAProveedor.add(getPnBotonesRealizarPedidoAProveedorCancelarSiguiente(),
					BorderLayout.EAST);
			pnBotonesRealizarPedidoAProveedor.add(getPnRealizarPedidoAProveedorEspacio(), BorderLayout.WEST);
		}
		return pnBotonesRealizarPedidoAProveedor;
	}

	private JButton getBtnSiguienteRealizarPedidoAProveedor() {
		if (btnSiguienteRealizarPedidoAProveedor == null) {
			btnSiguienteRealizarPedidoAProveedor = new JButton("Siguiente");
			btnSiguienteRealizarPedidoAProveedor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (productosAsignados.getRowCount() == 0) {
						JOptionPane.showMessageDialog(null, "No se puede crear un pedido sin productos asignados");
					} else {
						spEstablecerCantidadProductoAPedido.setValue(1);
						copiarTabla(productosDelPedido, productosAsignados);
						mostrarPanel("confirmarPedidoAlProveedor");
						log.saveLog(usuarioActivo.getCodigo(),
								"VentanaPrincipal.GestionDeProductos.RealizarPedidoAlProveedor",
								"Realizada creacion del pedido");
					}
				}
			});
		}
		return btnSiguienteRealizarPedidoAProveedor;
	}

	private JButton getBtnCancelarRealizarPedidoAProveedor() {
		if (btnCancelarRealizarPedidoAProveedor == null) {
			btnCancelarRealizarPedidoAProveedor = new JButton("Cancelar");
			btnCancelarRealizarPedidoAProveedor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					spEstablecerCantidadProductoAPedido.setValue(1);
					limpiarTabla(productosAsignados);
					descuentos.clear();
					mostrarPanel("menu_gp");
				}
			});
		}
		return btnCancelarRealizarPedidoAProveedor;
	}

	private JPanel getPnRealizarPedidoTablas() {
		if (pnRealizarPedidoTablas == null) {
			pnRealizarPedidoTablas = new JPanel();
			pnRealizarPedidoTablas.setLayout(new GridLayout(0, 2, 3, 0));
			pnRealizarPedidoTablas.add(getPnProductosAsignarRealizarPedido());
			pnRealizarPedidoTablas.add(getPnProductosAsignadosRealizarPedido());
		}
		return pnRealizarPedidoTablas;
	}

	private JPanel getPnBotonesRealizarPedidoInteracturaConTablas() {
		if (pnBotonesRealizarPedidoInteracturaConTablas == null) {
			pnBotonesRealizarPedidoInteracturaConTablas = new JPanel();
			pnBotonesRealizarPedidoInteracturaConTablas.setLayout(new GridLayout(0, 2, 3, 0));
			pnBotonesRealizarPedidoInteracturaConTablas.add(getPnBotonesRealizarPedidoAsignarProductos());
			pnBotonesRealizarPedidoInteracturaConTablas.add(getPnBotonRealizarPedidoEliminarProducto());
		}
		return pnBotonesRealizarPedidoInteracturaConTablas;
	}

	private JPanel getPnBotonesRealizarPedidoAsignarProductos() {
		if (pnBotonesRealizarPedidoAsignarProductos == null) {
			pnBotonesRealizarPedidoAsignarProductos = new JPanel();
			pnBotonesRealizarPedidoAsignarProductos.setLayout(new GridLayout(0, 2, 0, 0));
			pnBotonesRealizarPedidoAsignarProductos.add(getPnEstablecerCantidadProductoAPedido());
			pnBotonesRealizarPedidoAsignarProductos.add(getPnRealizarPedidoAñadir());
		}
		return pnBotonesRealizarPedidoAsignarProductos;
	}

	private JPanel getPnBotonRealizarPedidoEliminarProducto() {
		if (pnBotonRealizarPedidoEliminarProducto == null) {
			pnBotonRealizarPedidoEliminarProducto = new JPanel();
			pnBotonRealizarPedidoEliminarProducto.add(getBtnEliminarRealizarPedidoAProveedor());
		}
		return pnBotonRealizarPedidoEliminarProducto;
	}

	private JButton getBtnEliminarRealizarPedidoAProveedor() {
		if (btnEliminarRealizarPedidoAProveedor == null) {
			btnEliminarRealizarPedidoAProveedor = new JButton("Eliminar");
			btnEliminarRealizarPedidoAProveedor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = tableProductosAsignadosRealizarPedido.getSelectedRow();
					if (selectedRow < 0) {
						JOptionPane.showMessageDialog(null, "Selecciona primero el producto a eliminar");
					} else {
						descuentos.remove(productosAsignados.getValueAt(selectedRow, 0));
						productosAsignados.removeRow(selectedRow);
						log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.PedidoAProveedor",
								"Eliminado el producto" + productosAsignados.getValueAt(selectedRow, 0).toString()
										+ "del pedido");
					}
				}
			});
		}
		return btnEliminarRealizarPedidoAProveedor;
	}

	private JPanel getPnEstablecerCantidadProductoAPedido() {
		if (pnEstablecerCantidadProductoAPedido == null) {
			pnEstablecerCantidadProductoAPedido = new JPanel();
			pnEstablecerCantidadProductoAPedido.setLayout(new GridLayout(0, 1, 0, 0));
			pnEstablecerCantidadProductoAPedido.add(getLblEstablecerCatidadProductoAPedido());
			pnEstablecerCantidadProductoAPedido.add(getSpEstablecerCantidadProductoAPedido());
		}
		return pnEstablecerCantidadProductoAPedido;
	}

	private JPanel getPnRealizarPedidoAñadir() {
		if (pnRealizarPedidoAñadir == null) {
			pnRealizarPedidoAñadir = new JPanel();
			pnRealizarPedidoAñadir.add(getBtnAñadirRealizarPedidoAProveedor());
		}
		return pnRealizarPedidoAñadir;
	}

	private JButton getBtnAñadirRealizarPedidoAProveedor() {
		if (btnAñadirRealizarPedidoAProveedor == null) {
			btnAñadirRealizarPedidoAProveedor = new JButton("Añadir");
			btnAñadirRealizarPedidoAProveedor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int cantidad = (int) spEstablecerCantidadProductoAPedido.getValue();
					if (cantidad == 0) {
						JOptionPane.showMessageDialog(null, "Selecciona primero la cantidad a añadir");
					} else {
						int selectedRow = tableProductosAsignarRealizarPedido.getSelectedRow();
						if (selectedRow < 0) {
							JOptionPane.showMessageDialog(null, "Selecciona primero el producto a añadir");
						} else {
							añadirLineaATablaPedido(selectedRow);
							SwingUtil.autoAdjustColumns(tableProductosAsignadosRealizarPedido);
						}
					}
				}
			});
		}
		return btnAñadirRealizarPedidoAProveedor;
	}

	private void añadirLineaATablaPedido(int selectedRow) {
		String codigo = productosDelProveedor.getValueAt(selectedRow, 0).toString();
		log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionDeProductos.RealizarPedidoAlProveedor",
				"Añadido producto " + codigo + " al pedido");
		boolean repetido = false;
		int cantidadAAñadir = (int) spEstablecerCantidadProductoAPedido.getValue();
		int i = 0;
		for (i = 0; i < productosAsignados.getRowCount(); i++) {
			if (productosAsignados.getValueAt(i, 0).equals(codigo)) {
				repetido = true;
				break;
			}
		}
		if (repetido) {
			int cantidadAnterior = Integer.parseInt(productosAsignados.getValueAt(i, 4).toString());
			int cantidadFinal = cantidadAAñadir + cantidadAnterior;
			productosAsignados.setValueAt(cantidadFinal, i, 4);

			if (cantidadFinal > 10 && cantidadFinal <= 20)
				descuentos.put(codigo, 5.0);
			else if (cantidadFinal > 20 && cantidadFinal <= 50)
				descuentos.put(codigo, 10.0);
			else if (cantidadFinal > 50)
				descuentos.put(codigo, 20.0);

			double precioSinDescuento = Double.parseDouble(productosDelProveedor.getValueAt(selectedRow, 3).toString())
					* cantidadFinal;

			double precioFinal;

			if (descuentos.containsKey(codigo)) {
				precioFinal = precioSinDescuento - precioSinDescuento * (descuentos.get(codigo) / 100);
				productosAsignados.setValueAt(precioFinal + " (-" + Math.round(descuentos.get(codigo)) + "%)", i, 3);
			} else {
				productosAsignados.setValueAt(precioSinDescuento, i, 3);
			}

			log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.RealizarPedidoAProveedor",
					"Añadidas " + cantidadAAñadir + " unidades del producto "
							+ productosDelProveedor.getValueAt(selectedRow, 0).toString() + " al pedido");
		} else {

			if (cantidadAAñadir > 10 && cantidadAAñadir <= 20)
				descuentos.put(codigo, 5.0);
			else if (cantidadAAñadir > 20 && cantidadAAñadir <= 50)
				descuentos.put(codigo, 10.0);
			else if (cantidadAAñadir > 50)
				descuentos.put(codigo, 20.0);

			double precioSinDescuento = Double.parseDouble(productosDelProveedor.getValueAt(selectedRow, 3).toString())
					* cantidadAAñadir;

			double precioFinal;
			String valorColumnaPrecio = null;

			if (descuentos.containsKey(codigo)) {
				precioFinal = precioSinDescuento - precioSinDescuento * (descuentos.get(codigo) / 100);
				valorColumnaPrecio = precioFinal + " (-" + Math.round(descuentos.get(codigo)) + "%)";
			} else {
				valorColumnaPrecio = "" + precioSinDescuento;
			}

			productosAsignados
					.addRow(new String[] { codigo, productosDelProveedor.getValueAt(selectedRow, 1).toString(),
							productosDelProveedor.getValueAt(selectedRow, 2).toString(), valorColumnaPrecio,
							cantidadAAñadir + "" });
			log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.RealizarPedidoAProveedor",
					"Añadidas " + cantidadAAñadir + " unidades del producto "
							+ productosDelProveedor.getValueAt(selectedRow, 0).toString() + " al pedido");
		}
	}

	private JLabel getLblEstablecerCatidadProductoAPedido() {
		if (lblEstablecerCatidadProductoAPedido == null) {
			lblEstablecerCatidadProductoAPedido = new JLabel("Establecer cantidad a añadir:");
		}
		return lblEstablecerCatidadProductoAPedido;
	}

	private JSpinner getSpEstablecerCantidadProductoAPedido() {
		if (spEstablecerCantidadProductoAPedido == null) {
			spEstablecerCantidadProductoAPedido = new JSpinner();
			spEstablecerCantidadProductoAPedido.setModel(new SpinnerNumberModel(1, 1, null, 1));
		}
		return spEstablecerCantidadProductoAPedido;
	}

	private JPanel getPnProductosAsignarRealizarPedido() {
		if (pnProductosAsignarRealizarPedido == null) {
			pnProductosAsignarRealizarPedido = new JPanel();
			pnProductosAsignarRealizarPedido.setLayout(new BorderLayout(0, 0));
			pnProductosAsignarRealizarPedido.add(getScrollProductosAsignarRealizarPedido());
		}
		return pnProductosAsignarRealizarPedido;
	}

	private JPanel getPnProductosAsignadosRealizarPedido() {
		if (pnProductosAsignadosRealizarPedido == null) {
			pnProductosAsignadosRealizarPedido = new JPanel();
			pnProductosAsignadosRealizarPedido.setLayout(new BorderLayout(0, 0));
			pnProductosAsignadosRealizarPedido.add(getScrollProductosAsignadosRealizarPedido());
		}
		return pnProductosAsignadosRealizarPedido;
	}

	private JScrollPane getScrollProductosAsignarRealizarPedido() {
		if (scrollProductosAsignarRealizarPedido == null) {
			scrollProductosAsignarRealizarPedido = new JScrollPane(getTableProductosAsignarRealizarPedido());
		}
		return scrollProductosAsignarRealizarPedido;
	}

	private JScrollPane getScrollProductosAsignadosRealizarPedido() {
		if (scrollProductosAsignadosRealizarPedido == null) {
			scrollProductosAsignadosRealizarPedido = new JScrollPane(getTableProductosAsignadosRealizarPedido());
		}
		return scrollProductosAsignadosRealizarPedido;
	}

	private JTable getTableProductosAsignarRealizarPedido() {
		if (tableProductosAsignarRealizarPedido == null) {
			tableProductosAsignarRealizarPedido = new JTable(productosDelProveedor);
			tableProductosAsignarRealizarPedido.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableProductosAsignarRealizarPedido.setFillsViewportHeight(true);
		}
		return tableProductosAsignarRealizarPedido;
	}

	private JTable getTableProductosAsignadosRealizarPedido() {
		if (tableProductosAsignadosRealizarPedido == null) {
			tableProductosAsignadosRealizarPedido = new JTable(productosAsignados);
			tableProductosAsignadosRealizarPedido.setFillsViewportHeight(true);
		}
		return tableProductosAsignadosRealizarPedido;
	}

	// Confirmar pedido al proveedor

	private JPanel confirmarPedido;
	private JPanel pnEditarProductosConfirmarPedido;
	private JPanel pnBotonesConfirmarPedido;
	private JButton btnVolverConfirmarPedido;
	private JButton btnConfirmarConfirmarPedido;
	private JPanel pnMostrarProductosConfirmarPedido;
	private JPanel pnBotonesEditarProductosConfirmarPedido;
	private JPanel pnEliminarParcialConfirmarPedido;
	private JPanel pnEliminarTotalConfirmarPedido;
	private JButton btnEliminarTotalConfirmarPedido;
	private JPanel pnCantidadAEliminarConfirmarPedido;
	private JPanel pnBotonEliminarParcialConfirmarPedido;
	private JButton btnEliminarParcialConfirmarPedido;
	private JLabel lblCantidadAEliminar;
	private JSpinner spCantidadAEliminarConfirmarPedido;
	private JScrollPane scrollProductosAIncluir;
	private JTable tableProductosAIncluir;

	private ModeloTablaNoEditable productosDelPedido;
	private JButton btnRealizarPedidoAProveedor;
	private JButton btnAñadirTrabajador;
	private JTabbedPane tabbedPaneCP;
	private JPanel panelCPModelo;
	private JPanel panelCPListasNorteM;
	private JLabel lblCPModelos;
	private JPanel panelCPTablasM;
	private JPanel panelCPListasSurM;
	private JButton btnCPAñadirModelo;
	private JButton btnCPEliminarM;
	private JScrollPane scrollPaneModelos;
	private JTableModelos tableCPModelos;
	private JLabel lblCPPresupuesto2;
	private JPanel panelGraficos;
	private JPanel panelGCentro;
	private JPanel panelGBotones;
	private JButton btnGAtras;
	private JPanel panelG1;
	private JPanel panelGVisualizar;
	private JButton btngVisualizar;
	private JPanel panelG1Norte;
	private JPanel panelG2;
	private JPanel panelG1Arriba;
	private JPanel panelG1Abajo;
	private JLabel lblPeriodo;
	private JLabel lblDatos;
	private JRadioButton rdbtnGMensual;
	private JRadioButton rdbtnGAnual;
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private JRadioButton rdbtnGVendedores;
	private JRadioButton rdbtnBalance;
	private final ButtonGroup buttonGroup_2 = new ButtonGroup();
	private JPanel panelGOpcionesLabel;
	private JPanel panelGOpcionesBotones;
	private JLabel lblTipoDeGrfico;
	private JPanel panelGLineas;
	private JRadioButton rdbtnLineas;
	private JPanel panelGArea;
	private JRadioButton rdbtnArea;
	private JPanel panelGBarras;
	private JRadioButton rdbtnBarras;
	private JPanel panelGSectores;
	private JRadioButton rdbtnSectores;
	private final ButtonGroup buttonGroup_3 = new ButtonGroup();
	private JButton btnEstablecerUsuarioContraseña;
	private JPanel panelMenuBotonesSalir;
	private JButton btnCerrarSesion;
	private JButton btnSalir;
	private JPanel panelContenedorMenu;
	private JButton btnDescuentosAplicables;
	private JPanel pnBotonesRealizarPedidoAProveedorCancelarSiguiente;
	private JPanel pnRealizarPedidoAProveedorEspacio;
	private JLabel lblRealizarPedidoAProveedorEspacio;
	private JLabel lblRealizarPedidoAProveedorEspacio2;
	private JLabel lblRealizarPedidoAProveedorEspacio3;
	private JLabel lblRealizarPedidoAProveedorEspacio4;

	public JPanel getConfirmarPedido() {
		if (confirmarPedido == null) {
			productosDelPedido = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_PRODUCTOSASIGNADOS);
			confirmarPedido = new JPanel();
			confirmarPedido.setBorder(new EmptyBorder(5, 5, 5, 5));
			confirmarPedido.setLayout(new BorderLayout(0, 0));
			confirmarPedido.add(getPnEditarProductosConfirmarPedido(), BorderLayout.CENTER);
			confirmarPedido.add(getPnBotonesConfirmarPedido(), BorderLayout.SOUTH);
			SwingUtil.autoAdjustColumns(tableProductosAIncluir);
		}
		return confirmarPedido;

	}

	/**
	 * Metodo que copia una tabla b en una tabla a. Ambas tablas deben tener el
	 * mismo numero de columnas
	 * 
	 * @param a Tabla en la que se guardará la copia
	 * @param b Tabla de la que se sacará los datos a copiar
	 */
	private void copiarTabla(DefaultTableModel a, DefaultTableModel b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException("Las tablas que se pasan no pueden ser null");
		}

		if (a.getColumnCount() != b.getColumnCount()) {
			throw new IllegalArgumentException("Las tablas que se pasan deben tener el mismo numero de columnas");
		}

		limpiarTabla(a);

		for (int i = 0; i < b.getRowCount(); i++) {
			Object[] valores = new Object[b.getColumnCount()];
			for (int j = 0; j < b.getColumnCount(); j++) {
				valores[j] = b.getValueAt(i, j);
			}
			a.insertRow(i, valores);
			;
		}
	}

	private JPanel getPnEditarProductosConfirmarPedido() {
		if (pnEditarProductosConfirmarPedido == null) {
			pnEditarProductosConfirmarPedido = new JPanel();
			pnEditarProductosConfirmarPedido.setLayout(new BorderLayout(0, 0));
			pnEditarProductosConfirmarPedido.add(getPnMostrarProductosConfirmarPedido(), BorderLayout.CENTER);
			pnEditarProductosConfirmarPedido.add(getPnBotonesEditarProductosConfirmarPedido(), BorderLayout.SOUTH);
		}
		return pnEditarProductosConfirmarPedido;
	}

	private JPanel getPnBotonesConfirmarPedido() {
		if (pnBotonesConfirmarPedido == null) {
			pnBotonesConfirmarPedido = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnBotonesConfirmarPedido.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			pnBotonesConfirmarPedido.add(getBtnVolverConfirmarPedido());
			pnBotonesConfirmarPedido.add(getBtnConfirmarConfirmarPedido());
		}
		return pnBotonesConfirmarPedido;
	}

	private JButton getBtnVolverConfirmarPedido() {
		if (btnVolverConfirmarPedido == null) {
			btnVolverConfirmarPedido = new JButton("Volver");
			btnVolverConfirmarPedido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					spCantidadAEliminarConfirmarPedido.setValue(1);
					copiarTabla(productosAsignados, productosDelPedido);
					mostrarPanel("realizarPedidoAlProveedor");
				}
			});
		}
		return btnVolverConfirmarPedido;
	}

	private JButton getBtnConfirmarConfirmarPedido() {
		if (btnConfirmarConfirmarPedido == null) {
			btnConfirmarConfirmarPedido = new JButton("Confirmar");
			btnConfirmarConfirmarPedido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (productosDelPedido.getRowCount() == 0) {
						JOptionPane.showMessageDialog(null, "No se puede crear un pedido sin productos");
					} else {
						spCantidadAEliminarConfirmarPedido.setValue(1);
						limpiarTabla(productosAsignados);
						descuentos.clear();
						añadirPedido();
						mostrarPanel("menu");
					}
				}
			});
		}
		return btnConfirmarConfirmarPedido;
	}

	private void añadirPedido() {
		Pedido pedido = new Pedido();
		List<Producto> productos = new ArrayList<Producto>();
		for (int i = 0; i < productosDelPedido.getRowCount(); i++) {
			Producto p;
			String[] valores = new String[productosDelPedido.getColumnCount()];
			for (int j = 0; j < productosDelPedido.getColumnCount(); j++) {
				valores[j] = productosDelPedido.getValueAt(i, j).toString();
			}
			String precio = valores[3].split(" ")[0];
			p = new Producto(valores[0], valores[1], valores[2], Integer.parseInt(valores[4]),
					Double.parseDouble(precio));
			productos.add(p);
		}
		pedido.setProductos(productos);
		pedido.setTipoPedido("Manual");
		ProductoDelPedidoConsultas.añadirPedido(pedido);
		log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionDeProductos.ConfirmarPedido",
				"Confirmado y realizado pedido " + pedido.getCodigoPedido() + " al proveedor");
	}

	private JPanel getPnMostrarProductosConfirmarPedido() {
		if (pnMostrarProductosConfirmarPedido == null) {
			pnMostrarProductosConfirmarPedido = new JPanel();
			pnMostrarProductosConfirmarPedido.setLayout(new BorderLayout(0, 0));
			pnMostrarProductosConfirmarPedido.add(getScrollProductosAIncluir(), BorderLayout.CENTER);
		}
		return pnMostrarProductosConfirmarPedido;
	}

	private JPanel getPnBotonesEditarProductosConfirmarPedido() {
		if (pnBotonesEditarProductosConfirmarPedido == null) {
			pnBotonesEditarProductosConfirmarPedido = new JPanel();
			pnBotonesEditarProductosConfirmarPedido.setLayout(new GridLayout(0, 2, 3, 0));
			pnBotonesEditarProductosConfirmarPedido.add(getPnEliminarParcialConfirmarPedido());
			pnBotonesEditarProductosConfirmarPedido.add(getPnEliminarTotalConfirmarPedido());
		}
		return pnBotonesEditarProductosConfirmarPedido;
	}

	private JPanel getPnEliminarParcialConfirmarPedido() {
		if (pnEliminarParcialConfirmarPedido == null) {
			pnEliminarParcialConfirmarPedido = new JPanel();
			pnEliminarParcialConfirmarPedido.setLayout(new GridLayout(0, 3, 0, 0));
			pnEliminarParcialConfirmarPedido.add(getPnCantidadAEliminarConfirmarPedido());
			pnEliminarParcialConfirmarPedido.add(getPnBotonEliminarParcialConfirmarPedido());
		}
		return pnEliminarParcialConfirmarPedido;
	}

	private JPanel getPnEliminarTotalConfirmarPedido() {
		if (pnEliminarTotalConfirmarPedido == null) {
			pnEliminarTotalConfirmarPedido = new JPanel();
			pnEliminarTotalConfirmarPedido.add(getBtnEliminarTotalConfirmarPedido());
		}
		return pnEliminarTotalConfirmarPedido;
	}

	private JButton getBtnEliminarTotalConfirmarPedido() {
		if (btnEliminarTotalConfirmarPedido == null) {
			btnEliminarTotalConfirmarPedido = new JButton("Eliminar Producto");
			btnEliminarTotalConfirmarPedido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = tableProductosAIncluir.getSelectedRow();
					if (selectedRow < 0) {
						JOptionPane.showMessageDialog(null,
								"Para eliminar un producto, primero tienes que seleccionarlo");
					} else {
						descuentos.remove(productosDelPedido.getValueAt(selectedRow, 0));
						productosDelPedido.removeRow(selectedRow);
					}
				}
			});
		}
		return btnEliminarTotalConfirmarPedido;
	}

	private JPanel getPnCantidadAEliminarConfirmarPedido() {
		if (pnCantidadAEliminarConfirmarPedido == null) {
			pnCantidadAEliminarConfirmarPedido = new JPanel();
			pnCantidadAEliminarConfirmarPedido.setLayout(new GridLayout(2, 1, 0, 0));
			pnCantidadAEliminarConfirmarPedido.add(getLblCantidadAEliminar());
			pnCantidadAEliminarConfirmarPedido.add(getSpCantidadAEliminarConfirmarPedido());
		}
		return pnCantidadAEliminarConfirmarPedido;
	}

	private JPanel getPnBotonEliminarParcialConfirmarPedido() {
		if (pnBotonEliminarParcialConfirmarPedido == null) {
			pnBotonEliminarParcialConfirmarPedido = new JPanel();
			pnBotonEliminarParcialConfirmarPedido.add(getBtnEliminarParcialConfirmarPedido());
		}
		return pnBotonEliminarParcialConfirmarPedido;
	}

	private JButton getBtnEliminarParcialConfirmarPedido() {
		if (btnEliminarParcialConfirmarPedido == null) {
			btnEliminarParcialConfirmarPedido = new JButton("Eliminar Unidades");
			btnEliminarParcialConfirmarPedido.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int selectedRow = tableProductosAIncluir.getSelectedRow();
					if (selectedRow < 0) {
						JOptionPane.showMessageDialog(null,
								"Para eliminar un producto, primero tienes que seleccionarlo");
					} else {
						eliminarCantidadDeProductosPedido(selectedRow);
					}
				}
			});
		}
		return btnEliminarParcialConfirmarPedido;
	}

	private void eliminarCantidadDeProductosPedido(int selectedRow) {
		int cantidadActual = Integer.parseInt(productosDelPedido.getValueAt(selectedRow, 4).toString());
		int cantidadAEliminar = (int) spCantidadAEliminarConfirmarPedido.getValue();
		int cantidadFinal = cantidadActual - cantidadAEliminar;
		if (cantidadFinal <= 0) {
			descuentos.remove(productosDelPedido.getValueAt(selectedRow, 0));
			productosDelPedido.removeRow(selectedRow);
		} else {
			String codigo = productosDelPedido.getValueAt(selectedRow, 0).toString();

			double precioInicial;

			if (descuentos.containsKey(codigo)) {
				precioInicial = Double
						.parseDouble(productosDelPedido.getValueAt(selectedRow, 3).toString().split(" ")[0].strip());
				precioInicial = (precioInicial / (1.0 - descuentos.get(codigo) / 100))
						- cantidadAEliminar * getPrecioProveedor(codigo);
			} else {
				precioInicial = Double.parseDouble(productosDelPedido.getValueAt(selectedRow, 3).toString());
			}

			if (cantidadFinal > 10 && cantidadFinal <= 20)
				descuentos.put(codigo, 5.0);
			else if (cantidadFinal > 20 && cantidadFinal <= 50)
				descuentos.put(codigo, 10.0);
			else if (cantidadFinal > 50)
				descuentos.put(codigo, 20.0);
			else
				descuentos.remove(codigo);

			double precioFinal = 0.0;

			if (descuentos.containsKey(codigo)) {
				precioFinal = precioInicial - precioInicial * (descuentos.get(codigo) / 100);
				productosDelPedido.setValueAt(precioFinal + " (-" + Math.round(descuentos.get(codigo)) + "%)",
						selectedRow, 3);
			} else {
				precioFinal = cantidadFinal * getPrecioProveedor(codigo);
				productosDelPedido.setValueAt("" + precioFinal, selectedRow, 3);
			}

			productosDelPedido.setValueAt(cantidadFinal, selectedRow, 4);
		}
	}

	private double getPrecioProveedor(String codigo) {
		for (int i = 0; i < productosDelProveedor.getRowCount(); i++) {
			if (productosDelProveedor.getValueAt(i, 0).equals(codigo))
				return Double.parseDouble(productosDelProveedor.getValueAt(i, 3).toString());
		}

		return 0.0;
	}

	private JLabel getLblCantidadAEliminar() {
		if (lblCantidadAEliminar == null) {
			lblCantidadAEliminar = new JLabel("Cantidad a eliminar:");
		}
		return lblCantidadAEliminar;
	}

	private JSpinner getSpCantidadAEliminarConfirmarPedido() {
		if (spCantidadAEliminarConfirmarPedido == null) {
			spCantidadAEliminarConfirmarPedido = new JSpinner();
			spCantidadAEliminarConfirmarPedido.setModel(new SpinnerNumberModel(1, 1, null, 1));
		}
		return spCantidadAEliminarConfirmarPedido;
	}

	private JScrollPane getScrollProductosAIncluir() {
		if (scrollProductosAIncluir == null) {
			scrollProductosAIncluir = new JScrollPane(getTableProductosAIncluir());
		}
		return scrollProductosAIncluir;
	}

	private JTable getTableProductosAIncluir() {
		if (tableProductosAIncluir == null) {
			tableProductosAIncluir = new JTable(productosDelPedido);
		}
		return tableProductosAIncluir;
	}

	private JButton getBtnMenuAlmacen() {
		if (btnMenuAlmacen == null) {
			btnMenuAlmacen = new JButton("Gestión de almacen");
			btnMenuAlmacen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionDeProductos", "Gestionar almacen");
					ga = new GestionAlmacen();

					tableAlmacen = null;
					scrollPaneAlmacen.setViewportView(getTableAlmacen());

					List<Producto> productosAlmacen = ga.getProductosEnAlmacen();
					for (Producto p : productosAlmacen)
						tableAlmacen
								.addRow(new Object[] { p.getCodigo(), p.getNombre(), p.getTipo(), p.getUnidades() });
					SwingUtil.autoAdjustColumns(tableAlmacen);
					mostrarPanel("almacen");
				}
			});
			btnMenuAlmacen.setFont(new Font("Times New Roman", Font.BOLD, 24));
			btnMenuAlmacen.setFocusPainted(false);
		}
		return btnMenuAlmacen;
	}

	private JPanel getPanelAlmacen() {
		if (panelAlmacen == null) {
			panelAlmacen = new JPanel();
			panelAlmacen.setBackground(Color.WHITE);
			panelAlmacen.setLayout(new BorderLayout(0, 0));
			panelAlmacen.add(getPnAlmacenNorte(), BorderLayout.NORTH);
			panelAlmacen.add(getPnAlmacenCentro());
			panelAlmacen.add(getPnAlmacenSur(), BorderLayout.SOUTH);
		}
		return panelAlmacen;
	}

	private JPanel getPnAlmacenNorte() {
		if (pnAlmacenNorte == null) {
			pnAlmacenNorte = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnAlmacenNorte.getLayout();
			flowLayout.setAlignment(FlowLayout.LEFT);
			pnAlmacenNorte.setBackground(Color.WHITE);
			pnAlmacenNorte.add(getLblAlmacenTitulo());
		}
		return pnAlmacenNorte;
	}

	private JPanel getPnAlmacenCentro() {
		if (pnAlmacenCentro == null) {
			pnAlmacenCentro = new JPanel();
			pnAlmacenCentro.setBackground(Color.WHITE);
			pnAlmacenCentro.setLayout(new GridLayout(0, 1, 0, 0));
			pnAlmacenCentro.add(getScrollPaneAlmacen());
		}
		return pnAlmacenCentro;
	}

	private JPanel getPnAlmacenSur() {
		if (pnAlmacenSur == null) {
			pnAlmacenSur = new JPanel();
			FlowLayout flowLayout = (FlowLayout) pnAlmacenSur.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			pnAlmacenSur.setBackground(Color.WHITE);
			pnAlmacenSur.add(getBtnAlmacenAtras());
		}
		return pnAlmacenSur;
	}

	private JButton getBtnAlmacenAtras() {
		if (btnAlmacenAtras == null) {
			btnAlmacenAtras = new JButton("Atrás");
			btnAlmacenAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu_gp");
				}
			});
			btnAlmacenAtras.setForeground(Color.WHITE);
			btnAlmacenAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnAlmacenAtras.setBackground(new Color(128, 0, 0));
		}
		return btnAlmacenAtras;
	}

	private JLabel getLblAlmacenTitulo() {
		if (lblAlmacenTitulo == null) {
			lblAlmacenTitulo = new JLabel("Datos Generales Almacen");
			lblAlmacenTitulo.setHorizontalAlignment(SwingConstants.CENTER);
			lblAlmacenTitulo.setFont(new Font("Tahoma", Font.BOLD, 20));
		}
		return lblAlmacenTitulo;
	}

	private JScrollPane getScrollPaneAlmacen() {
		if (scrollPaneAlmacen == null) {
			scrollPaneAlmacen = new JScrollPane();
			scrollPaneAlmacen.setViewportView(getTableAlmacen());
		}
		return scrollPaneAlmacen;
	}

	private JTableAlmacen getTableAlmacen() {
		if (tableAlmacen == null) {
			tableAlmacen = new JTableAlmacen(this);
			tableAlmacen.setBackground(Color.WHITE);
			tableAlmacen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableAlmacen.setDefaultEditor(Object.class, null);
			tableAlmacen.setFillsViewportHeight(true);
		}
		return tableAlmacen;
	}

	public void setTransportista(Transportista t) {
		this.transportista = t;

	}

	public Trabajador getUsuarioActivo() {
		return usuarioActivo;
	}

	public Venta getVentaSeleccionada() {
		return ventaSeleccionada;
	}

	private JPanel cambiarPrecioProducto;
	private JPanel pnCambiarPrecioProducto;
	private JPanel pnBtnVolverCambioPrecioProducto;
	private JButton btnVolverCambioPrecioProducto;
	private JPanel pnMostrarProductosCambioPrecio;
	private JPanel pnBotonesDeCambioDePrecio;
	private JPanel pnMostrarNombreProductoSeleccionadoCambioPrecio;
	private JPanel pnEstablecerNuevoPrecioProducto;
	private JPanel pnBtnActualizarCambioPrecioProducto;
	private JButton btnActualizar;
	private JLabel lblEstablecerNuevoPrecio;
	private JTextField txfEstablecerNuevoPrecioProducto;
	private JLabel lblNombreDelProductoCambioPrecio;
	private JTextField txfNombreProductoSeleccionadoCambioPrecio;
	private JScrollPane scrollMostrarProductosCambiarPrecio;
	private JTable tableProductosCambiarPrecio;

	private ModeloTablaNoEditable modeloProductosCambioPrecio = null;
	private JButton btnCambiarPrecioProducto;

	public JPanel getCambiarPrecio() {
		if (cambiarPrecioProducto == null) {
			modeloProductosCambioPrecio = new ModeloTablaNoEditable(new String[][] {}, COLUMNAS_CP);
			cambiarPrecioProducto = new JPanel();
			cambiarPrecioProducto.setBorder(new EmptyBorder(5, 5, 5, 5));
			cambiarPrecioProducto.setLayout(new BorderLayout(0, 0));
			cambiarPrecioProducto.add(getPnCambiarPrecioProducto(), BorderLayout.CENTER);
			cambiarPrecioProducto.add(getPnBtnVolverCambioPrecioProducto(), BorderLayout.SOUTH);
		}
		return cambiarPrecioProducto;
	}

	private void asignarProductosATabla(ModeloTablaNoEditable tabla, List<Producto> lista) {
		for (Producto p : lista) {
			tabla.addRow(new String[] { p.getCodigo(), p.getNombre(), p.getTipo(), p.getPrecio() + "" });
		}
	}

	private JPanel getPnCambiarPrecioProducto() {
		if (pnCambiarPrecioProducto == null) {
			pnCambiarPrecioProducto = new JPanel();
			pnCambiarPrecioProducto.setLayout(new BorderLayout(0, 0));
			pnCambiarPrecioProducto.add(getPnMostrarProductosCambioPrecio(), BorderLayout.CENTER);
			pnCambiarPrecioProducto.add(getPnBotonesDeCambioDePrecio(), BorderLayout.SOUTH);
		}
		return pnCambiarPrecioProducto;
	}

	private JPanel getPnBtnVolverCambioPrecioProducto() {
		if (pnBtnVolverCambioPrecioProducto == null) {
			pnBtnVolverCambioPrecioProducto = new JPanel();
			FlowLayout fl_pnBtnVolverCambioPrecioProducto = (FlowLayout) pnBtnVolverCambioPrecioProducto.getLayout();
			fl_pnBtnVolverCambioPrecioProducto.setAlignment(FlowLayout.RIGHT);
			pnBtnVolverCambioPrecioProducto.add(getBtnVolverCambioPrecioProducto());
		}
		return pnBtnVolverCambioPrecioProducto;
	}

	private JButton getBtnVolverCambioPrecioProducto() {
		if (btnVolverCambioPrecioProducto == null) {
			btnVolverCambioPrecioProducto = new JButton("Volver");
			btnVolverCambioPrecioProducto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					limpiarTabla(modeloProductosCambioPrecio);
					txfNombreProductoSeleccionadoCambioPrecio.setText("");
					txfEstablecerNuevoPrecioProducto.setText("");
					mostrarPanel("menu_gp");
				}
			});
		}
		return btnVolverCambioPrecioProducto;
	}

	private JPanel getPnMostrarProductosCambioPrecio() {
		if (pnMostrarProductosCambioPrecio == null) {
			pnMostrarProductosCambioPrecio = new JPanel();
			pnMostrarProductosCambioPrecio.setLayout(new BorderLayout(0, 0));
			pnMostrarProductosCambioPrecio.add(getScrollMostrarProductosCambiarPrecio(), BorderLayout.CENTER);
		}
		return pnMostrarProductosCambioPrecio;
	}

	private JPanel getPnBotonesDeCambioDePrecio() {
		if (pnBotonesDeCambioDePrecio == null) {
			pnBotonesDeCambioDePrecio = new JPanel();
			pnBotonesDeCambioDePrecio.setLayout(new GridLayout(1, 3, 3, 0));
			pnBotonesDeCambioDePrecio.add(getPnMostrarNombreProductoSeleccionadoCambioPrecio());
			pnBotonesDeCambioDePrecio.add(getPnEstablecerNuevoPrecioProducto());
			pnBotonesDeCambioDePrecio.add(getPnBtnActualizarCambioPrecioProducto());
		}
		return pnBotonesDeCambioDePrecio;
	}

	private JPanel getPnMostrarNombreProductoSeleccionadoCambioPrecio() {
		if (pnMostrarNombreProductoSeleccionadoCambioPrecio == null) {
			pnMostrarNombreProductoSeleccionadoCambioPrecio = new JPanel();
			pnMostrarNombreProductoSeleccionadoCambioPrecio.setLayout(new GridLayout(2, 1, 0, 0));
			pnMostrarNombreProductoSeleccionadoCambioPrecio.add(getLblNombreDelProductoCambioPrecio());
			pnMostrarNombreProductoSeleccionadoCambioPrecio.add(getTxfNombreProductoSeleccionadoCambioPrecio());
		}
		return pnMostrarNombreProductoSeleccionadoCambioPrecio;
	}

	private JPanel getPnEstablecerNuevoPrecioProducto() {
		if (pnEstablecerNuevoPrecioProducto == null) {
			pnEstablecerNuevoPrecioProducto = new JPanel();
			pnEstablecerNuevoPrecioProducto.setLayout(new GridLayout(2, 1, 0, 0));
			pnEstablecerNuevoPrecioProducto.add(getLblEstablecerNuevoPrecio());
			pnEstablecerNuevoPrecioProducto.add(getTxfEstablecerNuevoPrecioProducto());
		}
		return pnEstablecerNuevoPrecioProducto;
	}

	private JPanel getPnBtnActualizarCambioPrecioProducto() {
		if (pnBtnActualizarCambioPrecioProducto == null) {
			pnBtnActualizarCambioPrecioProducto = new JPanel();
			pnBtnActualizarCambioPrecioProducto.add(getBtnActualizar());
		}
		return pnBtnActualizarCambioPrecioProducto;
	}

	private JButton getBtnActualizar() {
		if (btnActualizar == null) {
			btnActualizar = new JButton("Actualizar");
			btnActualizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (tableProductosCambiarPrecio.getSelectedRow() < 0) {
						JOptionPane.showMessageDialog(null, "Primero debes seleccionar un producto de la tabla");
					} else {
						try {
							double precio = Double.parseDouble(txfEstablecerNuevoPrecioProducto.getText());
							precio = Math.floor(precio * 100) / 100;
							int index = tableProductosCambiarPrecio.getSelectedRow();
							ProductosConsultas.actualizarPrecio(
									tableProductosCambiarPrecio.getValueAt(index, 0).toString(), precio);
							actualizarPreciosDePresupuestos(precio, index);
							tableProductosCambiarPrecio.setValueAt(precio, index, 3);
							txfEstablecerNuevoPrecioProducto.setText(precio + "");
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null, "Establece un precio válido");
						}
					}
				}
			});
		}
		return btnActualizar;
	}

	private void actualizarPreciosDePresupuestos(double precio, int index) {
		String codigoDelProducto = tableProductosCambiarPrecio.getValueAt(index, 0).toString();
		List<ContieneProducto> relacion = PresupuestosConsultas.getContieneProductos(codigoDelProducto);
		for (ContieneProducto cp : relacion) {
			if (cp.getPrecioProducto() > precio) {
				double precioAnterior = cp.getPrecioProducto();
				cp.setPrecioProducto(precio);
				PresupuestosConsultas.updateContieneProducto(cp);
				Presupuesto p = PresupuestosConsultas.findByCode(cp.getCodigoPresupuesto());
				double precioActualizado = p.getImportePresupuesto();
				precioActualizado -= precioAnterior * cp.getUnidadesProducto();
				precioActualizado += precio * cp.getUnidadesProducto();
				p.setImportePresupuesto(precioActualizado);
				PresupuestosConsultas.update(p);
			}
		}
		log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.GestionDeProductos.CambiarPrecioProducto",
				"Cambio de precio del producto " + codigoDelProducto + " a " + precio);
	}

	private JLabel getLblEstablecerNuevoPrecio() {
		if (lblEstablecerNuevoPrecio == null) {
			lblEstablecerNuevoPrecio = new JLabel("Establecer nuevo precio del producto:");
		}
		return lblEstablecerNuevoPrecio;
	}

	private JTextField getTxfEstablecerNuevoPrecioProducto() {
		if (txfEstablecerNuevoPrecioProducto == null) {
			txfEstablecerNuevoPrecioProducto = new JTextField();
			txfEstablecerNuevoPrecioProducto.setHorizontalAlignment(SwingConstants.RIGHT);
			txfEstablecerNuevoPrecioProducto.setColumns(10);
		}
		return txfEstablecerNuevoPrecioProducto;
	}

	private JLabel getLblNombreDelProductoCambioPrecio() {
		if (lblNombreDelProductoCambioPrecio == null) {
			lblNombreDelProductoCambioPrecio = new JLabel("Nombre del producto seleccionado:");
		}
		return lblNombreDelProductoCambioPrecio;
	}

	private JTextField getTxfNombreProductoSeleccionadoCambioPrecio() {
		if (txfNombreProductoSeleccionadoCambioPrecio == null) {
			txfNombreProductoSeleccionadoCambioPrecio = new JTextField();
			txfNombreProductoSeleccionadoCambioPrecio.setEditable(false);
			txfNombreProductoSeleccionadoCambioPrecio.setColumns(10);
		}
		return txfNombreProductoSeleccionadoCambioPrecio;
	}

	private JScrollPane getScrollMostrarProductosCambiarPrecio() {
		if (scrollMostrarProductosCambiarPrecio == null) {
			scrollMostrarProductosCambiarPrecio = new JScrollPane(getTableProductosCambiarPrecio());
		}
		return scrollMostrarProductosCambiarPrecio;
	}

	private JTable getTableProductosCambiarPrecio() {
		if (tableProductosCambiarPrecio == null) {
			tableProductosCambiarPrecio = new JTable(modeloProductosCambioPrecio);
			tableProductosCambiarPrecio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableProductosCambiarPrecio.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int index = tableProductosCambiarPrecio.getSelectedRow();
					if (index < 0)
						return;
					else {
						mostrarDatosDelCambioDePrecio(index);
					}
				}
			});
		}
		return tableProductosCambiarPrecio;
	}

	private void mostrarDatosDelCambioDePrecio(int i) {
		txfNombreProductoSeleccionadoCambioPrecio.setText(tableProductosCambiarPrecio.getValueAt(i, 1).toString());
		txfEstablecerNuevoPrecioProducto.setText(tableProductosCambiarPrecio.getValueAt(i, 3).toString());
	}

	private JPanel estadisticasVendedores;
	private JPanel pnMostrarEstadisticasVendedores;
	private JPanel pnBotonesEstadisticasVendedores;
	private JPanel pnSeleccionarFechaVendedores;
	private JLabel lblSeleccionarFechaVendedores;
	private JComboBox<String> cbSeleccionarFechaVendedores;
	private JPanel pnRellenoEstadisticasVendedores;
	private JPanel pnBotonVolverEstadisticasVendedores;
	private JButton btnVolverEstadisticasVendedores;
	private JScrollPane scrollEstadisticasVendedores;
	private JTable tableEstadisticasVendedores;

	private ModeloTablaNoEditable modeloEstadisticasVendedores = null;
	public final static String[] COLUMNAS_ESTADISTICAS_VENDEDORES = new String[] { "Vendedor",
			"Total generado por ventas", "Numero de ventas realizadas" };
	private List<EstadisticasVendedor> estadisticas;
	private JPanel panelMenuEstadisticas;
	private JPanel panelCentralME;
	private JPanel panelVacio;
	private JButton btnEstadisticasDeLos;
	private JButton btnGrficosEstadsticos;
	private JPanel panelVacio_1;
	private JPanel panelSME;
	private JButton btnSalir_1;
	private JButton btnEstadisticas;
	private JPanel panelMenuGP;
	private JPanel panelCentralGP;
	private JPanel panelSGP;
	private JButton btnSalirGP;
	private JButton btnGP;
	private JPanel panelGPP;
	private JPanel panelCentralGPP;
	private JPanel panelSGPP;
	private JButton btnSalirGPP;
	private JButton btnGestinDePresupuestos;

	public JPanel getEstadisticasVendedores() {
		if (estadisticasVendedores == null) {
			estadisticas = new ArrayList<EstadisticasVendedor>();
			modeloEstadisticasVendedores = new ModeloTablaNoEditable(new String[][] {},
					COLUMNAS_ESTADISTICAS_VENDEDORES);
			estadisticasVendedores = new JPanel();
			estadisticasVendedores.setBorder(new EmptyBorder(5, 5, 5, 5));
			estadisticasVendedores.setLayout(new BorderLayout(0, 0));
			estadisticasVendedores.add(getPnMostrarEstadisticasVendedores(), BorderLayout.CENTER);
			estadisticasVendedores.add(getPnBotonesEstadisticasVendedores(), BorderLayout.SOUTH);
		}
		return estadisticasVendedores;
	}

	private void actualizarInterfazEstadisticasVendedores() {
		List<String> fechas = new ArrayList<String>();
		List<Vendedor> vendedores = TrabajadorConsultas.getVendedores();
		for (Vendedor v : vendedores) {
			List<Venta> ventas = VentaConsultas.getVentasPorVendedor(v.getCodigo());
			for (Venta venta : ventas) {
				if (!fechas.contains(venta.getFechaVentaCreada())) {
					fechas.add(venta.getFechaVentaCreada());
				}
			}
			EstadisticasVendedor ev = new EstadisticasVendedor(v, ventas);
			estadisticas.add(ev);
		}
		cbSeleccionarFechaVendedores.addItem("");
		for (String s : fechas) {
			cbSeleccionarFechaVendedores.addItem(s);
		}
		if (cbSeleccionarFechaVendedores.getItemCount() > 1) {
			cbSeleccionarFechaVendedores.setSelectedIndex(0);
			actualizarTablaEstadisticasVendedores();
		}
	}

	private void actualizarTablaEstadisticasVendedores() {
		limpiarTabla(modeloEstadisticasVendedores);
		String fechaSeleccionada = cbSeleccionarFechaVendedores.getSelectedItem().toString();

		List<EstadisticasVendedor> listaAux = new ArrayList<EstadisticasVendedor>();
		for (int i = 0; i < estadisticas.size(); i++) {
			EstadisticasVendedor actual = estadisticas.get(i);
			boolean añadido = false;
			for (int j = 0; j < listaAux.size(); j++) {
				if (actual.getGananciasTotalesPorFecha(fechaSeleccionada) > listaAux.get(j)
						.getGananciasTotalesPorFecha(fechaSeleccionada)) {
					listaAux.add(j, actual);
					añadido = true;
					break;
				}
			}
			if (!añadido) {
				listaAux.add(actual);
			}
		}
		estadisticas = listaAux;

		for (int i = 0; i < estadisticas.size(); i++) {
			EstadisticasVendedor evAux = estadisticas.get(i);
			modeloEstadisticasVendedores
					.addRow(new String[] { evAux.getVendedor().getNombre() + " " + evAux.getVendedor().getApellidos(),
							evAux.getGananciasTotalesPorFecha(fechaSeleccionada) + "",
							evAux.getNumeroDeVentasPorFecha(fechaSeleccionada) + "" });
		}
	}

	private JPanel getPnMostrarEstadisticasVendedores() {
		if (pnMostrarEstadisticasVendedores == null) {
			pnMostrarEstadisticasVendedores = new JPanel();
			pnMostrarEstadisticasVendedores.setLayout(new BorderLayout(0, 0));
			pnMostrarEstadisticasVendedores.add(getScrollEstadisticasVendedores());
		}
		return pnMostrarEstadisticasVendedores;
	}

	private JPanel getPnBotonesEstadisticasVendedores() {
		if (pnBotonesEstadisticasVendedores == null) {
			pnBotonesEstadisticasVendedores = new JPanel();
			pnBotonesEstadisticasVendedores.setLayout(new GridLayout(1, 3, 0, 0));
			pnBotonesEstadisticasVendedores.add(getPnSeleccionarFechaVendedores());
			pnBotonesEstadisticasVendedores.add(getPnRellenoEstadisticasVendedores());
			pnBotonesEstadisticasVendedores.add(getPnBotonVolverEstadisticasVendedores());
		}
		return pnBotonesEstadisticasVendedores;
	}

	private JPanel getPnSeleccionarFechaVendedores() {
		if (pnSeleccionarFechaVendedores == null) {
			pnSeleccionarFechaVendedores = new JPanel();
			pnSeleccionarFechaVendedores.setLayout(new GridLayout(2, 1, 0, 0));
			pnSeleccionarFechaVendedores.add(getLblSeleccionarFechaVendedores());
			pnSeleccionarFechaVendedores.add(getCbSeleccionarFechaVendedores());
		}
		return pnSeleccionarFechaVendedores;
	}

	private JLabel getLblSeleccionarFechaVendedores() {
		if (lblSeleccionarFechaVendedores == null) {
			lblSeleccionarFechaVendedores = new JLabel("Seleccionar una fecha:");
		}
		return lblSeleccionarFechaVendedores;
	}

	private JComboBox<String> getCbSeleccionarFechaVendedores() {
		if (cbSeleccionarFechaVendedores == null) {
			cbSeleccionarFechaVendedores = new JComboBox<String>();
			cbSeleccionarFechaVendedores.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					actualizarTablaEstadisticasVendedores();
				}
			});
		}
		return cbSeleccionarFechaVendedores;
	}

	private JPanel getPnRellenoEstadisticasVendedores() {
		if (pnRellenoEstadisticasVendedores == null) {
			pnRellenoEstadisticasVendedores = new JPanel();
		}
		return pnRellenoEstadisticasVendedores;
	}

	private JPanel getPnBotonVolverEstadisticasVendedores() {
		if (pnBotonVolverEstadisticasVendedores == null) {
			pnBotonVolverEstadisticasVendedores = new JPanel();
			pnBotonVolverEstadisticasVendedores.add(getBtnVolverEstadisticasVendedores());
		}
		return pnBotonVolverEstadisticasVendedores;
	}

	private JButton getBtnVolverEstadisticasVendedores() {
		if (btnVolverEstadisticasVendedores == null) {
			btnVolverEstadisticasVendedores = new JButton("Volver");
			btnVolverEstadisticasVendedores.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cbSeleccionarFechaVendedores.removeAll();
					limpiarTabla(modeloEstadisticasVendedores);
					mostrarPanel("menu_estadisticas");
				}
			});
		}
		return btnVolverEstadisticasVendedores;
	}

	private JScrollPane getScrollEstadisticasVendedores() {
		if (scrollEstadisticasVendedores == null) {
			scrollEstadisticasVendedores = new JScrollPane(getTableEstadisticasVendedores());
		}
		return scrollEstadisticasVendedores;
	}

	private JTable getTableEstadisticasVendedores() {
		if (tableEstadisticasVendedores == null) {
			tableEstadisticasVendedores = new JTable(modeloEstadisticasVendedores);
			tableEstadisticasVendedores.setRowSelectionAllowed(false);
			tableEstadisticasVendedores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		return tableEstadisticasVendedores;
	}

	private JTabbedPane getTabbedPaneCP() {
		if (tabbedPaneCP == null) {
			tabbedPaneCP = new JTabbedPane(JTabbedPane.TOP);
			tabbedPaneCP.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					int i = tabbedPaneCP.getSelectedIndex();
					if (i == 0) {
						btnCPAplicarFiltro.setEnabled(true);
						btnCPQuitarFiltro.setEnabled(true);
						panelCPTablasM.remove(getScrollPaneCPIzquierda());
						panelCPListasCentro.add(getScrollPaneCPIzquierda());
						actualizarPrecio();
					} else {
						btnCPAplicarFiltro.setEnabled(false);
						btnCPQuitarFiltro.setEnabled(false);
						panelCPListasCentro.remove(getScrollPaneCPIzquierda());
						panelCPTablasM.add(getScrollPaneCPIzquierda());
						actualizarPrecio();
					}
				}
			});
			tabbedPaneCP.setFont(new Font("Tahoma", Font.PLAIN, 14));
			tabbedPaneCP.addTab("Catálogo", getPanelCPListas());
			tabbedPaneCP.addTab("Modelos", null, getPanelCPModelo(), null);
		}
		return tabbedPaneCP;
	}

	private JPanel getPanelCPModelo() {
		if (panelCPModelo == null) {
			panelCPModelo = new JPanel();
			panelCPModelo.setBackground(Color.WHITE);
			panelCPModelo.setLayout(new BorderLayout(0, 0));
			panelCPModelo.add(getPanelCPListasNorteM(), BorderLayout.NORTH);
			panelCPModelo.add(getPanelCPTablasM(), BorderLayout.CENTER);
			panelCPModelo.add(getPanelCPListasSurM(), BorderLayout.SOUTH);
		}
		return panelCPModelo;
	}

	private JPanel getPanelCPListasNorteM() {
		if (panelCPListasNorteM == null) {
			panelCPListasNorteM = new JPanel();
			panelCPListasNorteM.setBackground(Color.WHITE);
			panelCPListasNorteM.setLayout(new GridLayout(0, 2, 10, 0));
			panelCPListasNorteM.add(getLblCPModelos());
			panelCPListasNorteM.add(getLblCPPresupuesto2());
		}
		return panelCPListasNorteM;
	}

	private JLabel getLblCPModelos() {
		if (lblCPModelos == null) {
			lblCPModelos = new JLabel("  Modelos");
			lblCPModelos.setHorizontalAlignment(SwingConstants.LEFT);
			lblCPModelos.setFont(new Font("Tahoma", Font.BOLD, 16));
		}
		return lblCPModelos;
	}

	private JPanel getPanelCPTablasM() {
		if (panelCPTablasM == null) {
			panelCPTablasM = new JPanel();
			panelCPTablasM.setBackground(Color.WHITE);
			panelCPTablasM.setLayout(new GridLayout(0, 2, 10, 0));
			panelCPTablasM.add(getScrollPaneModelos());
		}
		return panelCPTablasM;
	}

	private JPanel getPanelCPListasSurM() {
		if (panelCPListasSurM == null) {
			panelCPListasSurM = new JPanel();
			panelCPListasSurM.setBackground(Color.WHITE);
			panelCPListasSurM.setLayout(new GridLayout(0, 2, 10, 0));
			panelCPListasSurM.add(getBtnCPAñadirModelo());
			panelCPListasSurM.add(getBtnCPEliminarM());
		}
		return panelCPListasSurM;
	}

	private JButton getBtnCPAñadirModelo() {
		if (btnCPAñadirModelo == null) {
			btnCPAñadirModelo = new JButton("Añadir modelos");
			btnCPAñadirModelo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					añadirModelos();
				}
			});
			btnCPAñadirModelo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnCPAñadirModelo;
	}

	private void añadirModelos() {
		int amount;
		boolean check = false;
		List<Producto> productos;
		int row;
		for (int i = 0; i < tableCPModelos.getRowCount(); i++) {
			amount = tableCPModelos.getSpinnerValue(i);
			if (amount > 0) {
				productos = cp.getProductosModelo(tableCPModelos.getCodigo(i));
				for (Producto p : productos) {
					row = compruebaPresupuesto(p.getCodigo());
					if (row < 0) {
						modeloTableProdPresupuesto.addRow(new Object[] { p.getCodigo(), p.getNombre(), p.getTipo(),
								String.format("%.2f", p.getPrecio()), new SpinnerCell(p.getUnidades() * amount) });
						cp.addPresupuesto(p.getCodigo(),p.getUnidades() * amount);
					} else {
						((JTablePresupuesto) tableCPIzquierda).aumentarSpinner(row, p.getUnidades() * amount);
					}
				}
				check = true;
			}
		}
		actualizarPrecio();
		if (check) {
			actualizarModelos();
			JOptionPane.showMessageDialog(this, "Los productos de los modelos han sido añadidos satisfactoriamente.");
		} else {
			JOptionPane.showMessageDialog(this,
					"No se añadio ningún producto. Aumente las unidades de un modelo al menos por favor.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private int compruebaPresupuesto(String codigo) {
		for (int i = 0; i < tableCPIzquierda.getRowCount(); i++) {
			if (tableCPIzquierda.getValueAt(i, 0).toString().equals(codigo))
				return i;
		}
		return -1;
	}

	private JButton getBtnCPEliminarM() {
		if (btnCPEliminarM == null) {
			btnCPEliminarM = new JButton("Eliminar");
			btnCPEliminarM.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int index = tableCPIzquierda.getSelectedRow();
					if (index < 0)
						return;
					else {
						cp.removePresupuesto(modeloTableProdPresupuesto.getValueAt(index, 0).toString());
						actualizarPresupuestoEliminar(index);
					}
				}
			});
			btnCPEliminarM.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btnCPEliminarM;
	}

	private JScrollPane getScrollPaneModelos() {
		if (scrollPaneModelos == null) {
			scrollPaneModelos = new JScrollPane();
			scrollPaneModelos.setViewportView(getTableCPModelos());
		}
		return scrollPaneModelos;
	}

	private JTable getTableCPModelos() {
		if (tableCPModelos == null) {
			tableCPModelos = new JTableModelos(this);
			tableCPModelos.setFillsViewportHeight(true);
			tableCPModelos.setDefaultEditor(Object.class, null);
		}
		return tableCPModelos;
	}

	private JLabel getLblCPPresupuesto2() {
		if (lblCPPresupuesto2 == null) {
			lblCPPresupuesto2 = new JLabel("  Productos Presupuesto");
			lblCPPresupuesto2.setHorizontalAlignment(SwingConstants.LEFT);
			lblCPPresupuesto2.setFont(new Font("Tahoma", Font.BOLD, 16));
		}
		return lblCPPresupuesto2;
	}

	private JPanel getPanelGraficos() {
		if (panelGraficos == null) {
			panelGraficos = new JPanel();
			panelGraficos.setBackground(Color.WHITE);
			panelGraficos.setLayout(new BorderLayout(0, 0));
			panelGraficos.add(getPanelGCentro(), BorderLayout.CENTER);
			panelGraficos.add(getPanelGBotones(), BorderLayout.SOUTH);
		}
		return panelGraficos;
	}

	private JPanel getPanelGCentro() {
		if (panelGCentro == null) {
			panelGCentro = new JPanel();
			panelGCentro.setBackground(Color.WHITE);
			panelGCentro.setLayout(new BorderLayout(0, 10));
			panelGCentro.add(getPanelG1(), BorderLayout.CENTER);
			panelGCentro.add(getPanelGVisualizar(), BorderLayout.SOUTH);
		}
		return panelGCentro;
	}

	private JPanel getPanelGBotones() {
		if (panelGBotones == null) {
			panelGBotones = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelGBotones.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelGBotones.setBackground(Color.WHITE);
			panelGBotones.add(getBtnGAtras());
		}
		return panelGBotones;
	}

	private JButton getBtnGAtras() {
		if (btnGAtras == null) {
			btnGAtras = new JButton("Atrás");
			btnGAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu_estadisticas");
				}
			});
			btnGAtras.setForeground(Color.WHITE);
			btnGAtras.setFont(new Font("Tahoma", Font.PLAIN, 14));
			btnGAtras.setBackground(new Color(128, 0, 0));
		}
		return btnGAtras;
	}

	private JPanel getPanelG1() {
		if (panelG1 == null) {
			panelG1 = new JPanel();
			panelG1.setBackground(Color.WHITE);
			panelG1.setLayout(new BorderLayout(0, 10));
			panelG1.add(getPanelG1Norte(), BorderLayout.NORTH);
			panelG1.add(getPanelG2(), BorderLayout.CENTER);
		}
		return panelG1;
	}

	private JPanel getPanelGVisualizar() {
		if (panelGVisualizar == null) {
			panelGVisualizar = new JPanel();
			panelGVisualizar.setBackground(Color.WHITE);
			panelGVisualizar.setBorder(new LineBorder(Color.GRAY));
			panelGVisualizar.add(getBtngVisualizar());
		}
		return panelGVisualizar;
	}

	private JButton getBtngVisualizar() {
		if (btngVisualizar == null) {
			btngVisualizar = new JButton("    Visualizar    ");
			btngVisualizar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					visualizarGrafico();
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.Estadisticas.Graficos",
							"Solicitud de visualizacion de graficos");
				}
			});
			btngVisualizar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		}
		return btngVisualizar;
	}

	private void visualizarGrafico() {
		String str;
		if (rdbtnGMensual.isSelected())
			str = GraficoData.MES;
		else
			str = GraficoData.AÑO;
		if (rdbtnBalance.isSelected())
			visualizarGraficoBalance(str);
		else
			visualizarGraficoVendedores(str);
	}

	private void visualizarGraficoBalance(String str) {
		if (rdbtnLineas.isSelected())
			new GraficoLineasBalance("Balance ganancias-gastos ", str);
		else if (rdbtnArea.isSelected())
			new GraficoAreaBalance("Balance ganancias-gastos ", str);
		else if (rdbtnBarras.isSelected())
			new GraficoBarrasBalance("Balance ganancias-gastos ", str);
		else
			new GraficoSectoresBalance("Balance ganancias-gastos ", str);
	}

	private void visualizarGraficoVendedores(String str) {
		if (rdbtnLineas.isSelected())
			new GraficoLineasVendedores("Ganancias por vendedor ", str);
		else if (rdbtnArea.isSelected())
			new GraficoAreaVendedores("Ganancias por vendedor ", str);
		else if (rdbtnBarras.isSelected())
			new GraficoBarrasVendedores("Ganancias por vendedor ", str);
		else
			new GraficoSectoresVendedores("Ganancias por vendedor ", str);
	}

	private JPanel getPanelG1Norte() {
		if (panelG1Norte == null) {
			panelG1Norte = new JPanel();
			panelG1Norte.setBackground(Color.WHITE);
			panelG1Norte.setLayout(new GridLayout(0, 1, 0, 10));
			panelG1Norte.add(getPanelG1Arriba());
			panelG1Norte.add(getPanelG1Abajo());
		}
		return panelG1Norte;
	}

	private JPanel getPanelG2() {
		if (panelG2 == null) {
			panelG2 = new JPanel();
			panelG2.setBackground(Color.WHITE);
			panelG2.setBorder(new LineBorder(Color.GRAY));
			panelG2.setLayout(new BorderLayout(0, 0));
			panelG2.add(getPanelGOpcionesLabel(), BorderLayout.NORTH);
			panelG2.add(getPanelGOpcionesBotones(), BorderLayout.CENTER);
		}
		return panelG2;
	}

	private JPanel getPanelG1Arriba() {
		if (panelG1Arriba == null) {
			panelG1Arriba = new JPanel();
			panelG1Arriba.setPreferredSize(new Dimension(10, 100));
			panelG1Arriba.setMinimumSize(new Dimension(10, 100));
			panelG1Arriba.setBorder(new LineBorder(Color.GRAY));
			FlowLayout flowLayout = (FlowLayout) panelG1Arriba.getLayout();
			flowLayout.setVgap(35);
			flowLayout.setHgap(30);
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelG1Arriba.setBackground(Color.WHITE);
			panelG1Arriba.setBorder(new LineBorder(Color.GRAY));
			panelG1Arriba.add(getLblPeriodo());
			panelG1Arriba.add(getRdbtnGMensual());
			panelG1Arriba.add(getRdbtnGAnual());
		}
		return panelG1Arriba;
	}

	private JPanel getPanelG1Abajo() {
		if (panelG1Abajo == null) {
			panelG1Abajo = new JPanel();
			panelG1Abajo.setPreferredSize(new Dimension(10, 100));
			panelG1Abajo.setMinimumSize(new Dimension(10, 100));
			FlowLayout flowLayout = (FlowLayout) panelG1Abajo.getLayout();
			flowLayout.setVgap(35);
			flowLayout.setHgap(30);
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelG1Abajo.setBackground(Color.WHITE);
			panelG1Abajo.setBorder(new LineBorder(Color.GRAY));
			panelG1Abajo.add(getLblDatos());
			panelG1Abajo.add(getRdbtnBalance());
			panelG1Abajo.add(getRdbtnGVendedores());
		}
		return panelG1Abajo;
	}

	private JLabel getLblPeriodo() {
		if (lblPeriodo == null) {
			lblPeriodo = new JLabel("Periodo a visualizar: ");
			lblPeriodo.setFont(new Font("Times New Roman", Font.BOLD, 20));
		}
		return lblPeriodo;
	}

	private JLabel getLblDatos() {
		if (lblDatos == null) {
			lblDatos = new JLabel("Datos a visualizar: ");
			lblDatos.setFont(new Font("Times New Roman", Font.BOLD, 20));
		}
		return lblDatos;
	}

	private JRadioButton getRdbtnGMensual() {
		if (rdbtnGMensual == null) {
			rdbtnGMensual = new JRadioButton("Mensual");
			rdbtnGMensual.setSelected(true);
			buttonGroup_1.add(rdbtnGMensual);
			rdbtnGMensual.setBackground(Color.WHITE);
			rdbtnGMensual.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		}
		return rdbtnGMensual;
	}

	private JRadioButton getRdbtnGAnual() {
		if (rdbtnGAnual == null) {
			rdbtnGAnual = new JRadioButton("Anual");
			buttonGroup_1.add(rdbtnGAnual);
			rdbtnGAnual.setBackground(Color.WHITE);
			rdbtnGAnual.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		}
		return rdbtnGAnual;
	}

	private JRadioButton getRdbtnGVendedores() {
		if (rdbtnGVendedores == null) {
			rdbtnGVendedores = new JRadioButton("Datos vendedores");
			buttonGroup_2.add(rdbtnGVendedores);
			rdbtnGVendedores.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			rdbtnGVendedores.setBackground(Color.WHITE);
		}
		return rdbtnGVendedores;
	}

	private JRadioButton getRdbtnBalance() {
		if (rdbtnBalance == null) {
			rdbtnBalance = new JRadioButton("Balance ganancias/pérdidas");
			rdbtnBalance.setSelected(true);
			buttonGroup_2.add(rdbtnBalance);
			rdbtnBalance.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			rdbtnBalance.setBackground(Color.WHITE);
		}
		return rdbtnBalance;
	}

	private JPanel getPanelGOpcionesLabel() {
		if (panelGOpcionesLabel == null) {
			panelGOpcionesLabel = new JPanel();
			panelGOpcionesLabel.setMinimumSize(new Dimension(10, 70));
			panelGOpcionesLabel.setSize(new Dimension(10, 70));
			FlowLayout flowLayout = (FlowLayout) panelGOpcionesLabel.getLayout();
			flowLayout.setVgap(20);
			flowLayout.setHgap(30);
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelGOpcionesLabel.setBackground(Color.WHITE);
			panelGOpcionesLabel.add(getLblTipoDeGrfico());
		}
		return panelGOpcionesLabel;
	}

	private JPanel getPanelGOpcionesBotones() {
		if (panelGOpcionesBotones == null) {
			panelGOpcionesBotones = new JPanel();
			panelGOpcionesBotones.setBackground(Color.WHITE);
			panelGOpcionesBotones.setLayout(new GridLayout(0, 1, 0, 0));
			panelGOpcionesBotones.add(getPanelGLineas());
			panelGOpcionesBotones.add(getPanelGArea());
			panelGOpcionesBotones.add(getPanelGBarras());
			panelGOpcionesBotones.add(getPanelGSectores());
		}
		return panelGOpcionesBotones;
	}

	private JLabel getLblTipoDeGrfico() {
		if (lblTipoDeGrfico == null) {
			lblTipoDeGrfico = new JLabel("Tipo de gráficos disponibles: ");
			lblTipoDeGrfico.setFont(new Font("Times New Roman", Font.BOLD, 20));
		}
		return lblTipoDeGrfico;
	}

	private JPanel getPanelGLineas() {
		if (panelGLineas == null) {
			panelGLineas = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelGLineas.getLayout();
			flowLayout.setHgap(300);
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelGLineas.setBackground(Color.WHITE);
			panelGLineas.add(getRdbtnLineas());
		}
		return panelGLineas;
	}

	private JRadioButton getRdbtnLineas() {
		if (rdbtnLineas == null) {
			rdbtnLineas = new JRadioButton("Gráfico de líneas");
			rdbtnLineas.setSelected(true);
			buttonGroup_3.add(rdbtnLineas);
			rdbtnLineas.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			rdbtnLineas.setBackground(Color.WHITE);
		}
		return rdbtnLineas;
	}

	private JPanel getPanelGArea() {
		if (panelGArea == null) {
			panelGArea = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelGArea.getLayout();
			flowLayout.setHgap(300);
			flowLayout.setAlignment(FlowLayout.LEFT);
			panelGArea.setBackground(Color.WHITE);
			panelGArea.add(getRdbtnArea());
		}
		return panelGArea;
	}

	private JRadioButton getRdbtnArea() {
		if (rdbtnArea == null) {
			rdbtnArea = new JRadioButton("Gráfico de área");
			buttonGroup_3.add(rdbtnArea);
			rdbtnArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			rdbtnArea.setBackground(Color.WHITE);
		}
		return rdbtnArea;
	}

	private JPanel getPanelGBarras() {
		if (panelGBarras == null) {
			panelGBarras = new JPanel();
			FlowLayout fl_panelGBarras = (FlowLayout) panelGBarras.getLayout();
			fl_panelGBarras.setAlignment(FlowLayout.LEFT);
			fl_panelGBarras.setHgap(300);
			panelGBarras.setBackground(Color.WHITE);
			panelGBarras.add(getRdbtnBarras());
		}
		return panelGBarras;
	}

	private JRadioButton getRdbtnBarras() {
		if (rdbtnBarras == null) {
			rdbtnBarras = new JRadioButton("Gráfico de barras");
			buttonGroup_3.add(rdbtnBarras);
			rdbtnBarras.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			rdbtnBarras.setBackground(Color.WHITE);
		}
		return rdbtnBarras;
	}

	private JPanel getPanelGSectores() {
		if (panelGSectores == null) {
			panelGSectores = new JPanel();
			FlowLayout fl_panelGSectores = (FlowLayout) panelGSectores.getLayout();
			fl_panelGSectores.setHgap(300);
			fl_panelGSectores.setAlignment(FlowLayout.LEFT);
			panelGSectores.setBackground(Color.WHITE);
			panelGSectores.add(getRdbtnSectores());
		}
		return panelGSectores;
	}

	private JRadioButton getRdbtnSectores() {
		if (rdbtnSectores == null) {
			rdbtnSectores = new JRadioButton("Gráfico de sectores");
			buttonGroup_3.add(rdbtnSectores);
			rdbtnSectores.setFont(new Font("Times New Roman", Font.PLAIN, 20));
			rdbtnSectores.setBackground(Color.WHITE);
		}
		return rdbtnSectores;
	}

	private JButton getBtnEstablecerUsuarioContraseña() {
		if (btnEstablecerUsuarioContraseña == null) {
			btnEstablecerUsuarioContraseña = new JButton("Establecer usuario y contraseña");
			btnEstablecerUsuarioContraseña.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AsignarUsuarioContraseña auc = new AsignarUsuarioContraseña(trabajadorACrear);
					auc.setVisible(true);
				}
			});
		}
		return btnEstablecerUsuarioContraseña;
	}

	private JPanel getPanelMenuBotonesSalir() {
		if (panelMenuBotonesSalir == null) {
			panelMenuBotonesSalir = new JPanel();
			panelMenuBotonesSalir.setBackground(Color.WHITE);
			FlowLayout flowLayout = (FlowLayout) panelMenuBotonesSalir.getLayout();
			flowLayout.setVgap(15);
			flowLayout.setAlignOnBaseline(true);
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelMenuBotonesSalir.add(getBtnCerrarSesion());
			panelMenuBotonesSalir.add(getBtnSalir());
		}
		return panelMenuBotonesSalir;
	}

	private JButton getBtnCerrarSesion() {
		if (btnCerrarSesion == null) {
			btnCerrarSesion = new JButton("Cerrar Sesión");
			btnCerrarSesion.setBackground(new Color(128, 0, 0));
			btnCerrarSesion.setForeground(Color.WHITE);
			btnCerrarSesion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null, "¿Estás segur@ de que quieres cerrar sesión?",
							"Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal", "Cerrar sesion");
						volverAInicioDeSesion();
					}

				}
			});
		}
		return btnCerrarSesion;
	}

	private void volverAInicioDeSesion() {
		dispose();
		DialogoInicioSesion dis = new DialogoInicioSesion();
		dis.setLocationRelativeTo(null);
		dis.setVisible(true);
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.setBackground(new Color(128, 0, 0));
			btnSalir.setForeground(Color.WHITE);
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (JOptionPane.showConfirmDialog(null, "¿Estás segur@ de que quieres salir?", "Confirmación",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal", "Cerrar sesion");
						System.exit(0);
					}

				}
			});
		}
		return btnSalir;
	}

	private JPanel getPanelContenedorMenu() {
		if (panelContenedorMenu == null) {
			panelContenedorMenu = new JPanel();
			panelContenedorMenu.setLayout(new BorderLayout(0, 0));
			panelContenedorMenu.add(getPanelMenu(), BorderLayout.CENTER);
			panelContenedorMenu.add(getPanelMenuBotonesSalir(), BorderLayout.SOUTH);
		}
		return panelContenedorMenu;
	}

	private JButton getButtonDescuentosAplicables() {
		if (btnDescuentosAplicables == null) {
			btnDescuentosAplicables = new JButton("Descuentos Aplicables");
			btnDescuentosAplicables.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(null,
							"<html><b>5%</b> de descuento en más de <b>10 unidades</b></html>\n"
									+ "<html><b>10%</b> de descuento en más de <b>20 unidades</b></html>\n"
									+ "<html><b>20%</b> de descuento en más de <b>50 unidades</b></html>",
							"Descuentos Aplicables", JOptionPane.INFORMATION_MESSAGE);
					log.saveLog(usuarioActivo.getCodigo(),
							"VentanaPrincipal.GestionDeProductos.RealizarPedidoAlProveedor",
							"Consulta de descuentos del proveedor");
				}
			});
			btnDescuentosAplicables.setMargin(new Insets(2, 6, 2, 14));
			btnDescuentosAplicables.setIconTextGap(10);
			btnDescuentosAplicables.setIcon(new ImageIcon(
					new ImageIcon(VentanaPrincipal.class.getResource("/uo/ips/muebleria/img/descuentos.png")).getImage()
							.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
		}
		return btnDescuentosAplicables;
	}

	private JPanel getPnBotonesRealizarPedidoAProveedorCancelarSiguiente() {
		if (pnBotonesRealizarPedidoAProveedorCancelarSiguiente == null) {
			pnBotonesRealizarPedidoAProveedorCancelarSiguiente = new JPanel();
			pnBotonesRealizarPedidoAProveedorCancelarSiguiente.add(getBtnCancelarRealizarPedidoAProveedor());
			pnBotonesRealizarPedidoAProveedorCancelarSiguiente.add(getBtnSiguienteRealizarPedidoAProveedor());
		}
		return pnBotonesRealizarPedidoAProveedorCancelarSiguiente;
	}

	private JPanel getPnRealizarPedidoAProveedorEspacio() {
		if (pnRealizarPedidoAProveedorEspacio == null) {
			pnRealizarPedidoAProveedorEspacio = new JPanel();
			pnRealizarPedidoAProveedorEspacio.setLayout(new GridLayout(0, 4, 30, 0));
			pnRealizarPedidoAProveedorEspacio.add(getLblRealizarPedidoAProveedorEspacio());
			pnRealizarPedidoAProveedorEspacio.add(getLblRealizarPedidoAProveedorEspacio2());
			pnRealizarPedidoAProveedorEspacio.add(getLblRealizarPedidoAProveedorEspacio3());
			pnRealizarPedidoAProveedorEspacio.add(getLblRealizarPedidoAProveedorEspacio4());
		}
		return pnRealizarPedidoAProveedorEspacio;
	}

	private JLabel getLblRealizarPedidoAProveedorEspacio() {
		if (lblRealizarPedidoAProveedorEspacio == null) {
			lblRealizarPedidoAProveedorEspacio = new JLabel("");
			lblRealizarPedidoAProveedorEspacio.setBorder(new EmptyBorder(0, 0, 0, 50));
		}
		return lblRealizarPedidoAProveedorEspacio;
	}

	private JLabel getLblRealizarPedidoAProveedorEspacio2() {
		if (lblRealizarPedidoAProveedorEspacio2 == null) {
			lblRealizarPedidoAProveedorEspacio2 = new JLabel("");
			lblRealizarPedidoAProveedorEspacio2.setBorder(new EmptyBorder(0, 0, 0, 50));
		}
		return lblRealizarPedidoAProveedorEspacio2;
	}

	private JLabel getLblRealizarPedidoAProveedorEspacio3() {
		if (lblRealizarPedidoAProveedorEspacio3 == null) {
			lblRealizarPedidoAProveedorEspacio3 = new JLabel("");
			lblRealizarPedidoAProveedorEspacio3.setBorder(new EmptyBorder(0, 0, 0, 50));
		}
		return lblRealizarPedidoAProveedorEspacio3;
	}

	private JLabel getLblRealizarPedidoAProveedorEspacio4() {
		if (lblRealizarPedidoAProveedorEspacio4 == null) {
			lblRealizarPedidoAProveedorEspacio4 = new JLabel("");
			lblRealizarPedidoAProveedorEspacio4.setBorder(new EmptyBorder(0, 0, 0, 50));
		}
		return lblRealizarPedidoAProveedorEspacio4;
	}

	private JPanel getPanelMenuEstadisticas() {
		if (panelMenuEstadisticas == null) {
			panelMenuEstadisticas = new JPanel();
			panelMenuEstadisticas.setBackground(Color.WHITE);
			panelMenuEstadisticas.setLayout(new BorderLayout(0, 0));
			panelMenuEstadisticas.add(getPanelCentralME());
			panelMenuEstadisticas.add(getPanelSME(), BorderLayout.SOUTH);
		}
		return panelMenuEstadisticas;
	}

	private JPanel getPanelCentralME() {
		if (panelCentralME == null) {
			panelCentralME = new JPanel();
			panelCentralME.setBackground(Color.WHITE);
			panelCentralME.setLayout(new GridLayout(0, 1, 0, 100));
			panelCentralME.add(getPanelVacio_2());
			panelCentralME.add(getBtnEstadisticasDeLos_1());
			panelCentralME.add(getBtnGrficosEstadsticos());
			panelCentralME.add(getPanelVacio_1_1());
		}
		return panelCentralME;
	}

	private JPanel getPanelVacio_2() {
		if (panelVacio == null) {
			panelVacio = new JPanel();
			panelVacio.setBackground(Color.WHITE);
		}
		return panelVacio;
	}

	private JButton getBtnEstadisticasDeLos_1() {
		if (btnEstadisticasDeLos == null) {
			btnEstadisticasDeLos = new JButton("Estadisticas de los vendedores");
			btnEstadisticasDeLos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					actualizarInterfazEstadisticasVendedores();
					mostrarPanel("estadisticasVendedores");
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.Estadisticas",
							"Consulta de estadisticas de los vendedores");
				}
			});
			btnEstadisticasDeLos.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnEstadisticasDeLos;
	}

	private JButton getBtnGrficosEstadsticos() {
		if (btnGrficosEstadsticos == null) {
			btnGrficosEstadsticos = new JButton("Gráficos estadísticos");
			btnGrficosEstadsticos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("graficos");
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal.Estadisticas",
							"Consulta de graficos de ventas");
				}
			});
			btnGrficosEstadsticos.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnGrficosEstadsticos;
	}

	private JPanel getPanelVacio_1_1() {
		if (panelVacio_1 == null) {
			panelVacio_1 = new JPanel();
			panelVacio_1.setBackground(Color.WHITE);
		}
		return panelVacio_1;
	}

	private JPanel getPanelSME() {
		if (panelSME == null) {
			panelSME = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelSME.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelSME.setBackground(Color.WHITE);
			panelSME.add(getBtnSalir_1());
		}
		return panelSME;
	}

	private JButton getBtnSalir_1() {
		if (btnSalir_1 == null) {
			btnSalir_1 = new JButton("Atrás");
			btnSalir_1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu");
				}
			});
			btnSalir_1.setForeground(Color.WHITE);
			btnSalir_1.setBackground(new Color(128, 0, 0));
		}
		return btnSalir_1;
	}

	private JButton getBtnEstadisticas() {
		if (btnEstadisticas == null) {
			btnEstadisticas = new JButton("Estadísticas");
			btnEstadisticas.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu_estadisticas");
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal", "Solicitud de consulta de estadísticas");
				}
			});
			btnEstadisticas.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnEstadisticas;
	}

	private JPanel getPanelMenuGP() {
		if (panelMenuGP == null) {
			panelMenuGP = new JPanel();
			panelMenuGP.setBackground(Color.WHITE);
			panelMenuGP.setLayout(new BorderLayout(0, 0));
			panelMenuGP.add(getPanelCentralGP(), BorderLayout.CENTER);
			panelMenuGP.add(getPanelSGP(), BorderLayout.SOUTH);
		}
		return panelMenuGP;
	}

	private JPanel getPanelCentralGP() {
		if (panelCentralGP == null) {
			panelCentralGP = new JPanel();
			panelCentralGP.setBackground(Color.WHITE);
			panelCentralGP.setLayout(new GridLayout(0, 1, 0, 50));
			panelCentralGP.add(getBtnSeguimientoPedido());
			panelCentralGP.add(getBtnRealizarPedidoAProveedor());
			panelCentralGP.add(getBtnMenuAlmacen());
			panelCentralGP.add(getBtnCambiarPrecioProducto());
		}
		return panelCentralGP;
	}

	private JPanel getPanelSGP() {
		if (panelSGP == null) {
			panelSGP = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelSGP.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelSGP.setBackground(Color.WHITE);
			panelSGP.add(getBtnSalirGP());
		}
		return panelSGP;
	}

	private JButton getBtnSalirGP() {
		if (btnSalirGP == null) {
			btnSalirGP = new JButton("Atrás");
			btnSalirGP.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu");
				}
			});
			btnSalirGP.setForeground(Color.WHITE);
			btnSalirGP.setBackground(new Color(128, 0, 0));
		}
		return btnSalirGP;
	}

	private JButton getBtnGP() {
		if (btnGP == null) {
			btnGP = new JButton("Gestión de productos");
			btnGP.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu_gp");
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal", "Selicitud de gestion de productos");
				}
			});
			btnGP.setFont(new Font("Times New Roman", Font.BOLD, 24));
		}
		return btnGP;
	}

	private JPanel getPanelGPP() {
		if (panelGPP == null) {
			panelGPP = new JPanel();
			panelGPP.setBackground(Color.WHITE);
			panelGPP.setLayout(new BorderLayout(10, 10));
			panelGPP.add(getPanelCentralGPP(), BorderLayout.CENTER);
			panelGPP.add(getPanelSGPP(), BorderLayout.SOUTH);
		}
		return panelGPP;
	}

	private JPanel getPanelCentralGPP() {
		if (panelCentralGPP == null) {
			panelCentralGPP = new JPanel();
			panelCentralGPP.setBackground(Color.WHITE);
			panelCentralGPP.setLayout(new GridLayout(0, 1, 0, 70));
			panelCentralGPP.add(getBtnMenuCrearPresupuesto());
			panelCentralGPP.add(getBtnMenuAsignarClienteAPresupuesto());
			panelCentralGPP.add(getBtnMenuCrearVenta());
		}
		return panelCentralGPP;
	}

	private JPanel getPanelSGPP() {
		if (panelSGPP == null) {
			panelSGPP = new JPanel();
			FlowLayout flowLayout = (FlowLayout) panelSGPP.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			panelSGPP.setBackground(Color.WHITE);
			panelSGPP.add(getBtnSalirGPP());
		}
		return panelSGPP;
	}

	private JButton getBtnSalirGPP() {
		if (btnSalirGPP == null) {
			btnSalirGPP = new JButton("Atrás");
			btnSalirGPP.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu");
				}
			});
			btnSalirGPP.setForeground(Color.WHITE);
			btnSalirGPP.setBackground(new Color(128, 0, 0));
		}
		return btnSalirGPP;
	}

	private JButton getBtnGestinDePresupuestos() {
		if (btnGestinDePresupuestos == null) {
			btnGestinDePresupuestos = new JButton("Gestión de presupuestos");
			btnGestinDePresupuestos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mostrarPanel("menu_gpp");
					log.saveLog(usuarioActivo.getCodigo(), "VentanaPrincipal", "Solicitud de gestion de presupuestos");
				}
			});
			btnGestinDePresupuestos.setFont(new Font("Times New Roman", Font.BOLD, 24));
			btnGestinDePresupuestos.setFocusPainted(false);
		}
		return btnGestinDePresupuestos;
	}
}
