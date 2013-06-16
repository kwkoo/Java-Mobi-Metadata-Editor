package gui;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

class GenericFileFilter extends FileFilter implements FilenameFilter
{
    public String extension;

    public String getExtension() {
        return extension;
    }

    public GenericFileFilter(String extension) {
        this.extension = extension.toLowerCase();
    }

	public String getDescription()
	{
		return extension;
	}

	public boolean accept(File f, String name)
	{
		return (accept(f));
	}

	public boolean accept(File f)
    {
        if (f.isDirectory()) return true;

        return (f.getName().toLowerCase().endsWith(extension));
    }

}
