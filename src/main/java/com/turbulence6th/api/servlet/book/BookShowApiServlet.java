package com.turbulence6th.api.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.turbulence6th.model.Book;

@WebServlet("/pages/api/books/*")
public class BookShowApiServlet extends BookApiServlet {
	
	private static final long serialVersionUID = -7549671871394986437L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonObject jsonResponse = new JsonObject();
		
		try {
			Book book = this.requestBook(request);
			if (book == null) {
				jsonResponse.addProperty("success", false);
				jsonResponse.addProperty("message", "Not found");
			}

			else {
				jsonResponse.addProperty("success", true);
				jsonResponse.add("book", gson.toJsonTree(book));
			}

		} catch(RuntimeException e) {
			e.printStackTrace();
			jsonResponse.addProperty("success", false);
			jsonResponse.addProperty("message", "Not Found");
		}
		
		print(response, jsonResponse);
	}
	
}
