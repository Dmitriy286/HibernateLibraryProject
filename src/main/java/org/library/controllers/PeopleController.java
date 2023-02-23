package org.library.controllers;

import org.library.models.Book;
import org.library.models.Person;
import org.library.services.PeopleService;
import org.library.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String showAllPeople(Model model) {
        List<Person> people = peopleService.findAll();
        model.addAttribute("people", people);

        return "people/show-all";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findById(id);
        List<Book> books = peopleService.getPersonBooks(id);
        model.addAttribute("person", person);
        model.addAttribute("books", books);

        return "people/show";
    }

    @GetMapping("/new")
    public String getNewPersonForm(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        System.out.println(person);
        peopleService.save(person);

        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String getEditPersonForm(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findById(id);
        model.addAttribute("person", person);

        return "people/edit";
    }

    @PostMapping("/{id}")
    public String editPerson(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                             BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        peopleService.update(id, person);

        return "redirect:/people";
    }

    @PostMapping("/{id}/delete")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.delete(id);

        return "redirect:/people";
    }
}
