package com.example.demo.model;

import org.springframework.data.annotation.Id;
import jakarta.validation.constraints.*;

public record Course(
    @Id @PositiveOrZero Long id,
    @NotBlank String code,
    @NotBlank @Size(min = 2) String name,
    String description,
    @NotNull @Min(1) @Max(10) Integer credits
) {}