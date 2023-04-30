package org.library.services;

import org.hibernate.Hibernate;
import org.library.models.Book;
import org.library.models.Person;
import org.library.repositories.BooksRepository;
import org.library.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        Optional<Person> foundPerson = peopleRepository.findById(id);

        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setPersonId(id);

        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        peopleRepository.deleteById(id);
    }

    public List<Book> getPersonBooks(int personId) {
        checkDates();

        Optional<Person> person = peopleRepository.findById(personId);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }
    }

    public void checkDates() {
        List<Book> issuedBooks = booksRepository.findAll().stream().filter(e -> e.getPerson() != null).collect(Collectors.toList());
        Date currentDate = new Date();

        for (Book book : issuedBooks) {
            Date bookRentDate = book.getRentDate();
            int goneTime = (int) (currentDate.getTime() / 86_400_000 - bookRentDate.getTime() / 86_400_000);

            if (goneTime > 10) {
                book.setOutOfDate(true);
            }
        }
    }

}
