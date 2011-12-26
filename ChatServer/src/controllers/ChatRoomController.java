package controllers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import answer.ChatRoomTable;
import answer.MessagesTable;
import answer.MyMap;
import our.Response;
import our.ResponseCodes;
import our.UserRequest;
import parse.MyXML;

public class ChatRoomController
{
	public void deleteAllUsersFromChatRoom(UserRequest http_request,Response http_response) //+
	{
		
	}
	public void deleteUserFromChatRoom(UserRequest http_request,Response http_response)// +
	{
		
	}
	public void deleteChatRoom(UserRequest http_request,Response http_response) //+
	{
		
	}
	public void addToChatRoom(UserRequest http_request,Response http_response) throws SQLException //POST +
, ParserConfigurationException, TransformerFactoryConfigurationError, IOException, TransformerException
	{
		final String URL = "jdbc:mysql://localhost/chat_db";
		
		final String USERNAME = "root";
		
		final String PASSWORD = "ghbvf777ghbvf";
		
		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		String body = http_request.getBody();
		HashMap <String,String> map = new HashMap<String,String>();
		
		
		try 
		{
			MyXML.parse(map, body);
			ChatRoomTable table = new ChatRoomTable(conn);
			MyMap toDB = new MyMap();
			toDB.add("users_id", map.get("users_id"));
			toDB.add("chatroom_id", map.get("chatroom_id"));
			table.insert(toDB);
		
			ResultSet users = table.getAllUsersFromChatRoom(Integer.parseInt(map.get("chatroom_id")));
			
			MessagesTable msg = new MessagesTable(conn);
			ResultSet msgs = msg.getMessageFromChatRoom(Integer.parseInt(map.get("chatroom_id")), 1);
			http_response.setBody(MyXML.createXML_Room_UsersMSG(users, msgs));
			//http_response.setBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><chatroom><id>"+i+"</id></chatroom>");
			http_response.setResponseCode(ResponseCodes.UserAddedToRoom);
			
			
			
			//System.out.println(i);
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void createChatRoom(UserRequest http_request,Response http_response) throws SQLException //POST + 
	{
		final String URL = "jdbc:mysql://localhost/chat_db";
		
		final String USERNAME = "root";
		
		final String PASSWORD = "ghbvf777ghbvf";
		
		Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
		String body = http_request.getBody();
		HashMap <String,String> map = new HashMap<String,String>();
		
		
		try 
		{
			MyXML.parse(map, body);
			ChatRoomTable table = new ChatRoomTable(conn);
			MyMap toDB = new MyMap();
			toDB.add("name", map.get("name"));
			toDB.add("info", map.get("info"));
			toDB.add("user_id", map.get("user_id"));
			table.insert(toDB);
			int i = table.returnId(map.get("name"), map.get("user_id"));
			
			http_response.setBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><chatroom><id>"+i+"</id></chatroom>");
			http_response.setResponseCode(ResponseCodes.RoomAdded);
			
			
			
			System.out.println(i);
		} 
		catch (UnsupportedEncodingException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}