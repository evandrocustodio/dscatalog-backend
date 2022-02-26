package com.devsuperior.dscatalog.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.Factory;
import com.devsuperior.dscatalog.entities.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTests {

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductService productservice;

	private PageImpl<ProductDTO> page;
	private Long existingId; 
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception{		
		existingId =1L;
		nonExistingId =1000L;
		countTotalProducts= 25L;
	
/*
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
		*/

	}

	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
		ResultActions result = mockMvc.perform(get("/products?page=0&size=10&sort=name,asc").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));

	}
	

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

		ProductDTO dto = Factory.createProductDTO();
		String body = mapper.writeValueAsString(dto);
		
		String expectedName =dto.getName(); 

		ResultActions actions = mockMvc.perform(put("/products/{id}", existingId).content(body)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isCreated());
		actions.andExpect(jsonPath("$.id").exists());
		actions.andExpect(jsonPath("$.name").exists());
		actions.andExpect(jsonPath("$.description").exists());		
		actions.andExpect(jsonPath("$.name").value(expectedName));
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {

		ProductDTO dto = Factory.createProductDTO();
		String body = mapper.writeValueAsString(dto);

		ResultActions actions = mockMvc.perform(put("/products/{id}", nonExistingId).content(body)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
		actions.andExpect(status().isNotFound());
	}

}
