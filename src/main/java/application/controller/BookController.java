package application.controller;

import application.dto.BookDto;
import application.dto.BookSearchParametersDto;
import application.dto.CreateBookRequestDto;
import application.mapper.BookMapper;
import application.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping
    public List<BookDto> getAll() {
        return bookService.findAll()
                .stream()
                .map(b -> bookMapper.toDto(b))
                .toList();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookMapper.toDto(bookService.getBookById(id));
    }

    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookService.createBook(requestDto));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id, @RequestBody CreateBookRequestDto requestDto) {
        return bookMapper.toDto(bookService.updateBook(id, bookMapper.toModel(requestDto)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @GetMapping("/search")
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
