package edu.psu.se411.lab13.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Product model. Money uses BigDecimal (exact decimal arithmetic) instead of Double,
 * matching DECIMAL(10,2) in schema.sql.
 */
public record Product(
		@Id
		@Positive(message = "Product id must be a positive number")
		Integer id,

		@NotBlank(message = "Product name cannot be empty")
		@Size(max = 100, message = "Product name cannot be longer than 100 characters")
		String name,

		@Size(max = 100, message = "Description cannot be longer than 100 characters")
		String description,

		@NotNull(message = "Price is required")
		@DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0")
		@Digits(integer = 8, fraction = 2, message = "Price must have at most 8 digits and 2 decimals")
		BigDecimal price
) {
}
