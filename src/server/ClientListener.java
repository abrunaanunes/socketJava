package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import client.Home;
import common.Utils;

public class ClientListener implements Runnable {
	private Socket socket;
	private Server server;
	private boolean running;
//	private Utils utils;
	
	public ClientListener(Socket socket, Server server) throws IOException {
		this.socket = socket;
		this.server = server;
		this.running = false;
//		this.utils = new Utils(this.connection);
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
				Utils utils = new Utils(Home.socket);
				String response = utils.receiveMessage();
				System.out.println(response);
				
				
			} catch (IOException | ParseException e) {
				System.out.println("[CLIENT LISTENER ERROR]: " + e.getMessage() );
			}
		 }
	}
}