package sender;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import logger.Logger;

import our.Response;

public class SenderThread implements Runnable
{
	static Queue<Response> events = new LinkedList<Response>();
	//static ArrayList<SocketMap> online_list =new ArrayList<SocketMap>();
	static ArrayList <SenderMap> online_list = new ArrayList<SenderMap>();
	//            idTable,idOnLine,Socket
	//              +                 +
	@Override
	public synchronized void run() 
	{
		Iterator it=events.iterator();
		while(true)
		{
			//���������� ���� ���� ������ 
			if(it.hasNext())
			{
				try
				{
					System.out.println("SEND TO = "+events.peek().getMailto());
					int idArray = getIdArray(events.peek().getMailto());
					OutputStream output = online_list.get(idArray).s.getOutputStream();
					PrintWriter r = new PrintWriter(output,true);
					r.println(events.peek().TOSTR());
					events.poll();
				}
				catch(Exception e)
				{e.printStackTrace();}
			}
			try 
			{
				Thread.currentThread().sleep(50);
			} 
			catch (InterruptedException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				try{Logger.writeEvent(e.toString());}catch(Exception exep){exep.printStackTrace();}
			}
		}
		
	}
	private synchronized static int getIdArray(int TableId)
	{
		for(int i = 0;i < online_list.size();i++)
		{
			if(online_list.get(i).idTable == TableId)
			{
				return online_list.get(i).idArray;
			}
		}
		return 0;
	}
	public synchronized static void addEvent(Response httpEvent,int mailto)
	{
		httpEvent.setMailto(mailto);
		System.out.println(httpEvent.getMailto());
		events.add(httpEvent);
	}
	public synchronized static void addUserToOnLine(int id,Socket s) throws IOException
	{
		online_list.add(new SenderMap(id,s));
		online_list.get(online_list.size()-1).setIdArray(online_list.size()-1);
		OutputStream output = s.getOutputStream();
		PrintWriter r = new PrintWriter(output,true);
		Logger.writeEvent("Successfully added the listener! ip : " + s.getInetAddress().toString() + " port : " + s.getPort());
		System.out.println("Successfully added the listener! ip : " + s.getInetAddress().toString() + " port : " + s.getPort());
		/////////////////////////////////////
		for(int i = 0;i < online_list.size();i++)
		{
			System.out.println("ONLINE : " + online_list.get(i).idTable);
		}
		////////////////////////////////////
	}
	public synchronized static void removeUserFromOnLine(int id) throws IOException
	{
		//online_list.add(new SenderMap(id,s));
		//online_list.get(online_list.size()-1).setIdArray(online_list.size()-1);
		//OutputStream output = s.getOutputStream();
		//PrintWriter r = new PrintWriter(output,true);
		//Logger.writeEvent("Successfully added the listener! ip : " + s.getInetAddress().toString() + " port : " + s.getPort());
		//System.out.println("Successfully added the listener! ip : " + s.getInetAddress().toString() + " port : " + s.getPort());
		int index = getIdArray(id);
		online_list.remove(index);
		if(!online_list.isEmpty())
			if(index == 0)
				{online_list.get(0).setIdArray(0);}
		//////////// STEP1 ///////////////////////
		for(int i = 0;i < online_list.size();i++)
		{
			System.out.println("=========Before================");
			System.out.println("");
			System.out.println("idArray : " + online_list.get(i).idArray);
			System.out.println("idTable : " + online_list.get(i).idTable);
			System.out.println("================================");
		}
		////////////////////////////////////////////
		for(int i = 0;i < online_list.size();i++)
		{
			if(i == 0)
			{
				
			}
			else
			{
				online_list.get(i).setIdArray(online_list.get(i).idArray-1);
				if(online_list.size() == 1)
				{
					online_list.get(i).setIdArray(0);
				}
				
			}
		}
		//System.out.println("OFFLINE : " + online_list.get(index).idTable);
		for(int i = 0;i < online_list.size();i++)
		{
			System.out.println("=========Atfer================");
			System.out.println("");
			System.out.println("idArray : " + online_list.get(i).idArray);
			System.out.println("idTable : " + online_list.get(i).idTable);
			System.out.println("================================");
		}
	}

}