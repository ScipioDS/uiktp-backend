package uiktp.team19.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uiktp.team19.model.auth.User;
import uiktp.team19.model.dto.UserReducedDTO;

@Service
@RequiredArgsConstructor
public class UserConverterService {
    public UserReducedDTO getUserReducedDTO(User user) {
        return new UserReducedDTO(
                user.getId(),
                user.getEmail(),
                user.getUsername()
        );
    }
}
