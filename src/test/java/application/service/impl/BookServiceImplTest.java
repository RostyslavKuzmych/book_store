package application.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import application.dto.book.BookDto;
import application.dto.book.BookDtoWithoutCategoriesIds;
import application.dto.book.BookSearchParametersDto;
import application.dto.book.CreateBookRequestDto;
import application.exception.EntityNotFoundException;
import application.mapper.BookMapper;
import application.model.Book;
import application.repository.BookRepository;
import application.repository.builders.BookSpecificationBuilder;
import java.math.BigDecimal;
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
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static final String EXCEPTION = "Can't find book by id ";
    private static final Integer GREAT_GATSBY_ID = 0;
    private static final Integer PRIDE_AND_PREJUDICE_ID = 1;
    private static final Integer BOOK_1984_ID = 2;
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final Long VALID_BOOK_ID = 1L;
    private static final Long INVALID_BOOK_ID = 20L;
    private static final Long VALID_CATEGORY_ID = 1L;
    private static final Integer ONE_TIME = 1;
    private static List<Book> books;
    private static List<BookDto> bookDtos;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder specificationBuilder;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    @BeforeAll
    static void beforeAll() {
        Book greatGatsby = new Book()
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9780743273565")
                .setPrice(BigDecimal.valueOf(11))
                .setDescription("The story of the fabulously wealthy Jay Gatsby")
                .setCoverImage("https://example.com/book1-cover-image.jpg");
        Book prideAndPrejudice = new Book()
                .setTitle("Pride and Prejudice")
                .setAuthor("Jane Austen")
                .setIsbn("9780141439518")
                .setPrice(BigDecimal.valueOf(20))
                .setDescription("A romantic novel")
                .setCoverImage("https://example.com/book2-cover-image.jpg");
        Book book1984 = new Book()
                .setTitle("1984")
                .setAuthor("George Orwell")
                .setIsbn("9780451524935")
                .setPrice(BigDecimal.valueOf(15))
                .setDescription("A dystopian social science fiction novel")
                .setCoverImage("https://example.com/book3-cover-image.jpg");
        BookDto greatGatsbyDto = new BookDto()
                .setTitle(greatGatsby.getTitle())
                .setAuthor(greatGatsby.getAuthor())
                .setPrice(greatGatsby.getPrice())
                .setIsbn(greatGatsby.getIsbn())
                .setDescription(greatGatsby.getDescription())
                .setCoverImage(greatGatsby.getCoverImage());
        BookDto prideAndPrejudiceDto = new BookDto()
                .setTitle(prideAndPrejudice.getTitle())
                .setAuthor(prideAndPrejudice.getAuthor())
                .setPrice(prideAndPrejudice.getPrice())
                .setIsbn(prideAndPrejudice.getIsbn())
                .setDescription(prideAndPrejudice.getDescription())
                .setCoverImage(prideAndPrejudice.getCoverImage());
        BookDto book1984Dto = new BookDto()
                .setTitle(book1984.getTitle())
                .setAuthor(book1984.getAuthor())
                .setPrice(book1984.getPrice())
                .setIsbn(book1984.getIsbn())
                .setDescription(book1984.getDescription())
                .setCoverImage(book1984.getCoverImage());
        books = List.of(greatGatsby, prideAndPrejudice, book1984);
        bookDtos = List.of(greatGatsbyDto, prideAndPrejudiceDto, book1984Dto);

    }

    @Test
    @DisplayName("""
            Verify save() method with correct requestDto
            """)
    void saveBook_ValidBookRequest_ReturnBookDto() {
        // given
        CreateBookRequestDto theHobbitRequestDto = new CreateBookRequestDto()
                .setTitle("The Hobbit")
                .setAuthor("J.R.R. Tolkien")
                .setPrice(BigDecimal.valueOf(15))
                .setIsbn("9780451524930");
        Book theHobbit = new Book()
                .setTitle(theHobbitRequestDto.getTitle())
                .setAuthor(theHobbitRequestDto.getAuthor())
                .setPrice(theHobbitRequestDto.getPrice())
                .setIsbn(theHobbitRequestDto.getIsbn());
        BookDto theHobbitDto = new BookDto()
                .setTitle(theHobbit.getTitle())
                .setAuthor(theHobbit.getAuthor())
                .setIsbn(theHobbit.getIsbn())
                .setPrice(theHobbit.getPrice());

        // when
        when(bookMapper.toModel(theHobbitRequestDto)).thenReturn(theHobbit);
        when(bookRepository.save(theHobbit)).thenReturn(theHobbit);
        when(bookMapper.toDto(theHobbit)).thenReturn(theHobbitDto);

        // then
        BookDto actual = bookServiceImpl.createBook(theHobbitRequestDto);
        assertNotNull(actual);
        assertEquals(theHobbitDto, actual);
        verify(bookRepository, times(ONE_TIME)).save(theHobbit);
    }

    @Test
    @DisplayName("""
            Verify findAll() method
            """)
    void findAllBooks_ValidPageable_ReturnThreeBooks() {
        // given
        PageImpl<Book> bookPage = new PageImpl<>(books);

        // when
        when(bookRepository.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)))
                .thenReturn(bookPage);
        when(bookMapper.toDto(books.get(GREAT_GATSBY_ID)))
                .thenReturn(bookDtos.get(GREAT_GATSBY_ID));
        when(bookMapper.toDto(books.get(PRIDE_AND_PREJUDICE_ID)))
                .thenReturn(bookDtos.get(PRIDE_AND_PREJUDICE_ID));
        when(bookMapper.toDto(books.get(BOOK_1984_ID)))
                .thenReturn(bookDtos.get(BOOK_1984_ID));

        // then
        List<BookDto> expected = List.of(bookDtos.get(GREAT_GATSBY_ID),
                bookDtos.get(PRIDE_AND_PREJUDICE_ID), bookDtos.get(BOOK_1984_ID));
        List<BookDto> actual = bookServiceImpl
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
        verify(bookRepository, times(ONE_TIME))
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
    }

    @Test
    @DisplayName("""
            Verify getBookDtoById() method with correct bookId
            """)
    void findBookById_ValidBookId_ReturnBookDto() {
        // when
        when(bookRepository
                .findById(VALID_BOOK_ID))
                .thenReturn(Optional.ofNullable(books.get(PRIDE_AND_PREJUDICE_ID)));
        when(bookMapper.toDto(books.get(PRIDE_AND_PREJUDICE_ID)))
                .thenReturn(bookDtos.get(PRIDE_AND_PREJUDICE_ID));

        // then
        BookDto actual = bookServiceImpl.getBookDtoById(VALID_BOOK_ID);
        assertNotNull(actual);
        assertEquals(bookDtos.get(PRIDE_AND_PREJUDICE_ID), actual);
        verify(bookRepository, times(ONE_TIME)).findById(VALID_BOOK_ID);
    }

    @Test
    @DisplayName("""
            Verify getBookDtoById() method with invalid bookId
            """)
    void findBookById_InvalidBookId_ThrowException() {
        // when
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.getBookDtoById(INVALID_BOOK_ID));

        // then
        String expected = EXCEPTION + INVALID_BOOK_ID;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository, times(ONE_TIME)).findById(INVALID_BOOK_ID);
    }

    @Test
    @DisplayName("""
            Verify updateBook() method with correct requestDto
            """)
    void updateBook_ValidBookRequest_ReturnBookDto() {
        // given
        CreateBookRequestDto animalFarmRequestDto = new CreateBookRequestDto()
                .setTitle("Animal Farm")
                .setAuthor("George Orwell")
                .setPrice(BigDecimal.valueOf(12))
                .setIsbn("9780451524956");
        Book animalFarm = new Book()
                .setTitle(animalFarmRequestDto.getTitle())
                .setAuthor(animalFarmRequestDto.getAuthor())
                .setPrice(animalFarmRequestDto.getPrice())
                .setIsbn(animalFarmRequestDto.getIsbn());
        BookDto animalFarmDto = new BookDto()
                .setTitle(animalFarm.getTitle())
                .setAuthor(animalFarm.getAuthor())
                .setPrice(animalFarm.getPrice())
                .setIsbn(animalFarm.getIsbn());

        // when
        when(bookRepository.findById(VALID_BOOK_ID))
                .thenReturn(Optional.ofNullable(books.get(BOOK_1984_ID)));
        when(bookMapper.toModel(animalFarmRequestDto)).thenReturn(animalFarm);
        when(bookRepository.save(animalFarm)).thenReturn(animalFarm);
        when(bookMapper.toDto(animalFarm)).thenReturn(animalFarmDto);

        // then
        BookDto actual = bookServiceImpl.updateBook(VALID_BOOK_ID, animalFarmRequestDto);
        assertNotNull(actual);
        assertEquals(animalFarmDto, actual);
        verify(bookRepository, times(ONE_TIME)).findById(VALID_BOOK_ID);
        verify(bookRepository, times(ONE_TIME)).save(animalFarm);
    }

    @Test
    @DisplayName("""
            Verify getBookDtosByParameters() method with correct params
            """)
    void getBooksByParams_ValidParams_ReturnOneBook() {
        // given
        BookSearchParametersDto bookSearchParametersDto
                = new BookSearchParametersDto(new String[]{"1984"},
                        new String[]{"George Orwell"});

        // when
        when(specificationBuilder
                .build(bookSearchParametersDto)).thenReturn(Specification.where(null));
        when(bookRepository
                .findAll(Specification.where(null)))
                .thenReturn(List.of(books.get(BOOK_1984_ID)));
        when(bookMapper.toDto(books.get(BOOK_1984_ID)))
                .thenReturn(bookDtos.get(BOOK_1984_ID));

        // then
        List<BookDto> expected = List.of(bookDtos.get(BOOK_1984_ID));
        List<BookDto> actual = bookServiceImpl.getBookDtosByParameters(bookSearchParametersDto);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(bookRepository, times(ONE_TIME)).findAll(Specification.where(null));
    }

    @Test
    @DisplayName("""
            Verify getBookDtosByCategoryId() method with correct categoryId
            """)
    void getBooksByCategoryId_ValidCategoryId_ReturnOneBook() {
        // given
        BookDtoWithoutCategoriesIds greatGatsbyDto = new BookDtoWithoutCategoriesIds()
                .setTitle(books.get(GREAT_GATSBY_ID).getTitle())
                .setAuthor(books.get(GREAT_GATSBY_ID).getAuthor())
                .setPrice(books.get(GREAT_GATSBY_ID).getPrice())
                .setIsbn(books.get(GREAT_GATSBY_ID).getIsbn())
                .setDescription(books.get(GREAT_GATSBY_ID).getDescription())
                .setCoverImage(books.get(GREAT_GATSBY_ID).getCoverImage());

        // when
        when(bookRepository.findAllByCategoryId(VALID_CATEGORY_ID))
                .thenReturn(List.of(books.get(GREAT_GATSBY_ID)));
        when(bookMapper
                .toDtoWithoutCategoriesIds(books.get(GREAT_GATSBY_ID))).thenReturn(greatGatsbyDto);

        // then
        List<BookDtoWithoutCategoriesIds> expected = List.of(greatGatsbyDto);
        List<BookDtoWithoutCategoriesIds> actual
                = bookServiceImpl.getBookDtosByCategoryId(VALID_CATEGORY_ID);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(bookRepository, times(ONE_TIME))
                .findAllByCategoryId(VALID_CATEGORY_ID);
    }
}
