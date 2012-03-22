package gui;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class CustomJTable extends JTable
{
	/**
	 * This code was adapted from
	 * http://www.javalobby.org/java/forums/t84905.html
	 * 
	 * A traditional JTable lets you specify custom renderers and editors on a
	 * column by column basis. This class lets you have different renderers and
	 * editors in the same column (but different rows).
	 */
	private static final long serialVersionUID = 1L;

	public CustomJTable(TableModel model)
	{
		super(model);
	}
	
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		TableColumn tableColumn = getColumnModel().getColumn(column);
		TableCellRenderer renderer = tableColumn.getCellRenderer();
		if (renderer != null) return renderer;
		if (getValueAt(row, column) != null)
		{
			return getDefaultRenderer(getValueAt(row, column).getClass());
		}
		return getDefaultRenderer(getColumnClass(column));
	}
	
	public TableCellEditor getCellEditor(int row, int column)
	{
		TableColumn tableColumn = getColumnModel().getColumn(column);
		TableCellEditor editor = tableColumn.getCellEditor();
		if (editor != null) return editor;
		if (getValueAt(row, column) != null)
		{
			return getDefaultEditor(getValueAt(row, column).getClass());
		}
		return getDefaultEditor(getColumnClass(column));
	}
}
