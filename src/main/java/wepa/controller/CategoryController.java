package wepa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wepa.domain.Category;
import wepa.repository.CategoryRepository;

@Controller
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/{name}")
    @Transactional
    public String getNewsInCategory(@PathVariable String name, Model model) {
        Category category = categoryRepository.findByNameIgnoreCase(name);
        model.addAttribute("articles", category.getArticles());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("category", category);
        return "category";
    }
}
