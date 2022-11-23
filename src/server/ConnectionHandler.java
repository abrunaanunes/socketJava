package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.User;
import common.Utils;

public class ConnectionHandler implements Runnable {
	private Utils socketClient;
	private Server server;
	private User user;
	private boolean running;
	
	public ConnectionHandler(User user, Utils socketClient, Server server) throws IOException {
		this.socketClient = socketClient;
		this.user = user;
		this.server = server;
		this.running = false;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	
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
				
				String operation = request.get("operacao").toString();
				
				switch(operation) {
	                case "logout" : {
	                	JSONObject response = Server.user.logout(request.toJSONString());
	                	socketClient.sendMessage(response);
	                	System.out.println("[SERVIDOR->CLIENTE]" + response.toJSONString());
	                	socketClient.close();
	                	running = false;
	                	break;
	                }
	            }
				 
			} catch (IOException | ParseException e) {
				System.out.println("[CLIENT LISTENER ERROR]: " + e.getMessage() );
			}
		 }
	}

	@Override
	public String toString() {
		return "ClientListener [socket=" + socketClient + ", server=" + server + ", user=" + user + ", running=" + running
				+ "]";
	}
	
	public boolean sendMessage(JSONObject message)
    {     
    	return socketClient.sendMessage(message);
    }
	
	public String receiveMessage() throws IOException, ParseException, NullPointerException
    {
    	return socketClient.receiveMessage();
    		
    }

	public User getUser() {
		return user;
	}
}