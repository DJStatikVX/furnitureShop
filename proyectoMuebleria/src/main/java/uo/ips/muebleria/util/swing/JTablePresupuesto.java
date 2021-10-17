package uo.ips.muebleria.util.swing;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class JTablePresupuesto extends JTable{
	
	private DefaultTableModel modeloTableProdPresupuesto;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public JTablePresupuesto(DefaultTableModel modelo) {
		super(modelo);
		this.modeloTableProdPresupuesto = modelo;
	}
	
	public void stopEditions() {
		for(int i=0; i<modeloTableProdPresupuesto.getRowCount();i++) {
			Object o = modeloTableProdPresupuesto.getValueAt(i, 4);
			if (o instanceof SpinnerCell) {
				SpinnerCell sp = (SpinnerCell) o;
				sp.activateStop();
				((SpinnerCell) o).stopCellEditing();
				modeloTableProdPresupuesto.setValueAt(sp.getCellEditorValue(), i, 4);
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

	public void aumentarSpinner(int row, int i) {
		SpinnerCell sp = (SpinnerCell) modeloTableProdPresupuesto.getValueAt(row, 4);
		sp.rise(i);
	}
    
    
	
}
