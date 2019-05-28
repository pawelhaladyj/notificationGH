package pl.haladyj.notifications.User;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final HttpSession session;
    private final HttpServletRequest request;

    @Autowired
    public UserController(UserService userService, UserConverter userConverter, UserRepository userRepository, HttpSession session, HttpServletRequest request) {
        this.userService = userService;
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.session = session;
        this.request = request;
    }

    @GetMapping
    public List<UserDTO> findAll(){
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) throws UserNotFoundException{
        return ResponseEntity.ok().body(userService.findById(id));
    }

    @PostMapping(value = "/create", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUser (@Valid @RequestBody UserDTO userDTO){
        return userService.createUser(userDTO);
    }


    @PutMapping(value = "/{id}",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> update (@RequestBody UserDTO userDTO, @PathVariable Long id) {

        userService.updateUser(userDTO, id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userDTO);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }

   @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loginUser(@RequestParam("login") String login, @RequestParam("password") String password){

        try{
            User user = userService.loginUser(login, password);

            request.getSession(true);
            session.setAttribute("USER",true);

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
