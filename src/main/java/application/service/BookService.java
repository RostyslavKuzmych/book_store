package application.service;

import application.dto.BookSearchParametersDto;
import application.dto.CreateBookRequestDto;
import application.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();

    Book getBookById(Long id);

    Book createBook(CreateBookRequestDto requestDto);

    Book updateBook(Long id, Book book);

    void deleteBookById(Long id);

    List<Book> booksByParameters(BookSearchParametersDto bookSearchParametersDto);
}
