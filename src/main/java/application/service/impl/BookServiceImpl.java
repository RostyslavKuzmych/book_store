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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private static final String FIND_EXCEPTION = "Can't find a book by id ";
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(Book book) {
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto createBook(CreateBookRequestDto requestDto) {
        return save(bookMapper.toModel(requestDto));
    }

    @Override
    public BookDto getBookDtoById(Long bookId) {
        Book book = findById(bookId);
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(Long bookId, CreateBookRequestDto requestDto) {
        Book book = findById(bookId);
        Book inputBook = bookMapper.toModel(requestDto);
        inputBook.setId(bookId);
        return bookMapper.toDto(bookRepository.save(inputBook));
    }

    @Override
    public void deleteBookById(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public List<BookDto> getBookDtosByParameters(
            BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = bookSpecificationBuilder.build(bookSearchParametersDto);
        return bookRepository
                .findAll(specification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoriesIds> getBookDtosByCategoryId(Long categoryId) {
        return bookRepository
                .findAllByCategoryId(categoryId)
                .stream()
                .map(bookMapper::toDtoWithoutCategoriesIds)
                .toList();
    }

    private Book findById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException(FIND_EXCEPTION + bookId));
    }
}
