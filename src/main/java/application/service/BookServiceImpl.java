package application.service;

import application.dto.CreateBookRequestDto;
import application.exception.EntityNotFoundException;
import application.mapper.BookMapper;
import application.model.Book;
import application.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book createBook(CreateBookRequestDto requestDto) {
        return save(bookMapper.toModel(requestDto));
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't get book by id " + id));
    }

    @Override
    public Book updateBook(Long id, Book inputBook) {
        bookRepository.updateBook(id, inputBook.getAuthor(), inputBook.getTitle(),
                inputBook.getCoverImage(), inputBook.getDescription(), inputBook.getPrice());
        return Optional.of(getBookById(id))
                .orElseThrow(() -> new EntityNotFoundException("Can't find book"));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
