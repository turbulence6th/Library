package com.turbulence6th.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.model.Book;

@WebServlet("/books/*")
public class BookEditServlet extends BookServlet {

	private static final long serialVersionUID = -9037745287739869520L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Book book = requestBook(request);
		if(book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		else {
			request.setAttribute("book", book);
			super.doGet(request, response);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Book book = requestBook(request);
		if(book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		
		else if(this.bookRepository.update(book)){
			response.sendRedirect("/books");
		}
		
		else {
			request.setAttribute("book", book);
			super.doGet(request, response);
		}
	}

	private Book requestBook(HttpServletRequest request) {
		String[] pathParts = request.getRequestURI().split("/");

		if (pathParts.length == 3) {

			Long id = Long.valueOf(pathParts[2]);

			Book book = this.bookRepository.findById(id);
			
			if (request.getMethod().equals("POST")) {
				String name = request.getParameter("book[name]");
				String author = request.getParameter("book[author]");
				String publishDate = request.getParameter("book[publishDate]");

				book.setName(name);
				book.setAuthor(author);
				book.setPublishDate(null);
			}
			
			return book;
		}
		
		return null;
	}

	@Override
	protected String title() {
		return "Edit Book";
	}

	@Override
	protected String view() {
		return super.view() + "/edit.jsp";
	}

}
