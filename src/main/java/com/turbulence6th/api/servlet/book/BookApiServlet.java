package com.turbulence6th.api.servlet.book;

import javax.servlet.ServletException;

import com.turbulence6th.api.servlet.AbstractApiServlet;
import com.turbulence6th.repository.BookRepository;

public abstract class BookApiServlet extends AbstractApiServlet {

	private static final long serialVersionUID = 4859402327371805154L;
	
	protected BookRepository bookRepository;

	@Override
	public void init() throws ServletException {
		super.init();
		this.bookRepository = (BookRepository) this.context.getAttribute("bookRepository");
	}

}
