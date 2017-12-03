package wepa.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
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
    @OneToMany(mappedBy = "writers")
    private List<Article> articles;
}
