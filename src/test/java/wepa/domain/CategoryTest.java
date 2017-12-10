package wepa.domain;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CategoryTest {

    public CategoryTest() {
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

    @Test
    public void testGetName() {
        Category instance = new Category("asd");
        String expResult = "asd";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testGetArticles() {
        Category instance = new Category();
        List<Article> articles = new ArrayList<>();
        instance.setArticles(articles);
        List<Article> result = instance.getArticles();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetName() {
        String name = "Abc";
        Category instance = new Category();
        instance.setName(name);
        assertEquals(name, instance.getName());
    }
}
