package org.library.util;

import org.library.models.Person;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;

@Component
public class PersonValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();

        if ((currentYear - 10) < person.getYearOfBirth()) {
            errors.rejectValue("yearOfBirth", "", "В библиотеке можно регистрировать лиц только старше 10 лет");
        }
    }
}
