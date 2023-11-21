package application.repository.providers;

import application.model.Book;
import application.repository.SpecificationProvider;
import application.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream().filter(spec -> spec.getKey()
                        .equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Can't get specificationProvider by key " + key));
    }
}
