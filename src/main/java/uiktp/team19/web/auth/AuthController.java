package uiktp.team19.web.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uiktp.team19.model.auth.LoginCreds;
import uiktp.team19.model.auth.Role;
import uiktp.team19.model.auth.User;
import uiktp.team19.repository.auth.RoleRepo;
import uiktp.team19.repository.auth.UserRepo;
import uiktp.team19.util.auth.JWTUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepo userRepo;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    @PostMapping("/register")
    public Map<String, Object> registerHandler(@RequestBody User user) {
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);

        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(new HashSet<>(Collections.singleton(userRole)));

        user = userRepo.save(user);
        String token = jwtUtil.generateToken(user.getUsername(), List.of("ROLE_USER"));
        return Collections.singletonMap("jwt-token", token);
    }

    @PostMapping("/login")
    public Map<String,Object> loginHandler(
            @RequestBody LoginCreds body
    ){
        try{
            UsernamePasswordAuthenticationToken authInputToken =
                    new UsernamePasswordAuthenticationToken(body.getUsername(), body.getPassword());
//            authenticationManager.authenticate(authInputToken);
            Authentication auth = authenticationManager.authenticate(authInputToken);

            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            String token = jwtUtil.generateToken(body.getUsername(), roles);
            return Collections.singletonMap("jwt-token",token);
        } catch(AuthenticationException authExc){
            throw new RuntimeException("Invalid username/password.");
        }

    }

}
