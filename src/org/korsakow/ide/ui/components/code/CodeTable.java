/**
 * 
 */
package org.korsakow.ide.ui.components.code;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import org.korsakow.ide.code.k5.K5Code;
import org.korsakow.ide.util.UIUtil;

public class CodeTable extends JTable
{
	public static String TIME_IDENTIFIER = "time";
	public static String CODE_IDENTIFIER = "code";
	public static String MAXLINKS_IDENTIFIER = "maxlinks";
	public CodeTable()
	{
		super(new CodeTableModel());
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE); // thank you jrdasm, via http://www.codeguru.com/forum/archive/index.php/t-301102.html
		setAutoCreateColumnsFromModel(false);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		UIUtil.addColumn(this, "Time", TIME_IDENTIFIER);
		UIUtil.addColumn(this, "", CODE_IDENTIFIER);
		UIUtil.addColumn(this, "MaxLinks", MAXLINKS_IDENTIFIER);
		getColumn(TIME_IDENTIFIER).setCellEditor(new TimeCellEditor());
		getColumn(TIME_IDENTIFIER).setCellRenderer(new TimeCellRenderer());
		getColumn(TIME_IDENTIFIER).setMaxWidth(70);
		getColumn(CODE_IDENTIFIER).setCellRenderer(new CodeCellRenderer());
		getColumn(CODE_IDENTIFIER).setCellEditor(new CodeCellEditor());
		getColumn(MAXLINKS_IDENTIFIER).setCellEditor(new MaxLinksCellEditor());
		getColumn(MAXLINKS_IDENTIFIER).setCellRenderer(new MaxLinksCellRenderer());
		getColumn(MAXLINKS_IDENTIFIER).setMaxWidth(60);
		setRowHeight(20);
		addMouseListener(new ComponentCellEditorSimpleClickAdapter(0));
		
		getModel().addTableModelListener(new CodeTableController(this));
	}
	/**
	 * We set this to false in the ctor but the super ctor calls this!!!! 
	 */
	@Override
	public boolean getAutoCreateColumnsFromModel() {
		return false;
	}
    @Override
	public void setModel(TableModel dataModel) {
    	setModel(((CodeTableModel)(dataModel)));
    }
	public void setModel(CodeTableModel dataModel) {
		dataModel.setColumnName(CodeTableModel.TIME_COLUMN, TIME_IDENTIFIER);
		dataModel.setColumnName(CodeTableModel.CODE_COLUMN, CODE_IDENTIFIER);
		dataModel.setColumnName(CodeTableModel.MAXLINKS_COLUMN, MAXLINKS_IDENTIFIER);
    	super.setModel(dataModel);
    	
		getModel().addTableModelListener(new CodeTableController(CodeTable.this));
    }
	@Override
	public CodeTableModel getModel()
	{
		return (CodeTableModel)super.getModel();
	}
	public String getRawCodeAt(int row)
	{
		return ((K5Code)getValueAt(row, 1)).getRawCode();
	}
	@Override
	public boolean isCellEditable(int row, int col)
	{
		return true;
	}
	
	public int getColumnIndex(Object identifier)
	{
		return getColumnModel().getColumnIndex(identifier);
	}

}
