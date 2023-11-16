package application.mapper;

import application.config.MapperConfig;
import application.dto.BookDto;
import application.dto.CreateBookRequestDto;
import application.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
