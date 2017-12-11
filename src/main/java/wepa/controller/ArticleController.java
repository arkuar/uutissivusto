package wepa.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import wepa.domain.Article;
import wepa.repository.*;
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

    private static final String CATEGORIES = "categories";
    private static final String ARTICLE = "article";
    private static final String REDIRECT_NEWS = "redirect:/news/";

    @GetMapping("/")
    public String index(Model model) {
        articleService.getLatest(model);
        model.addAttribute(CATEGORIES, categoryRepository.findAll());
        return "index";
    }

    /*
    * Lisää uutisen kutsumalla articleServicen add metodia.
    * Parametreina uutisen tiedot.
     */
    @PostMapping("/news")
    @Transactional
    public String addArticle(@RequestParam Map<String, String> requestParams,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime publishdate,
            @RequestParam("file") MultipartFile file,
            @RequestParam("categoryId") Long[] categoryIds,
            @RequestParam("authorId") Long[] authorIds) throws IOException {
        articleService.add(requestParams, publishdate, file, categoryIds, authorIds);
        return "redirect:/";
    }

    /*
    * Palauttaa uutisen ja hakee articleServicen avulla listaukset
     */
    @GetMapping("/news/{id}")
    @Transactional
    public String getOne(@PathVariable Long id, Model model) {
        Article article = articleRepository.getOne(id);
        article.incrementCount();
        articleRepository.save(article);
        model.addAttribute(ARTICLE, articleRepository.getOne(id));
        model.addAttribute(CATEGORIES, categoryRepository.findAll());
        articleService.getLatestAndPopular(model);
        return ARTICLE;
    }

    @GetMapping(path = "/images/{id}/content", produces = "image/jpg")
    @ResponseBody
    @Transactional
    public byte[] getImage(@PathVariable Long id) {
        if (articleRepository.existsById(id)) {
            Article article = articleRepository.getOne(id);
            return article.getImage();
        }
        return new byte[0];
    }

    /*
    * Palauttaa uutisen lisäämiseen tarkoitetun näkymän.
     */
    @GetMapping("/news/new")
    public String add(Model model) {
        model.addAttribute(CATEGORIES, categoryRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        return "new";
    }

    /*
    * Palauttaa listauksen suosituimmista uutisista.
     */
    @GetMapping("/popular")
    public String mostPopular(Model model) {
        articleService.getMostPopular(model);
        model.addAttribute(CATEGORIES, categoryRepository.findAll());
        return "popular";
    }

    /*
    * Palauttaa uutisen muokkaukseen tarkoitetun näkymän. 
     */
    @GetMapping("/news/{id}/edit")
    @Transactional
    public String editArticle(@PathVariable Long id, Model model) {
        model.addAttribute(ARTICLE, articleRepository.getOne(id));
        model.addAttribute(CATEGORIES, categoryRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        return "edit";
    }

    /*
    * Muokkaa uutista kutsumalla articleServicen edit metodia.
    * Parametreina uutisen tiedot.
     */
    @PostMapping("/news/{id}/edit")
    @Transactional
    public String updateArticle(@PathVariable Long id, @RequestParam Map<String, String> requestParams, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime publishdate,
            @RequestParam("file") MultipartFile file) throws IOException {
        articleService.edit(id, requestParams, publishdate, file);
        return REDIRECT_NEWS + id;
    }

    /*
    * Lisää uutiselle kategorian.
     */
    @PostMapping("/news/{id}/edit/category")
    @Transactional
    public String addCategoryToArticle(@RequestParam Long categoryId, @PathVariable Long id) {
        Article article = articleRepository.getOne(id);
        article.getCategories().add(categoryRepository.getOne(categoryId));
        articleRepository.save(article);
        return REDIRECT_NEWS + id;
    }

    /*
    * Lisää uutiselle kirjoittajan.
     */
    @PostMapping("/news/{id}/edit/author")
    @Transactional
    public String addAuthorToArticle(@RequestParam Long authorId, @PathVariable Long id) {
        Article article = articleRepository.getOne(id);
        article.getWriters().add(authorRepository.getOne(authorId));
        articleRepository.save(article);
        return REDIRECT_NEWS + id;
    }

    /*
    * Poistaa uutisen.
     */
    @PostMapping("/news/{id}/delete")
    @Transactional
    public String deleteArticle(@PathVariable Long id, Model model) {
        articleRepository.deleteById(id);
        articleService.getLatest(model);
        model.addAttribute(CATEGORIES, categoryRepository.findAll());
        return "redirect:/";
    }
}
