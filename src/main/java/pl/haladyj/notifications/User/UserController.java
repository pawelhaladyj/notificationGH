package pl.haladyj.notifications.User;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter) {
        this.userService = userService;
        this.userConverter = userConverter;
    }

    @GetMapping
    public List<UserDTO> findAll(){
        return userService.findAllUsers();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser (@RequestParam("map") String createdUser){
        Gson gson =new Gson();
        User user = gson.fromJson(createdUser, User.class);

        userService.createUser(userConverter.toDTO(user));

        return ResponseEntity.ok().body(userConverter.toDTO(user));
    }

    @PutMapping(value = "/update",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateUser(@RequestParam("map") String createdUser){
        Gson gson =new Gson();
        User user = gson.fromJson(createdUser, User.class);

        userService.updateUser(userConverter.toDTO(user));

        return ResponseEntity.ok().body(userConverter.toDTO(user));
    }


    @DeleteMapping(value = "/delete",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteUser(@RequestParam("map") String createdUser){
        Gson gson =new Gson();
        User user = gson.fromJson(createdUser, User.class);

        userService.deleteUser(userConverter.toDTO(user));

        return ResponseEntity.ok().body(userConverter.toDTO(user));
    }


}
