package application.service;

import application.dto.book.BookDtoWithoutCategoriesIds;
import application.dto.category.CategoryDto;
import application.dto.category.CategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long categoryId);

    CategoryDto save(CategoryRequestDto categoryRequestDto);

    List<BookDtoWithoutCategoriesIds> getBooksByCategoryId(Long categoryId);

    CategoryDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);
}
