package uo.ips.muebleria.util.swing;

import javax.swing.table.DefaultTableModel;

import uo.ips.muebleria.ui.VentanaPrincipal;

public class ModeloTablaTransporte extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ModeloTablaTransporte() {
		super(VentanaPrincipal.COLUMNAS_PRODUCTOS_A_TRANSPORTAR, 0);
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		Class<?> clase = String.class;
		
		switch (columnIndex) {
		case 4:
			clase = Boolean.class;
			break;
		}
		
		return clase;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 4;
	}

}
