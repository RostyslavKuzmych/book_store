package application.repository.providers;

import application.model.Book;
import application.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorsSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(String [] params) {
        return ((root, query, criteriaBuilder) -> root.get("author")
                .in(Arrays.stream(params).toArray()));
    }
}
