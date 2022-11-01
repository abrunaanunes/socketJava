package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.CategoryList;
import classes.User;
import classes.UserList;
import common.Utils;

public class Server {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 4444;
    private ServerSocket socketServer;
    private ArrayList<ClientListener> clients;

    @SuppressWarnings("unused")
	public Server() {
    	
    	try {
            clients = new ArrayList<ClientListener>();
            socketServer = new ServerSocket(PORT);
            System.out.println("Servidor iniciado na porta " + PORT);
            
            while (true) {
                Socket socketClient = socketServer.accept();
                Utils utils = new Utils(socketClient);
                User user = new User(new CategoryList(), new UserList());
                JSONObject response;
              
                String temp = utils.receiveMessage();
                JSONObject request;
				JSONParser parserMessage = new JSONParser();
				request = (JSONObject) parserMessage.parse(temp);
				
                System.out.println("[CLIENTE->SERVIDOR]" + request.toJSONString());
                String operation = request.get("operacao").toString();
                
                switch(operation) {
	                case "login" : {
	                	response = user.login(request);
	                	System.out.println(response.toJSONString());
	                	if(Integer.parseInt(response.get("status").toString()) == 200) {
			                ClientListener clientListener = new ClientListener(socketClient, this);
			                clients.add(clientListener);
	                	}
	                	utils.sendMessage(response);
	                	System.out.println("[SERVIDOR->CLIENTE]" + response.toJSONString());
	                	break;
	                }
	                case "cadastrar" : {
	                	response = user.register(request);
	                	if(Integer.parseInt(response.get("status").toString()) == 200) {
			                ClientListener clientListener = new ClientListener(socketClient, this);
			                clients.add(clientListener);
	                	}
	                	utils.sendMessage(response);
	                	System.out.println("[SERVIDOR->CLIENTE]" + response.toJSONString());
	                	break;
	                }
                }
            }
        } catch (IOException | ParseException ex) {
            System.err.println("[ERROR:Server] -> " + ex.getMessage());
        }
    }

    public ArrayList<ClientListener> getClients() {
        return clients;
    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
		Server server = new Server();
    }
}