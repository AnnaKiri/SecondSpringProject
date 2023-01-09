package ru.kirillova.springcourse.dao;

import org.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import ru.kirillova.springcourse.models.*;

import java.util.*;

@Component
public class BooksDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public BooksDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional(readOnly = true)
    public List<Book> index() {
        Session session = sessionFactory.getCurrentSession();

        List<Book> books = session.createQuery("select p from Book p", Book.class).getResultList();

        return books;
    }

    public Book show(int id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(Book.class, id);
    }

    public List<Book> showBooksList(int id) {
        Session session = sessionFactory.getCurrentSession();
        Person person = session.get(Person.class, id);
        Hibernate.initialize(person.getBooks());
        return person.getBooks();
    }

    public Optional<Person> showPerson(int id) {
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        return Optional.ofNullable(book.getOwner());
    }

    public void save(Book book) {
        Session session = sessionFactory.getCurrentSession();
        session.save(book);
    }

    public void update(int id, Book updatedBook) {
        Session session = sessionFactory.getCurrentSession();
        Book bookToBeUpdated = session.get(Book.class, id);
        bookToBeUpdated.setTitle(updatedBook.getTitle());
        bookToBeUpdated.setAuthor(updatedBook.getAuthor());
        bookToBeUpdated.setYear(updatedBook.getYear());
    }

    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(session.get(Book.class, id));
    }

    public void release(int id) {
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, id);
        book.setOwner(null);
    }

    public void assignBook(int idBook, int idPerson) {
        Session session = sessionFactory.getCurrentSession();
        Book book = session.get(Book.class, idBook);
        book.setOwner(session.get(Person.class, idPerson));
    }
}

