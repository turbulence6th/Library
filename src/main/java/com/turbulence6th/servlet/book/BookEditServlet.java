package com.turbulence6th.servlet.book;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.turbulence6th.model.Book;
import com.turbulence6th.validator.BookValidator;

@WebServlet("/pages/books/*")
public class BookEditServlet extends BookServlet {

	private static final long serialVersionUID = -9037745287739869520L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Book book = requestBook(request);
		if (book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		else {
			request.setAttribute("book", book);
			this.forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Book book = requestBook(request);
		if (book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		else if (BookValidator.validate(book) && this.bookRepository.update(book)) {
			response.sendRedirect("/books");
		}

		else {
			request.setAttribute("book", book);
			this.forward(request, response);
		}
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Book book = requestBook(request);
		
		if (book == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}

		else {
			JsonObject jsonResponse = new JsonObject();
			if (this.bookRepository.delete(book)) {
				jsonResponse.addProperty("success", true);
			}

			else {
				jsonResponse.addProperty("success", false);
			}
			
			response.setContentType("application/json");
			response.getWriter().println(jsonResponse.toString());
		}
	}

	private Book requestBook(HttpServletRequest request) {
		String[] pathParts = request.getRequestURI().split("/");

		if (pathParts.length == 4) {

			Long id = Long.valueOf(pathParts[3]);

			Book book = this.bookRepository.findById(id);

			if (request.getMethod().equals("POST")) {
				String name = request.getParameter("book[name]").trim().replaceAll(" +", " ");
				String author = request.getParameter("book[author]").trim().replaceAll(" +", " ");
				String publishDate = request.getParameter("book[publishDate]");

				book.setName(name);
				book.setAuthor(author);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
				book.setPublishDate(LocalDate.parse(publishDate, formatter));
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
