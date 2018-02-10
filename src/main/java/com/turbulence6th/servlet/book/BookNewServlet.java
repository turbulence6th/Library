package com.turbulence6th.servlet.book;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.model.Book;
import com.turbulence6th.validator.BookValidator;

@WebServlet("/books/new")
public class BookNewServlet extends BookServlet {

	private static final long serialVersionUID = 6466708297400885178L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Book book = this.requestBook(request);
		if(BookValidator.validate(book) && this.bookRepository.create(book)) {
			response.sendRedirect("/books");
		}
		
		else {
			request.setAttribute("book", book);
			this.forward(request, response);
		}
	}
	
	private Book requestBook(HttpServletRequest request) {
		String name = request.getParameter("book[name]").trim().replaceAll(" +", " ");
        String author = request.getParameter("book[author]").trim().replaceAll(" +", " ");
        String publishDate = request.getParameter("book[publishDate]");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return new Book(name, author, LocalDate.parse(publishDate, formatter));
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
