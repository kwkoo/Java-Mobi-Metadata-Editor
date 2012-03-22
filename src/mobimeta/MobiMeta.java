package mobimeta;

import java.io.*;
import java.util.*;

public class MobiMeta
{
	public final static int BUFFER_SIZE = 4096;
	
	protected	PDBHeader			pdbHeader;
	protected	MobiHeader			mobiHeader;
	protected	String				characterEncoding;
	protected	List<EXTHRecord>	exthRecords;
	private		File				inputFile;

	
	public MobiMeta(File f) throws MobiMetaException
	{
		inputFile = f;
		FileInputStream in = null;
		try
		{
			in					= new FileInputStream(f);
			pdbHeader			= new PDBHeader(in);
			mobiHeader			= new MobiHeader(in, pdbHeader.getMobiHeaderSize());
			exthRecords			= mobiHeader.getEXTHRecords();
			characterEncoding	= mobiHeader.getCharacterEncoding();
		}
		catch (IOException e)
		{
			throw new MobiMetaException("Could not parse mobi file "
										+ f.getAbsolutePath()
										+ ": "
										+ e.getMessage());
		}
		finally
		{
			if (in != null) try { in.close(); } catch (IOException e) {}
		}
	}
	
	public void saveToNewFile(File outputFile) throws MobiMetaException
	{
		saveToNewFile(outputFile, true);
	}
	
	public void saveToNewFile(File outputFile, boolean packHeader) throws MobiMetaException
	{
		long readOffset = pdbHeader.getOffsetAfterMobiHeader();
		
		if (!MobiCommon.safeMode && packHeader)
		{
			mobiHeader.pack();
			pdbHeader.adjustOffsetsAfterMobiHeader(mobiHeader.size());
		}

		FileInputStream		in	= null;
		FileOutputStream	out	= null;
		try
		{
			out = new FileOutputStream(outputFile);
			pdbHeader.write(out);
			mobiHeader.write(out);
			
			int		bytesRead;
			byte[]	buffer		= new byte[BUFFER_SIZE];
			in					= new FileInputStream(inputFile);
			in.skip(readOffset);
			while ((bytesRead = in.read(buffer)) != -1)
			{
				out.write(buffer, 0, bytesRead);
			}
		}
		catch (IOException e)
		{
			throw new MobiMetaException(
					"Problems encountered while writing to "
							+ outputFile.getAbsolutePath() + ": "
							+ e.getMessage());
		}
		finally
		{
			if (in != null) try { in.close(); } catch (IOException e) {}
			if (out != null) try { out.close(); } catch (IOException e) {}
		}
	}
	
	public String getCharacterEncoding()
	{
		return mobiHeader.getCharacterEncoding();
	}
	
	public String getFullName()
	{
		return mobiHeader.getFullName();
	}
	
	public void setFullName(String s)
	{
		mobiHeader.setFullName(s);
	}
	
	public List<EXTHRecord> getEXTHRecords()
	{
		return exthRecords;
	}
	
	public void setEXTHRecords()
	{
		mobiHeader.setEXTHRecords(exthRecords);
	}
	
	public int getLocale()
	{
		return mobiHeader.getLocale();
	}

	public int getDictInput()
	{
		return mobiHeader.getInputLanguage();
	}

	public int getDictOutput()
	{
		return mobiHeader.getOutputLanguage();
	}

	public void setLanguages(int locale, int dictInput, int dictOutput)
	{
		mobiHeader.setLocale(locale);
		mobiHeader.setInputLanguage(dictInput);
		mobiHeader.setOutputLanguage(dictOutput);
	}
	
	public String getMetaInfo()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("PDB Header\r\n");
		sb.append("----------\r\n");
		sb.append("Name: ");
		sb.append(pdbHeader.getName());
		sb.append("\r\n");
		String[] attributes = getPDBHeaderAttributes();
		if (attributes.length > 0)
		{
			sb.append("Attributes: ");
			for (int i=0; i<attributes.length; i++)
			{
				if (i > 0) sb.append(", ");
				sb.append(attributes[i]);
			}
			sb.append("\r\n");
		}
		sb.append("Version: ");
		sb.append(pdbHeader.getVersion());
		sb.append("\r\n");
		sb.append("Creation Date: ");
		sb.append(pdbHeader.getCreationDate());
		sb.append("\r\n");
		sb.append("Modification Date: ");
		sb.append(pdbHeader.getModificationDate());
		sb.append("\r\n");
		sb.append("Last Backup Date: ");
		sb.append(pdbHeader.getLastBackupDate());
		sb.append("\r\n");
		sb.append("Modification Number: ");
		sb.append(pdbHeader.getModificationNumber());
		sb.append("\r\n");
		sb.append("App Info ID: ");
		sb.append(pdbHeader.getAppInfoID());
		sb.append("\r\n");
		sb.append("Sort Info ID: ");
		sb.append(pdbHeader.getSortInfoID());
		sb.append("\r\n");
		sb.append("Type: ");
		sb.append(pdbHeader.getType());
		sb.append("\r\n");
		sb.append("Creator: ");
		sb.append(pdbHeader.getCreator());
		sb.append("\r\n");
		sb.append("Unique ID Seed: ");
		sb.append(pdbHeader.getUniqueIDSeed());
		sb.append("\r\n\r\n");

