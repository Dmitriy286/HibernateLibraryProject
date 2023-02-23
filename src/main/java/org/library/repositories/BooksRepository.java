package org.library.repositories;

import org.library.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByPersonId(int personId);

    Book findByNameAuthorAndYear(String name, String author, int year);
}
