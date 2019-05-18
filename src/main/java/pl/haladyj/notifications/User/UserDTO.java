package pl.haladyj.notifications.User;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String password;
    private String login;
    private boolean Admin;
}
