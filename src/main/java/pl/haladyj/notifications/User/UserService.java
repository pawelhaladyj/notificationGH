package pl.haladyj.notifications.User;

import com.google.gson.Gson;
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
        return userConverter.toDTO(userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Nie odnaleziono użytkowniaka o id::%d", id))));
    }

    public List<UserDTO> findAllUsers(){
        List<UserDTO> userDTOs = new ArrayList<>();

        userRepository.findAll().forEach(user -> userDTOs.add(userConverter.toDTO(user)));

        return userDTOs;
    }

    public UserDTO createUser(UserDTO userDTO){

        if(userDTO.getId()==null){userDTO.setId(Long.MIN_VALUE);}
        userRepository.findById(userDTO.getId()).ifPresent(user ->
        {throw new UserDuplicateException("Użytkownik o podanym id już istnieje");});
        User user = userConverter.toEntity(userDTO);

        return userConverter.toDTO(userRepository.save(user));
    }

    public UserDTO updateUser (UserDTO userDTO){
        User user = userRepository.findById(userDTO.getId()).orElseThrow(()->
                new UserNotFoundException(String.format("nie odnaleziono użytkownika o id::%d",userDTO.getId())));

        user = userConverter.toEntity(userDTO);

        return userConverter.toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(()->
                new UserNotFoundException(String.format("Nie odnaleziono użytkowniaka o id::%d", id)));
        userRepository.deleteById(id);
    }

    public User loginUser(String login, String password){
        User user = userRepository.findByLogin(login).orElseThrow(()->
                new UserNotFoundException(String.format("Nie odnaleziono użytkowniaka o login::%s", login)));
        if(password!=user.getPassword()){
            throw new InvalidPasswordException("Błędne hasło");
        }
        userRepository.save(user);
        return user;
    }


}
