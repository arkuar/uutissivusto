package wepa.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Article extends AbstractPersistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String headline;
    private String lead;
    @Lob
    private byte[] image;
    @Lob
    private String bodyText;
    private LocalDateTime publishDate;
    @ManyToMany
    private List<Category> categories;
    @ManyToMany
    private List<Author> writers;
    private int count;

    public List<Category> getCategories() {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        }
        return this.categories;
    }

    public List<Author> getWriters() {
        if (this.writers == null) {
            this.writers = new ArrayList<>();
        }
        return this.writers;
    }

    public void incrementCount() {
        this.count++;
    }
}
