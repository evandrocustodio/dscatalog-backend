package com.devsuperior.dscatalog.services.validations;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.entities.UserInsertDTO;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.resources.FieldMessage;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

	@Autowired
	UserRepository userRepository;

	@Override
	public void initialize(UserInsertValid constraintAnnotation) {
	}

	@Override
	public boolean isValid(UserInsertDTO value, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		User user = userRepository.findByEmail(value.getEmail());

		if (user != null) {
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
