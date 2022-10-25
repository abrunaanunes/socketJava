package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import common.Utils;

public class ClientListener implements Runnable {
	private Socket connection;
	private Server server;
	private boolean running;
	
	public ClientListener(Socket connection, Server server) {
		this.connection = connection;
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
		 String message;
		 while(running) {
			 
		 }
	}
}