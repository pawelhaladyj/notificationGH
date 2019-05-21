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

        if(userDTO.getId()==null){userDTO.setId(Long.MIN_VALUE);}
        userRepository.findById(userDTO.getId()).ifPresent(user -> {throw new UserDuplicateException("Użytkownik o podanym id już istnieje");});
        User user = userConverter.toEntity(userDTO);

        return userConverter.toDTO(userRepository.save(user));
    }

    public void updateUser(UserDTO userDTO){
        User user = userRepository.findById(userDTO.getId()).orElseThrow(()-> new UserNotFoundException());
        userRepository.save(user);
    }

    public void deleteUser(UserDTO userDTO){
        User user = userRepository.findById(userDTO.getId()).orElseThrow(()-> new UserNotFoundException());
        userRepository.delete(user);
    }

    public User loginUser(String login, String password){
        User user = userRepository.findByLogin(login).orElseThrow(()->new UserNotFoundException());
        if(password!=user.getPassword()){
            throw new InvalidPasswordException("Błędne hasło");
        }
        userRepository.save(user);
        return user;
    }


}
