package org.library.services;

import org.library.models.Book;
import org.library.models.Person;
import org.library.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
        return booksRepository.findAll();
    }

    public List<Book> findPerPageAndSort(int pageNumber, int booksPerPage, boolean isSorted) {
        if (isSorted) {
            return booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage, Sort.by("year"))).getContent();
        } else {
            return booksRepository.findAll(PageRequest.of(pageNumber, booksPerPage)).getContent();
        }
    }

    public int getBooksQuantity() {
        return booksRepository.findAll().size();
    }

    public Book findById(int id) {
        Optional<Book> book = booksRepository.findById(id);

        return book.orElse(null);
    }

    public Book findByNameAndAuthorAndYear(String bookName, String author, int year) {
        return booksRepository.findByNameAndAuthorAndYear(bookName, author, year);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Person person = booksRepository.findById(id).get().getPerson();

        updatedBook.setBookId(id);
        updatedBook.setPerson(person);

        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void setToPerson(int bookId, int personId) {
        Book givenBook = findById(bookId);
        Person targetPerson = peopleService.findById(personId);

        if (targetPerson.getBooks().isEmpty()) {
            targetPerson.setBooks(new ArrayList<>(Collections.singletonList(givenBook)));
        } else {
            targetPerson.getBooks().add(givenBook);
        }

        givenBook.setRentDate(new Date());
        givenBook.setPerson(targetPerson);

        peopleService.save(targetPerson);
    }

    @Transactional
    public void returnBook(int bookId) {
        Book returnedBook = findById(bookId);
        Person targetPerson = returnedBook.getPerson();

        returnedBook.setPerson(null);
        returnedBook.setRentDate(null);

        targetPerson.getBooks().remove(returnedBook);
    }

    public Book searchBook(String pattern) {
        Optional<Book> book = booksRepository.findByNameStartingWithIgnoreCase(pattern);

        return book.orElse(null);
    }

}
