package cli;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import mobimeta.*;

public class WhisperPrep
{
	private	File			inDir;
	private	File			outDir;
	private	BufferedReader	in;

	public final static void main(String[] args)
	{
		if (args.length != 2) printUsage();
		
		File inDir	= new File(args[0]);
		File outDir	= new File(args[1]);
		
		if (!inDir.exists() || !inDir.isDirectory())
			printUsage("Input directory " + args[0] + " does not exist.");

		if (!outDir.exists() || !outDir.isDirectory())
			printUsage("Output directory " + args[1] + " does not exist.");

		if (inDir.getAbsolutePath().equals(outDir.getAbsolutePath()))
			printUsage("Input and output directories cannot be the same.");

		WhisperPrep wp = new WhisperPrep(inDir, outDir);
		wp.start();
	}
	
	private static void printUsage()
	{
		printUsage(null);
	}

	private static void printUsage(String message)
	{
		if (message != null) System.err.println(message);
		System.err.println("Usage: WhisperPrep <input directory> <output directory>");
		System.exit(0);
	}

	public WhisperPrep(File inDir, File outDir)
	{
		this.inDir	= inDir;
		this.outDir	= outDir;
	}
	
	public void start()
	{
		LinkedList<File> fileList = new LinkedList<File>();
		for (File f : inDir.listFiles())
		{
			if (f.isFile() && f.getName().toLowerCase().endsWith(".mobi"))
				fileList.add(f);
		}

		in = new BufferedReader(new InputStreamReader(System.in));

		int fileCount	= fileList.size();
		int current		= 1;
		for (File f : fileList)
		{
			log("********************");
			log("File " + (current++) + "/" + fileCount);
			log("Filename: " + f.getName());
			MobiMeta meta = null;
			try
			{
				meta = new MobiMeta(f);
			}
			catch (MobiMetaException e)
			{
				log("Error: " + e.getMessage());
				continue;
			}
			
			log("Fullname: " + meta.getFullName());
			List<EXTHRecord> exthList = meta.getEXTHRecords();
			String encoding	= meta.getCharacterEncoding();
			String author	= null;
			String isbn		= null;
			String oldasin	= null;
			for (EXTHRecord rec : exthList)
			{
				switch (rec.getRecordType())
				{
					case 100:
						author = StreamUtils.byteArrayToString(rec.getData(), encoding);
						break;

					case 104:
						isbn = StreamUtils.byteArrayToString(rec.getData(), encoding);
						break;

					case 113:
						oldasin = StreamUtils.byteArrayToString(rec.getData(), encoding);
						break;

					default:
						break;
				}
			}
			
			if (author != null) log("Author: " + author);
			if (isbn != null) log("ISBN: " + isbn);
			String asin = null;
			if (oldasin == null)
				asin = getInput("ASIN: ");
			else
				asin = getInput("ASIN [" + oldasin + "]: ");

			if (asin.length() == 0)
			{
				if (oldasin != null)
					asin = oldasin;
				else
					asin = null;
			}

			for (EXTHRecord rec : exthList)
			{
				int recType = rec.getRecordType();
				if (recType == 113)
				{
					if (asin != null) exthList.remove(rec);
				}
				else if (recType == 501)
					exthList.remove(rec);
			}
			
			if (asin != null) exthList.add(new EXTHRecord(113, asin, encoding));
			exthList.add(new EXTHRecord(501, "EBOK", encoding));
			meta.setEXTHRecords();
			
			try
			{
				meta.saveToNewFile(new File(outDir, f.getName()));
			}
			catch (MobiMetaException e)
			{
				log("Error saving file: " + e.getMessage());
			}

			log("");
		}
	}
	
	private void log(String message)
	{
		System.out.println(message);
	}

	private String getInput(String message)
	{
		System.out.print(message);
		String value = null;
		try
		{
			value = in.readLine();
		}
		catch (IOException e)
		{
		}
		
		return (value == null)?"":value;
	}
}
