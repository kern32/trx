package ua.in.javac.exceptions;

public class CredentialNotFoundException extends RuntimeException {
    private String username;

    public CredentialNotFoundException(String username) {
        super("Credential with username [" + username + "] not found!");
        this.username = username;
    }
}

