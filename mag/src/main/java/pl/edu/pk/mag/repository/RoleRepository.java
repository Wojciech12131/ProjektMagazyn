package pl.edu.pk.mag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pk.mag.repository.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
