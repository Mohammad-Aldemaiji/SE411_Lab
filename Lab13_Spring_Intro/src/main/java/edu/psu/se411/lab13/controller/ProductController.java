package edu.psu.se411.lab13.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.psu.se411.lab13.model.Product;
import edu.psu.se411.lab13.service.ProductService;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	private final ProductService productService;

	// Spring injects the ProductService bean from its IoC container
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	/** GET /api/v1/products : all products */
	@GetMapping()
	public ResponseEntity<Iterable<Product>> findAll() {
		return ResponseEntity.ok(productService.findAll());
	}

	/**
	 * GET /api/v1/products/{id} : one product.
	 * 200 if found, 404 if no product has this id,
	 * 400 Bad Request if the id is not positive (e.g. /api/v1/products/-5).
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Product> findById(
			@PathVariable @Positive(message = "Product id must be a positive number") Integer id) {
		return productService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
