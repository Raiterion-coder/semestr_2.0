package org.example.tetetete.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @FXML
    private TextField usernameField; // Поле для ввода имени пользователя

    @FXML
    private PasswordField passwordField; // Поле для ввода пароля

    @FXML
    private Button loginButton; // Кнопка для входа

    private Stage primaryStage; // Основное окно приложения

    @FXML
    public void initialize() {
        // Устанавливаем обработчик событий для кнопки входа
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (authenticate(username, password)) {
                openChatWindow(); // Открываем окно чата после успешного входа
            } else {
                logger.warn("Authentication failed for user: {}", username); // Логируем неудачную попытку входа
            }
        });
    }

    // Метод для аутентификации пользователя (пример)
    private boolean authenticate(String username, String password) {
        // Здесь можно реализовать логику аутентификации
        return !username.isEmpty() && !password.isEmpty();
    }

    // Метод для открытия окна чата
    private void openChatWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tetetete/chat.fxml"));
            Parent root = loader.load();
            ChatController chatController = loader.getController();

            // Создаем ClientSocketHandler и передаем ему контроллер чата
            ClientSocketHandler socketHandler = new ClientSocketHandler("localhost", 8080, chatController);
            chatController.setSocketHandler(socketHandler);

            Stage chatStage = new Stage();
            chatStage.setTitle("Chat Client");
            chatStage.setScene(new Scene(root));
            chatStage.show();

            // Закрываем окно логина
            primaryStage.close();
        } catch (IOException e) {
            logger.error("Error while opening chat window", e); // Логируем ошибку при открытии окна чата
        }
    }

    // Метод для установки основного окна приложения
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
