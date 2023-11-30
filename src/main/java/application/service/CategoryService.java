package application.service;

import application.dto.category.CategoryRequestDto;
import application.model.Book;
import application.model.Category;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<Category> findAll(Pageable pageable);

    Category getById(Long id);

    Category save(CategoryRequestDto categoryDto);

    List<Book> getBookDtosByCategoryId(Long id);

    Category update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);
}
