package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import common.Utils;

public class ClientListener implements Runnable {
	private Socket connection;
	private Server server;
	private boolean running;
	private Utils utils;
	
	public ClientListener(Socket connection, Server server) throws IOException {
		this.connection = connection;
		this.server = server;
		this.running = false;
		this.utils = new Utils(this.connection);
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
				String response = utils.receiveMessage();
				System.out.println(response);
				
				
			} catch (IOException | ParseException e) {
				System.out.println("[CLIENT LISTENER ERROR]: " + e.getMessage() );
			}
		 }
	}
}