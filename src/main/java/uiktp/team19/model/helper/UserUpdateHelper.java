package uiktp.team19.model.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateHelper {
    private UUID userId;
    private String username;
    private String email;
    private String oldPassword;
    private String newPassword;
}
