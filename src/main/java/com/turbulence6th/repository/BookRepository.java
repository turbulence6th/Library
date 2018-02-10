package com.turbulence6th.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.turbulence6th.model.Book;

public class BookRepository {
	
	private Connection connection;

	public BookRepository(Connection connection) {
		this.connection = connection;
	}
	
	public boolean create(Book book) {
		
		String sql = "INSERT INTO books (name, author, publish_date) VALUES (?, ?, ?)";
		
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			
			statement.setString(1, book.getName());
			statement.setString(2, book.getAuthor());
			statement.setTimestamp(3, book.getPublishDate() != null ? 
					Timestamp.valueOf(book.getPublishDate().atStartOfDay()) : null);
			
			System.out.println(statement);
			
			statement.execute();
			
			try(ResultSet resultSet = statement.getGeneratedKeys()) {
				if(resultSet.next()) {
					book.setId(resultSet.getLong(1));
				}
			}
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public boolean update(Book book) {
		
		String sql = "UPDATE books SET name = ?, author = ?, publish_date = ? WHERE book_id = ?";
		
		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
			
			statement.setString(1, book.getName());
			statement.setString(2, book.getAuthor());
			statement.setTimestamp(3, book.getPublishDate() != null ? 
					Timestamp.valueOf(book.getPublishDate().atStartOfDay()) : null);
			statement.setLong(4, book.getId());
			
			System.out.println(statement);
			
			statement.execute();
			
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Book findById(Long id) {
		
		String sql = "SELECT * FROM books WHERE book_id = ?";

		try (PreparedStatement statement = this.connection.prepareStatement(sql)) {

			statement.setLong(1, id);
			
			System.out.println(statement);
			
			try(ResultSet resultSet = statement.executeQuery()) {
				
				if (resultSet.next()) {
					return new Book(resultSet.getLong("book_id"), resultSet.getString("name"),
							resultSet.getString("author"), null);
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
				books.add(new Book(resultSet.getLong("book_id"), resultSet.getString("name"),
						resultSet.getString("author"), null));
			}
			
			return books;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
