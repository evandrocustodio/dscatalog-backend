package com.devsuperior.dscatalog.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.entities.UserUpdateDTO;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.FieldMessage;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

	@Autowired
	HttpServletRequest request;
	@Autowired
	UserRepository userRepository;

	@Override
	public void initialize(UserUpdateValid constraintAnnotation) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid(UserUpdateDTO value, ConstraintValidatorContext context) {
		
		Map<String,String> parametros = (Map<String,String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		long id = Long.parseLong(parametros.get("id"));
							

		List<FieldMessage> list = new ArrayList<>();

		User user = userRepository.findByEmail(value.getEmail());

		if (user != null && user.getId() != id) {
			list.add(new FieldMessage("email", "Email já cadastrado"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}

		return list.isEmpty();
	}

}
