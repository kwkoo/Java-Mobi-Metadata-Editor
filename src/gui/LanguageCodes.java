package gui;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

public class LanguageCodes
{
	public final static String PROP_FILENAME = "languagecodes.properties";
	
	private HashMap<Integer,String> codeHash;
	private HashMap<String,Integer> descriptionHash;
	private String[] descriptions;

	public LanguageCodes()
	{
		Properties props = new Properties();
		try
		{
			props.load(ClassLoader.getSystemClassLoader().getResourceAsStream(PROP_FILENAME));
		}
		catch (IOException e)
		{
			// we really have to find a better way to handle this
		}

		codeHash = new HashMap<Integer,String>();
		descriptionHash = new HashMap<String,Integer>();
		Vector<String> vec = new Vector<String>();
		Enumeration<Object> keys = props.keys();
		while (keys.hasMoreElements())
		{
			String key = (String)keys.nextElement();
			String description = props.getProperty(key) + " (" + key + ")";

			try
			{
				Integer code = Integer.valueOf(key);
				codeHash.put(code, description);
				descriptionHash.put(description, code);
				vec.add(description);
			}
			catch (NumberFormatException e)
			{
			}
			Collections.sort(vec);
			descriptions = vec.toArray(new String[vec.size()]);
		}
	}

	public String[] getDescriptions()
	{
		return descriptions;
	}
	
	public boolean codeExists(int code)
	{
		return (codeToDescription(code) != null);
	}
	
	public int descriptionToCode(String description)
	{
		Integer code = descriptionHash.get(description);
		if (code == null) return -1;
		
		return code.intValue();
	}
	
	public String codeToDescription(int code)
	{
		return codeHash.get (Integer.valueOf(code));
	}
}
