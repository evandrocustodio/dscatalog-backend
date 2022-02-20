package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.entities.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productservice;

	private PageImpl<ProductDTO> page;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 1500L;

		page = new PageImpl<ProductDTO>(List.of(Factory.createProductDTO()));

		Mockito.when(productservice.findAllPaged((Pageable) ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(productservice.findById(existingId)).thenReturn(Factory.createProductDTO());
		Mockito.when(productservice.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		Mockito.when(productservice.update(eq(existingId), any())).thenReturn(Factory.createProductDTO());
		Mockito.when(productservice.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		Mockito.when(productservice.insert((ProductDTO) any())).thenReturn(new ProductDTO(Factory.createProduct()));
		
		Mockito.doNothing().when(productservice).delete(existingId);
		Mockito.doThrow(ResourceNotFoundException.class).when(productservice).delete(nonExistingId);
		Mockito.doThrow(DatabaseException.class).when(productservice).delete(dependentId);
		

	}

	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions actions = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isOk());
	}

	@Test
	public void findAllShouldReturnProductDTOWhenIdExists() throws Exception {
		ResultActions actions = mockMvc.perform(get("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isOk());
		actions.andExpect(jsonPath("$.id").exists());
		actions.andExpect(jsonPath("$.name").exists());
		actions.andExpect(jsonPath("$.description").exists());
	}

	@Test
	public void findAllShouldReturnResourceNotFoundException() throws Exception {
		ResultActions actions = mockMvc
				.perform(get("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isNotFound());
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

		String body = mapper.writeValueAsString(Factory.createProductDTO());

		ResultActions actions = mockMvc.perform(put("/products/{id}", existingId).content(body)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isCreated());
		actions.andExpect(jsonPath("$.id").exists());
		actions.andExpect(jsonPath("$.name").exists());
		actions.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void deleteShouldReturnResourceNotFoundException() throws Exception {
		ResultActions actions = mockMvc
				.perform(delete("/products/{id}", nonExistingId).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnDatabaseException() throws Exception {
		ResultActions actions = mockMvc
				.perform(delete("/products/{id}", dependentId).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isBadRequest());
	}
	
	@Test
	public void deleteShouldReturnNothing() throws Exception {
		ResultActions actions = mockMvc
				.perform(delete("/products/{id}", existingId).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isNoContent());
	}

	@Test
	public void insertShouldReturnProduct() throws Exception {

		String body = mapper.writeValueAsString(Factory.createProductDTO());

		ResultActions actions = mockMvc.perform(post("/products").content(body)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isCreated());
		actions.andExpect(jsonPath("$.id").exists());
	}

}
