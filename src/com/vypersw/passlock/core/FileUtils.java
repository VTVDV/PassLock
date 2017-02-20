package com.vypersw.passlock.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class FileUtils 
{
	private static FileUtils instance = null;
	private Properties props;
	
	public static FileUtils getInstance() 
	{
		if (instance == null)
		{
			return new FileUtils();
		}
		return instance;
	}
	
	public void loadProperties(SQLUtility sqlite)
	{
		File file = new File("Application.properties");
		if (!file.exists())
		{
			try(InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("Application.properties"))
			{
				props = new Properties();
				props.load(resourceStream);
				sqlite.setEncryptionKey(props.getProperty("ENCRYPTION_KEY"));
				System.out.println(props.getProperty("ENCRYPTION_KEY"));
				savePropertiesFile();
			}
			catch (Exception e)
			{
				sqlite.setEncryptionKey(VyperConstants.DEFAULT_ENCRYPTION_KEY);
				System.out.println("Exception: " + e.getMessage() + " occured. Setting encryption key to default");
			}
		} 

	}
	
	public void savePropertiesFile()
	{
		try
		{
			props.store(new FileOutputStream("Application.properties"), null); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}
