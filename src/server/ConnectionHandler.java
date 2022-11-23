package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.User;
import classes.UserList;
import common.Utils;

public class ConnectionHandler implements Runnable {
	private Socket socket;
	private User user;
	private boolean running;
	private final BufferedReader in;	
	private final PrintWriter out;
	
	public ConnectionHandler(User user, Socket socket) throws IOException {
		this.user = user;
		this.socket = socket;
		this.running = false;
		this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
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
				String temp = this.receiveMessage(); // Recebe em string e faz o parse pra JSON
				if(temp == null) {
					continue;
				}
				System.out.println("[CLIENTE->SERVIDOR]" + temp);
				
				JSONParser parserMessage = new JSONParser();
				JSONObject request = (JSONObject) parserMessage.parse(temp);
				
				String operation = request.get("operacao").toString();
				
				switch(operation) {
	                case "logout" : {
	                	JSONObject response = User.logout(request.toJSONString());
	                	this.sendMessage(response);
	                	System.out.println("[SERVIDOR->CLIENTE]" + response.toJSONString());

	    				Integer status = Integer.parseInt(response.get("status").toString());
	    				
	    				if(status == 600) {
	    					JSONObject params = (JSONObject) request.get("parametros");
	    					String ra = (String) params.get("ra");
	    					String password = (String) params.get("senha");
	    					
	    					ConnectionHandler client;
	    					if((client = getConnection(ra, password)) != null) {
	    						Server.clients.remove(client);
	    					}
	    				}
	    				
	                	this.close();
	                	running = false;
	                	Utils.broadcast();
	                	break;
	                }
	            }
				 
			} catch (IOException | ParseException e) {
				System.out.println("[CLIENT LISTENER ERROR]: " + e.getMessage() );
			}
		 }
	}
	
	public boolean sendMessage(JSONObject message)
    {     
    	out.println(message.toJSONString());

        return !out.checkError();
    }
	
	public String receiveMessage() throws IOException, ParseException, NullPointerException
    {
    	String temp = in.readLine();
    	if(temp != null || temp.equals("null")) {    		
    		JSONParser parserMessage = new JSONParser();
    		JSONObject response = (JSONObject) parserMessage.parse(temp);
    		return response.toJSONString();
    	}
    	
    	return null;
    		
    }
	
	public void close() throws IOException
    {
    	this.in.close();
        this.out.close();
        this.socket.close();
    }

	public User getUser() 
	{
		return user;
	}
	
	public static ConnectionHandler getConnection(String ra, String password) {
	    for (ConnectionHandler client : Server.clients) {
	        if (client.getUser().getRa().equals(ra) && client.getUser().getPassword().equals(password)) {
	            return client;
	        }
	    }
	    return null;
    }
}