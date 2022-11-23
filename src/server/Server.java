package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.CategoryList;
import classes.User;
import classes.UserList;
import common.Utils;

public class Server {

	private ServerSocket socketServer;
	
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8085;    
    public static ArrayList<ConnectionHandler> clients;

    @SuppressWarnings("unused")
	public Server() {
    	
    	// Corrigir pra não ser necessário instanciar 
    	CategoryList categoryList = new CategoryList();
    	UserList userList = new UserList();
    	
    	try {
            clients = new ArrayList<ConnectionHandler>();
            socketServer = new ServerSocket(PORT);
            System.out.println("Servidor iniciado na porta " + PORT);
            
            while (true) {
            	Socket socket = socketServer.accept();
                Utils socketClient = new Utils(socket);
                System.out.println("[SERVIDOR]: Conexão aberta para: " + socketClient.getRemoteSocketAddress().toString());
                JSONObject response;
              
                String temp = socketClient.receiveMessage(); // Recebe em string e faz o parse pra JSON
				if(temp == null) {
					continue;
				}
                JSONParser parserMessage = new JSONParser();
				JSONObject request = (JSONObject) parserMessage.parse(temp);
				JSONObject params = (JSONObject) request.get("parametros");
				String operation = request.get("operacao").toString();
				
				System.out.println("[CLIENTE->SERVIDOR]" + request.toJSONString());                
                switch(operation) {
	                case "login" : {
	                	response = User.login(request);
	                	if(Integer.parseInt(response.get("status").toString()) == 200) {
	                		User userConnection = User.getUser(params.get("ra").toString(), params.get("senha").toString());
			                ConnectionHandler connectionHandler = new ConnectionHandler(userConnection, socket);
			                clients.add(connectionHandler);
			                new Thread(connectionHandler).start();
	                	}
	                	socketClient.sendMessage(response);
	                	System.out.println("[SERVIDOR->CLIENTE]" + response.toJSONString());
	                	break;
	                }
	                case "cadastrar" : {
	                	// Fechar o socket no cadastro
	                	response = User.register(request);	                	
	                	socketClient.sendMessage(response);
	                	socketClient.close();
	                	System.out.println("[SERVIDOR->CLIENTE]" + response.toJSONString());
	                	break;
	                }
                }
                Utils.broadcast();
            }
        } catch (IOException | ParseException ex) {
            System.err.println("[ERROR:Server] -> " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        @SuppressWarnings("unused")
		Server server = new Server();
    }
}