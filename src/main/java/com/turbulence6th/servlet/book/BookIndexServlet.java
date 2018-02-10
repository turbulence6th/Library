package com.turbulence6th.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/pages/books")
public class BookIndexServlet extends BookServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("books", this.bookRepository.getBooks());
		this.forward(request, response);
	}

	@Override
	protected String title() {
		return "Index Books";
	}

	@Override
	protected String view() {
		return super.view() + "/index.jsp";
	}

}
