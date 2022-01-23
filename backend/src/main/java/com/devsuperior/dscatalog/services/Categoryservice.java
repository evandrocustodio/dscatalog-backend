package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.CategoryDTO;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

@Service
public class Categoryservice {

	@Autowired
	private CategoryRepository categoryRepository;

	public Categoryservice() {
	}
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> categories = categoryRepository.findAll();
		return categories.stream().map( c -> new CategoryDTO(c) ).collect(Collectors.toList());
	}
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> object = categoryRepository.findById(id);		
		return new CategoryDTO(object.get());
	}

}
