package edu.psu.se411.lab13.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.psu.se411.lab13.model.Product;

/**
 * Spring Data generates the implementation: CRUD operations with paging and sorting.
 */
@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>, PagingAndSortingRepository<Product, Integer> {

}
