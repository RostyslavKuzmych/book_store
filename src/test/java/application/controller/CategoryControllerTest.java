package application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.book.BookDtoWithoutCategoriesIds;
import application.dto.category.CategoryDto;
import application.dto.category.CategoryRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    private static final String CLASSICS_ID = "/3";
    private static final String BOOKS = CategoryController.BOOKS;
    private static final String ADVENTURE_DESCRIPTION
            = "Thrilling journeys and exciting escapades into the unknown.";
    private static final Integer CLASSICS_ID_INT0_LIST = 2;
    private static final String NOVEL_ID = "/2";
    private static final Long HORROR_ID_INTO_DB = 6L;
    private static final String HORROR_ID = "/6";
    private static final String FANTASY_ID = "/7";
    private static final String ID = "id";
    private static final String API = CategoryController.BASE_URL;
    private static final String PATH = "classpath:database/categories/";
    private static final String FICTION_DESCRIPTION = "Genre encompassing all types of works "
            + "created based on imagination or invention";
    private static final String NOVEL_DESCRIPTION = "Genre represented or exemplified by novels";
    private static final String CLASSICS_DESCRIPTION
            = "Category of literature that encompasses classic works";
    private static List<CategoryDto> categoryList;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        CategoryDto fictionDto = new CategoryDto()
                .setId(1L)
                .setName("fiction")
                .setDescription(FICTION_DESCRIPTION);
        CategoryDto novelDto = new CategoryDto()
                .setId(2L)
                .setName("novel")
                .setDescription(NOVEL_DESCRIPTION);
        CategoryDto classicsDto = new CategoryDto()
                .setId(3L)
                .setName("classics")
                .setDescription(CLASSICS_DESCRIPTION);
        categoryList = List.of(fictionDto, novelDto, classicsDto);
    }

    @Test
    @DisplayName("""
            Verify createCategory() method with valid categoryRequest
            """)
    @WithMockUser(username = "admin", roles = {ControllerTestUtil.ADMIN})
    @Sql(scripts = PATH + "remove_category_adventure_from_categories_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidCategoryRequest_ReturnCategoryDto() throws Exception {
        // given
        CategoryRequestDto adventureRequest =
                new CategoryRequestDto().setName("adventure")
                        .setDescription(ADVENTURE_DESCRIPTION);
        String jsonRequest = objectMapper.writeValueAsString(adventureRequest);

        // when
        MvcResult mvcResult = mockMvc.perform(post(API).content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        CategoryDto expected =
                new CategoryDto().setName(adventureRequest.getName())
                        .setDescription(adventureRequest.getDescription());
        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), CategoryDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, ID);
    }

    @Test
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify getAll() method
            """)
    void getAll_NonEmptyDb_ReturnThreeCategories() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(API)).andReturn();

        // then
        List<CategoryDto> expected = categoryList;
        List<CategoryDto> actual = objectMapper
                .readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<CategoryDto>>() {});
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify getCategoryById() method with correct categoryId
            """)
    void getCategoryById_ValidCategoryId_ReturnCategoryDto() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(API + CLASSICS_ID)).andReturn();

        // then
        CategoryDto expected = categoryList.get(CLASSICS_ID_INT0_LIST);
        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = ControllerTestUtil.ADMIN)
    @DisplayName("""
            Verify updateBook() method with correct categoryRequest
            """)
    @Sql(scripts = PATH + "save_category_horror_to_categories_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = PATH + "remove_category_mystery_from_categories_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateBook_ValidCategoryRequest_Success() throws Exception {
        // given
        CategoryRequestDto mysteryRequest =
                new CategoryRequestDto().setName("mystery")
                        .setDescription("Wonderful category");
        String jsonRequest = objectMapper.writeValueAsString(mysteryRequest);

        // when
        MvcResult mvcResult = mockMvc.perform(put(API + HORROR_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isContinue())
                .andReturn();

        // then
        CategoryDto expected =
                new CategoryDto().setId(HORROR_ID_INTO_DB)
                        .setName(mysteryRequest.getName())
                        .setDescription(mysteryRequest.getDescription());
        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CategoryDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin",
            roles = {ControllerTestUtil.ADMIN, ControllerTestUtil.USER})
    @DisplayName("""
            Verify deleteCategory() method with valid categoryId
            """)
    @Sql(scripts = PATH + "save_category_fantasy_to_categories_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteBook_ValidCategoryId_Success() throws Exception {
        // when
        mockMvc.perform(delete(API + FANTASY_ID))
                .andExpect(status().isNoContent());
        MvcResult mvcResult = mockMvc.perform(get(API)).andReturn();

        // then
        List<CategoryDto> expected = categoryList;
        List<CategoryDto> actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<CategoryDto>>() {});
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify getBooksByCategoryId() method with correct categoryId
            """)
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    void getBooksByCategoryId_ValidCategoryId_ReturnTwoBooks() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(API + NOVEL_ID + BOOKS))
                .andReturn();

        // then
        BookDtoWithoutCategoriesIds prideAndPrejudiceDto =
                new BookDtoWithoutCategoriesIds().setId(2L)
                .setTitle("Pride and Prejudice")
                .setAuthor("Jane Austen")
                .setIsbn("9780141439518")
                .setPrice(BigDecimal.valueOf(20))
                .setDescription("A romantic novel")
                .setCoverImage("https://example.com/book2-cover-image.jpg");
        BookDtoWithoutCategoriesIds book1984Dto = new BookDtoWithoutCategoriesIds()
                .setId(3L)
                .setTitle("1984")
                .setAuthor("George Orwell")
                .setIsbn("9780451524935")
                .setPrice(BigDecimal.valueOf(9))
                .setDescription("A dystopian social science fiction novel")
                .setCoverImage("https://example.com/book3-cover-image.jpg");
        List<BookDtoWithoutCategoriesIds> expected =
                List.of(prideAndPrejudiceDto, book1984Dto);
        List<BookDtoWithoutCategoriesIds> actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                    new TypeReference<List<BookDtoWithoutCategoriesIds>>() {});
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }
}
