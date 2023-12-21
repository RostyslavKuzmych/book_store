package application.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.book.BookDto;
import application.dto.book.BookSearchParametersDto;
import application.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final String API = "/api/books";
    private static final String SEARCH = "/search";
    private static final String NIGHT_CIRCUS_ID = "/10";
    private static final Long NIGHT_CIRCUS_ID_INTO_DB = 10L;
    private static final String GREAT_GATSBY_ID = "/1";
    private static final Long FICTION_ID = 1L;
    private static final Long NOVEL_ID = 2L;
    private static final String ID = "id";
    private static final String PATH = "classpath:database/books/";
    private static final Integer GREAT_GATSBY_DTO_ID = 0;
    private static final String HARRY_POTTER_ID = "/8";
    private static List<BookDto> dtoList;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        BookDto greatGatsbyDto = new BookDto()
                .setId(1L)
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9780743273565")
                .setPrice(BigDecimal.valueOf(11))
                .setDescription("The story of the fabulously wealthy Jay Gatsby")
                .setCoverImage("https://example.com/book1-cover-image.jpg")
                .setCategoriesIds(Set.of(FICTION_ID));
        BookDto prideAndPrejudiceDto = new BookDto()
                .setId(2L)
                .setTitle("Pride and Prejudice")
                .setAuthor("Jane Austen")
                .setIsbn("9780141439518")
                .setPrice(BigDecimal.valueOf(20))
                .setDescription("A romantic novel")
                .setCoverImage("https://example.com/book2-cover-image.jpg")
                .setCategoriesIds(Set.of(NOVEL_ID));
        BookDto book1984Dto = new BookDto()
                .setId(3L)
                .setTitle("1984")
                .setAuthor("George Orwell")
                .setIsbn("9780451524935")
                .setPrice(BigDecimal.valueOf(9))
                .setDescription("A dystopian social science fiction novel")
                .setCoverImage("https://example.com/book3-cover-image.jpg")
                .setCategoriesIds(Set.of(NOVEL_ID));
        dtoList = List.of(greatGatsbyDto, prideAndPrejudiceDto, book1984Dto);
    }

    @Test
    @WithMockUser(username = "admin", roles = ControllerTestUtil.ADMIN)
    @DisplayName("""
            Verify createBook() method with correct requestDto
            """)
    @Sql(scripts = PATH + "remove_tom-book_from_books_table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_ValidBookRequest_ReturnBookDto() throws Exception {
        // given
        CreateBookRequestDto tomRequestDto = new CreateBookRequestDto()
                .setTitle("Cat Tom")
                .setAuthor("Cute story about Tom the cat")
                .setPrice(BigDecimal.valueOf(21))
                .setIsbn("9780451524230");
        String jsonRequest = objectMapper.writeValueAsString(tomRequestDto);

        // when
        MvcResult mvcResult = mockMvc.perform(post(API)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // then
        BookDto expected = new BookDto()
                .setTitle(tomRequestDto.getTitle())
                .setAuthor(tomRequestDto.getAuthor())
                .setPrice(tomRequestDto.getPrice())
                .setIsbn(tomRequestDto.getIsbn());
        BookDto actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, ID);
    }

    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @Test
    @DisplayName("""
            Verify getAll() method
            """)
    void getAllBooks_NonEmptyDb_ReturnThreeBooks() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(API)).andReturn();

        // then
        List<BookDto> expected = dtoList;
        List<BookDto> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<BookDto>>() {});
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @Test
    @DisplayName("""
            Verify getBookById() method with correct bookId
            """)
    void getBookById_ValidBookId_ReturnBookDto() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(API + GREAT_GATSBY_ID))
                .andReturn();

        // then
        BookDto expected = dtoList.get(GREAT_GATSBY_DTO_ID);
        BookDto actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        BookDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = ControllerTestUtil.ADMIN)
    @Test
    @Sql(scripts = PATH
            + "save_the_night_circus_book_to_books_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = PATH
             + "remove_lord-of-the-rings_book_from_books_table.sql",
             executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("""
            Verify updateBook() method with correct bookRequestDto
            """)
    void updateBook_ValidBookRequest_ReturnBookDto() throws Exception {
        // given
        CreateBookRequestDto harryPotterRequest =
                new CreateBookRequestDto().setAuthor("J.R.R. Tolkien")
                        .setTitle("The Lord of the Rings")
                        .setIsbn("9780743273760")
                        .setPrice(BigDecimal.valueOf(25));
        String jsonRequest = objectMapper.writeValueAsString(harryPotterRequest);

        // when
        MvcResult mvcResult = mockMvc.perform(put(API + NIGHT_CIRCUS_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();

        // then
        BookDto expected = new BookDto()
                .setId(NIGHT_CIRCUS_ID_INTO_DB)
                .setAuthor(harryPotterRequest.getAuthor())
                .setTitle(harryPotterRequest.getTitle())
                .setIsbn(harryPotterRequest.getIsbn())
                .setPrice(harryPotterRequest.getPrice())
                .setCategoriesIds(new HashSet<>());
        BookDto actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                BookDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user",
            roles = {ControllerTestUtil.ADMIN, ControllerTestUtil.USER})
    @DisplayName("""
            Verify deleteBookById() method with correct bookId
            """)
    @Sql(scripts = PATH
            + "save_harry-potter_book_to_books_table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteBookById_ValidBookId_Success() throws Exception {
        // when
        mockMvc.perform(delete(API + HARRY_POTTER_ID))
                .andExpect(status().isNoContent());
        MvcResult mvcResult = mockMvc.perform(get(API))
                .andReturn();

        // then
        List<BookDto> expected = dtoList;
        List<BookDto> actual =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<List<BookDto>>() {});
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = ControllerTestUtil.USER)
    @DisplayName("""
            Verify getAllByParams() method with correct params
            """)
    void getAllByParams_ValidParams_ReturnOneBook() throws Exception {
        BookSearchParametersDto parametersDto
                = new BookSearchParametersDto(new String[]{"F. Scott Fitzgerald"},
                        new String[]{"The Great Gatsby"});

        MvcResult mvcResult = mockMvc.perform(get(API + SEARCH)
                        .param("authors", String.join(",", parametersDto.authors()))
                        .param("titles", String.join(",", parametersDto.titles())))
                .andReturn();

        List<BookDto> expected = List.of(dtoList.get(GREAT_GATSBY_DTO_ID));
        List<BookDto> actual = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<BookDto>>() {});
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }
}
