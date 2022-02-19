package com.devsuperior.dscatalog;

import java.time.Instant;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.entities.ProductDTO;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L, "Phone","Good Price", 25.32, "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/1-big.jpg", Instant.parse("2020-07-13T20:50:07.12345Z"));
		product.getCategories().add(new Category(2L,"Electonics"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}

}
