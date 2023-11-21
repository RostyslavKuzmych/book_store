package application.repository.providers;

import application.model.Book;
import application.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitlesSpecificationProvider implements SpecificationProvider<Book> {
    private static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    public Specification<Book> getSpecification(String [] params) {
        return ((root, query, criteriaBuilder) -> root.get(TITLE)
                .in(Arrays.stream(params).toArray()));
    }
}
