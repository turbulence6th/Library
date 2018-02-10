package com.turbulence6th.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Book {

	private Long id;
	
	private String name;
	
	private String author;
	
	private LocalDate publishDate;
	
	private Map<String, List<String>> errors;

	public Book() {
		
	}
	
	public Book(String name, String author, LocalDate publishDate) {
		this.name = name;
		this.author = author;
		this.publishDate = publishDate;
	}
	
	public Book(Long id, String name, String author, LocalDate publishDate) {
		this(name, author, publishDate);
		this.id = id;
	}
	
	@Override
	public String toString() {
		return String.format("Book[id: %d, name: %s, author: %s, publishDate: %tb]", this.id, this.name, this.author, this.publishDate);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public LocalDate getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(LocalDate publishDate) {
		this.publishDate = publishDate;
	}
	
	public Map<String, List<String>> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, List<String>> errors) {
		this.errors = errors;
	}
	
}
