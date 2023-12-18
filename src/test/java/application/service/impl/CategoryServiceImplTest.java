package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import application.dto.category.CategoryDto;
import application.dto.category.CategoryRequestDto;
import application.exception.EntityNotFoundException;
import application.mapper.CategoryMapper;
import application.model.Category;
import application.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static final Integer FICTION_ID = 0;
    private static final Integer NOVEL_ID = 1;
    private static final Integer CLASSICS_ID = 2;
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final Long INVALID_ID = 11L;
    private static final Long VALID_ID = 2L;
    private static final Integer ONE_TIME = 1;
    private static List<Category> categories;
    private static List<CategoryDto> categoryDtos;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @BeforeAll
    static void beforeAll() {
        Category fiction = new Category().setName("Fiction")
                .setDescription("Breathtaking genre");
        Category novel = new Category().setName("Novel")
                .setDescription("Magnificent genre");
        Category classics = new Category().setName("Classics")
                .setDescription("Category of literature that encompasses classic works");
        CategoryDto fictionDto = new CategoryDto().setName(fiction.getName())
                .setDescription(fiction.getDescription());
        CategoryDto novelDto = new CategoryDto().setName(novel.getName())
                .setDescription(novel.getDescription());
        CategoryDto classicsDto = new CategoryDto().setName(classics.getName())
                .setDescription(classics.getDescription());
        categories = List.of(fiction, novel, classics);
        categoryDtos = List.of(fictionDto, novelDto, classicsDto);
    }

    @Test
    @DisplayName("""
            Verify findAll() method
            """)
    void findAllCategories_ValidPageable_ReturnExpectedList() {
        PageImpl<Category> categoryPage = new PageImpl<>(categories);
        when(categoryRepository
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)))
                .thenReturn(categoryPage);
        when(categoryMapper.toDto(categories.get(FICTION_ID)))
                .thenReturn(categoryDtos.get(FICTION_ID));
        when(categoryMapper.toDto(categories.get(NOVEL_ID)))
                .thenReturn(categoryDtos.get(NOVEL_ID));
        when(categoryMapper.toDto(categories.get(CLASSICS_ID)))
                .thenReturn(categoryDtos.get(CLASSICS_ID));

        List<CategoryDto> expected
                = List.of(categoryDtos.get(FICTION_ID), categoryDtos.get(NOVEL_ID),
                categoryDtos.get(CLASSICS_ID));
        List<CategoryDto> actual
                = categoryServiceImpl
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));

        assertEquals(3, actual.size());
        assertEquals(expected, actual);
        verify(categoryRepository, times(ONE_TIME))
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
    }

    @Test
    @DisplayName("""
            Verify correct categoryDto with categoryId
            """)
    void findCategoryById_ValidCategoryId_ReturnExpectedCategory() {
        when(categoryRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(categories.get(FICTION_ID)));
        when(categoryMapper.toDto(categories.get(FICTION_ID)))
                .thenReturn(categoryDtos.get(FICTION_ID));

        CategoryDto actual = categoryServiceImpl.getById(VALID_ID);
        assertNotNull(actual);
        assertEquals(categoryDtos.get(FICTION_ID), actual);
        verify(categoryRepository, times(ONE_TIME))
                .findById(VALID_ID);
    }

    @Test
    @DisplayName("""
            Verify throwing exception with invalid categoryId
            """)
    void findCategoryById_InvalidCategoryId_ReturnException() {
        when(categoryRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryServiceImpl.getById(INVALID_ID));
        String expected = "Can't find category by id " + INVALID_ID;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(categoryRepository, times(ONE_TIME))
                .findById(INVALID_ID);
    }

    @Test
    @DisplayName("""
            Verify save() method with correct categoryRequest
            """)
    void saveCategory_ValidCategoryRequestDto_ReturnCategoryDto() {
        CategoryRequestDto horrorRequestDto
                = new CategoryRequestDto().setName("Horror")
                .setDescription("Unbelievable genre");
        Category horror = new Category()
                .setName(horrorRequestDto.getName())
                .setDescription(horrorRequestDto.getDescription());
        CategoryDto horrorDto = new CategoryDto()
                .setName(horror.getName())
                .setDescription(horror.getDescription());

        when(categoryMapper.toEntity(horrorRequestDto)).thenReturn(horror);
        when(categoryRepository.save(horror)).thenReturn(horror);
        when(categoryMapper.toDto(horror)).thenReturn(horrorDto);

        CategoryDto actual = categoryServiceImpl.save(horrorRequestDto);
        assertNotNull(actual);
        assertEquals(horrorDto, actual);
        verify(categoryRepository, times(ONE_TIME))
                .save(horror);
    }

    @Test
    @DisplayName("""
            Verify update() method with correct params
            """)
    void updateCategoryById_ValidParams_ReturnCategoryDto() {
        CategoryRequestDto mysteryRequestDto = new CategoryRequestDto()
                .setName("Mystery")
                .setDescription("Worth reading");
        Category mystery = new Category()
                .setName(mysteryRequestDto.getName())
                .setDescription(mysteryRequestDto.getDescription());
        CategoryDto mysteryDto = new CategoryDto()
                .setName(mystery.getName())
                        .setDescription(mystery.getDescription());

        when(categoryRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(categories.get(NOVEL_ID)));
        when(categoryMapper.toEntity(mysteryRequestDto)).thenReturn(mystery);
        when(categoryRepository.save(mystery)).thenReturn(mystery);
        when(categoryMapper.toDto(mystery)).thenReturn(mysteryDto);

        CategoryDto actual = categoryServiceImpl.update(VALID_ID, mysteryRequestDto);
        assertNotNull(actual);
        assertEquals(mysteryDto, actual);
        verify(categoryRepository, times(ONE_TIME))
                .findById(VALID_ID);
    }
}
