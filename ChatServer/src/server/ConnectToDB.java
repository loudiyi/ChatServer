package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.*;

import logger.Logger;

import our.Response;
import our.ResponseCodes;


public class ConnectToDB {
	private static Connection conn = null;
	private static String URL = null;
	private static String nick = null;
	private static String pass = null;
	private static void getAuth()
	{
		try
		{
			RandomAccessFile r = new RandomAccessFile("key.auth","r");
			try
			{
				URL = r.readLine();
				nick = r.readLine();
				pass = r.readLine();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			try{Logger.writeEvent(e.toString());}catch(Exception exep){exep.printStackTrace();}
		}
	}
	public static Connection getConnection ()
	{
		return conn;
		
	}
	public static void startUpConnection() throws IOException
	{
		try 
		{
			getAuth();
			conn = DriverManager.getConnection(URL, nick, pass);
			Logger.writeEvent("Successfully established a connection to the database " + URL);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			try{Logger.writeEvent(e.toString());}catch(Exception exep){exep.printStackTrace();}
		}
		
	}
}
