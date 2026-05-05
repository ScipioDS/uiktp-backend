package uiktp.team19.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import uiktp.team19.model.auth.Role;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
