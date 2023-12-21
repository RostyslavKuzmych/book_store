package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    private static final String EXCEPTION = "Can't find category by id ";
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
<<<<<<< HEAD
<<<<<<< HEAD
            Verify findAll() method
=======
            Verify getAll() method
>>>>>>> d3f759ea45dd7fff4ca70074796c63ee4ecc2efb
=======
            Verify getAll() method
>>>>>>> 97bffe1b6c514f7fed73285654358f277a70f043
            """)
    void findAllCategories_ValidPageable_ReturnThreeCategoryDto() {
        // given
        PageImpl<Category> categoryPage = new PageImpl<>(categories);

        // when
        when(categoryRepository
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)))
                .thenReturn(categoryPage);
        when(categoryMapper.toDto(categories.get(FICTION_ID)))
                .thenReturn(categoryDtos.get(FICTION_ID));
        when(categoryMapper.toDto(categories.get(NOVEL_ID)))
                .thenReturn(categoryDtos.get(NOVEL_ID));
        when(categoryMapper.toDto(categories.get(CLASSICS_ID)))
                .thenReturn(categoryDtos.get(CLASSICS_ID));

        // then
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
<<<<<<< HEAD
<<<<<<< HEAD
            Verify correct categoryDto with categoryId
=======
            Verify findCategoryById() method with correct categoryId
>>>>>>> d3f759ea45dd7fff4ca70074796c63ee4ecc2efb
=======
            Verify findCategoryById() method with correct categoryId
>>>>>>> 97bffe1b6c514f7fed73285654358f277a70f043
            """)
    void findCategoryById_ValidCategoryId_ReturnCategoryDto() {
        // when
        when(categoryRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(categories.get(FICTION_ID)));
        when(categoryMapper.toDto(categories.get(FICTION_ID)))
                .thenReturn(categoryDtos.get(FICTION_ID));

        // then
        CategoryDto actual = categoryServiceImpl.getById(VALID_ID);
        assertNotNull(actual);
        assertEquals(categoryDtos.get(FICTION_ID), actual);
<<<<<<< HEAD
<<<<<<< HEAD
        verify(categoryRepository, times(ONE_TIME))
                .findById(VALID_ID);
=======
        verify(categoryRepository, times(ONE_TIME)).findById(VALID_ID);
>>>>>>> d3f759ea45dd7fff4ca70074796c63ee4ecc2efb
=======
        verify(categoryRepository, times(ONE_TIME)).findById(VALID_ID);
>>>>>>> 97bffe1b6c514f7fed73285654358f277a70f043
    }

    @Test
    @DisplayName("""
            Verify throwing exception with invalid categoryId
            """)
    void findCategoryById_InvalidCategoryId_ThrowException() {
        // when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryServiceImpl.getById(INVALID_ID));

        // then
        String expected = EXCEPTION + INVALID_ID;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
<<<<<<< HEAD
<<<<<<< HEAD
        verify(categoryRepository, times(ONE_TIME))
                .findById(INVALID_ID);
=======
        verify(categoryRepository, times(ONE_TIME)).findById(INVALID_ID);
>>>>>>> d3f759ea45dd7fff4ca70074796c63ee4ecc2efb
=======
        verify(categoryRepository, times(ONE_TIME)).findById(INVALID_ID);
>>>>>>> 97bffe1b6c514f7fed73285654358f277a70f043
    }

    @Test
    @DisplayName("""
            Verify save() method with correct categoryRequest
            """)
    void saveCategory_ValidCategoryRequest_ReturnCategoryDto() {
        // given
        CategoryRequestDto horrorRequestDto
                = new CategoryRequestDto().setName("Horror")
                .setDescription("Unbelievable genre");
        Category horror = new Category()
                .setName(horrorRequestDto.getName())
                .setDescription(horrorRequestDto.getDescription());
        CategoryDto horrorDto = new CategoryDto()
                .setName(horror.getName())
                .setDescription(horror.getDescription());

        // when
        when(categoryMapper.toEntity(horrorRequestDto)).thenReturn(horror);
        when(categoryRepository.save(horror)).thenReturn(horror);
        when(categoryMapper.toDto(horror)).thenReturn(horrorDto);

        // then
        CategoryDto actual = categoryServiceImpl.save(horrorRequestDto);
        assertNotNull(actual);
        assertEquals(horrorDto, actual);
<<<<<<< HEAD
<<<<<<< HEAD
        verify(categoryRepository, times(ONE_TIME))
                .save(horror);
=======
        verify(categoryRepository, times(ONE_TIME)).save(horror);
>>>>>>> d3f759ea45dd7fff4ca70074796c63ee4ecc2efb
=======
        verify(categoryRepository, times(ONE_TIME)).save(horror);
>>>>>>> 97bffe1b6c514f7fed73285654358f277a70f043
    }

    @Test
    @DisplayName("""
            Verify update() method with correct requestDto
            """)
<<<<<<< HEAD
<<<<<<< HEAD
    void updateCategoryById_ValidParams_ReturnCategoryDto() {
=======
    void updateCategory_ValidCategoryRequest_ReturnCategoryDto() {
        // given
>>>>>>> d3f759ea45dd7fff4ca70074796c63ee4ecc2efb
=======
    void updateCategory_ValidCategoryRequest_ReturnCategoryDto() {
        // given
>>>>>>> 97bffe1b6c514f7fed73285654358f277a70f043
        CategoryRequestDto mysteryRequestDto = new CategoryRequestDto()
                .setName("Mystery")
                .setDescription("Worth reading");
        Category mystery = new Category()
                .setName(mysteryRequestDto.getName())
                .setDescription(mysteryRequestDto.getDescription());
        CategoryDto mysteryDto = new CategoryDto()
                .setName(mystery.getName())
                .setDescription(mystery.getDescription());

        // when
        when(categoryRepository.findById(VALID_ID))
                .thenReturn(Optional.ofNullable(categories.get(NOVEL_ID)));
        when(categoryMapper.toEntity(mysteryRequestDto)).thenReturn(mystery);
        when(categoryRepository.save(mystery)).thenReturn(mystery);
        when(categoryMapper.toDto(mystery)).thenReturn(mysteryDto);

        // then
        CategoryDto actual = categoryServiceImpl.update(VALID_ID, mysteryRequestDto);
        assertNotNull(actual);
        assertEquals(mysteryDto, actual);
<<<<<<< HEAD
<<<<<<< HEAD
        verify(categoryRepository, times(ONE_TIME))
                .findById(VALID_ID);
=======
        verify(categoryRepository, times(ONE_TIME)).findById(VALID_ID);
        verify(categoryRepository, times(ONE_TIME)).save(mystery);
>>>>>>> d3f759ea45dd7fff4ca70074796c63ee4ecc2efb
=======
        verify(categoryRepository, times(ONE_TIME)).findById(VALID_ID);
        verify(categoryRepository, times(ONE_TIME)).save(mystery);
>>>>>>> 97bffe1b6c514f7fed73285654358f277a70f043
    }
}

