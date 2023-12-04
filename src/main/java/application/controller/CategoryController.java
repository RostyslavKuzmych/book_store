package application.controller;

import application.dto.book.BookDtoWithoutCategoryIds;
import application.dto.category.CategoryDto;
import application.dto.category.CategoryRequestDto;
import application.mapper.BookMapper;
import application.mapper.CategoryMapper;
import application.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Management categories", description = "Endpoints for management categories")
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a category", description
            = "An endpoint for creating a category")
    public CategoryDto createCategory(@RequestBody @Valid CategoryRequestDto c) {
        return categoryMapper.toDto(categoryService.save(c));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all categories", description
            = "An endpoint for getting all categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable).stream().map(categoryMapper::toDto).toList();
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    @Operation(summary = "Get a category by id", description
            = "An endpoint for getting a category by id")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryMapper.toDto(categoryService.getById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a category by id", description
            = "An endpoint for updating a category by id")
    @ResponseStatus(HttpStatus.CONTINUE)
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @RequestBody CategoryRequestDto categoryDto) {
        return categoryMapper.toDto(categoryService.update(id, categoryDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a category by id", description
            = "An endpoint for deleting a category by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @GetMapping("/{id}/books")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all books by category id",
            description = "An endpoint for getting all books by category id")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id) {
        return categoryService.getBookDtosByCategoryId(id).stream()
                .map(bookMapper::toDtoWithoutCategories).toList();
    }
}
