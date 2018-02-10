package com.turbulence6th.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.model.Book;

@WebServlet("/books/new")
public class BookNewServlet extends BookServlet {

	private static final long serialVersionUID = 6466708297400885178L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Book book = this.requestBook(request);
		if(this.bookRepository.create(book)) {
			response.sendRedirect("/books");
		}
		
		else {
			request.setAttribute("book", book);
			super.doPost(request, response);
		}
	}
	
	private Book requestBook(HttpServletRequest request) {
		String name = request.getParameter("book[name]");
        String author = request.getParameter("book[author]");
        String publishdate = request.getParameter("book[publishDate]");
        return new Book(name, author, null);
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
