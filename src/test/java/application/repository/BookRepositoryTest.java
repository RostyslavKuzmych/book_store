package application.repository;

import static org.junit.Assert.assertEquals;

import application.model.Book;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    private static final Long NOVEL_ID = 2L;
    private static final Long FICTION_ID = 1L;
    private static final Long NON_EXISTED_CATEGORY_ID = 10L;
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Verify findAllByCategoryId() method with correct categoryId
            """)
    void findAllByCategoryId_ValidCategoryId_ReturnTwoBooks() {
        Book prideAndPrejudice = new Book()
                .setId(2L)
                .setTitle("Pride and Prejudice")
                .setAuthor("Jane Austen")
                .setIsbn("9780141439518")
                .setPrice(BigDecimal.valueOf(20))
                .setDescription("A romantic novel")
                .setCoverImage("https://example.com/book2-cover-image.jpg");
        Book book1984 = new Book()
                .setId(3L)
                .setTitle("1984")
                .setAuthor("George Orwell")
                .setIsbn("9780451524935")
                .setPrice(BigDecimal.valueOf(9))
                .setDescription("A dystopian social science fiction novel")
                .setCoverImage("https://example.com/book3-cover-image.jpg");

        List<Book> expected = List.of(prideAndPrejudice, book1984);
        List<Book> actual = bookRepository.findAllByCategoryId(NOVEL_ID);
        assertEquals(2, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify findAllByCategoryId() method with invalid categoryId
            """)
    void findAllByCategoryId_InvalidCategoryId_ReturnEmptyList() {
        List<Book> expected = new ArrayList<>();
        List<Book> actual = bookRepository.findAllByCategoryId(NON_EXISTED_CATEGORY_ID);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify findAllByCategoryId() method with correct categoryId
            """)
    void findAllByCategoryId_ValidCategoryId_ReturnOneBook() {
        Book greatGatsby = new Book()
                .setId(1L)
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9780743273565")
                .setPrice(BigDecimal.valueOf(11))
                .setDescription("The story of the fabulously wealthy Jay Gatsby")
                .setCoverImage("https://example.com/book1-cover-image.jpg");

        List<Book> expected = List.of(greatGatsby);
        List<Book> actual = bookRepository.findAllByCategoryId(FICTION_ID);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
    }
}
