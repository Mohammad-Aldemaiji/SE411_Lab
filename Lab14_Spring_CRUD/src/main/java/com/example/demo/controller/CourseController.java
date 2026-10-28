package com.example.demo.controller;

import java.net.URI;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

import com.example.demo.model.Course;
import com.example.demo.repository.CourseRepository;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseRepository repo;
    private final JdbcAggregateTemplate template;

    public CourseController(CourseRepository repo, JdbcAggregateTemplate template) {
        this.repo = repo;
        this.template = template;
    }

    // POST /api/v1/courses -> 201 + Location, or 422 if id already exists
    @PostMapping
    public ResponseEntity<Course> create(@Valid @RequestBody Course course) {
        Course saved;
        if (course.id() != null) {
            if (repo.existsById(course.id())) {
                return ResponseEntity.unprocessableEntity().build(); // 422
            }
            saved = template.insert(course); // force INSERT with the given id
        } else {
            saved = repo.save(course); // id auto-generated
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(saved.id()).toUri();
        return ResponseEntity.created(location).body(saved); // 201
    }

    // GET /api/v1/courses -> 200
    @GetMapping
    public Iterable<Course> getAll() {
        return repo.findAll();
    }

    // GET /api/v1/courses/{id} -> 200 or 404
    @GetMapping("/{id}")
    public ResponseEntity<Course> getById(@PathVariable Long id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/v1/courses/{id} -> 204 or 404
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Course course) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.save(new Course(id, course.code(), course.name(), course.description(), course.credits()));
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/v1/courses/{id} -> 204 or 404
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}