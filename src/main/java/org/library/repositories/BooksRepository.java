package org.library.repositories;

import org.library.models.Book;
import org.library.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByPerson(Person person);
    Book findByNameAndAuthorAndYear(String name, String author, int year);

    Optional<Book> findByNameStartingWithIgnoreCase(String pattern);
}
