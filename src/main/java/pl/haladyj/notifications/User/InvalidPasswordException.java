package pl.haladyj.notifications.User;

import org.springframework.remoting.RemoteTimeoutException;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException(String message) {
        super(message);
    }
}
