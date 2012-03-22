package gui;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

class MobiFileFilter extends FileFilter implements FilenameFilter
{

	// to make it work with JFileChooser
	//
	public boolean accept(File f)
	{
		if (f.isDirectory()) return true;
		
		return (f.getName().toLowerCase().endsWith(".azw") || f.getName().toLowerCase().endsWith(".mobi"));
	}

	public String getDescription()
	{
		return "*.azw,*.mobi";
	}

	// to make it work with java.awt.FileDialog
	//
	public boolean accept(File f, String name)
	{
		return (name.toLowerCase().endsWith(".azw") || name.toLowerCase().endsWith(".mobi"));
	}

}
