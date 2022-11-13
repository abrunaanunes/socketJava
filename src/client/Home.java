package client;

import common.GUI;
import common.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.User;
import server.Server;

public class Home extends GUI {

	private ServerSocket server;
	private static String request;
	private ArrayList<String> connected_users;

	// Declarando componentes
	private JLabel title;
    private JButton jb_get_connected, jb_message;
    private JList jlist;
    private JScrollPane scroll;
   
    private static Utils socket;

    public Home(Socket socket, String request) throws ParseException, IOException {
        super("Chat - Home");
		
        this.request = request;
        this.socket = new Utils(socket);
        this.title.setText("Bem-vindo(a)");
        this.connected_users = new ArrayList<String>();
        this.setTitle("Home");
        this.getUser(request);
    }

    @Override
    protected void initComponents() {
        title = new JLabel();
        jb_get_connected = new JButton("Buscar usuários");
        jlist = new JList();
        scroll = new JScrollPane(jlist);
        jb_message = new JButton("Abrir Conversa");
    }

    @Override
    protected void configComponents() {
        this.setLayout(null);
        this.setMinimumSize(new Dimension(600, 500));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(10, 10, 370, 40);
        title.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        jb_get_connected.setBounds(400, 10, 180, 40);
        jb_get_connected.setFocusable(false);

        jb_message.setBounds(10, 400, 575, 40);
        jb_message.setFocusable(false);

        jlist.setBorder(BorderFactory.createTitledBorder("Usuários online"));
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scroll.setBounds(10, 60, 575, 335);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
    }

    @Override
    protected void insertComponents() {
        this.add(title);
        this.add(jb_get_connected);
        this.add(scroll);
        this.add(jb_message);
    }

    @Override
    protected void insertActions() {
    	this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            	JSONObject user = getUser(request);
            	
            	JSONObject params = new JSONObject();
    			params.put("ra", user.get("ra"));
    			params.put("senha", user.get("senha"));
    			
    			JSONObject request = new JSONObject();
    			request.put("operacao", "logout");
    			request.put("parametros", params);
    			
    			socket.sendMessage(request);
    			
    			try {
    				String temp = socket.receiveMessage();
    				JSONObject response;
    				JSONParser parserMessage = new JSONParser();
    				response = (JSONObject) parserMessage.parse(temp);
    				Integer status = Integer.parseInt(response.get("status").toString());
    				
    				if(status == 600) {
    					socket.close();
    					System.out.println("[CLIENTE->SERVIDOR]: Conexão fechada para " + socket);
    				} 
    			} catch (ParseException | IOException ex) {
    				System.out.println("[CLIENTE->SERVER: ] Erro ao realizar logout. " + ex.getMessage());
    			}		
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    	
    	this.jb_message.addActionListener(event -> {
    		System.out.println("Abrir chat");
    	});
    	
    	this.jb_get_connected.addActionListener(event -> getConnectedUsers());
       
    }
    
    public static JSONObject getUser(String temp)
    {	
    	JSONObject userObj = new JSONObject();
		
    	try {
			JSONParser parserMessage = new JSONParser();
			JSONObject json;
			json = (JSONObject) parserMessage.parse(temp);
			JSONObject data = (JSONObject) json.get("dados");
			userObj = (JSONObject) data.get("usuario");
			
		} catch (ParseException e) {
			//@TODO
		}		
		
		return userObj;
    }
    
    private void getConnectedUsers() {
    	JSONObject user = getUser(request);
    	
    	JSONObject params = new JSONObject();
		params.put("categoria_id", 0);
		
		JSONObject request = new JSONObject();
		request.put("operacao", "obter_usuarios");
		request.put("parametros", params);
		
		socket.sendMessage(request);
		
//		try {
//			String temp = utils.receiveMessage();
//			JSONObject response;
//			JSONParser parserMessage = new JSONParser();
//			response = (JSONObject) parserMessage.parse(temp);
//			Integer status = Integer.parseInt(response.get("status").toString());
//			
//			if(status == 600) {
//				utils.close();
//				System.out.println("[CLIENTE->SERVIDOR]: Conexão fechada para " + socket);
//			} 
//		} catch (ParseException | IOException ex) {
//			System.out.println("[CLIENTE->SERVER: ] Erro ao realizar logout. " + ex.getMessage());
//		}
    }
    

    @Override
    protected void start() {
        this.pack();
        this.setVisible(true);
    }
}