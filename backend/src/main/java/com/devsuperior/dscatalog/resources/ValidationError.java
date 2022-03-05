package com.devsuperior.dscatalog.resources;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

	private static final long serialVersionUID = 1L;

	private List<FieldMessage> erros = new ArrayList<FieldMessage>();

	public List<FieldMessage> getErros() {
		return erros;
	}

	public void addErros(String field, String message) {
		erros.add(new FieldMessage(field, message));
	}
}
