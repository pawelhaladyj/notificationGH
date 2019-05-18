package pl.haladyj.notifications.User;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final HttpSession session;
    private final HttpServletRequest request;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter, HttpSession session, HttpServletRequest request) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.session = session;
        this.request = request;
    }

    @GetMapping
    public List<UserDTO> findAll(){
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser (@RequestBody @Validated UserDTO userDto){
        userDto = userService.createUser(userDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(userDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(userDto);

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

   @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginUser(@RequestParam("login") String login, @RequestParam("password") String password){

        try{
            User user = userService.loginUser(login, password);

            request.getSession(true);
            session.setAttribute("USER",true);
            session.setAttribute("ADMIN", user.isAdmin());

            return ResponseEntity.ok("{\"message\": \"Logged on\", \"success\" : true}");
        } catch (InvalidPasswordException | UserNotFoundException e){
            try {
                session.invalidate();
            } catch (IllegalStateException ex) {
                // ignore
            }
            return ResponseEntity.ok("{\"message\": \"" + e.getMessage() + "\", \"success\" : false}");
        }

    }
}
