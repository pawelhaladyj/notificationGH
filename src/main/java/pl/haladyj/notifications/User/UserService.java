package pl.haladyj.notifications.User;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<UserDTO> findAllUsers(){
        List<UserDTO> userDTOs = new ArrayList<>();

        userRepository.findAll().forEach(user -> userDTOs.add(userConverter.toDTO(user)));

        return userDTOs;
    }

    public UserDTO createUser(UserDTO userDTO){

        userRepository.findById(userDTO.getId()).ifPresent(user -> new DuplicateUserException());
        User user = userConverter.toEntity(userDTO);

        return userConverter.toDTO(userRepository.save(user));
    }
}
