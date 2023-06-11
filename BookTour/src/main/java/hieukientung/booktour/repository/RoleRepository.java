package hieukientung.booktour.repository;

import hieukientung.booktour.model.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(String name);
}
