package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.entities.CategoryDTO;
import com.devsuperior.dscatalog.services.Categoryservice;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	@Autowired
	private Categoryservice categoryservice;

	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		List<CategoryDTO> categories = categoryservice.findAll();

		return ResponseEntity.ok().body(categories);

	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		CategoryDTO categorie = categoryservice.findById(id);
		return ResponseEntity.ok().body(categorie);
	}
	}
