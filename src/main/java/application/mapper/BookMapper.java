package application.mapper;

import application.config.MapperConfig;
import application.dto.book.BookDto;
import application.dto.book.BookDtoWithoutCategoriesIds;
import application.dto.book.CreateBookRequestDto;
import application.model.Book;
import application.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    BookDtoWithoutCategoriesIds toDtoWithoutCategoriesIds(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    Book toModelFromDto(BookDto bookDto);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> longList = book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoriesIds((longList));
    }

}
