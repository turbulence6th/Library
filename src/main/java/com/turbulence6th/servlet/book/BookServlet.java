package com.turbulence6th.servlet.book;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.turbulence6th.repository.BookRepository;
import com.turbulence6th.servlet.AbstractServlet;

public abstract class BookServlet extends AbstractServlet {

	private static final long serialVersionUID = -2399002301004833284L;
	
	protected BookRepository bookRepository;
	
	@Override
	public void init() throws ServletException {
		super.init();
		this.bookRepository = (BookRepository) this.context.getAttribute("bookRepository");
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}
	
	@Override
	protected String view() {
		return "/book";
	}

}
