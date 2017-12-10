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

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Author extends AbstractPersistable<Long> {

    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "writers")
    private List<Article> articles;

    public Author(String name) {
        this.name = name;
        this.articles = new ArrayList<>();
    }

}
