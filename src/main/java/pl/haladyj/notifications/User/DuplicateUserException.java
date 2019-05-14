package pl.haladyj.notifications.User;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(){
        super("User already exists");
    }
}
