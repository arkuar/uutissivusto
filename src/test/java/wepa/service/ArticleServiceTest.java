package wepa.service;

import com.google.common.collect.HashBiMap;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import wepa.domain.Article;
import wepa.repository.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ArticleServiceTest {

    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private ArticleService articleService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }
    
    @Test
    @Transactional
    public void initShouldWork(){
        articleService.init();
        assertTrue(!categoryRepository.findAll().isEmpty());
        assertTrue(!authorRepository.findAll().isEmpty());
    }
    
    @Test
    @Transactional
    public void editShouldWork() throws IOException{
        Long id = articleRepository.save(new Article("Asd", "Asd", "0".getBytes(), "text", LocalDateTime.MIN)).getId();
        MockMultipartFile file = new MockMultipartFile("file", "1".getBytes());
        Map<String, String> params = new HashMap<>();
        params.put("headline", "This is headline");
        params.put("lead", "This is lead");
        params.put("bodytext", "This is body");
        LocalDateTime time = LocalDateTime.MAX;
        
        articleService.edit(id, params, time, file);
        
        assertTrue(articleRepository.getOne(id).getHeadline().equals("This is headline"));
    }
}
