package application.service;

import application.dto.book.BookDto;
import application.dto.category.CategoryDto;
import application.dto.category.CategoryRequestDto;
import application.exception.EntityNotFoundException;
import application.mapper.BookMapper;
import application.mapper.CategoryMapper;
import application.model.Book;
import application.model.Category;
import application.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookService bookService;

    @Override
    public List<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find category by id " + id)));
    }

    @Override
    public Category save(CategoryRequestDto categoryDto) {
        return categoryRepository.save(categoryMapper.toEntity(categoryDto));
    }

    @Override
    public List<Book> getBookDtosByCategoryId(Long id) {
        return bookService.getBooksByCategoryId(id);
    }

    @Override
    public Category update(Long id, CategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category.setId(id);
        return categoryRepository.save(category);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
