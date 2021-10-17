package uo.ips.muebleria.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import uo.ips.muebleria.ui.DialogoModeloPresupuesto;
import uo.ips.muebleria.ui.VentanaPrincipal;

public class JTableModelos extends JTable {

	private static final long serialVersionUID = 1L;

	private ModeloTableAlmacen modelo;
	private VentanaPrincipal vp;

	public JTableModelos(VentanaPrincipal vp) {
		super();
		this.modelo = new ModeloTableAlmacen();
		this.setModel(modelo);
		this.vp = vp;
		this.getColumnModel().getColumn(1).setMinWidth(70);
		this.getColumnModel().getColumn(2).setMinWidth(70);
		this.getColumnModel().getColumn(1).setMaxWidth(70);
		this.getColumnModel().getColumn(2).setMaxWidth(70);
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int col = getSelectedColumn();
				if(col==2) {
					((ButtonEditor) getColumn(modelo.getColumnNames()[2]).getCellEditor()).doClick();
				}
			};
		});
	}

	public void stopEditions() {
		for (int i = 0; i < modelo.getRowCount(); i++) {
			Object o = modelo.getValueAt(i, 1);
			if (o instanceof SpinnerCell) {
				SpinnerCellModelo sp = (SpinnerCellModelo) o;
				sp.activateStop();
				((SpinnerCellModelo) o).stopCellEditing();
				modelo.setValueAt(sp.getCellEditorValue(), i, 1);
			}
		}

	}

	@Override
	public TableCellRenderer getCellRenderer(final int rowIndex, int colIndex) {

		int reaRowlIndex = convertRowIndexToModel(rowIndex);
		int realColumnIndex = convertColumnIndexToModel(colIndex);

		Object o = this.getModel().getValueAt(reaRowlIndex, realColumnIndex);

		if (o instanceof TableCellRenderer) {
			return (TableCellRenderer) o;
		} else {
			return super.getCellRenderer(reaRowlIndex, realColumnIndex);
		}
	}

	//
	@Override
	public TableCellEditor getCellEditor(final int rowIndex, int colIndex) {

		int reaRowlIndex = convertRowIndexToModel(rowIndex);
		int realColumnIndex = convertColumnIndexToModel(colIndex);

		Object o = this.getModel().getValueAt(reaRowlIndex, realColumnIndex);

		if (o instanceof TableCellEditor) {
			return (TableCellEditor) o;
		} else {
			return super.getCellEditor(reaRowlIndex, realColumnIndex);
		}
	}

	public int getSpinnerValue(int i) {
		return ((SpinnerCellModelo) modelo.getValueAt(i, 1)).getSpinnerValue();
	}

	public void addRow(Object[] obj) {
		modelo.addRow(obj);
		getColumn(modelo.getColumnNames()[2]).setCellRenderer(new ButtonRenderer());
		getColumn(modelo.getColumnNames()[2]).setCellEditor(new ButtonEditor(new JCheckBox(), createCallable()));
	}

	private Callable<Void> createCallable() {
		Callable<Void> call = new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				int row = getSelectedRow();
				String codigo = modelo.getCodigoAt(row);
				String nombre = modelo.getValueAt(row, 0).toString();
				DialogoModeloPresupuesto d = new DialogoModeloPresupuesto(codigo,nombre,vp);
				d.setVisible(true);
				return null;
			}
		};
		return call;
	}

	public String getCodigo(int i) {
		return modelo.getCodigoAt(i);
	}

	private class SpinnerCellModelo extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

		private boolean check = false;

		private static final long serialVersionUID = 1L;
		private JSpinner editSpinner, renderSpinner;

		public SpinnerCellModelo() {
			editSpinner = generateSpinner();
			JTextField tf = ((JSpinner.DefaultEditor) editSpinner.getEditor()).getTextField();
			tf.setForeground(Color.black);
			tf.setEditable(false);
			tf.setBackground(Color.WHITE);
			renderSpinner = generateSpinner();
			JTextField tf2 = ((JSpinner.DefaultEditor) renderSpinner.getEditor()).getTextField();
			tf2.setForeground(Color.black);
			tf2.setEditable(false);
			tf2.setBackground(Color.WHITE);
		}

		public int getSpinnerValue() {
			return (int) editSpinner.getValue();
		}

		@Override
		public Object getCellEditorValue() {
			return editSpinner.getValue();
		}

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {

			return editSpinner;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return renderSpinner;
		}

		public String toString() {
			return editSpinner.getValue().toString();
		}

		@Override
		public boolean isCellEditable(EventObject evt) {
			return true;
		}

		private JSpinner generateSpinner() {
			JSpinner s = new JSpinner();
			s.setModel(new SpinnerNumberModel(0, 0, null, 1));
			s.setFont(new Font("Tahoma", Font.PLAIN, 11));
			return s;
		}

		@Override
		public void cancelCellEditing() {
			if (check)
				super.cancelCellEditing();
		}

		@Override
		public boolean stopCellEditing() {
			if (check)
				return super.stopCellEditing();
			else
				return true;
		}

		public void activateStop() {
			check = true;
		}
	}

	public class ModeloTableAlmacen extends DefaultTableModel {

		private static final long serialVersionUID = 1L;

		private final String[] COLUMNAS = new String[] { "Nombre", "Unidades", "Detalles" };

		private List<String> codigos;

		public ModeloTableAlmacen() {
			super(new String[] { "Nombre", "Unidades", "Detalles" }, 0);
			codigos = new ArrayList<String>();
		}

		public String getCodigoAt(int row) {
			return codigos.get(row);
		}

		@Override
		public void addRow(Object[] rowData) {
			super.addRow(generateRowData(rowData));
		}

		private Object[] generateRowData(Object[] rowData) {
			codigos.add(rowData[0].toString());
			return new Object[] { rowData[1], new SpinnerCellModelo(), "Más" };
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
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
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
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
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
				
			}
			isPushed = false;
			return label;
		}

		@Override
		public boolean stopCellEditing() {
			isPushed = false;
			return true;
		}
		
		public void doClick() {
			try {
				func.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
