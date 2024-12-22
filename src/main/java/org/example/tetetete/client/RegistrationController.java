package org.example.tetetete.client;
import javafx.scene.control.Button;


import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @FXML
    private TextField usernameField; // Поле для ввода имени пользователя

    @FXML
    private PasswordField passwordField; // Поле для ввода пароля

    @FXML
    private Button registerButton; // Кнопка для регистрации

    private ClientSocketHandler socketHandler; // Обработчик сокета для связи с сервером

    @FXML
    public void initialize() {
        // Устанавливаем обработчик событий для кнопки регистрации
        registerButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
                socketHandler.sendRegistrationRequest(username, password);
            } else {
                showError("Please enter both username and password.");
            }
        });
    }

    // Метод для установки обработчика сокета
    public void setSocketHandler(ClientSocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    // Метод для отображения сообщений об ошибке
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
