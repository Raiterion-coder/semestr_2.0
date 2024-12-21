package org.example.tetetete.server;

import org.example.tetetete.server.exception.UserAlreadyExistsException;


import java.util.HashMap;
import java.util.Map;

public class UserService {
    private final Map<String, String> users = new HashMap<>();

    public void register(String username, String hashedPassword) throws UserAlreadyExistsException {
        if (users.containsKey(username)) {
            throw new UserAlreadyExistsException("Пользователь уже существует: " + username);
        }
        users.put(username, hashedPassword);
    }

    public boolean validate(String username, String hashedPassword) {
        return hashedPassword.equals(users.get(username));
    }
}
