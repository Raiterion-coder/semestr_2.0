package org.example.tetetete.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.impl.StaticLoggerBinder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final Logger logger = LoggerFactory.getLogger(ChatServer.class);
    private final int port;
    private final Map<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public ChatServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Сервер запущен на порту {}", port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket, this)).start();
            }
        } catch (IOException e) {
            logger.error("Ошибка запуска сервера:", e);
        }
    }

    public void broadcast(String message, ClientHandler sender) {
        clients.values().forEach(client -> {
            if (client != sender) {
                client.sendMessage(message);
            }
        });
    }

    public synchronized void addClient(String username, ClientHandler clientHandler) {
        clients.put(username, clientHandler);
        broadcast("Пользователь " + username + " присоединился к чату.", null);
        logger.info("Пользователь {} подключен", username);
    }

    public synchronized void removeClient(String username) {
        clients.remove(username);
        broadcast("Пользователь " + username + " покинул чат.", null);
        logger.info("Пользователь {} отключен", username);
    }

    public static void main(String[] args) {
        int port = 8080;
        new ChatServer(port).start();
    }

    public static class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final ChatServer chatServer;
        private BufferedReader reader;
        private PrintWriter writer;
        private String username;

        public ClientHandler(Socket clientSocket, ChatServer chatServer) {
            this.clientSocket = clientSocket;
            this.chatServer = chatServer;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {
                this.reader = reader;
                this.writer = writer;

                // Регистрация пользователя
                writer.println("Введите ваше имя пользователя:");
                username = reader.readLine();

                if (username == null || username.isBlank()) {
                    writer.println("Недопустимое имя пользователя. Подключение завершено.");
                    return;
                }

                synchronized (chatServer) {
                    if (chatServer.clients.containsKey(username)) {
                        writer.println("Имя пользователя уже занято. Подключение завершено.");
                        return;
                    }
                    chatServer.addClient(username, this);
                }

                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.equalsIgnoreCase("/exit")) {
                        break;
                    }
                    chatServer.broadcast(username + ": " + message, this);
                }
            } catch (IOException e) {
                logger.error("Ошибка связи с клиентом:", e);
            } finally {
                chatServer.removeClient(username);
                closeResources();
            }
        }

        public void sendMessage(String message) {
            if (writer != null) {
                writer.println(message);
            }
        }

        private void closeResources() {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                logger.error("Ошибка при закрытии ресурсов:", e);
            }
        }
    }
}
