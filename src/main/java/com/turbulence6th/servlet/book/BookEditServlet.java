package com.turbulence6th.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.model.Book;
import com.turbulence6th.validator.BookValidator;

@WebServlet("/pages/books/edit/*")
public class BookEditServlet extends BookServlet {

	private static final long serialVersionUID = -9037745287739869520L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Book book = requestBook(request);
			if (book == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}

			else {
				request.setAttribute("book", book);
				this.forward(request, response);
			}
		} catch(RuntimeException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Book book = requestBook(request);
			if (book == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
			}

			else if (BookValidator.validate(book) && this.bookRepository.update(book)) {
				this.broadcastUpdate(this.bookIndexSessions, gson, book);
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
		return "Edit Book";
	}

	@Override
	protected String view() {
		return super.view() + "/edit.jsp";
	}

}
