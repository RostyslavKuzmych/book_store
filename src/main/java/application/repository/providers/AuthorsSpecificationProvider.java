package application.repository.providers;

import application.model.Book;
import application.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorsSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR = "author";

    @Override
    public String getKey() {
        return AUTHOR;
    }

    public Specification<Book> getSpecification(String [] params) {
        return ((root, query, criteriaBuilder) -> root.get(AUTHOR)
                .in(Arrays.stream(params).toArray()));
    }
}
