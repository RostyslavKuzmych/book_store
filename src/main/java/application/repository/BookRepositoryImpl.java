package application.repository;

import application.model.Book;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            try {
                transaction = session.beginTransaction();
                session.persist(book);
                transaction.commit();
                return book;
            } catch (Exception exception) {
                if (transaction != null) {
                    transaction.rollback();
                }
                throw new RuntimeException("Can't save book: " + book, exception);
            }
        }
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Book> sessionQuery = session.createQuery("from Book", Book.class);
            return sessionQuery.getResultList();
        }
    }
}
