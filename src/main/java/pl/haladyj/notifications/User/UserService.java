package pl.haladyj.notifications.User;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserConverter userConverter;

    public UserService(UserConverter userConverter) {
        this.userConverter = userConverter;
    }
}
