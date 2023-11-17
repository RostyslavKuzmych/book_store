package application.repository.builders;

import application.dto.BookSearchParametersDto;
import application.model.Book;
import application.repository.SpecificationBuilder;
import application.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (authorsNotNull(bookSearchParametersDto)) {
            specification = specification
                    .and(getSpecificationByKey("title", bookSearchParametersDto.authors()));
        }
        if (titlesNotNull(bookSearchParametersDto)) {
            specification = specification
                    .and(getSpecificationByKey("title", bookSearchParametersDto.titles()));
        }
        return specification;
    }

    private boolean authorsNotNull(BookSearchParametersDto bookSearchParametersDto) {
        return bookSearchParametersDto.authors() != null
                && bookSearchParametersDto.authors().length > 0;
    }

    private boolean titlesNotNull(BookSearchParametersDto bookSearchParametersDto) {
        return bookSearchParametersDto.titles() != null
                && bookSearchParametersDto.titles().length > 0;
    }

    private Specification<Book> getSpecificationByKey(String name, String [] params) {
        return Specification.where(bookSpecificationProviderManager
                .getSpecificationProvider(name)
                .getSpecification(params));
    }
}

