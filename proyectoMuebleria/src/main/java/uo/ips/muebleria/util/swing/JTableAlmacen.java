package uo.ips.muebleria.util.swing;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import uo.ips.muebleria.ui.DialogoAlmacen;
import uo.ips.muebleria.ui.VentanaPrincipal;

public class JTableAlmacen extends JTable {

	private static final long serialVersionUID = 1L;
	
	private ModeloTableAlmacen modelo;
	private VentanaPrincipal vp;

	public JTableAlmacen(VentanaPrincipal vp) {
		super();
		this.modelo = new ModeloTableAlmacen();
		this.setModel(modelo);
		this.vp = vp;
	}
	
	public void addRow(Object[] obj) {
		modelo.addRow(obj);
		getColumn(modelo.getColumnNames()[4]).setCellRenderer(new ButtonRenderer());
        getColumn(modelo.getColumnNames()[4]).setCellEditor(new ButtonEditor(new JCheckBox(),createCallable()));
	}

	private Callable<Void> createCallable() {
		Callable<Void> call = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				mostrarDatosProductosAlmacen();
				return null;
			}
		};
		return call;
	}
	

	private void mostrarDatosProductosAlmacen() {
		int row = getSelectedRow();
		String codigo = modelo.getValueAt(row, 0).toString();
		VentanaPrincipal.log.saveLog(vp.getUsuarioActivo().getCodigo(), "VentanaPrincipal.GestionDeProductos.GestionarAlmacen", "Se consulta informacion del producto " + codigo);
		DialogoAlmacen v = new DialogoAlmacen(codigo);
		v.setLocationRelativeTo(vp);
		v.setVisible(true);
	}

	public class ModeloTableAlmacen extends DefaultTableModel {

		private static final long serialVersionUID = 1L;

		private final String[] COLUMNAS = new String[] { "Código", "Nombre", "Tipo", "Stock", "Avanzado" };

		public ModeloTableAlmacen() {
			super(new String[] { "Código", "Nombre", "Tipo", "Stock", "Avanzado" },0);
		}

		@Override
		public void addRow(Object[] rowData) {
			super.addRow(generateRowData(rowData));
		}

		private Object[] generateRowData(Object[] rowData) {
			return new Object[] {rowData[0],rowData[1],rowData[2],rowData[3],"Más"};
		}
		
		public String[] getColumnNames() {
			return COLUMNAS;
		}

	}
	
	public class ButtonRenderer extends JButton implements TableCellRenderer {

		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
	        setOpaque(true);
	    }

	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	        if (isSelected) {
	            setForeground(table.getSelectionForeground());
	            setBackground(table.getSelectionBackground());
	        } else {
	            setForeground(table.getForeground());
	            setBackground(UIManager.getColor("Button.background"));
	        }
	        setText((value == null) ? "" : value.toString());
	        return this;
	    }
	}
	
	public class ButtonEditor extends DefaultCellEditor {

	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		protected JButton button;
	    private String label;
	    private boolean isPushed;
	    private Callable<Void> func;

	    public ButtonEditor(JCheckBox checkBox, Callable<Void> func) {
	        super(checkBox);
	        button = new JButton();
	        button.setFont(new Font("Tahoma", Font.PLAIN, 11));
	        button.setFocusPainted(false);
	        button.setOpaque(true);
	        button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                fireEditingStopped();
	            }
	        });
	        this.func = func;
	    }

	    @Override
	    public Component getTableCellEditorComponent(JTable table, Object value,
	            boolean isSelected, int row, int column) {
	        if (isSelected) {
	            button.setForeground(table.getSelectionForeground());
	            button.setBackground(table.getSelectionBackground());
	        } else {
	            button.setForeground(table.getForeground());
	            button.setBackground(table.getBackground());
	        }
	        label = (value == null) ? "" : value.toString();
	        button.setText(label);
	        isPushed = true;
	        return button;
	    }

	    @Override
	    public Object getCellEditorValue() {
	        if (isPushed) {
	            try {
					func.call();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        isPushed = false;
	        return label;
	    }

	    @Override
	    public boolean stopCellEditing() {
	        isPushed = false;
	        return super.stopCellEditing();
	    }
	}
}
