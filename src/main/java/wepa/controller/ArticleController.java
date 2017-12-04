package wepa.controller;

import java.time.LocalDate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wepa.domain.Article;
import wepa.repository.ArticleRepository;
import wepa.repository.AuthorRepository;
import wepa.repository.CategoryRepository;
import wepa.service.ArticleService;

@Controller
public class ArticleController {

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleService articleService;

    @GetMapping("/")
    public String index(Model model) {
        PageRequest req = PageRequest.of(0, 5, Sort.Direction.DESC, "publishDate");
        model.addAttribute("articles", articleRepository.findAll(req));
        model.addAttribute("categories", categoryRepository.findAll());
        return "index";
    }

    @PostMapping("/news")
    public String addArticle(@RequestParam Map<String, String> requestParams, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishdate,
            @RequestParam("file") MultipartFile file, @RequestParam Long categoryId,
            @RequestParam Long authorId) throws Exception {
        articleService.add(requestParams, publishdate, file, categoryId, authorId);
        return "redirect:/";
    }

    @GetMapping("/news/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        Article article = articleRepository.getOne(id);
        model.addAttribute("article", articleRepository.getOne(id));
        model.addAttribute("categories", article.getCategories());
        model.addAttribute("authors", article.getWriters());
        return "article";
    }

    @GetMapping(path = "/images/{id}/content", produces = "image/jpg")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        if (articleRepository.existsById(id)) {
            Article article = articleRepository.getOne(id);
            return article.getImage();
        }
        return null;
    }

    @GetMapping("/news/add")
    public String add(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        return "new";
    }
}
