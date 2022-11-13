package client;

import common.GUI;
import common.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.Server;

public class Login extends GUI {

    private JButton jb_login;
    private JLabel jl_ra, jl_password, jl_title;
    private JTextField jt_ra, jt_password;

    public Login() {
        super("Login");
    }

    @Override
    protected void initComponents() {
        jl_title = new JLabel("CHAT", SwingConstants.CENTER);
        jb_login = new JButton("Entrar");
        jl_ra = new JLabel("RA", SwingConstants.LEFT);
        jl_password = new JLabel("Senha", SwingConstants.LEFT);
        jt_ra = new JTextField();
        jt_password = new JTextField();
    }

    @Override
    protected void configComponents() {
    	this.setLayout(null);
		this.setMinimumSize(new Dimension(500,500));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		
		jl_title.setBounds(50, 50, 400, 100);
		jb_login.setBounds(50, 300, 400, 40);
		
		jl_ra.setBounds(50, 130, 400, 40);	
		jl_password.setBounds(50, 200, 400, 40);
		
		jt_ra.setBounds(50, 165, 400, 40);
		jt_password.setBounds(50, 235, 400, 40);
    }

    @Override
    protected void insertComponents() {
        this.add(jl_title);
        this.add(jb_login);
        this.add(jl_password);
        this.add(jl_ra);
        this.add(jt_password);
        this.add(jt_ra);
    }

    @Override
    protected void insertActions() {
        jb_login.addActionListener(event -> {
        	try {
//        		Socket socket = new Socket(Server.HOST, Server.PORT);
        		Socket socket = new Socket("51.81.87.67", 8082);
        		Utils utils = new Utils(socket);
				String ra = jt_ra.getText();
				String password = jt_password.getText();
				
				jt_ra.setText("");
				jt_password.setText("");
				
				
				JSONObject params = new JSONObject();
		        params.put("ra", ra);
		        params.put("senha", password);

		        JSONObject request = new JSONObject();
		        request.put("operacao", "login");
		        request.put("parametros", params);
		        utils.sendMessage(request);
		        
		        String temp = utils.receiveMessage();
		        JSONObject response;
				JSONParser parserMessage = new JSONParser();
				response = (JSONObject) parserMessage.parse(temp);
		  
				Integer status = Integer.parseInt(response.get("status").toString());
				
				if(status == 200) {
					Home home = new Home(socket, response.toJSONString());
					this.dispose();
				} else {
					JOptionPane.showMessageDialog(this, response.get("mensagem"));
				}
			} catch( IOException | ParseException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
        });
    }

    @Override
    protected void start() {
        this.pack();
        this.setVisible(true);
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        Login login = new Login();
    }

}