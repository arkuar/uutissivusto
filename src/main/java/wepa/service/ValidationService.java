package wepa.service;

import org.springframework.stereotype.Service;
import wepa.domain.Article;

@Service
public class ValidationService {

    /*
    * Tarkistaa ettei mikään annetuista tiedoista ole tyhjä.
    */
    public boolean validateArticle(Article article) {
        return !(article.getHeadline().isEmpty() || article.getLead().isEmpty() || article.getBodyText().isEmpty()
                || article.getPublishDate() == null || article.getImage() == null);
    }
}
