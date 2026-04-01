package uiktp.team19.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uiktp.team19.model.auth.Role;
import uiktp.team19.repository.auth.RoleRepo;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepo roleRepo;

    public Role save(String name) {
        Role role = new Role();
        role.setName(name);
        return roleRepo.save(role);
    }
}
