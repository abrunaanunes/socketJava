package client;

import common.GUI;
import common.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import classes.CategoryList;
import server.Server;

public class Register extends GUI {

    private JButton jb_register;
    private JLabel jl_name, jl_ra, jl_password, jl_description, jl_category, jl_title;
    private JTextField jt_name, jt_ra, jt_password, jt_description, jt_category;
    private JComboBox<String>  jc_category;
    private CategoryList categoryController;

    public Register() {
        super("Register");
    }

    @Override
    protected void initComponents() {
        jl_title = new JLabel("CHAT", SwingConstants.CENTER);
        jb_register = new JButton("Cadastrar");
        
        jl_name = new JLabel("Nome", SwingConstants.LEFT);
        jl_ra = new JLabel("RA", SwingConstants.LEFT);
        jl_password = new JLabel("Senha", SwingConstants.LEFT);
        jl_description = new JLabel("Descrição", SwingConstants.LEFT);
        jl_category = new JLabel("Categoria", SwingConstants.LEFT);
        
        jt_name = new JTextField();
        jt_ra = new JTextField();
        jt_password = new JTextField();
        jt_description = new JTextField();
        jt_category = new JTextField();
        
        jc_category = new JComboBox<String>();
        categoryController = new CategoryList();
    }

    @Override
    protected void configComponents() {
    	int i;
    	this.setLayout(null);
		this.setMinimumSize(new Dimension(500,600));
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		
		jl_title.setBounds(50, 5, 400, 100);
		jb_register.setBounds(50,470,400,40);

		jl_name.setBounds(50, 60, 400, 40);
		jl_ra.setBounds(50, 130, 400, 40);	
		jl_password.setBounds(50, 200, 400, 40);
		jl_description.setBounds(50, 270, 400, 40);
		jl_category.setBounds(50, 340, 400, 40);
		
		jt_name.setBounds(50, 95, 400, 40);
		jt_ra.setBounds(50, 165, 400, 40);
		jt_password.setBounds(50, 235, 400, 40);
		jt_description.setBounds(50, 305, 400, 40);
		jc_category.setBounds(50, 375, 400, 40);
		
		for (i = 0; i < categoryController.categories.length; i++) {
			jc_category.addItem(categoryController.categories[i].getName());
		}
    }

    @Override
    protected void insertComponents() {
        this.add(jl_title);
        this.add(jb_register);
        
        this.add(jl_name);
        this.add(jt_name);
        this.add(jl_ra);
        this.add(jt_ra);
        this.add(jl_password);
        this.add(jt_password);
        this.add(jl_description);
        this.add(jt_description);
        this.add(jl_category);
        this.add(jc_category);
    }

    @Override
    protected void insertActions() {
        jb_register.addActionListener(event -> {
        	try {
//        		Utils socket = new Utils(new Socket(Server.HOST, Server.PORT));
        		Utils socket = new Utils(new Socket("10.20.8.119", 23000));
        		
        		String name = jt_name.getText();
				String ra = jt_ra.getText();
				String password = jt_password.getText();
				String description = jt_description.getText();
				Integer category_id = jc_category.getSelectedIndex();
				
				jt_name.setText("");
				jt_ra.setText("");
				jt_password.setText("");
				jt_description.setText("");
				
				JSONObject params = new JSONObject();
		        params.put("nome", name);
		        params.put("ra", ra);
		        params.put("senha", password);
		        params.put("descricao", description);
		        params.put("categoria_id", category_id);

		        JSONObject request = new JSONObject();
		        request.put("operacao", "cadastrar");
		        request.put("parametros", params);
		        socket.sendMessage(request);
		        String temp = socket.receiveMessage();
		        System.out.println("[SERVIDOR->CLIENTE]: " + temp);
		        JSONObject response;
				JSONParser parserMessage = new JSONParser();
				response = (JSONObject) parserMessage.parse(temp);
		        
				Integer status = Integer.parseInt(response.get("status").toString()) ;
				
				if(status == 201) {
					socket.close();
					Login login = new Login();
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

    public static void main(String[] args) {
        @SuppressWarnings("unused")
		Register register = new Register();
    }

}