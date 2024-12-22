package org.example.tetetete.client;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientSocketHandler.class);

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ChatController chatController;

    public ClientSocketHandler(String host, int port, ChatController chatController) throws IOException {
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.chatController = chatController;
        startListening();
    }

    // Метод для регистрации пользователя
    public void sendRegistrationRequest(String username, String password) {
        out.println("REGISTER " + username + " " + password);
        logger.info("Registration request sent: {} {}", username, password);
    }

    public void sendMessage(String message) {
        out.println(message);
        logger.info("Sent message: {}", message);
    }

    private void startListening() {
        new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    chatController.appendMessage(message);
                    logger.info("Received message: {}", message);
                }
            } catch (IOException e) {
                logger.error("Error while listening for messages", e);
            }
        }).start();
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
        logger.info("Connection closed");
    }
}