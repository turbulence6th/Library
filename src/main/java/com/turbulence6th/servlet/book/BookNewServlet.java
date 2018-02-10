package com.turbulence6th.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.model.Book;
import com.turbulence6th.validator.BookValidator;

@WebServlet("/pages/books/new")
public class BookNewServlet extends BookServlet {

	private static final long serialVersionUID = 6466708297400885178L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Book book = this.requestBook(request);
			if(BookValidator.validate(book) && this.bookRepository.create(book)) {
				this.broadcastAdd(this.bookIndexSessions, gson, book);
				response.sendRedirect("/books");
			}
			
			else {
				request.setAttribute("book", book);
				this.forward(request, response);
			}
		} catch(RuntimeException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Check whether all fields are valid");
		}
	}
	
	@Override
	protected String title() {
		return "New Book";
	}
	
	@Override
	protected String view() {
		return super.view() + "/new.jsp";
	}

}
