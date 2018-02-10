package com.turbulence6th.servlet.book;

import javax.servlet.ServletException;

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
	
	@Override
	protected String view() {
		return "/book";
	}

}
