package ru.yandex.practicum.interaction.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.interaction.exception.shopping.cart.NotAuthorizedUserException;

public class UsernameAuthCheckValidator implements ConstraintValidator<UsernameAuthCheck, String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (username == null || username.isBlank()) {
            throw new NotAuthorizedUserException();
        }

        return true;
    }
}
