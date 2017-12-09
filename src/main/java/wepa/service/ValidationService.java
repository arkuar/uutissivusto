package wepa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wepa.domain.Article;
import wepa.repository.ArticleRepository;
import wepa.repository.AuthorRepository;
import wepa.repository.CategoryRepository;

@Service
public class ValidationService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public boolean validateArticle(Article article) {
        if (article.getHeadline().isEmpty() || article.getLead().isEmpty() || article.getBodyText().isEmpty()
                || article.getPublishDate() == null || article.getImage() == null) {
            return false;
        }
        return true;
    }
}
