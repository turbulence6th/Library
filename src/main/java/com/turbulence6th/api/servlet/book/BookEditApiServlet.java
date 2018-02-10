package com.turbulence6th.api.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.turbulence6th.model.Book;
import com.turbulence6th.validator.BookValidator;

@WebServlet("/pages/api/books/edit/*")
public class BookEditApiServlet extends BookApiServlet {

	private static final long serialVersionUID = -6260819167197289437L;

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonObject jsonResponse = new JsonObject();
		
		try {
			Book book = this.requestBook(request);
			if (book == null) {
				jsonResponse.addProperty("success", false);
				jsonResponse.addProperty("message", "Not found");
			}

			else if (BookValidator.validate(book) && this.bookRepository.update(book)) {
				jsonResponse.addProperty("success", true);
				this.broadcastUpdate(this.bookIndexSessions, gson, book);
			}

			else {
				jsonResponse.addProperty("success", false);
				jsonResponse.add("errors", gson.toJsonTree(book.getErrors()));
			}

		} catch(RuntimeException e) {
			e.printStackTrace();
			jsonResponse.addProperty("success", false);
			jsonResponse.addProperty("message", "Check whether all fields are valid");
		}
		
		this.print(response, jsonResponse);
	}

}
