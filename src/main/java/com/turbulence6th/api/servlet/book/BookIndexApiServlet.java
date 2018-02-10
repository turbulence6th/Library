package com.turbulence6th.api.servlet.book;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.model.Book;

@WebServlet("/pages/api/books")
public class BookIndexApiServlet extends BookApiServlet {

	private static final long serialVersionUID = 2270517188927627340L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Book> books = this.bookRepository.getBooks();
		this.print(response, books);
	}
	
}
