package application.service;

import application.dto.book.BookDto;
import application.dto.category.CategoryDto;
import application.dto.category.CategoryRequestDto;
import application.exception.EntityNotFoundException;
import application.mapper.BookMapper;
import application.mapper.CategoryMapper;
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
    private final BookMapper bookMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll().stream().map(categoryMapper::toDto).toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper
                .toDto(categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Can't find category by id " + id)));
    }

    @Override
    public CategoryDto save(CategoryRequestDto categoryDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryDto)));
    }

    @Override
    public List<BookDto> getBookDtosByCategoryId(Long id) {
        return bookService.getBooksByCategoryId(id).stream().map(bookMapper::toDto).toList();
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category.setId(id);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
