package client;

import common.GUI;
import common.Utils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Chat extends GUI {

    private JLabel jl_title;
    private JEditorPane messages;
    private JTextField jt_message;
    private JButton jb_message;
    private JPanel panel;
    private JScrollPane scroll;

    private ArrayList<String> message_list;
    private Home home;
    private Socket connection;
    private String connection_info;

    public Chat(Home home, Socket connection, String connection_info, String title) {
        super("Chat " + title);
        this.home = home;
        this.connection_info = connection_info;
        message_list = new ArrayList<String>();
        this.connection = connection;
        this.jl_title.setText(connection_info.split(":")[0]);
        this.jl_title.setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void initComponents() {
        jl_title = new JLabel();
        messages = new JEditorPane();
        scroll = new JScrollPane(messages);
        jt_message = new JTextField();
        jb_message = new JButton("Enviar");
        panel = new JPanel(new BorderLayout());
    }

    @Override
    protected void configComponents() {
        this.setMinimumSize(new Dimension(480, 720));
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        messages.setContentType("text/html");
        messages.setEditable(false);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jb_message.setSize(100, 40);
    }

    @Override
    protected void insertComponents() {
        this.add(jl_title, BorderLayout.NORTH);
        this.add(scroll, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
        panel.add(jt_message, BorderLayout.CENTER);
        panel.add(jb_message, BorderLayout.EAST);
    }

    @Override
    protected void insertActions() {
        
    }

    public void append_message(String received) {
        message_list.add(received);
        String message = "";
        for (String str : message_list) {
            message += str;
        }
        messages.setText(message);
    }

    @Override
    protected void start() {
        this.pack();
        this.setVisible(true);
    }

    private void send() {

    }

}