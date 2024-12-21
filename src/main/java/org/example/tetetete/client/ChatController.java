package org.example.tetetete.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @FXML
    private TextArea chatArea; // Область для отображения сообщений чата

    @FXML
    private TextField messageField; // Поле для ввода сообщений

    @FXML
    private Button sendButton; // Кнопка для отправки сообщений

    private ClientSocketHandler socketHandler; // Обработчик сокета для взаимодействия с сервером

    @FXML
    public void initialize() {
        // Устанавливаем обработчик событий для кнопки отправки сообщений
        sendButton.setOnAction(event -> {
            String message = messageField.getText();
            if (message != null && !message.trim().isEmpty()) {
                socketHandler.sendMessage(message); // Отправляем сообщение на сервер
                messageField.clear(); // Очищаем поле ввода
            }
        });
    }

    // Метод для установки обработчика сокета
    public void setSocketHandler(ClientSocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    // Метод для добавления сообщения в область чата
    public void appendMessage(String message) {
        chatArea.appendText(message + "\n");
    }
}
