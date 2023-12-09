package application.service;

import application.dto.book.BookDto;
import application.dto.book.BookDtoWithoutCategoriesIds;
import application.dto.book.BookSearchParametersDto;
import application.dto.book.CreateBookRequestDto;
import application.model.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(Book book);

    List<BookDto> findAll(Pageable pageable);

    BookDto getBookDtoById(Long bookId);

    BookDto createBook(CreateBookRequestDto requestDto);

    BookDto updateBook(Long id, CreateBookRequestDto requestDto);

    void deleteBookById(Long bookId);

    List<BookDto> getBookDtosByParameters(BookSearchParametersDto bookSearchParametersDto);

    List<BookDtoWithoutCategoriesIds> getBookDtosByCategoryId(Long categoryId);
}
