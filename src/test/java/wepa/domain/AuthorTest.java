package wepa.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuthorTest {

    public AuthorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class Author.
     */
    @Test
    public void testGetName() {
        Author instance = new Author("Abc");
        String expResult = "Abc";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getArticles method, of class Author.
     */
    @Test
    public void testGetArticles() {
        Author instance = new Author();
        List<Article> expResult = null;
        List<Article> result = instance.getArticles();
        assertEquals(expResult, result);
    }

    /**
     * Test of setName method, of class Author.
     */
    @Test
    public void testSetName() {
        String name = "Abc";
        Author instance = new Author();
        instance.setName(name);
        assertEquals(name, instance.getName());
    }

    /**
     * Test of setArticles method, of class Author.
     */
    @Test
    public void testSetArticles() {
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("Abc", "Def", "0".getBytes(), "text", LocalDateTime.MIN));
        Author instance = new Author();
        instance.setArticles(articles);
        assertTrue(instance.getArticles().size() == 1);
    }
    
    @Test
    public void testAllArgs(){
        Author author = new Author("name", new ArrayList<>());
        assertEquals("name", author.getName());
        assertTrue(author.getArticles().isEmpty());
    }
}