package application.service;

import application.dto.book.BookSearchParametersDto;
import application.dto.book.CreateBookRequestDto;
import application.model.Book;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Book save(Book book);

    List<Book> findAll(Pageable pageable);

    Book getBookById(Long id);

    Book createBook(CreateBookRequestDto requestDto);

    Book updateBook(Long id, Book book);

    void deleteBookById(Long id);

    List<Book> booksByParameters(BookSearchParametersDto bookSearchParametersDto);

    List<Book> getBooksByCategoryId(Long id);
}
