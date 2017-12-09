package wepa.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Category extends AbstractPersistable<Long> {

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private List<Article> articles;

    public Category(String name) {
        this.name = name;
        this.articles = new ArrayList<>();
    }
}
