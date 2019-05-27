package pl.haladyj.notifications.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String password;
    private String login;
}
