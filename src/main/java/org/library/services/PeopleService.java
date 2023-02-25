package org.library.services;

import net.bytebuddy.implementation.bytecode.member.HandleInvocation;
import org.hibernate.Hibernate;
import org.library.models.Book;
import org.library.models.Person;
import org.library.repositories.BooksRepository;
import org.library.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
//        Person person = findById(personId);
//        List<Book> books = booksRepository.findAllByPerson(person);
//
//        return books;
        System.out.println("Запрос к БД (человек):");
        Optional<Person> person = peopleRepository.findById(personId);

        if (person.isPresent()) {
            System.out.println("Гибер инициализация книг:");
            Hibernate.initialize(person.get().getBooks());
            System.out.println("Возвращаем список книг (еще один вызов геттера):");
            return person.get().getBooks();
        } else {
            return Collections.emptyList();
        }

    }
}
