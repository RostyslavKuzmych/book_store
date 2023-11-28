package application.controller;

import application.dto.book.BookDto;
import application.dto.book.BookSearchParametersDto;
import application.dto.book.CreateBookRequestDto;
import application.mapper.BookMapper;
import application.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    @Operation(summary = "Get all books from db",
            description = "Get a list of all available books")
    public List<BookDto> getAll(Pageable pageable) {
        return bookService.findAll(pageable)
                .stream()
                .map(b -> bookMapper.toDto(b))
                .toList();
    }

    @Operation(summary = "Get a book by id", description = "Get a book by id from db")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookMapper.toDto(bookService.getBookById(id));
    }

    @Operation(summary = "Create a new book", description = "Can save inputs to db")
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookService.createBook(requestDto));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update a book by id", description = "Update a book in db by id")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookService.updateBook(id, bookMapper.toModel(requestDto)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a book by id", description = "Delete a book in db by id")
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Find all books by inputs",
            description = "Find a list of books by inputs from db")
    public ResponseEntity<List<BookDto>> getAllByInputs(BookSearchParametersDto
                                                                    bookSearchParametersDto) {
        List<BookDto> bookDtoList = bookService.booksByParameters(bookSearchParametersDto)
                .stream()
                .map(bookMapper::toDto)
                .toList();
        return !bookDtoList.isEmpty()
                ? new ResponseEntity<>(bookDtoList, HttpStatus.ACCEPTED)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
