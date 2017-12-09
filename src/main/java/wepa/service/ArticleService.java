package wepa.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import wepa.domain.Article;
import wepa.domain.Author;
import wepa.domain.Category;
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
    @Autowired
    private ValidationService validationService;
    
    @PostConstruct
    public void init(){
        if(categoryRepository.findAll().isEmpty() && authorRepository.findAll().isEmpty()){
            categoryRepository.save(new Category("World"));
            categoryRepository.save(new Category("Sports"));
            authorRepository.save(new Author("James Writer"));
            authorRepository.save(new Author("Hunter S. Thompson"));
        }
    }

    @Transactional
    public void add(Map<String, String> params, LocalDateTime publishdate, MultipartFile file,
            Long[] categoryIds, Long[] authorIds) throws IOException {
        Article article = new Article();
        article.setHeadline(params.get("headline"));
        article.setLead(params.get("lead"));
        article.setBodyText(params.get("bodytext"));
        article.setPublishDate(publishdate);
        article.setImage(file.getBytes());
        for (Long i : categoryIds) {
            article.getCategories().add(categoryRepository.getOne(i));
        }
        for (Long i : authorIds) {
            article.getWriters().add(authorRepository.getOne(i));
        }
        if (validationService.validateArticle(article)) {
            articleRepository.save(article);
        }
    }

    public void edit(Long id, Map<String, String> params, LocalDateTime publishdate, MultipartFile file) throws IOException {
        Article article = articleRepository.getOne(id);
        article.setHeadline(params.get("headline"));
        article.setLead(params.get("lead"));
        article.setBodyText(params.get("bodytext"));
        article.setPublishDate(publishdate);
        if (!file.isEmpty()) {
            article.setImage(file.getBytes());
        }
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
