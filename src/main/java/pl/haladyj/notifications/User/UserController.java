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
@RequestMapping("/user")
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
    public User createUser (@Valid @RequestBody UserDTO userDTO){
        return userConverter.toEntity(userService.createUser(userDTO));
    }

 /*   @PutMapping(value = "/update",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO)
            throws UserNotFoundException{

        userService.updateUser(userDTO);

        return ResponseEntity.noContent().build();
    }*/


    @PutMapping(value = "/update",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update (@RequestBody UserDTO userDTO) {

        Optional<User> user = userRepository.findById(userDTO.getId());

        if (!user.isPresent())
            return ResponseEntity.notFound().build();

        userService.updateUser(userDTO);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(userDTO);
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
