package hieukientung.booktour.repository;

import hieukientung.booktour.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Email findByEmail(String emailAddress);
    List<Email> findAll();
}