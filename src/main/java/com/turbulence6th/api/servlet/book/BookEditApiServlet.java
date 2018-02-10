package com.turbulence6th.api.servlet.book;

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

@WebServlet("/pages/api/books/*")
public class BookEditApiServlet extends BookApiServlet {

	private static final long serialVersionUID = -6260819167197289437L;

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Book book = requestBook(request);
		if (book == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}

		else {
			JsonObject jsonResponse = new JsonObject();
			if (BookValidator.validate(book) && this.bookRepository.update(book)) {
				jsonResponse.addProperty("success", true);
			}

			else {
				jsonResponse.addProperty("success", false);
				jsonResponse.add("errors", gson.toJsonTree(book.getErrors()));
			}

			this.print(response, jsonResponse);
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

			print(response, jsonResponse);
		}
	}

	private Book requestBook(HttpServletRequest request) {
		String[] pathParts = request.getRequestURI().split("/");

		if (pathParts.length == 5) {

			Long id = Long.valueOf(pathParts[4]);

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

}