		sb.append("PalmDOC Header\r\n");
		sb.append("--------------\r\n");
		sb.append("Compression: ");
		sb.append(mobiHeader.getCompression());
		sb.append("\r\n");
		sb.append("Text Length: ");
		sb.append(mobiHeader.getTextLength());
		sb.append("\r\n");
		sb.append("Record Count: ");
		sb.append(mobiHeader.getRecordCount());
		sb.append("\r\n");
		sb.append("Record Size: ");
		sb.append(mobiHeader.getRecordSize());
		sb.append("\r\n");
		sb.append("Encryption Type: ");
		sb.append(mobiHeader.getEncryptionType());
		sb.append("\r\n\r\n");
		
		sb.append("MOBI Header\r\n");
		sb.append("-----------\r\n");
		sb.append("Header Length: ");
		sb.append(mobiHeader.getHeaderLength());
		sb.append("\r\n");
		sb.append("Mobi Type: ");
		sb.append(mobiHeader.getMobiType());
		sb.append("\r\n");
		sb.append("Unique ID: ");
		sb.append(mobiHeader.getUniqueID());
		sb.append("\r\n");
		sb.append("File Version: ");
		sb.append(mobiHeader.getFileVersion());
		sb.append("\r\n");
		sb.append("Orthographic Index: ");
		sb.append(mobiHeader.getOrthographicIndex());
		sb.append("\r\n");
		sb.append("Inflection Index: ");
		sb.append(mobiHeader.getInflectionIndex());
		sb.append("\r\n");
		sb.append("Index Names: ");
		sb.append(mobiHeader.getIndexNames());
		sb.append("\r\n");
		sb.append("Index Keys: ");
		sb.append(mobiHeader.getIndexKeys());
		sb.append("\r\n");
		sb.append("Extra Index 0: ");
		sb.append(mobiHeader.getExtraIndex0());
		sb.append("\r\n");
		sb.append("Extra Index 1: ");
		sb.append(mobiHeader.getExtraIndex1());
		sb.append("\r\n");
		sb.append("Extra Index 2: ");
		sb.append(mobiHeader.getExtraIndex2());
		sb.append("\r\n");
		sb.append("Extra Index 3: ");
		sb.append(mobiHeader.getExtraIndex3());
		sb.append("\r\n");
		sb.append("Extra Index 4: ");
		sb.append(mobiHeader.getExtraIndex4());
		sb.append("\r\n");
		sb.append("Extra Index 5: ");
		sb.append(mobiHeader.getExtraIndex5());
		sb.append("\r\n");
		sb.append("First Non-Book Index: ");
		sb.append(mobiHeader.getFirstNonBookIndex());
		sb.append("\r\n");
		sb.append("Full Name Offset: ");
		sb.append(mobiHeader.getFullNameOffset());
		sb.append("\r\n");
		sb.append("Full Name Length: ");
		sb.append(mobiHeader.getFullNameLength());
		sb.append("\r\n");
		sb.append("Min Version: ");
		sb.append(mobiHeader.getMinVersion());
		sb.append("\r\n");
		sb.append("Huffman Record Offset: ");
		sb.append(mobiHeader.getHuffmanRecordOffset());
		sb.append("\r\n");
		sb.append("Huffman Record Count: ");
		sb.append(mobiHeader.getHuffmanRecordCount());
		sb.append("\r\n");
		sb.append("Huffman Table Offset: ");
		sb.append(mobiHeader.getHuffmanTableOffset());
		sb.append("\r\n");
		sb.append("Huffman Table Length: ");
		sb.append(mobiHeader.getHuffmanTableLength());
		sb.append("\r\n");

		return sb.toString();
	}
	
	private String[] getPDBHeaderAttributes()
	{
		LinkedList<String> list = new LinkedList<String>();
		int attr = pdbHeader.getAttributes();
		if ((attr & 0x02) != 0) list.add("Read-Only");
		if ((attr & 0x04) != 0) list.add("Dirty AppInfoArea");
		if ((attr & 0x08) != 0) list.add("Backup This Database");
		if ((attr & 0x10) != 0) list.add("OK To Install Newer Over Existing Copy");
		if ((attr & 0x20) != 0) list.add("Force The PalmPilot To Reset After This Database Is Installed");
		if ((attr & 0x40) != 0) list.add("Don't Allow Copy Of File To Be Beamed To Other Pilot");

		String[] ret = new String[list.size()];
		int index = 0;
		for (String s : list) ret[index++] = s;

		return ret;
	}
}
