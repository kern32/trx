package ua.in.javac.exceptions;

public class UserNotFoundException extends RuntimeException {
    private String username;

    public UserNotFoundException(String username) {
        super("User with username [" + username + "] not found!");
        this.username = username;
    }
}
