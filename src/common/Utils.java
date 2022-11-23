package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.User;
import client.Home;
import server.ConnectionHandler;
import server.Server;

public class Utils {
	private final BufferedReader in;	
	private final PrintWriter out;
	private Socket socket;
	public Utils(Socket socket) throws IOException {
		this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
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
    
    public void flush() 
    {
    	this.out.flush();
    }
    
    public void close() throws IOException
    {
    	this.in.close();
        this.out.close();
        this.socket.close();
    }

	@Override
	public String toString() {
		return "Utils [in=" + in + ", out=" + out + ", socket=" + socket + "]";
	}

	public Object getRemoteSocketAddress() {
		return this.socket.getRemoteSocketAddress();
	}
	
	public static void broadcast() {
    	JSONArray users = new JSONArray();
    	JSONObject data = new JSONObject();
    	JSONObject response = new JSONObject();
    	
    	for(ConnectionHandler client : Server.clients) {
    		JSONObject userObj = new JSONObject();
    		
    		User user = new User();
    		user = client.getUser();
    		
    		userObj.put("nome", user.getName());
    		userObj.put("ra", user.getRa());
    		userObj.put("categoria_id", user.getCategoryId());
    		userObj.put("descricao", user.getDescription());
    		userObj.put("disponivel", user.getIsAvailable());
    		
    		users.add(userObj);
    	}
    	
    	data.put("usuarios", users);
    	response.put("status", 203);
    	response.put("mensagem", "Lista de usuários");
    	response.put("dados", data);
    	
    	for(ConnectionHandler client : Server.clients) {
    		if(client != null) {
    			client.sendMessage(response);
    		}
    	}
    	
//    	Home.getConnectedUsers(response);
    }
}