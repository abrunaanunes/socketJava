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

import server.Server;

public class Home extends GUI {

    private JLabel title;
    private ServerSocket server;
    private final Socket connection;
    private final String connection_info;
    private JButton jb_get_connected, jb_start_talk;
    private JList jlist;
    private JScrollPane scroll;

    public Home(Socket connection, String request) {
        super("Chat - Home");
		
        this.connection_info = request;
        this.connection = connection;
        this.title.setText("Bem-vindo(a)");
        this.setTitle("Home");
    }

    @Override
    protected void initComponents() {
        title = new JLabel();
        jb_get_connected = new JButton("Atualizar contatos");
        jlist = new JList();
        scroll = new JScrollPane(jlist);
        jb_start_talk = new JButton("Abrir Conversa");
    }

    @Override
    protected void configComponents() {
        this.setLayout(null);
        this.setMinimumSize(new Dimension(600, 480));
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.WHITE);

        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBounds(10, 10, 370, 40);
        title.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        jb_get_connected.setBounds(400, 10, 180, 40);
        jb_get_connected.setFocusable(false);

        jb_start_talk.setBounds(10, 400, 575, 40);
        jb_start_talk.setFocusable(false);

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
        this.add(jb_start_talk);
    }

    @Override
    protected void insertActions() {
       
    }

    @Override
    protected void start() {
        this.pack();
        this.setVisible(true);
    }
}