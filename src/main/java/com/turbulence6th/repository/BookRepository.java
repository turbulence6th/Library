package com.turbulence6th.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.turbulence6th.model.Book;

public class BookRepository extends Repository<Book> {

	public BookRepository(Connection connection) {
		super(connection);
	}
	
	public Book findById(Long id) {
		
		String sql = "SELECT * FROM books WHERE book_id = ? LIMIT 1";

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {

			statement.setLong(1, id);
			
			System.out.println(statement);
			
			try(ResultSet resultSet = statement.executeQuery()) {
				
				if (resultSet.next()) {
					return new Book(resultSet.getLong("book_id"), 
							resultSet.getString("name"),
							resultSet.getString("author"), 
							resultSet.getObject("publish_date", LocalDate.class));
				}
				
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<Book> getBooks() {

		String sql = "SELECT * FROM books ORDER BY book_id ASC";

		try (Statement statement = this.connection.createStatement(); 
				ResultSet resultSet = statement.executeQuery(sql)) {

			System.out.println(sql);
			
			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				books.add(new Book(resultSet.getLong("book_id"), 
						resultSet.getString("name"),
						resultSet.getString("author"), 
						resultSet.getObject("publish_date", LocalDate.class)));
			}
			
			return books;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
