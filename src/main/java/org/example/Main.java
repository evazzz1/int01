package org.example;

public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        new ClientGui(serverWindow, "Client 1", "127.8.8.1", "8189", "ivan_igorovich", "123456");
        new ClientGui(serverWindow, "Client 2", "123.6.7.0", "5555", "vera_nikolaevna", "654321");
    }
}