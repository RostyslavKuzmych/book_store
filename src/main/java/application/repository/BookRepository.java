package application.repository;

import application.model.Book;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    @Transactional
    @Modifying
    @Query("UPDATE Book b SET b.author = :author, b.coverImage = :coverImage, "
            + "b.title = :title, b.description = :description, b.price = :price where b.id = :id")
    void updateBook(Long id, String author, String title, String coverImage,
                    String description, BigDecimal price);

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<Book> findAllByCategoryId(Long categoryId);
}
