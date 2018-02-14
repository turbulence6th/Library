package com.turbulence6th.validator;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.turbulence6th.Assert;
import com.turbulence6th.annotation.Test;
import com.turbulence6th.model.Book;

public class BookValidatorTest {

	@Test("Test Empty Book Name")
	public void testEmptyName() {
		Book book = new Book("", "", LocalDate.now());
		BookValidator.validate(book);
		Map<String, List<String>> errors = book.getErrors();
		
		Assert.assertIn(BookValidator.NAME_LENGTH_ERROR, errors.get("name"));
	}
	
}
