package wepa.controller;

import java.time.LocalDate;
import java.util.ArrayList;
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
import wepa.repository.ArticleRepository;
import wepa.repository.CategoryRepository;

@Controller
public class ArticleController {

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("*")
    @ResponseBody
    public String hello() {
        return "Hello world!";
    }

    @PostMapping("/news")
    public String addArticle(@RequestParam Map<String, String> requestParams, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate publishdate,
            @RequestParam("file") MultipartFile file,
            @RequestParam Long categoryId) throws Exception {
        Article article = new Article();
        article.setHeadline(requestParams.get("headline"));
        article.setLead(requestParams.get("lead"));
        article.setBodyText(requestParams.get("bodytext"));
        article.setPublishDate(publishdate);
        article.setImage(file.getBytes());
        article.setCategories(new ArrayList<>());
        article.getCategories().add(categoryRepository.getOne(categoryId));
        articleRepository.save(article);
        return "redirect:/news/" + article.getId();
    }

    @GetMapping("/news/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        Article article = articleRepository.getOne(id);
        model.addAttribute("article", articleRepository.getOne(id));
        model.addAttribute("categories", article.getCategories());
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

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "index";
    }
}
