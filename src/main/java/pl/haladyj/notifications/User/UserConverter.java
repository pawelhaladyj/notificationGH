package pl.haladyj.notifications.User;

import org.springframework.stereotype.Component;
import pl.haladyj.notifications.Utils.Converter;

@Component
public class UserConverter implements Converter<User, UserDTO> {

    @Override
    public User toEntity(UserDTO userDTO) {
        User user = new User();

        user.setId(userDTO.getId());
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());
        user.setLogin(userDTO.getLogin());


        return user;
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setPassword(user.getPassword());
        userDTO.setLogin(user.getLogin());


        return userDTO;
    }
}
