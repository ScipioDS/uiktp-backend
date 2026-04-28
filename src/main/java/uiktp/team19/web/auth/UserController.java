package uiktp.team19.web.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import uiktp.team19.model.auth.User;
import uiktp.team19.model.dto.UserReducedDTO;
import uiktp.team19.model.helper.UserUpdateHelper;
import uiktp.team19.repository.auth.UserRepo;
import uiktp.team19.service.auth.UserConverterService;
import uiktp.team19.service.auth.UserService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserConverterService userConverterService;

    @GetMapping("/info")
    public UserReducedDTO getUserDetails() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String userName = userDetails.getUsername();
        return this.userConverterService.getUserReducedDTO(userRepo.findByUsername(userName).get());
    }

    @GetMapping("info/{id}")
    public UserReducedDTO getUser(@PathVariable UUID id) {
        return this.userConverterService.getUserReducedDTO(this.userService.loadJustUserById(id));
    }

    @PostMapping("/update")
    public UserReducedDTO updateUser(@RequestBody UserUpdateHelper userUpdateHelper) {
        return this.userConverterService.getUserReducedDTO(this.userService.updateUser(userUpdateHelper));
    }
}
