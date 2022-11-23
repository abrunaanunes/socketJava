package client;

import java.io.IOException;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.User;
import common.Utils;
import server.Server;

public class ConnectionHandler implements Runnable{
	
	private boolean running;
	private boolean chatOpen; // Caso o chat esteja aberto, estará como true
	private Socket socket;
	private Home home;
	private User user;
	private Chat chat;
	private Utils socketClient;
	
	public ConnectionHandler(Home home, Socket socket, Utils socketClient)
	{
		this.home = home;
		this.socket = socket;
		this.chatOpen = false;
		this.running = false;
		this.socketClient = socketClient;
	}
	
	@Override
	public void run() {
		running = true;
		 while(running) {
			 try {
				String temp = socketClient.receiveMessage(); // Recebe em string e faz o parse pra JSON
				if(temp == null) {
					continue;
				}
				System.out.println("[CLIENTE->SERVIDOR]" + temp);
				
				JSONParser parserMessage = new JSONParser();
				JSONObject request = (JSONObject) parserMessage.parse(temp);
				
				Integer status = Integer.parseInt(request.get("status").toString());
				
				switch(status) {
	                
	            }
				 
			} catch (IOException | ParseException e) {
				System.out.println("[CLIENT LISTENER ERROR]: " + e.getMessage() );
			}
		 }
	}

}
