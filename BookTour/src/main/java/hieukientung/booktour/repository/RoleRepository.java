package hieukientung.booktour.repository;

import hieukientung.booktour.model.Role;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository {
    Optional<Role> findByName(String name);
}
