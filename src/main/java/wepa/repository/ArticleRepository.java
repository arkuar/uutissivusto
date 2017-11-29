package wepa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wepa.domain.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {

}
