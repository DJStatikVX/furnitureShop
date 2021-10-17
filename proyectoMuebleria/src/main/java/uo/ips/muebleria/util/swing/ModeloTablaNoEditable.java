package uo.ips.muebleria.util.swing;

import javax.swing.table.DefaultTableModel;

public class ModeloTablaNoEditable extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ModeloTablaNoEditable(String[][] strings, String[] columnasCliente) {
		super(strings, columnasCliente);
	}

	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
