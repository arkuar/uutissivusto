package wepa.controller;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import wepa.domain.Article;
import wepa.domain.Author;
import wepa.domain.Category;
import wepa.repository.ArticleRepository;
import wepa.repository.AuthorRepository;
import wepa.repository.CategoryRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ArticleControllerTest {

    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    @Transactional
    public void statusOK() throws Exception {
        Long id = articleRepository.save(new Article("Otsikko", "Ingressi", "0".getBytes(), "teksti", LocalDateTime.MIN)).getId();
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/news/" + id))
                .andExpect(status().isOk());
        mockMvc.perform(get("/images/" + id + "/content"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/news/new"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/news/" + id + "/edit"))
                .andExpect(status().isOk());
    }

    @Test
    public void responseContainsLatestAndCategories() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(model().attributeExists("latest"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @Transactional
    public void addArticleWorks() throws Exception {
        Author author = new Author("Author");
        authorRepository.save(author);
        Category category = new Category("Category");
        categoryRepository.save(category);
        MockMultipartFile file = new MockMultipartFile("file", "name", null, "a".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/news")
                .file(file)
                .param("headline", "Otsikko")
                .param("lead", "ingressi")
                .param("bodytext", "teksti")
                .param("publishdate", LocalDateTime.now().toString())
                .param("categoryId", category.getId().toString())
                .param("authorId", author.getId().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void responseContainsMostPopular() throws Exception {
        mockMvc.perform(get("/popular"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("popular"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @Transactional
    public void addArticleAndItIsDisplayed() throws Exception {
        Long id = articleRepository.save(new Article("Otsikko", null, null, null, LocalDateTime.MIN)).getId();
        MvcResult res = mockMvc.perform(get("/"))
                .andReturn();
        Page page = (Page) res.getModelAndView().getModel().get("latest");
        Collection<Article> articles = page.getContent();
        assertTrue(articles.stream().anyMatch(e -> e.getHeadline().equals("Otsikko")));

        res = mockMvc.perform(get("/news/" + id))
                .andReturn();
        Article article = (Article) res.getModelAndView().getModel().get("article");
        assertTrue(article.getHeadline().equals("Otsikko"));
    }

    @Test
    @Transactional
    public void latestAndMostPopularShouldBeListed() throws Exception {
        Long id = articleRepository.save(new Article("Otsikko", "Ingressi", "0".getBytes(), "Teksti", LocalDateTime.MIN)).getId();
        MvcResult res = mockMvc.perform(get("/news/" + id))
                .andReturn();
        Page page = (Page) res.getModelAndView().getModel().get("latest");
        Collection<Article> articles = page.getContent();
        assertTrue(articles.stream().allMatch(e -> e.getHeadline().equals("Otsikko")));

        page = (Page) res.getModelAndView().getModel().get("popular");
        articles = page.getContent();
        assertTrue(articles.stream().allMatch(e -> e.getHeadline().equals("Otsikko")));
    }

    @Test
    @Transactional
    public void addCategoryToArticleShouldWork() throws Exception {
        Long id = articleRepository.save(new Article()).getId();
        Long categoryId = categoryRepository.save(new Category("Asd")).getId();
        mockMvc.perform(post("/news/" + id + "/edit/category")
                .param("categoryId", categoryId.toString()))
                .andExpect(status().is3xxRedirection());

        Article article = articleRepository.getOne(id);
        assertTrue(article.getCategories().size() == 1);
    }

    @Test
    @Transactional
    public void addAuthorToArticleShouldWork() throws Exception {
        Long id = articleRepository.save(new Article()).getId();
        Long authorId = authorRepository.save(new Author("Author")).getId();
        mockMvc.perform(post("/news/" + id + "/edit/author")
                .param("authorId", authorId.toString()))
                .andExpect(status().is3xxRedirection());

        Article article = articleRepository.getOne(id);
        assertTrue(article.getWriters().size() == 1);
    }

    @Test
    @Transactional
    public void deleteArticleShouldWork() throws Exception {
        Long id = articleRepository.save(new Article()).getId();
        mockMvc.perform(post("/news/" + id + "/delete"))
                .andExpect(status().is3xxRedirection());
        List<Article> articles = articleRepository.findAll();
        assertTrue(articles.isEmpty());
    }

}
