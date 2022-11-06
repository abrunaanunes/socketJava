package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    	out.println(message.toJSONString());

        return !out.checkError();
    }
    
    public String receiveMessage() throws IOException, ParseException, NullPointerException
    {
    	String temp =  in.readLine();
		JSONParser parserMessage = new JSONParser();
		JSONObject response = (JSONObject) parserMessage.parse(temp);
		return response.toJSONString();
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
}