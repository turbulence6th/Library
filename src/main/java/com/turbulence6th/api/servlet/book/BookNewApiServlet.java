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

@WebServlet("/pages/api/books/new")
public class BookNewApiServlet extends BookApiServlet {
	
	private static final long serialVersionUID = -5132744507814765831L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JsonObject jsonResponse = new JsonObject();
		
		try {
			Book book = requestBook(request);
			if(BookValidator.validate(book) && this.bookRepository.create(book)) {
				jsonResponse.addProperty("success", true);
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
	
	private Book requestBook(HttpServletRequest request) {
		String name = request.getParameter("book[name]").trim().replaceAll(" +", " ");
        String author = request.getParameter("book[author]").trim().replaceAll(" +", " ");
        String publishDate = request.getParameter("book[publishDate]");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return new Book(name, author, LocalDate.parse(publishDate, formatter));
	}
}
