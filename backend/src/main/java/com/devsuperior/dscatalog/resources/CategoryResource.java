package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.entities.Category;
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

}	
