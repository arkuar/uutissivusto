package wepa.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/")
    public String index(Model model) {
        articleService.getLatest(model);
        model.addAttribute("categories", categoryRepository.findAll());
        return "index";
    }

    @PostMapping("/news")
    public String addArticle(@RequestParam Map<String, String> requestParams,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime publishdate,
            @RequestParam("file") MultipartFile file,
            @RequestParam("categoryId") Long[] categoryIds,
            @RequestParam("authorId") Long[] authorIds) throws Exception {
        articleService.add(requestParams, publishdate, file, categoryIds, authorIds);
        return "redirect:/";
    }

    @GetMapping("/news/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        Article article = articleRepository.getOne(id);
        article.incrementCount();
        articleRepository.save(article);
        model.addAttribute("article", articleRepository.getOne(id));
        model.addAttribute("categories", categoryRepository.findAll());
        articleService.getLatestAndPopular(model);
        return "article";
    }

    @GetMapping(path = "/images/{id}/content", produces = "image/jpg")
    @ResponseBody
    public byte[] getImage(@PathVariable Long id) {
        if (articleRepository.existsById(id)) {
            Article article = articleRepository.getOne(id);
            return article.getImage();
        }
        return null;
    }

    @GetMapping("/news/new")
    public String add(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        return "new";
    }

    @GetMapping("/popular")
    public String mostPopular(Model model) {
        articleService.getMostPopular(model);
        model.addAttribute("categories", categoryRepository.findAll());
        return "popular";
    }

    @GetMapping("/news/{id}/edit")
    public String editArticle(@PathVariable Long id, Model model) {
        Article article = articleRepository.getOne(id);
        model.addAttribute("article", article);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("authors", authorRepository.findAll());
        return "edit";
    }

    @PostMapping("/news/{id}/edit")
    public String updateArticle(@PathVariable Long id, @RequestParam Map<String, String> requestParams, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime publishdate,
            @RequestParam("file") MultipartFile file) throws IOException {
        articleService.edit(id, requestParams, publishdate, file);
        return "redirect:/news/" + id;
    }

    @PostMapping("/news/{id}/edit/category")
    public String addCategoryToArticle(@RequestParam Long categoryId, @PathVariable Long id) {
        Article article = articleRepository.getOne(id);
        article.getCategories().add(categoryRepository.getOne(categoryId));
        articleRepository.save(article);
        return "redirect:/news/" + id;
    }

    @PostMapping("/news/{id}/edit/author")
    public String addAuthorToArticle(@RequestParam Long authorId, @PathVariable Long id) {
        Article article = articleRepository.getOne(id);
        article.getWriters().add(authorRepository.getOne(authorId));
        articleRepository.save(article);
        return "redirect:/news/" + id;
    }

    @PostMapping("/news/{id}/delete")
    public String deleteArticle(@PathVariable Long id, Model model) {
        articleRepository.deleteById(id);
        articleService.getLatest(model);
        model.addAttribute("categories", categoryRepository.findAll());
        return "redirect:/";
    }
}
