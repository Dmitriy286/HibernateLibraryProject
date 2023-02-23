package org.library.util;

import org.library.models.Book;
import org.library.services.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {
    private final BooksService booksService;

    @Autowired
    public BookValidator(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (booksService.findByNameAndAuthorAndYear(book.getName(), book.getAuthor(), book.getYear()) != null) {
            errors.rejectValue("name", "", "Такая книга уже зарегистрирована в библиотечной системе");
        }
    }
}
