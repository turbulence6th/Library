package com.turbulence6th.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.model.Book;

@WebServlet("/pages/books/*")
public class BookShowServlet extends BookServlet {	
		
	private static final long serialVersionUID = 8655928658895401277L;
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Book book = requestBook(request);
			
			if(book == null) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
			}
			
			else {
				request.setAttribute("book", book);
				this.forward(request, response);
			}
			
		} catch(RuntimeException e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not found");
		}
	}
	
	@Override
	protected String title() {
		return "Book";
	}
	
	@Override
	protected String view() {
		return super.view() + "/show.jsp";
	}

}
