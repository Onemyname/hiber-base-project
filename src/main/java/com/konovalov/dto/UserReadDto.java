package com.konovalov.dto;

public record UserReadDto(Long id, String name, CompanyReadDto company) {
}
