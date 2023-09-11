package com.konovalov.dto;



import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCreateDto(Long id, @NotNull String name, Long companyId) {
}
