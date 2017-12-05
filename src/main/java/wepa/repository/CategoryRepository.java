package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByNameIgnoreCase(String name);
}
