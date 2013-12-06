package com.mydomain;

import java.io.*;

public class log 
{

	private OutputStreamWriter osWriter = null;
	private OutputStream outStream = null;
	private File OutputFile = null;

	public log(String logFile) 
	{
		// TODO Auto-generated constructor stub
		try
		{
			OutputFile = new File(logFile);
			outStream = new FileOutputStream(OutputFile);
			osWriter = new OutputStreamWriter(outStream);
		}
		catch(IOException e)
		{
			e.printStackTrace();

		}
	}

	public void writeToLog(String string)
	{
		try {
			osWriter.write(string);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@SuppressWarnings("restriction")
	public void closeLog()
	{	
		try
		{
			outStream.flush();
			osWriter.flush();
			outStream.close();
			osWriter.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}		
}