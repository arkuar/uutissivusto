package wepa.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
    @Column(name = "image", columnDefinition = "bytea")
    private byte[] image;
    @Lob
    private String bodyText;
    private LocalDateTime publishDate;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Category> categories;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Author> writers;
    private int count;

    public Article(String headline, String lead, byte[] image, String bodyText, LocalDateTime publishDate) {
        this.headline = headline;
        this.lead = lead;
        this.image = image;
        this.bodyText = bodyText;
        this.publishDate = publishDate;
        this.count = 0;
    }

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
