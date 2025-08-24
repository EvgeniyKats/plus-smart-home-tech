package ru.yandex.practicum.interaction.validator;

import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameAuthCheckValidator.class)
public @interface UsernameAuthCheck {
    String message() default "Имя пользователя не должно быть пустым";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
