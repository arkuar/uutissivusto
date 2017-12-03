package wepa.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wepa.domain.Article;
import wepa.repository.ArticleRepository;
import wepa.repository.AuthorRepository;
import wepa.repository.CategoryRepository;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public void add(Map<String, String> params, LocalDate publishdate, MultipartFile file,
            Long categoryId, Long authorId) throws IOException {
Article article = new Article();
        article.setHeadline(params.get("headline"));
        article.setLead(params.get("lead"));
        article.setBodyText(params.get("bodytext"));
        article.setPublishDate(publishdate);
        article.setImage(file.getBytes());
        article.getCategories().add(categoryRepository.getOne(categoryId));
        article.getWriters().add(authorRepository.getOne(authorId));
        articleRepository.save(article);
    }
}
