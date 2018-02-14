package com.turbulence6th.repository;

import java.util.List;

import com.turbulence6th.annotation.Query;
import com.turbulence6th.model.Book;

public interface BookRepository extends Repository<Book> {
	
	@Query("SELECT * FROM books WHERE book_id = ? LIMIT 1")
	public Book findById(Long id);
	
	@Query("SELECT * FROM books ORDER BY book_id ASC")
	public List<Book> getBooks();
	
	public Book findBy(Object... params);
	
}
