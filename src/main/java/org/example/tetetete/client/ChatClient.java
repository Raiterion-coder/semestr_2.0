package org.example.tetetete.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ChatClient extends Application {
    private static final Logger logger = LoggerFactory.getLogger(ChatClient.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Загружаем FXML файл и получаем корневой элемент
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tetetete/login.fxml"));
        Parent root = loader.load();

        // Получаем контроллер чата
        ChatController chatController = loader.getController();

        // Создаем ClientSocketHandler и передаем ему контроллер чата
        ClientSocketHandler socketHandler = new ClientSocketHandler("localhost", 8080, chatController);
        chatController.setSocketHandler(socketHandler);

        // Настраиваем и отображаем основное окно приложения
        primaryStage.setTitle("Chat Client");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        logger.info("Chat client started");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
