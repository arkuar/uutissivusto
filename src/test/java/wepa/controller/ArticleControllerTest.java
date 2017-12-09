package wepa.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void statusOK() throws Exception {
        mockMvc.perform(get("/"))
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
        categoryRepository.save(new Category(1L, "Kategoria", new ArrayList<>()));
        authorRepository.save(new Author(2L, "Kirjoittaja", new ArrayList<>()));
        MockMultipartFile file = new MockMultipartFile("file", "name", null, "a".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/news")
                .file(file)
                .param("headline", "Otsikko")
                .param("lead", "ingressi")
                .param("bodytext", "teksti")
                .param("publishdate", LocalDateTime.now().toString())
                .param("categoryId", "1")
                .param("authorId", "2"))
                .andExpect(status().is3xxRedirection());

        Article article = articleRepository.getOne(articleRepository.count() + 1);
        Assert.assertTrue(article.getHeadline().equals("Otsikko"));
    }

    @Test
    public void responseContainsMostPopular() throws Exception {
        mockMvc.perform(get("/popular"))
                .andExpect(model().attributeExists("popular"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @Transactional
    public void addArticleAndItIsDisplayed() throws Exception {
        categoryRepository.save(new Category(1L, "Kategoria", new ArrayList<>()));
        authorRepository.save(new Author(2L, "Kirjoittaja", new ArrayList<>()));
        MockMultipartFile file = new MockMultipartFile("file", "name", null, "bar".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/news")
                .file(file)
                .param("headline", "Otsikko")
                .param("lead", "ingressi")
                .param("bodytext", "teksti")
                .param("publishdate", LocalDateTime.now().toString())
                .param("categoryId", "1")
                .param("authorId", "1"))
                .andExpect(status().is3xxRedirection());

        MvcResult res = mockMvc.perform(get("/"))
                .andReturn();
        Page page = (Page) res.getModelAndView().getModel().get("latest");
        Collection<Article> articles = page.getContent();
        assertTrue(articles.stream().anyMatch(e -> e.getHeadline().equals("Otsikko")));
    }

}
