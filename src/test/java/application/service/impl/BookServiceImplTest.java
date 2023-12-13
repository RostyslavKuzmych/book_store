package application.service.impl;

import application.dto.book.BookDto;
import application.dto.book.BookDtoWithoutCategoriesIds;
import application.dto.book.BookSearchParametersDto;
import application.dto.book.CreateBookRequestDto;
import application.exception.EntityNotFoundException;
import application.mapper.BookMapper;
import application.model.Book;
import application.repository.BookRepository;
import application.repository.builders.BookSpecificationBuilder;
import application.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static final Integer GREAT_GATSBY_ID = 0;
    private static final Integer PRIDE_AND_PREJUDICE_ID = 1;
    private static final Integer BOOK_1984_ID = 2;
    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 20;
    private static final Long VALID_BOOK_ID = 1L;
    private static final Long INVALID_BOOK_ID = 20L;
    private static final Long VALID_CATEGORY_ID = 1L;
    private static final Integer ONE_TIME = 1;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder specificationBuilder;
    @InjectMocks
    private BookServiceImpl bookServiceImpl;
    private static List<Book> books;

    @BeforeAll
    static void beforeAll() {
        Book greatGatsby = new Book().setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9780743273565")
                .setPrice(BigDecimal.valueOf(11))
                .setDescription("The story of the fabulously wealthy Jay Gatsby")
                .setCoverImage("https://example.com/book1-cover-image.jpg");
        Book prideAndPrejudice = new Book().setTitle("Pride and Prejudice")
                .setAuthor("Jane Austen")
                .setIsbn("9780141439518")
                .setPrice(BigDecimal.valueOf(20))
                .setDescription("A romantic novel")
                .setCoverImage("https://example.com/book1-cover-image.jpg");
        Book book1984 = new Book().setTitle("1984")
                .setAuthor("George Orwell")
                .setIsbn("9780451524935")
                .setPrice(BigDecimal.valueOf(15))
                .setDescription("A dystopian social science fiction novel")
                .setCoverImage("https://example.com/book1-cover-image.jpg");
        books = List.of(greatGatsby, prideAndPrejudice, book1984);
    }

    @Test
    @DisplayName("""
            Verify createBook() method with correct requestDto
            """)
    void saveBook_ValidRequestDto_ReturnBookDto() {
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

        when(bookMapper.toModel(theHobbitRequestDto)).thenReturn(theHobbit);
        when(bookRepository.save(theHobbit)).thenReturn(theHobbit);
        when(bookMapper.toDto(theHobbit)).thenReturn(theHobbitDto);

        BookDto actual = bookServiceImpl.createBook(theHobbitRequestDto);
        assertNotNull(actual);
        assertEquals(theHobbitDto, actual);
        verify(bookRepository, times(ONE_TIME)).save(theHobbit);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Verify findAll() method with correct pageable
            """)
    void findAllBooks_ValidPageable_ReturnExpectedDtoList() {
        BookDto greatGatsbyDto = new BookDto()
                .setTitle(books.get(GREAT_GATSBY_ID).getTitle())
                .setAuthor(books.get(GREAT_GATSBY_ID).getAuthor())
                .setPrice(books.get(GREAT_GATSBY_ID).getPrice())
                .setIsbn(books.get(GREAT_GATSBY_ID).getIsbn())
                .setDescription(books.get(GREAT_GATSBY_ID).getDescription())
                .setCoverImage(books.get(GREAT_GATSBY_ID).getCoverImage());
        BookDto prideAndPrejudiceDto = new BookDto()
                .setTitle(books.get(PRIDE_AND_PREJUDICE_ID).getTitle())
                .setAuthor(books.get(PRIDE_AND_PREJUDICE_ID).getAuthor())
                .setPrice(books.get(PRIDE_AND_PREJUDICE_ID).getPrice())
                .setIsbn(books.get(PRIDE_AND_PREJUDICE_ID).getIsbn())
                .setDescription(books.get(PRIDE_AND_PREJUDICE_ID).getDescription())
                .setCoverImage(books.get(PRIDE_AND_PREJUDICE_ID).getCoverImage());
        BookDto book1984Dto = new BookDto()
                .setTitle(books.get(BOOK_1984_ID).getTitle())
                .setAuthor(books.get(BOOK_1984_ID).getAuthor())
                .setPrice(books.get(BOOK_1984_ID).getPrice())
                .setIsbn(books.get(BOOK_1984_ID).getIsbn())
                .setDescription(books.get(BOOK_1984_ID).getDescription())
                .setCoverImage(books.get(BOOK_1984_ID).getCoverImage());
        PageImpl<Book> bookPage = new PageImpl<>(books);
        when(bookRepository.findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE)))
                .thenReturn(bookPage);
        when(bookMapper.toDto(books.get(GREAT_GATSBY_ID))).thenReturn(greatGatsbyDto);
        when(bookMapper.toDto(books.get(PRIDE_AND_PREJUDICE_ID))).thenReturn(prideAndPrejudiceDto);
        when(bookMapper.toDto(books.get(BOOK_1984_ID))).thenReturn(book1984Dto);

        List<BookDto> expected = List.of(greatGatsbyDto, prideAndPrejudiceDto, book1984Dto);
        List<BookDto> actual = bookServiceImpl
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
        assertEquals(3, actual.size());
        assertEquals(expected, actual);
        verify(bookRepository, times(ONE_TIME))
                .findAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Verify getBookDtoById() with correct bookId
            """)
    void findBookById_ValidBookId_ReturnBookDto() {
        BookDto prideAndPrejudiceDto = new BookDto()
                .setTitle(books.get(PRIDE_AND_PREJUDICE_ID).getTitle())
                .setAuthor(books.get(PRIDE_AND_PREJUDICE_ID).getAuthor())
                .setPrice(books.get(PRIDE_AND_PREJUDICE_ID).getPrice())
                .setIsbn(books.get(PRIDE_AND_PREJUDICE_ID).getIsbn())
                .setDescription(books.get(PRIDE_AND_PREJUDICE_ID).getDescription())
                .setCoverImage(books.get(PRIDE_AND_PREJUDICE_ID).getCoverImage());

        when(bookRepository
                .findById(VALID_BOOK_ID))
                .thenReturn(Optional.ofNullable(books.get(PRIDE_AND_PREJUDICE_ID)));
        when(bookMapper.toDto(books.get(PRIDE_AND_PREJUDICE_ID))).thenReturn(prideAndPrejudiceDto);

        BookDto actual = bookServiceImpl.getBookDtoById(VALID_BOOK_ID);
        assertNotNull(actual);
        assertEquals(prideAndPrejudiceDto, actual);
        verify(bookRepository, times(ONE_TIME)).findById(VALID_BOOK_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Verify getBookDtoById() with invalid bookId
            """)
    void findBookById_InvalidBookId_ReturnException() {

        when(bookRepository.findById(INVALID_BOOK_ID))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookServiceImpl.getBookDtoById(INVALID_BOOK_ID));
        String expected = "Can't find book by id " + INVALID_BOOK_ID;
        String actual = exception.getMessage();
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(bookRepository, times(ONE_TIME)).findById(INVALID_BOOK_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Verify updateBook() method with correct bookId
            """)
    void updateBook_ValidBookId_ReturnBookDto() {
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

        when(bookRepository.findById(VALID_BOOK_ID))
                .thenReturn(Optional.ofNullable(books.get(BOOK_1984_ID)));
        when(bookMapper.toModel(animalFarmRequestDto)).thenReturn(animalFarm);
        when(bookRepository.save(animalFarm)).thenReturn(animalFarm);
        when(bookMapper.toDto(animalFarm)).thenReturn(animalFarmDto);

        BookDto actual = bookServiceImpl.updateBook(VALID_BOOK_ID, animalFarmRequestDto);
        assertNotNull(actual);
        assertEquals(animalFarmDto, actual);
        verify(bookRepository, times(ONE_TIME)).findById(VALID_BOOK_ID);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Verify getBookDtosByParameters() method with params
            """)
    void getBookByParams_ValidParams_ReturnExpectedList() {
        BookSearchParametersDto bookSearchParametersDto
                = new BookSearchParametersDto(new String[]{"1984"},
                new String[]{"George Orwell"});
        BookDto book1984Dto = new BookDto()
                .setTitle(books.get(BOOK_1984_ID).getTitle())
                .setAuthor(books.get(BOOK_1984_ID).getAuthor())
                .setPrice(books.get(BOOK_1984_ID).getPrice())
                .setIsbn(books.get(BOOK_1984_ID).getIsbn())
                .setDescription(books.get(BOOK_1984_ID).getDescription())
                .setCoverImage(books.get(BOOK_1984_ID).getCoverImage());

        when(specificationBuilder
                .build(bookSearchParametersDto)).thenReturn(Specification.where(null));
        when(bookRepository
                .findAll(Specification.where(null)))
                .thenReturn(List.of(books.get(BOOK_1984_ID)));
        when(bookMapper.toDto(books.get(BOOK_1984_ID))).thenReturn(book1984Dto);

        List<BookDto> expected = List.of(book1984Dto);
        List<BookDto> actual = bookServiceImpl.getBookDtosByParameters(bookSearchParametersDto);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(bookRepository, times(ONE_TIME)).findAll(Specification.where(null));
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("""
            Verify getBookDtosByCategoryId() method with correct categoryId
            """)
    void getBooks_ValidCategoryId_ReturnExpectedList() {
        BookDtoWithoutCategoriesIds greatGatsbyDto = new BookDtoWithoutCategoriesIds()
                .setTitle(books.get(GREAT_GATSBY_ID).getTitle())
                .setAuthor(books.get(GREAT_GATSBY_ID).getAuthor())
                .setPrice(books.get(GREAT_GATSBY_ID).getPrice())
                .setIsbn(books.get(GREAT_GATSBY_ID).getIsbn())
                .setDescription(books.get(GREAT_GATSBY_ID).getDescription())
                .setCoverImage(books.get(GREAT_GATSBY_ID).getCoverImage());

        when(bookRepository.findAllByCategoryId(VALID_CATEGORY_ID))
                .thenReturn(List.of(books.get(GREAT_GATSBY_ID)));
        when(bookMapper
                .toDtoWithoutCategoriesIds(books.get(GREAT_GATSBY_ID))).thenReturn(greatGatsbyDto);

        List<BookDtoWithoutCategoriesIds> expected = List.of(greatGatsbyDto);
        List<BookDtoWithoutCategoriesIds> actual
                = bookServiceImpl.getBookDtosByCategoryId(VALID_CATEGORY_ID);
        assertEquals(1, actual.size());
        assertEquals(expected, actual);
        verify(bookRepository, times(ONE_TIME))
                .findAllByCategoryId(VALID_CATEGORY_ID);
        verifyNoMoreInteractions(bookRepository);
    }
}
