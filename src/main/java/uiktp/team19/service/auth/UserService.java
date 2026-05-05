package uiktp.team19.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uiktp.team19.model.auth.User;
import uiktp.team19.model.helper.UserUpdateHelper;
import uiktp.team19.repository.auth.UserRepo;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final MyUserDetailService myUserDetailService;
    private final PasswordEncoder passwordEncoder;

    public User updateUser(UserUpdateHelper userUpdateHelper) {
        User user = loadJustUserById(userUpdateHelper.getUserId());

        if (userUpdateHelper.getEmail() != null) {
            user.setEmail(userUpdateHelper.getEmail());
        }

        if (userUpdateHelper.getOldPassword() != null && userUpdateHelper.getNewPassword() != null) {
            if  (passwordEncoder.matches(userUpdateHelper.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(userUpdateHelper.getNewPassword()));
            }
        }

        if (userUpdateHelper.getUsername() != null) {
            user.setUsername(userUpdateHelper.getUsername());
        }

        return userRepo.save(user);
    }

    public User loadJustUserById(UUID id) {
        Optional<User> userRes = userRepo.findById(id);

        if (userRes.isEmpty()) {
            throw new UsernameNotFoundException("No user found with that id");
        }

        return userRes.get();
    }
}
