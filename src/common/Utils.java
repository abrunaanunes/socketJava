package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    	out.println(message);

        return !out.checkError();
    }
    
    public JSONObject receiveMessage() throws IOException, ParseException
    {
    	String temp =  in.readLine();
		JSONParser parserMessage = new JSONParser();
		return (JSONObject) parserMessage.parse(temp);
    }
}