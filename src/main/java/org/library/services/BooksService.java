package org.library.services;

import org.library.models.Book;
import org.library.models.Person;
import org.library.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final PeopleService peopleService;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleService peopleService) {
        this.booksRepository = booksRepository;
        this.peopleService = peopleService;
    }

    public List<Book> findAll() {
        List<Book> books = booksRepository.findAll();

        return books;
    }

    public Book findById(int id) {
        Optional<Book> book = booksRepository.findById(id);

        return book.orElse(null);
    }

    public Book findByNameAndAuthorAndYear(String bookName, String author, int year) {
        Book foundBook = booksRepository.findByNameAndAuthorAndYear(bookName, author, year);
        return foundBook;
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setBookId(id);
        booksRepository.save(book);

    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void setToPerson(int bookId, int personId) {
        Book givenBook = findById(bookId);
        System.out.println(givenBook);
        Person targetPerson = peopleService.findById(personId);
        System.out.println(targetPerson);
        System.out.println(targetPerson.getBooks());
        if (targetPerson.getBooks().isEmpty()) {
            targetPerson.setBooks(new ArrayList<>(Collections.singletonList(givenBook)));
        } else {
            targetPerson.getBooks().add(givenBook);
        }
        givenBook.setPerson(targetPerson);
        System.out.println(targetPerson.getBooks());

        peopleService.save(targetPerson);
        System.out.println("Success!");
    }

    @Transactional
    public void returnBook(int bookId) {
        Book returnedBook = findById(bookId);
        returnedBook.setPerson(null);
        Person targetPerson = returnedBook.getPerson();
        targetPerson.getBooks().remove(returnedBook);

        peopleService.save(targetPerson);
    }
}
