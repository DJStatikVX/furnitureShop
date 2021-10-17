package uo.ips.muebleria.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class SpinnerCell extends AbstractCellEditor implements TableCellEditor, TableCellRenderer{
	
	private boolean check = false;
	
	private static final long serialVersionUID = 1L;
    private JSpinner editSpinner, renderSpinner;

    public SpinnerCell() {
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

    public SpinnerCell(Integer unidades) {
    	editSpinner = generateSpinner(unidades);
        JTextField tf = ((JSpinner.DefaultEditor) editSpinner.getEditor()).getTextField();
        tf.setForeground(Color.black);
        tf.setEditable(false);
        tf.setBackground(Color.WHITE);
        renderSpinner = generateSpinner(unidades);
        JTextField tf2 = ((JSpinner.DefaultEditor) renderSpinner.getEditor()).getTextField();
        tf2.setForeground(Color.black);
        tf2.setEditable(false);
        tf2.setBackground(Color.WHITE);
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

    public JSpinner getSpinner() {
        return editSpinner;
    }

    @Override
    public boolean isCellEditable(EventObject evt) {
        return true;
    }
    
    private JSpinner generateSpinner() {
		JSpinner s = new JSpinner();
		s.setModel(new SpinnerNumberModel(1, 1, null, 1));
		s.setFont(new Font("Tahoma", Font.PLAIN, 11));
		return s;
	}
    
    private JSpinner generateSpinner(Integer unidades) {
    	JSpinner s = new JSpinner();
		s.setModel(new SpinnerNumberModel(1, 1, null, 1));
		s.setFont(new Font("Tahoma", Font.PLAIN, 11));
		s.setValue(unidades.intValue());
		return s;
	}

	@Override
	public void cancelCellEditing() {
		if(check)
			super.cancelCellEditing();
	}

	@Override
	public boolean stopCellEditing() {
		if(check)
			return super.stopCellEditing();
		else
			return true;
	}

	public void activateStop() {
    	check = true;
    }
	
	public void resetStop() {
    	check = false;
    }

	public void rise(int i) {
		Integer value = (Integer) editSpinner.getValue();
		editSpinner.setValue(value+i);
		renderSpinner.setValue(value+i);
	}

}
