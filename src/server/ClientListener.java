package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import client.Home;
import common.Utils;

public class ClientListener implements Runnable {
	private Socket socket;
	private Server server;
	private boolean running;
	
	public ClientListener(Socket socket, Server server) throws IOException {
		this.socket = socket;
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
				String temp = Server.utils.receiveMessage(); // Recebe em string e faz o parse pra JSON
				JSONParser parserMessage = new JSONParser();
				JSONObject request = (JSONObject) parserMessage.parse(temp);
				
				String operation = request.get("operacao").toString();
				
				switch(operation) {
	                case "logout" : {
	                	JSONObject response = Server.user.logout(request.toJSONString());
	                	if(Integer.parseInt(response.get("status").toString()) == 600) {
	                		//
	                	}
	                	Server.utils.sendMessage(response);
	                	System.out.println("[SERVIDOR->CLIENTE]" + response.toJSONString());
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
		return "ClientListener [socket=" + socket + ", server=" + server + ", running=" + running + "]";
	}
}