package wepa.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
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
        article.setCount(0);
        article.getCategories().add(categoryRepository.getOne(categoryId));
        article.getWriters().add(authorRepository.getOne(authorId));
        articleRepository.save(article);
    }

    public void getMostPopular(Model model) {
        PageRequest req = PageRequest.of(0, 10, Sort.Direction.DESC, "count");
        model.addAttribute("popular", articleRepository.findAll(req));
    }

    public void getLatest(Model model) {
        PageRequest req = PageRequest.of(0, 5, Sort.Direction.DESC, "publishDate");
        model.addAttribute("latest", articleRepository.findAll(req));
    }

    public void getLatestAndPopular(Model model) {
        getLatest(model);
        getMostPopular(model);
    }
}
