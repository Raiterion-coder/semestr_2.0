package org.example.tetetete.server;

import org.example.tetetete.server.exception.UserAlreadyExistsException;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, String> users = new HashMap<>(); // Словарь для хранения пользователей и их паролей

    // Регистрация нового пользователя
    public void register(String username, String hashedPassword) throws UserAlreadyExistsException {
        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException("Пользователь уже существует: " + username);
        }
        users.put(username, hashedPassword); // Добавляем пользователя в словарь
    }

    // Валидация пользователя при логине
    public boolean validate(String username, String hashedPassword) {
        return hashedPassword.equals(users.get(username)); // Проверяем, совпадает ли пароль с сохраненным
    }
}
