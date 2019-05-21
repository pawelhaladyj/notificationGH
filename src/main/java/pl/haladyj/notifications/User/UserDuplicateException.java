package pl.haladyj.notifications.User;

public class UserDuplicateException extends RuntimeException {
    public UserDuplicateException(String message){
        super(message);
    }
}
