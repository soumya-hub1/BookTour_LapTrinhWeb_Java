package hieukientung.booktour.repository;

import hieukientung.booktour.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Email findByEmailAddress(String emailAddress);
}