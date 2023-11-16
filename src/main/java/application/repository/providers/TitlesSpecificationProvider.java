package application.repository.providers;

import application.model.Book;
import application.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitlesSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    public Specification<Book> getSpecification(String [] params) {
        return ((root, query, criteriaBuilder) -> root.get("title")
                .in(Arrays.stream(params).toArray()));
    }
}
