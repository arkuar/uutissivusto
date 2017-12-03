package wepa.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Article extends AbstractPersistable<Long> {
    private String headline;
    private String lead;
    @Lob
    private byte[] image;
    private String bodyText;
    private LocalDate publishDate;
    @ManyToMany
    private List<Category> categories;
    @ManyToOne
    private List<Author> writers;
    
    public List<Category> getCategories(){
        if(this.categories == null){
            this.categories = new ArrayList<>();
        }
        return this.categories;
    }
    
    public List<Author> getWriters(){
        if(this.writers == null){
            this.writers = new ArrayList<>();
        }
        return this.writers;
    }
}
