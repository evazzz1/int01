package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 550;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JTextArea log = new JTextArea();
    private boolean isServerWorking;
    private List<ClientGui> clients;
    private static final String LOG_FILE = "chat_log.txt";

    public ServerWindow() {
        isServerWorking = false;
        clients = new ArrayList<>();

        btnStart.addActionListener(e -> {
            if (isServerWorking) {
                log.append("Server is already running\n");
            } else {
                isServerWorking = true;
                log.append("Server started\n");
            }
        });

        btnStop.addActionListener(e -> {
            if (!isServerWorking) {
                log.append("Server is not running\n");
            } else {
                isServerWorking = false;
                log.append("Server stopped\n");
                for (ClientGui client : clients) {
                    client.dispose();
                }
                clients.clear();
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Chat server");
        setAlwaysOnTop(true);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(btnStart);
        buttonPanel.add(btnStop);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(log), BorderLayout.CENTER);

        setVisible(true);
    }

    public void registerClient(ClientGui client) {
        clients.add(client);
    }

    public void broadcastMessage(String message) {
        log.append(message + "\n");
        appendToFile(message);
        for (ClientGui client : clients) {
            client.appendToLog(message);
        }
    }

    private void appendToFile(String message) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}