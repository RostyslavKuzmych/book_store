package application.service.impl;

import application.dto.category.CategoryDto;
import application.dto.category.CategoryRequestDto;
import application.exception.EntityNotFoundException;
import application.mapper.CategoryMapper;
import application.model.Category;
import application.repository.CategoryRepository;
import application.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private static final String FIND_EXCEPTION = "Can't find category by id ";
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository
                .findAll(pageable)
                .stream().map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long categoryId) {
        return categoryMapper.toDto(findById(categoryId));
    }

    @Override
    public CategoryDto save(CategoryRequestDto categoryDto) {
        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(categoryDto)));
    }

    @Override
    public CategoryDto update(Long categoryId, CategoryRequestDto requestDto) {
        Category category = findById(categoryId);
        Category inputCategory = categoryMapper.toEntity(requestDto);
        category.setId(categoryId);
        return categoryMapper.toDto(categoryRepository.save(inputCategory));
    }

    @Override
    public void deleteById(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    private Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(FIND_EXCEPTION + categoryId));
    }
}
