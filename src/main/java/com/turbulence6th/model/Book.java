package com.turbulence6th.model;

import java.time.LocalDate;

import com.turbulence6th.annotation.Column;
import com.turbulence6th.annotation.Id;
import com.turbulence6th.annotation.Table;

@Table(name = "books")
public class Book extends Model {

	@Id
	@Column(name = "book_id")
	private Long id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "publish_date")
	private LocalDate publishDate;

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
		return String.format("Book[id: %d, name: %s, author: %s, publishDate: %s]", this.id, this.name, this.author, this.publishDate);
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
	
	
	
}
