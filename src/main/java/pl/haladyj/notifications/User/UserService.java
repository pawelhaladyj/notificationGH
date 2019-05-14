package pl.haladyj.notifications.User;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;

    public UserService(UserConverter userConverter, UserRepository userRepository) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
    }

    public UserDTO findById(Long id){
        return userConverter.toDTO(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException()));
    }
}
