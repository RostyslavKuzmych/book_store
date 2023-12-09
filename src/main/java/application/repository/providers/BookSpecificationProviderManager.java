package application.repository.providers;

import application.exception.InvalidRequestException;
import application.model.Book;
import application.repository.SpecificationProvider;
import application.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private static final String FIND_PROVIDER_EXCEPTION
            = "Can't find specificationProvider by key ";
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream().filter(spec -> spec.getKey()
                        .equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new InvalidRequestException(FIND_PROVIDER_EXCEPTION + key));
    }
}
