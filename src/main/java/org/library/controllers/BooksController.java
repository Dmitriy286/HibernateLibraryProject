package org.library.controllers;

import org.library.models.Book;
import org.library.models.Person;
import org.library.services.BooksService;
import org.library.services.PeopleService;
import org.library.util.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService, BookValidator bookValidator) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.bookValidator = bookValidator;
    }

    @GetMapping()
    public String showAllBooks(@RequestParam("page") int pageNumber,
                               @RequestParam("books_per_page") int booksPerPage,
                               @RequestParam("sort_by_year") boolean sortByYear,
                               Model model) {

        List<Book> books = booksService.findPerPageAndSort(pageNumber, booksPerPage, sortByYear);
        int booksQuantity = booksService.getBooksQuantity();
        int totalPageNumbers = (booksQuantity % booksPerPage) == 0 ? booksQuantity / booksPerPage : booksQuantity / booksPerPage + 1;

        model.addAttribute("books", books);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("booksPerPage", booksPerPage);
        model.addAttribute("isSorted", sortByYear);
        model.addAttribute("booksQuantity", booksQuantity);
        model.addAttribute("totalPageNumbers", totalPageNumbers);

        return "books/show-all";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model, @ModelAttribute("emptyPerson") Person emptyPerson) {
        Book book = booksService.findById(id);
        Person person = book.getPerson() != null ? peopleService.findById(book.getPerson().getPersonId()) : null;
        List<Person> people = peopleService.findAll();

        model.addAttribute("book", book);
        model.addAttribute("person", person);
        model.addAttribute("people", people);

        return "books/show";
    }

    @GetMapping("/new")
    public String getNewBookForm(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.save(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String getEditBookForm(@PathVariable("id") int id, Model model) {
        Book book = booksService.findById(id);
        model.addAttribute("book", book);

        return "books/edit";
    }

    @PostMapping("/{id}")
    public String editBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.update(id, book);

        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.delete(id);

        return "redirect:/books";
    }

    @PostMapping("/{id}/setbook")
    public String setBookToPerson(@PathVariable("id") int id, @ModelAttribute("emptyPerson") Person emptyPerson) {
        booksService.setToPerson(id, emptyPerson.getPersonId());

        return "redirect:/books/{id}";
    }

    @PostMapping("/{id}/returnbook")
    public String returnBook(@PathVariable("id") int id) {
        booksService.returnBook(id);

        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String getSearchForm(@RequestParam(required = false, value = "pattern") String pattern, Model model) {
        Book book;

        if (pattern != null) {
            book = booksService.searchBook(pattern);
        } else {
            book = null;
        }

        model.addAttribute("book", book);
        model.addAttribute("pattern", pattern);

        return "books/search";
    }
}
