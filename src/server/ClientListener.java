package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.User;

public class ClientListener implements Runnable {
	private Socket socket;
	private Server server;
	private User user;
	private boolean running;
	
	public ClientListener(User user, Socket socket, Server server) throws IOException {
		this.socket = socket;
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
				String temp = Server.utils.receiveMessage(); // Recebe em string e faz o parse pra JSON
				JSONParser parserMessage = new JSONParser();
				JSONObject request = (JSONObject) parserMessage.parse(temp);
				
				String operation = request.get("operacao").toString();
				
				switch(operation) {
	                case "logout" : {
	                	JSONObject response = Server.user.logout(request.toJSONString());
	                	Server.utils.sendMessage(response);
	                	System.out.println("[SERVIDOR->CLIENTE]" + response.toJSONString());
	                	running = false;
	                	break;
	                }
	                case "obter_usuarios": {
	                	ArrayList<ClientListener> clients = server.getClients();
	                	
	                	System.out.println(clients);
//	                	
//	                	JSONObject response = new JSONObject();
//	                	JSONObject data = new JSONObject();
//	                	
//	                	response.put("status", 203);
//	                    response.put("mensagem", "Lista de usuários");
//	                    response.put("dados", data);
//	                	break;
	                }
	            }
				 
			} catch (IOException | ParseException e) {
				System.out.println("[CLIENT LISTENER ERROR]: " + e.getMessage() );
			}
		 }
	}

	@Override
	public String toString() {
		return "ClientListener [socket=" + socket + ", server=" + server + ", user=" + user + ", running=" + running
				+ "]";
	}

	public User getUser() {
		return user;
	}
}