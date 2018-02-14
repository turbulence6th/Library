package com.turbulence6th.validator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.turbulence6th.model.Book;

public class BookValidator {
	
	public static final String NAME_INVALID_ERROR = "Use alpha numeric characters , (') (-) or (:)";
	
	public static final String NAME_LENGTH_ERROR = "Length must be in 3 and 25";
	
	public static final String AUTHOR_INVALID_ERROR = "Use alphabatic characters or (')";
	
	public static final String AUTHOR_LENGTH_ERROR = "Length must be in 3 and 25";
	
	public static final String PUBLISH_DATE_FUTURE_ERROR = "Publish date must not be after today";

	public static boolean validate(Book book) {
		Map<String, List<String>> errors = new HashMap<>();
		
		String name = book.getName();
		
		if (!Pattern.matches("([a-zA-Z0-9'-:]|\\s)*", name)) {
			errors.merge("name", createList(NAME_INVALID_ERROR), BookValidator::mergeList);
		}
		
		if(name.length() < 3 || name.length() > 100) {
			errors.merge("name", createList(NAME_LENGTH_ERROR), BookValidator::mergeList);
		}
		
		String author = book.getAuthor();
		
		if (!Pattern.matches("([a-zA-Z']|\\s)*", author)) {
			errors.merge("author", createList(AUTHOR_INVALID_ERROR), BookValidator::mergeList);
		} 
		
		if(author.length() < 3 || author.length() > 100) {
			errors.merge("author", createList(AUTHOR_LENGTH_ERROR), BookValidator::mergeList);
		}
		
		LocalDate publishDate = book.getPublishDate();
		
		if(publishDate.isAfter(LocalDate.now())) {
			errors.merge("publishDate", createList(PUBLISH_DATE_FUTURE_ERROR), BookValidator::mergeList);
		}
		
		book.setErrors(errors);
		
		return errors.size() == 0  ? true : false;
	}
	
	private static List<String> mergeList(List<String> list1, List<String> list2) {
		list1.addAll(list2);
		return list1;
	}
	
	private static List<String> createList(String element) {
		List<String> list = new LinkedList<>();
		list.add(element);
		return list;
	}
}
