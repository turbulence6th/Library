package com.turbulence6th.api.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.turbulence6th.model.Book;

@WebServlet("/pages/api/books/delete/*")
public class BookDeleteApiServlet extends BookApiServlet {

	private static final long serialVersionUID = -4608537495565849016L;
	
	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JsonObject jsonResponse = new JsonObject();
		
		try {
			Book book = this.requestBook(request);
			if (book == null) {
				jsonResponse.addProperty("success", false);
				jsonResponse.addProperty("message", "Not found");
			}
			else if (this.bookRepository.delete(book)) {
				jsonResponse.addProperty("success", true);
				this.broadcastDelete(this.bookIndexSessions, book.getId());
			}
	
			else {
				jsonResponse.addProperty("success", false);
			}
	
		} catch(RuntimeException e) {
			e.printStackTrace();
			jsonResponse.addProperty("success", false);
			jsonResponse.addProperty("message", "Check whether all fields are valid");
		}
		
		print(response, jsonResponse);
	}
	
	
}
