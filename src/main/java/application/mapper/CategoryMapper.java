package application.mapper;

import application.config.MapperConfig;
import application.dto.category.CategoryDto;
import application.dto.category.CategoryRequestDto;
import application.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryDto);
}
