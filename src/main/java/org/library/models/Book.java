package org.library.models;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.*;

public class Book {
    private int bookId;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person person;

    @NotEmpty(message = "Введите название книги")
    @Size(min = 1, max = 50, message = "Название книги должно содержать от 1 до 50 символов")
    private String name;
    @NotEmpty(message = "Введите имя автора")
    @Size(min = 2, max = 50, message = "Имя автора должно содержать от 2 до 50 символов")
    private String author;
    @Min(value = 1452, message = "Рукописные издания допечатной эры не хранятся в нашей библиотеке")
    @Max(value = 2023, message = "Введите год до 2023")
    private int year;


    public Book() {

    }


    public Book(String name, String author, int year) {
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
            this.person = person;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        int persId = this.person == null ? 0 : this.person.getPersonId();
        return getBookId() + ", " + persId + ", " + getName();
    }
}
