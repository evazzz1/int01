package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ClientGui extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress;
    private final JTextField tfPort;
    private final JTextField tfLogin;
    private final JPasswordField tfPassword;
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final ServerWindow serverWindow;
    private final String clientName;

    public ClientGui(ServerWindow serverWindow, String clientName, String ipAddress, String port, String login, String password) {
        this.serverWindow = serverWindow;
        this.clientName = clientName;

        tfIPAddress = new JTextField(ipAddress);
        tfPort = new JTextField(port);
        tfLogin = new JTextField(login);
        tfPassword = new JPasswordField(password);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client: " + clientName);

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog, BorderLayout.CENTER);

        btnSend.addActionListener(e -> sendMessage());
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        btnLogin.addActionListener(e -> connectToServer());

        serverWindow.registerClient(this);

        setVisible(true);
    }

    private void sendMessage() {
        String message = tfMessage.getText().trim();
        if (!message.isEmpty()) {
            serverWindow.broadcastMessage(clientName + ": " + message);
            tfMessage.setText("");
        }
    }

    private void connectToServer() {
        log.append("Connected to the server\n");
        loadChatHistory();
    }

    private void loadChatHistory() {
        try (BufferedReader reader = new BufferedReader(new FileReader("chat_log.txt"))) {
            log.read(reader, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendToLog(String message) {
        log.append(message + "\n");
    }

    public static void main(String[] args) {
        new ClientGui(null, "Test Client", "11", "8189", "ii", "00");
    }
}