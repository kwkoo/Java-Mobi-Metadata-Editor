package gui;

import mobimeta.*;

import java.io.File;
import java.util.List;

import javax.swing.table.AbstractTableModel;

class GuiModel extends AbstractTableModel implements EXTHAddRecordListener, LanguageModel, MetaInfoProvider
{
	/**
	 * This is essentially a proxy to the MobiMeta object. In addition, it
	 * serves as a TableModel for the JTable
	 */
	private static final long serialVersionUID = 1L;

	private	MobiMeta				model		= null;
	
	// static globals for convenience
	//
	private static String			fullName			= null;
	private static List<EXTHRecord>	exthRecords			= null;
	private static String			characterEncoding	= null;
	
	
	public GuiModel()
	{
	}
	
	public void setModel(File f) throws GuiException
	{
		try
		{
			model = new MobiMeta(f);
		}
		catch (MobiMetaException e)
		{
			throw new GuiException(e.getMessage());
		}
		
		fullName			= model.getFullName();
		exthRecords			= model.getEXTHRecords();
		characterEncoding	= model.getCharacterEncoding();
	}
	
	public String getColumnName(int col)
	{
		if (col == 0)
			return "Record Type";
		else
			return "Value";
	}

	public int getColumnCount()
	{
		return 2;
	}

	public int getRowCount()
	{
		return (exthRecords == null)?0:exthRecords.size();
	}

	public Object getValueAt(int row, int col)
	{
		EXTHRecord	rec			= exthRecords.get(row);
		int			type		= rec.getRecordType();
		String		typeDesc	= rec.getTypeDescription();
		
		if (col == 0)
		{	
			if (typeDesc == null)
				return Integer.toString(type);
			else
				return type + " (" + typeDesc + ")";
		}
		else
		{
			byte[] data = rec.getData();
			
			if (typeDesc == null)
			{
				StringBuffer sb = new StringBuffer();
				sb.append("{ ");
				int len = data.length;
				for (int i=0; i<len; i++)
				{
					if (i > 0) sb.append(", ");
					sb.append(data[i] & 0xff);
				}
				sb.append(" }");
				return sb.toString();
			}
			else if (EXTHRecord.isBooleanType(type))
			{
				int value = StreamUtils.byteArrayToInt(data);
				if (value == 0)
					return Boolean.FALSE;
				else
					return Boolean.TRUE;
			}
			else
				return StreamUtils.byteArrayToString(data, characterEncoding);
		}
	}
	
	public boolean isCellEditable(int row, int col)
	{
		if ((col == 0) || MobiCommon.safeMode) return false;
		
		return exthRecords.get(row).isKnownType();
	}
	
	public void setValueAt(Object value, int row, int column)
	{
		if (column != 1) return;
		
		if (value instanceof String)
			exthRecords.get(row).setData((String)value, characterEncoding);
		else if (value instanceof Boolean)
			exthRecords.get(row).setData(((Boolean)value).booleanValue());
		else
			return;
			
		fireTableCellUpdated(row, column);
	}
	
	public String getFullName()
	{
		return fullName;
	}
	
	public void setFullName(String s)
	{
		fullName = s;
	}
	
	public void removeRecordAtRow(int row)
	{
		exthRecords.remove(row);
		fireTableRowsDeleted(row, row);
	}


	public void addEXTHRecord(EXTHRecord rec)
	{
		exthRecords.add(rec);
		int lastIndex = exthRecords.size() - 1;
		fireTableRowsInserted(lastIndex, lastIndex);
	}
	
	public static String getCharacterEncoding()
	{
		return characterEncoding;
	}
	
	public void save(File outputFile, boolean packHeader) throws GuiException
	{
		if (packHeader)
		{
			model.setFullName(fullName);
			model.setEXTHRecords();
		}

		try
		{
			model.saveToNewFile(outputFile, packHeader);
		}
		catch (MobiMetaException e)
		{
			throw new GuiException("Problems encountered while saving file: "
					+ e.getMessage());
		}
	}

	public int getLocale()
	{
		return model.getLocale();
	}

	public int getDictInput()
	{
		return model.getDictInput();
	}

	public int getDictOutput()
	{
		return model.getDictOutput();
	}

	public void setLanguages(int locale, int dictInput, int dictOutput)
	{
		model.setLanguages(locale, dictInput, dictOutput);
	}
	
	public String getMetaInfo()
	{
		return model.getMetaInfo();
	}
}
