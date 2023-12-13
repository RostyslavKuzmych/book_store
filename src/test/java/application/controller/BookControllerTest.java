package application.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.dto.book.BookDto;
import application.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "bob@example.com", roles = {"ADMIN"})
    @DisplayName("""
            Verify createBook() method with correct requestDto
            """)
    void createBook_ValidRequestDto_ReturnBookDto() throws Exception {
        CreateBookRequestDto tomRequestDto = new CreateBookRequestDto()
                .setTitle("Cat Tom")
                .setAuthor("Cute story about Tom the cat")
                .setPrice(BigDecimal.valueOf(21))
                .setIsbn("9780451524230");
        BookDto tomDto = new BookDto()
                .setTitle(tomRequestDto.getTitle())
                .setAuthor(tomRequestDto.getAuthor())
                .setPrice(tomRequestDto.getPrice())
                .setIsbn(tomRequestDto.getIsbn());
        String jsonRequest = objectMapper.writeValueAsString(tomRequestDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
    }
}
