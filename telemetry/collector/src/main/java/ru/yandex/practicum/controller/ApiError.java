package ru.yandex.practicum.controller;

import lombok.Getter;

@Getter
public record ApiError(String message, String stackTrace) {
}
