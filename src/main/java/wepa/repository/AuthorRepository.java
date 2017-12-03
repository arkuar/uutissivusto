package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
